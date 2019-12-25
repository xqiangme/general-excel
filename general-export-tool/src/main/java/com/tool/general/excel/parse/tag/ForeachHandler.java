package com.tool.general.excel.parse.tag;

import com.tool.general.excel.common.constants.XmlConstant;
import com.tool.general.excel.parse.BaseHandler;
import com.tool.general.excel.util.GenExcelToolUtils;
import com.tool.general.excel.parse.model.ParseXmSqlModel;
import com.tool.general.excel.util.GenExcelStringPool;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * foreach标签属性如下
 * <p>
 *
 * @author mengqiang
 */
public class ForeachHandler extends BaseHandler {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ForeachHandler.class);

    @Override
    public StringBuilder parse(StringBuilder sqlBuilder, ParseXmSqlModel paramModel, Map<String, Object> paramMap) {
        LOGGER.debug("[{}] >> [动态SQL条件解析] >> foreach 标签处理 start", paramModel.getTaskId());
        Element foreach = (Element) paramModel.getNode();
        String collection = attr(foreach, XmlConstant.COLLECTION);

        if (GenExcelToolUtils.isBlank(collection)) {
            return sqlBuilder;
        }

        if (!paramMap.containsKey("foreachConditionExpList")) {
            paramMap.put("foreachConditionExpList", new ArrayList<>());
        }

        if (paramMap.containsKey(collection)) {
            List<Map> foreachConditionExpList = (List<Map>) paramMap.get("foreachConditionExpList");
            Map<String, Object> foreachObj = new HashMap<>(2);
            foreachObj.put("foreachConditionExp", sqlBuilder.toString() + GenExcelStringPool.SPACE);
            foreachObj.put("foreachItemList", paramMap.get(collection));
            foreachConditionExpList.add(foreachObj);
            //重置
            sqlBuilder = new StringBuilder();
        }

        LOGGER.info("[{}] >> [动态SQL条件解析] >> foreach 标签处理 end", paramModel.getTaskId());
        return sqlBuilder;
    }

}
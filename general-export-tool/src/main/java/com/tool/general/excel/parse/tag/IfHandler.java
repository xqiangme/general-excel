package com.tool.general.excel.parse.tag;

import com.tool.general.excel.parse.BaseHandler;
import com.tool.general.excel.util.GenExcelToolUtils;
import com.tool.general.excel.parse.model.ParseXmSqlModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * if 标签 处理
 *
 * @author mengqiang
 */
public class IfHandler extends BaseHandler {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(IfHandler.class);


    @Override
    public StringBuilder parse(StringBuilder sqlBuilder, ParseXmSqlModel paramModel, Map<String, Object> paramMap) {
        //前置校验
        if (!this.parseBeforeCheck(paramModel)) {
            return sqlBuilder;
        }

        //解析-子集
        StringBuilder sunNodeValue = super.sunNodeDeal(paramModel, paramMap);

        //拼接-子节点内容
        super.appendSpaceAndContent(sqlBuilder, sunNodeValue);

        LOGGER.info("[{}] >> [动态SQL条件解析] >> if 标签处理 end", paramModel.getTaskId());
        return sqlBuilder;
    }

    private boolean parseBeforeCheck(ParseXmSqlModel paramModel) {
        LOGGER.debug("[{}] >> [动态SQL条件解析] >> if 标签处理 start", paramModel.getTaskId());
        if (null == paramModel.getNode()) {
            return false;
        }

        if (GenExcelToolUtils.isBlank(paramModel.getNode().getText())) {
            LOGGER.debug("[{}] >> [动态SQL条件解析] >> if 标签处理 end 当前内容为空", paramModel.getTaskId());
            return false;
        }
        return true;
    }


}
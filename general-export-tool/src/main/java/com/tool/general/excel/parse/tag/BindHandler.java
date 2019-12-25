package com.tool.general.excel.parse.tag;


import com.tool.general.excel.common.exception.GenExcelBusinessException;
import com.tool.general.excel.parse.BaseHandler;
import com.tool.general.excel.parse.model.ParseXmSqlModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * bind 标签 处理
 *
 * @author mengqiang
 */
public class BindHandler extends BaseHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(BindHandler.class);

    @Override
    public StringBuilder parse(StringBuilder sqlBuilder, ParseXmSqlModel paramModel, Map<String, Object> paramMap) {
        LOGGER.debug("[{}] 动态SQL条件解析 >> bind 标签处理", paramModel.getTaskId());
        throw new GenExcelBusinessException("[动态SQL条件解析] >> bind 标签暂未支持！");
    }

}
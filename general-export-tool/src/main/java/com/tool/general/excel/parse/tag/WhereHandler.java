package com.tool.general.excel.parse.tag;

import com.tool.general.excel.parse.BaseHandler;
import com.tool.general.excel.parse.model.ParseXmSqlModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * where 标签 处理
 *
 * @author mengqiang
 */
public class WhereHandler extends BaseHandler {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(WhereHandler.class);

    @Override
    public StringBuilder parse(StringBuilder sqlBuilder, ParseXmSqlModel paramModel, Map<String, Object> paramMap) {
        LOGGER.debug("[{}] >> [动态SQL条件解析] >> where 标签处理 end", paramModel.getTaskId());
        return sqlBuilder;
    }
}
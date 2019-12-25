package com.tool.general.excel.parse.tag;

import com.tool.general.excel.common.exception.GenExcelBusinessException;
import com.tool.general.excel.parse.BaseHandler;
import com.tool.general.excel.parse.model.ParseXmSqlModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * trim 标签 处理
 * <p>
 * prefix 前缀
 * prefixOverrides 前缀覆盖内容
 * <p>
 * suffix 后缀
 * suffixOverrides 后缀覆盖内容
 *
 * @author mengqiang
 */
public class TrimHandler extends BaseHandler {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TrimHandler.class);

    @Override
    public StringBuilder parse(StringBuilder sqlBuilder, ParseXmSqlModel paramModel, Map<String, Object> paramMap) {
        LOGGER.debug("[{}] >> [动态SQL条件解析] >>  trim 标签处理 start", paramModel.getTaskId());
        throw new GenExcelBusinessException("[动态SQL条件解析] >> trim 标签暂不支持！");
    }

}
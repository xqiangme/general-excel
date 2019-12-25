package com.tool.general.excel.util;

import com.tool.general.excel.common.exception.GenExcelBusinessException;
import com.tool.general.excel.parse.BaseHandler;
import com.tool.general.excel.parse.NodeHandlerFactory;
import com.tool.general.excel.parse.model.ParseXmSqlModel;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * SQL -xml 文件解析工具
 *
 * @author mengqiang
 */
public class GenExcelXmlSqlParseUtil {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenExcelXmlSqlParseUtil.class);

    /**
     * 解析XMl中SQL内容
     */
    public static String getSqlFromXml(String taskId, String xmlValue, Map<String, Object> paramMap) {
        StringBuilder sqlBuilder = parseSqlFromXml(taskId, xmlValue, paramMap);
        if (null == sqlBuilder) {
            return GenExcelStringPool.EMPTY;
        }
        String sqlStr = String.valueOf(sqlBuilder).trim();
        return GenExcelStringPool.SPACE + sqlStr;
    }

    /**
     * 解析XMl中SQL内容
     */
    private static StringBuilder parseSqlFromXml(String taskId, String xmlValue, Map<String, Object> paramMap) {
        if (GenExcelToolUtils.isBlank(xmlValue)) {
            throw new GenExcelBusinessException("[{0}]>> 公共导出-SQL解析 >> 动态SQL解析异常,请检查是否存在非法字符!");
        }

        //替换换行
        xmlValue = GenExcelToolUtils.replaceNewline(xmlValue, GenExcelStringPool.SPACE);
        //拼接一个根节点
        xmlValue = "<xml>" + xmlValue + "</xml>";
        //读取要解析的xml文件
        Document document;
        try {
            document = DocumentHelper.parseText(xmlValue);
        } catch (Exception e) {
            LOGGER.error("[{}]>> 公共导出-SQL解析 >> 动态SQL解析异常 stack={}", taskId, GenExcelToolUtils.getStackTrace(e));
            throw new GenExcelBusinessException("[{0}]>> 公共导出-SQL解析 >> 动态SQL解析异常,请检查是否存在非法字符!", taskId);
        }

        Element root = document.getRootElement();
        List<Node> contentList = root.content();
        if (CollectionUtils.isEmpty(contentList)) {
            return new StringBuilder();
        }

        ParseXmSqlModel parseParamModel = new ParseXmSqlModel(taskId);
        StringBuilder sqlBuilder = new StringBuilder();

        for (Node node : contentList) {
            parseParamModel.setNode(node);
            sqlBuilder = dealNode(sqlBuilder, parseParamModel, paramMap);
        }
        return sqlBuilder;
    }

    /**
     * 处理-XMl中SQL内容
     */
    public static StringBuilder dealNode(StringBuilder sqlBuilder, ParseXmSqlModel paramModel, Map<String, Object> paramMap) {
        //是否存在子节点
        if (paramModel.getNode() instanceof Element) {
            String nodeName = paramModel.getNode().getName();
            if (GenExcelToolUtils.isBlank(nodeName)) {
                return sqlBuilder;
            }

            //执行解析
            BaseHandler baseNode = NodeHandlerFactory.parseNode(nodeName);
            if (null != baseNode) {
                sqlBuilder = baseNode.parse(sqlBuilder, paramModel, paramMap);
            } else {
                throw new GenExcelBusinessException("标签 {0} 不支持解析 , 或语法不正确！", nodeName);
            }

        } else {
            //解析-各标签-构建SQL
            sqlBuilder.append(paramModel.getNode().getText());
        }
        return sqlBuilder;
    }

}
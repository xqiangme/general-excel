package com.tool.general.excel.parse;

import com.tool.general.excel.parse.model.ParseXmSqlModel;
import com.tool.general.excel.util.GenExcelXmlSqlParseUtil;
import com.tool.general.excel.util.GenExcelStringPool;
import com.tool.general.excel.util.GenExcelToolUtils;
import org.apache.ibatis.scripting.xmltags.OgnlCache;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.dom4j.Node;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;

/**
 * 抽象节点
 *
 * @author mengqiang
 */
public abstract class BaseHandler {


    /**
     * 解析SQL-抽象方法
     *
     * @param sqlBuilder
     * @param paramModel
     * @param paramMap
     * @return
     * @author mengqiang
     * @date 2019-08-09
     */
    public abstract StringBuilder parse(StringBuilder sqlBuilder, ParseXmSqlModel paramModel, Map<String, Object> paramMap);


    /**
     * 从xml中获取属性值
     *
     * @param e    the e
     * @param attr the attr
     * @return the string
     */
    protected static String attr(Element e, String attr) {
        if (e == null || attr == null) {
            return null;
        }
        Attribute attribute = e.attribute(attr);
        if (attribute == null) {
            return null;
        } else {
            return attribute.getText();
        }
    }

    /**
     * 处理子节点
     *
     * @param paramModel
     * @return
     * @author mengqiang
     * @date 2019-08-09
     */
    protected StringBuilder sunNodeDeal(ParseXmSqlModel paramModel, Map<String, Object> paramMap) {
        //标签的子集解析
        Element sunElement = (Element) paramModel.getNode();

        String testExp = attr(sunElement, "test");

        if (GenExcelToolUtils.isBlank(testExp)) {
            return new StringBuilder();
        }

        Boolean testExpFlag = (Boolean) OgnlCache.getValue(testExp, paramMap);
        if (!testExpFlag) {
            return new StringBuilder();
        }

        List<Node> sunContentList = sunElement.content();
        if (CollectionUtils.isEmpty(sunContentList)) {
            return new StringBuilder();
        }

        StringBuilder sunSqlBuilder = new StringBuilder();
        for (Node sunNode : sunContentList) {
            if (GenExcelToolUtils.isBlank(sunNode.getName())) {
                if (GenExcelToolUtils.isNotBlank(sunNode.getText())) {
                    sunSqlBuilder.append(sunNode.getText());
                }
            } else {
                paramModel.setNode(sunNode);
                sunSqlBuilder = GenExcelXmlSqlParseUtil.dealNode(sunSqlBuilder, paramModel, paramMap);
            }
        }

        return sunSqlBuilder;
    }


    /**
     * 拼接空格+内容
     */
    protected void appendSpaceAndContent(StringBuilder sqlBuilder, String content) {
        if (GenExcelToolUtils.isBlank(content)) {
            return;
        }
        sqlBuilder.append(GenExcelStringPool.SPACE);
        sqlBuilder.append(content.trim());
    }

    /**
     * 拼接空格+内容
     */
    protected void appendSpaceAndContent(StringBuilder sqlBuilder, StringBuilder contentBuilder) {
        sqlBuilder.append(GenExcelStringPool.SPACE);
        sqlBuilder.append(contentBuilder);
    }


}
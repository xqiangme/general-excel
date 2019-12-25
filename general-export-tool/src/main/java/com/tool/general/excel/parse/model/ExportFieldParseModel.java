package com.tool.general.excel.parse.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mengqiang
 */
public class ExportFieldParseModel implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * excel文件标题
     * key:参数名 value:标题名
     */
    private Map<String, String> excelHeadMap;

    /**
     * 字段参数规则
     * key:参数名 value:规则集
     */
    private Map<String, Map<String, Object>> fieldRuleMap;

    public static ExportFieldParseModel initDefault() {
        ExportFieldParseModel parseModel = new ExportFieldParseModel();
        parseModel.setExcelHeadMap(new HashMap<>(0));
        parseModel.setFieldRuleMap(new HashMap<>(0));
        return parseModel;
    }


    public ExportFieldParseModel() {
    }

    public ExportFieldParseModel(Map<String, String> excelHeadMap, Map<String, Map<String, Object>> fieldRuleMap) {
        this.excelHeadMap = excelHeadMap;
        this.fieldRuleMap = fieldRuleMap;
    }


    public Map<String, String> getExcelHeadMap() {
        return excelHeadMap;
    }

    public void setExcelHeadMap(Map<String, String> excelHeadMap) {
        this.excelHeadMap = excelHeadMap;
    }

    public Map<String, Map<String, Object>> getFieldRuleMap() {
        return fieldRuleMap;
    }

    public void setFieldRuleMap(Map<String, Map<String, Object>> fieldRuleMap) {
        this.fieldRuleMap = fieldRuleMap;
    }
}
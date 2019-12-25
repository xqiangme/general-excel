package com.tool.general.excel.common.enums;

/**
 * 模板类型枚举
 *
 * @author mengqiang
 */
public enum TemplateTypeEnum {

    /**
     * 模板类型 1-普通导出列表 ; 2-模板导出列表 ; 3-模板导出对象
     */
    ORDINARY_LIST(1, "普通导出列表"),
    TEMPLATE_LIST(2, "模板导出列表"),
    TEMPLATE_OBJECT(3, "模板导出对象"),
    TEMPLATE_OBJECT_AND_LIST(4, "模板导出+集合"),

    ;

    private Integer value;
    private String name;


    /**
     * 判断当前状态是否需要模板
     */
    public static Boolean needTemplage(Integer type) {
        if (TEMPLATE_LIST.getValue().equals(type)) {
            return true;
        }
        if (TEMPLATE_OBJECT.getValue().equals(type)) {
            return true;
        }

        return TEMPLATE_OBJECT_AND_LIST.getValue().equals(type);
    }

    TemplateTypeEnum(Integer value, String name) {
        this.value = value;
        this.name = name;
    }

    public Integer getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

}

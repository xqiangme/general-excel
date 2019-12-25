package com.tool.general.excel.common.enums;

/**
 * 模板更新标记
 *
 * @author mengqiang
 */
public enum TemplateRefreshFlagEnum {

    /**
     * 模板更新标记 1-未更新 ;2-已更新
     */
    UN_REFRESH(1, "未更新"),

    /**
     * 已更新
     */
    ALREADY_REFRESH(2, "已更新");


    private Integer value;
    private String name;

    TemplateRefreshFlagEnum(Integer value, String name) {
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
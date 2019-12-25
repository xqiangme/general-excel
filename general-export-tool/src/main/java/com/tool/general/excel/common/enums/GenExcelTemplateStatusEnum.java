package com.tool.general.excel.common.enums;

/**
 * 模板状态枚举
 *
 * @author mengqiang
 */
public enum GenExcelTemplateStatusEnum {

    /**
     * 启用
     */
    ENABLE(1, "启用"),

    /**
     * 停用
     */
    DISABLE(2, "停用");

    private Integer status;
    private String description;

    GenExcelTemplateStatusEnum(Integer status, String description) {
        this.status = status;
        this.description = description;
    }

    public Integer getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }


}
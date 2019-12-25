package com.tool.general.excel.common.model.support;


import java.io.Serializable;

/**
 * @author mengqiang
 */
public class GenExcelExportModel implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 总行数
     */
    private int rows;

    /**
     * 下载地址
     */
    private String downloadUrl;

    public GenExcelExportModel(int rows, String downloadUrl) {
        this.rows = rows;
        this.downloadUrl = downloadUrl;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
}
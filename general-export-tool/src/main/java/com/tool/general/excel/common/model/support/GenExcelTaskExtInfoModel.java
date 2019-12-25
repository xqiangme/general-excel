package com.tool.general.excel.common.model.support;

import java.io.Serializable;

/**
 * 任务扩展信息 model
 *
 * @author mengqiang
 */
public class GenExcelTaskExtInfoModel implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 模板分页容量
     */
    private Integer pageSize;


    /**
     * 下载链接效期 单位:秒 -1-永久有效
     */
    private Long downloadUrlExpire;

    public GenExcelTaskExtInfoModel() {
    }

    public GenExcelTaskExtInfoModel(Integer pageSize, Long downloadUrlExpire) {
        this.pageSize = pageSize;
        this.downloadUrlExpire = downloadUrlExpire;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    public Long getDownloadUrlExpire() {
        return downloadUrlExpire;
    }

    public void setDownloadUrlExpire(Long downloadUrlExpire) {
        this.downloadUrlExpire = downloadUrlExpire;
    }
}
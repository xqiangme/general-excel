package com.tool.general.excel.common.model.support;

import java.io.Serializable;

/**
 * 任务缓存 model
 *
 * @author mengqiang
 */
public class GenExcelTaskCacheModel implements Serializable {

    private static final long serialVersionUID = -1L;
    /**
     * 任务id
     */
    private String taskId;

    /**
     * 任务标题
     */
    private String taskTitle;

    /**
     * 任务备注
     */
    private String remarks;

    /**
     * 下载文件URL
     */
    private String downloadUrl;

    /**
     * 任务行数
     */
    private Integer taskRows;

    /**
     * 任务状态 1-处理中 ; 2-处理成功; 3-处理失败
     */
    private Integer taskStatus;

    /**
     * 执行耗时毫秒数
     */
    private Long taskDual;

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public Integer getTaskRows() {
        return taskRows;
    }

    public void setTaskRows(Integer taskRows) {
        this.taskRows = taskRows;
    }

    public Integer getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    public Long getTaskDual() {
        return taskDual;
    }

    public void setTaskDual(Long taskDual) {
        this.taskDual = taskDual;
    }
}
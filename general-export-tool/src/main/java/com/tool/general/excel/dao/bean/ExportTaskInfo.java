package com.tool.general.excel.dao.bean;

import java.util.Date;

/**
 * The table 导出任务表
 */
public class ExportTaskInfo {

    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 执行耗时毫秒数
     */
    private Long taskDual;

    /**
     * 任务ID
     */
    private String taskId;

    /**
     * 任务备注(失败原因)
     */
    private String remarks;

    /**
     * 任务参数(JSON)
     */
    private String taskParam;

    /**
     * 任务标题
     */
    private String taskTitle;

    /**
     * 平台方ID
     */
    private String platformId;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 下载文件URL
     */
    private String downloadUrl;

    /**
     * 扩展信息
     */
    private String extInfo;

    /**
     * 任务行数
     */
    private Integer taskRows;

    /**
     * 任务状态 1-处理中 ; 2-处理成功; 3-处理失败
     */
    private Integer taskStatus;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 自增ID
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 自增ID
     *
     * @return the string
     */
    public Integer getId() {
        return id;
    }

    /**
     * 执行耗时毫秒数
     */
    public void setTaskDual(Long taskDual) {
        this.taskDual = taskDual;
    }

    /**
     * 执行耗时毫秒数
     *
     * @return the string
     */
    public Long getTaskDual() {
        return taskDual;
    }

    /**
     * 任务ID
     */
    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    /**
     * 任务ID
     *
     * @return the string
     */
    public String getTaskId() {
        return taskId;
    }

    /**
     * 任务备注(失败原因)
     */
    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    /**
     * 任务备注(失败原因)
     *
     * @return the string
     */
    public String getRemarks() {
        return remarks;
    }

    /**
     * 任务参数(JSON)
     */
    public void setTaskParam(String taskParam) {
        this.taskParam = taskParam;
    }

    /**
     * 任务参数(JSON)
     *
     * @return the string
     */
    public String getTaskParam() {
        return taskParam;
    }

    /**
     * 任务标题
     */
    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    /**
     * 任务标题
     *
     * @return the string
     */
    public String getTaskTitle() {
        return taskTitle;
    }

    /**
     * 平台方ID
     */
    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    /**
     * 平台方ID
     *
     * @return the string
     */
    public String getPlatformId() {
        return platformId;
    }

    /**
     * 模板ID
     */
    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    /**
     * 模板ID
     *
     * @return the string
     */
    public String getTemplateId() {
        return templateId;
    }

    /**
     * 下载文件URL
     */
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    /**
     * 下载文件URL
     *
     * @return the string
     */
    public String getDownloadUrl() {
        return downloadUrl;
    }


    public String getExtInfo() {
        return extInfo;
    }

    public void setExtInfo(String extInfo) {
        this.extInfo = extInfo;
    }

    /**
     * 任务行数
     */
    public void setTaskRows(Integer taskRows) {
        this.taskRows = taskRows;
    }

    /**
     * 任务行数
     *
     * @return the string
     */
    public Integer getTaskRows() {
        return taskRows;
    }

    /**
     * 任务状态 1-处理中 ; 2-处理成功; 3-处理失败
     */
    public void setTaskStatus(Integer taskStatus) {
        this.taskStatus = taskStatus;
    }

    /**
     * 任务状态 1-处理中 ; 2-处理成功; 3-处理失败
     *
     * @return the string
     */
    public Integer getTaskStatus() {
        return taskStatus;
    }

    /**
     * 创建时间
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * 创建时间
     *
     * @return the string
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 更新时间
     *
     * @return the string
     */
    public Date getUpdateTime() {
        return updateTime;
    }
}

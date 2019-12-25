package com.tool.general.excel.dao.bean;

import java.util.Date;

/**
 * The table 导出模板表
 */
public class ExportTemplateInfo {

    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 排序条件表达式 示例：a.create_time desc
     */
    private String orderByExp;

    /**
     * 平台方ID
     */
    private String platformId;

    /**
     * 查询表名集
     */
    private String tableNames;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 数据库编码
     */
    private String dbCode;

    /**
     * 表列名
     */
    private String tableColumns;

    /**
     * 模板标题
     */
    private String templateTitle;

    /**
     * 导出文件名
     */
    private String exportFileName;

    /**
     * 查询条件
     */
    private String queryCondition;

    /**
     * 导出数据字段集（使用 | 分隔）
     */
    private String exportFieldsExp;

    /**
     * 子模板ID 0-无
     */
    private String templateChildId;

    /**
     * 模板文件下载地址
     */
    private String templateFileUrl;

    /**
     * 模板文件本地存储地址
     */
    private String templateFileLocalPath;

    /**
     * 分页大小
     */
    private Integer pageSize;

    /**
     * 下载链接效期 单位:秒 -1-永久有效
     */
    private Long downloadUrlExpire;

    /**
     * 模板类型 1-普通导出列表 ; 2-模板导出列表 ; 3-模板导出对象; 4-模板导出对象与列表
     */
    private Integer templateType;

    /**
     * 模板状态 1-启用; 2-停用
     */
    private Integer templateStatus;

    /**
     * 模板更新标记 1-未更新 ;2-已更新
     */
    private Integer templateRefreshFlag;

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
     * 排序条件表达式 示例：a.create_time desc
     */
    public void setOrderByExp(String orderByExp) {
        this.orderByExp = orderByExp;
    }

    /**
     * 排序条件表达式 示例：a.create_time desc
     *
     * @return the string
     */
    public String getOrderByExp() {
        return orderByExp;
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

    public String getDbCode() {
        return dbCode;
    }

    public void setDbCode(String dbCode) {
        this.dbCode = dbCode;
    }

    /**
     * 查询表名集
     */
    public void setTableNames(String tableNames) {
        this.tableNames = tableNames;
    }

    /**
     * 查询表名集
     *
     * @return the string
     */
    public String getTableNames() {
        return tableNames;
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
     * 表列名
     */
    public void setTableColumns(String tableColumns) {
        this.tableColumns = tableColumns;
    }

    /**
     * 表列名
     *
     * @return the string
     */
    public String getTableColumns() {
        return tableColumns;
    }

    /**
     * 模板标题
     */
    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }

    /**
     * 模板标题
     *
     * @return the string
     */
    public String getTemplateTitle() {
        return templateTitle;
    }

    /**
     * 导出文件名
     */
    public void setExportFileName(String exportFileName) {
        this.exportFileName = exportFileName;
    }

    /**
     * 导出文件名
     *
     * @return the string
     */
    public String getExportFileName() {
        return exportFileName;
    }

    /**
     * 查询条件
     */
    public void setQueryCondition(String queryCondition) {
        this.queryCondition = queryCondition;
    }

    /**
     * 查询条件
     *
     * @return the string
     */
    public String getQueryCondition() {
        return queryCondition;
    }

    /**
     * 导出数据字段集（使用 | 分隔）
     */
    public void setExportFieldsExp(String exportFieldsExp) {
        this.exportFieldsExp = exportFieldsExp;
    }

    /**
     * 导出数据字段集（使用 | 分隔）
     *
     * @return the string
     */
    public String getExportFieldsExp() {
        return exportFieldsExp;
    }

    /**
     * 子模板ID 0-无
     */
    public void setTemplateChildId(String templateChildId) {
        this.templateChildId = templateChildId;
    }

    /**
     * 子模板ID 0-无
     *
     * @return the string
     */
    public String getTemplateChildId() {
        return templateChildId;
    }

    /**
     * 模板文件下载地址
     */
    public void setTemplateFileUrl(String templateFileUrl) {
        this.templateFileUrl = templateFileUrl;
    }

    /**
     * 模板文件下载地址
     *
     * @return the string
     */
    public String getTemplateFileUrl() {
        return templateFileUrl;
    }

    /**
     * 模板文件本地存储地址
     */
    public void setTemplateFileLocalPath(String templateFileLocalPath) {
        this.templateFileLocalPath = templateFileLocalPath;
    }

    /**
     * 模板文件本地存储地址
     *
     * @return the string
     */
    public String getTemplateFileLocalPath() {
        return templateFileLocalPath;
    }

    /**
     * 分页大小
     */
    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 分页大小
     *
     * @return the string
     */
    public Integer getPageSize() {
        return pageSize;
    }

    public Long getDownloadUrlExpire() {
        return downloadUrlExpire;
    }

    public void setDownloadUrlExpire(Long downloadUrlExpire) {
        this.downloadUrlExpire = downloadUrlExpire;
    }

    /**
     * 模板类型 1-普通导出列表 ; 2-模板导出列表 ; 3-模板导出对象; 4-模板导出对象与列表
     */
    public void setTemplateType(Integer templateType) {
        this.templateType = templateType;
    }

    /**
     * 模板类型 1-普通导出列表 ; 2-模板导出列表 ; 3-模板导出对象; 4-模板导出对象与列表
     *
     * @return the string
     */
    public Integer getTemplateType() {
        return templateType;
    }

    /**
     * 模板状态 1-启用; 2-停用
     */
    public void setTemplateStatus(Integer templateStatus) {
        this.templateStatus = templateStatus;
    }

    /**
     * 模板状态 1-启用; 2-停用
     *
     * @return the string
     */
    public Integer getTemplateStatus() {
        return templateStatus;
    }

    /**
     * 模板更新标记 1-未更新 ;2-已更新
     */
    public void setTemplateRefreshFlag(Integer templateRefreshFlag) {
        this.templateRefreshFlag = templateRefreshFlag;
    }

    /**
     * 模板更新标记 1-未更新 ;2-已更新
     *
     * @return the string
     */
    public Integer getTemplateRefreshFlag() {
        return templateRefreshFlag;
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

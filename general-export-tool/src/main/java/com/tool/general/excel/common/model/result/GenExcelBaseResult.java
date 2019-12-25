package com.tool.general.excel.common.model.result;


import jodd.util.StringPool;

import java.io.Serializable;

/**
 * controller 返回统一对象
 *
 * @author mengqiang
 */
public class GenExcelBaseResult implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 请求是否成功
     */
    private Boolean success;
    /**
     * 返回的的数据
     */
    private Object data;
    /**
     * 错误代码
     */
    private String errorCode;
    /**
     * 错误消息
     */
    private String errorMsg;

    public GenExcelBaseResult() {
    }

    /**
     * 成功请求
     * success : true
     * errorCode : 默认 2000
     * errorMsg : 默认 ""
     */
    public static GenExcelBaseResult success(Object data) {
        return new GenExcelBaseResult(true, data, "2000", StringPool.EMPTY);
    }

    public GenExcelBaseResult(Boolean success, Object data) {
        this.success = success;
        this.data = data;
    }

    public GenExcelBaseResult(Boolean success, Object data, String errorCode, String errorMsg) {
        this.success = success;
        this.data = data;
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}

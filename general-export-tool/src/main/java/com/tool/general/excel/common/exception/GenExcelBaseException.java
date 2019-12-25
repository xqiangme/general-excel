package com.tool.general.excel.common.exception;

import java.text.MessageFormat;

/**
 * 基础异常
 *
 * @author mengqiang
 */
public class GenExcelBaseException extends RuntimeException {

    protected String msg;

    protected GenExcelBaseException(String message) {
        super(message);
    }

    protected GenExcelBaseException(String msgFormat, Object... args) {
        super(MessageFormat.format(msgFormat, args));
        this.msg = MessageFormat.format(msgFormat, args);
    }

    public String getMsg() {
        return this.msg;
    }

}
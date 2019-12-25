package com.tool.general.excel.common.exception;


/**
 * 公共业务异常
 *
 * @author mengqiang
 */
public class GenExcelBusinessException extends GenExcelBaseException {

    public GenExcelBusinessException(String message) {
        super(message);
    }

    public GenExcelBusinessException(String mess, Object... args) {
        super(mess, args);
    }

}
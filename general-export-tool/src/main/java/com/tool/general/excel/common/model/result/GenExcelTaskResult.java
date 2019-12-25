package com.tool.general.excel.common.model.result;

import java.io.Serializable;

/**
 * 任务返回对象
 *
 * @author mengqiang
 */
public class GenExcelTaskResult implements Serializable {

    private static final long serialVersionUID = -1L;
    /**
     * 任务ID
     */
    private String taskId;


    public GenExcelTaskResult() {
    }

    public GenExcelTaskResult(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}

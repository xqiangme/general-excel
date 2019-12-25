package com.tool.general.excel.common.model.param;

import com.tool.general.excel.common.exception.GenExcelBusinessException;
import com.tool.general.excel.util.GenExcelToolUtils;

import java.io.Serializable;

/**
 * 任务-查询参数
 *
 * @author mengqiang
 */
public class GenExcelTaskQueryParam implements Serializable {

    private static final long serialVersionUID = -1L;
    /**
     * 任务ID
     */
    private String taskId;

    public GenExcelTaskQueryParam() {
    }


    public GenExcelTaskQueryParam(String taskId) {
        this.taskId = taskId;
    }

    public static GenExcelTaskQueryParam create(String taskId) {
        if (GenExcelToolUtils.isBlank(taskId)) {
            throw new GenExcelBusinessException("任务ID >> taskId 不能为空！");
        }
        return new GenExcelTaskQueryParam(taskId);
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }
}

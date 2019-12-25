package com.tool.general.excel.service;

import com.tool.general.excel.common.model.param.GenExcelTaskAddParam;
import com.tool.general.excel.common.model.param.GenExcelTaskQueryParam;
import com.tool.general.excel.common.model.result.GenExcelTaskResult;
import com.tool.general.excel.common.model.support.GenExcelTaskCacheModel;

/**
 * 根据DB配置-导出普通excel通用接口
 *
 * @author mengqiang
 */
public interface GenExcelService {


    /**
     * 根据DB配置模板-动态导出excel
     * 1.先创建任务
     * 2.异步分页导出
     *
     * @param addParam
     * @return 任务ID
     */
    GenExcelTaskResult genExcel(GenExcelTaskAddParam addParam);

    /**
     * 获取导出任务详情
     *
     * @param taskQueryParam 任务ID
     * @return
     */
    GenExcelTaskCacheModel getExportTask(GenExcelTaskQueryParam taskQueryParam);
}
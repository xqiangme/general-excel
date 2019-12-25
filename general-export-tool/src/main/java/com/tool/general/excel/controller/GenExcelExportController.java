package com.tool.general.excel.controller;

import com.tool.general.excel.common.model.param.GenExcelTaskAddParam;
import com.tool.general.excel.common.model.param.GenExcelTaskQueryParam;
import com.tool.general.excel.common.model.result.GenExcelBaseResult;
import com.tool.general.excel.service.GenExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


/**
 * 通用导出公共接口
 *
 * @author mengqiang
 */
@RestController
@RequestMapping("/gen-excel")
public class GenExcelExportController {

    @Autowired
    private GenExcelService genExcelService;

    /**
     * 创建导出任务
     *
     * @param platformId   平台方ID
     * @param templateId   模板ID
     * @param paramContent 参数内容（JSON格式）
     */
    @RequestMapping("/export")
    public GenExcelBaseResult genExcel(@RequestParam("platformId") String platformId,
                                       @RequestParam("templateId") String templateId,
                                       @RequestParam("paramContent") String paramContent) {
        GenExcelTaskAddParam addParam = GenExcelTaskAddParam.create(platformId, templateId, paramContent);
        return GenExcelBaseResult.success(genExcelService.genExcel(addParam));
    }

    /**
     * 查询任务详情
     *
     * @param taskId 任务ID
     */
    @RequestMapping("/getTask")
    public GenExcelBaseResult getExportTask(@RequestParam("taskId") String taskId) {
        GenExcelTaskQueryParam taskQueryParam = GenExcelTaskQueryParam.create(taskId);
        return GenExcelBaseResult.success(genExcelService.getExportTask(taskQueryParam));
    }

}
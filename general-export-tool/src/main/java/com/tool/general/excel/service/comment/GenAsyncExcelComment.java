package com.tool.general.excel.service.comment;

import com.tool.general.excel.dao.bean.ExportTemplateInfo;
import com.tool.general.excel.common.enums.TemplateTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 通用导出-公共异步处理
 *
 * @author mengqiang
 */
@Component
public class GenAsyncExcelComment {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenAsyncExcelComment.class);

    @Autowired
    private GenExcelListExportComment generalExcelListExportComment;

    @Autowired
    private GenTemplateObjBothListExportComment templateObjAndListExportComment;

    /**
     * 异步执行导出
     *
     * @param taskId           任务ID
     * @param template         模板
     * @param businessParamMap 业务查询条件
     * @author mengqiang
     * @date 2019-11-24
     */
    @Async
    public void doGenExcelAsync(String taskId, ExportTemplateInfo template, Map<String, Object> businessParamMap) {

        //普通导出列表
        if (TemplateTypeEnum.ORDINARY_LIST.getValue().equals(template.getTemplateType())) {
            this.printlnLog(taskId, "普通导出列表", template.getTemplateTitle(), businessParamMap);
            generalExcelListExportComment.doGenExcelAsync(taskId, template, businessParamMap);
            return;
        }

        //模板导出列表
        if (TemplateTypeEnum.TEMPLATE_LIST.getValue().equals(template.getTemplateType())) {
            this.printlnLog(taskId, "模板导出列表", template.getTemplateTitle(), businessParamMap);
            generalExcelListExportComment.doGenExcelAsync(taskId, template, businessParamMap);
            return;
        }

        //模板导出对象
        if (TemplateTypeEnum.TEMPLATE_OBJECT.getValue().equals(template.getTemplateType())) {
            this.printlnLog(taskId, "模板导出单个对象", template.getTemplateTitle(), businessParamMap);
            templateObjAndListExportComment.doGenExcelAsync(taskId, template, businessParamMap);
            return;
        }

        //模板导出对象+集合
        if (TemplateTypeEnum.TEMPLATE_OBJECT_AND_LIST.getValue().equals(template.getTemplateType())) {
            this.printlnLog(taskId, "模板导出对象+集合", template.getTemplateTitle(), businessParamMap);
            templateObjAndListExportComment.doGenExcelAsync(taskId, template, businessParamMap);
        }
    }

    private void printlnLog(String taskId, String typeName, String title, Map<String, Object> businessParamMap) {
        LOGGER.info("[{}] >> [公共导出] >> {} >> 模板标题:{} businessParamMap:{}", taskId, typeName, title, businessParamMap);
    }


}
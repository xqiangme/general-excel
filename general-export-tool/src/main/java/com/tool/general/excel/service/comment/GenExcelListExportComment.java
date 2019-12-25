package com.tool.general.excel.service.comment;

import cn.hutool.core.io.FileUtil;
import cn.hutool.poi.excel.BigExcelWriter;
import cn.hutool.poi.excel.ExcelUtil;
import com.alibaba.excel.EasyExcel;
import com.tool.general.excel.common.constants.GenExcelMapperConstant;
import com.tool.general.excel.dao.bean.ExportTemplateInfo;
import com.tool.general.excel.common.constants.GenExcelNumberConstant;
import com.tool.general.excel.common.constants.GenExcelPathConstant;
import com.tool.general.excel.common.constants.GenExcelQueryConstant;
import com.tool.general.excel.common.enums.TemplateTypeEnum;
import com.tool.general.excel.common.exception.GenExcelBusinessException;
import com.tool.general.excel.common.model.support.GenExcelExportModel;
import com.tool.general.excel.parse.ExportFieldParseUtil;
import com.tool.general.excel.parse.model.ExportFieldParseModel;
import com.tool.general.excel.util.GenExcelToolUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 通用导出-公共异步处理
 *
 * @author mengqiang
 */
@Component
public class GenExcelListExportComment extends GenExcelBaeComment {
    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenExcelListExportComment.class);

    /**
     * 导出普通列表或模板列表
     *
     * @param taskId           任务ID
     * @param template         模板
     * @param businessParamMap 业务查询条件
     * @author mengqiang
     * @date 2019-11-24
     */
    public void doGenExcelAsync(String taskId, ExportTemplateInfo template, Map<String, Object> businessParamMap) {

        long start = System.currentTimeMillis();
        try {

            //解析-导出字段配置
            ExportFieldParseModel fieldParseModel = ExportFieldParseUtil.getHeadAndFiledRule(this.getExportFieldsExpArray(template));
            //构建-查询参数
            Map<String, Object> queryParamMap = this.buildQueryParamMap(taskId, template, businessParamMap);
            //执行分页导出
            GenExcelExportModel exportModel = this.doPageExport(taskId, template, queryParamMap, fieldParseModel);

            long end = System.currentTimeMillis();
            //执行成功-更新任务表
            this.updateSuccess((end - start), taskId, exportModel.getDownloadUrl(), exportModel.getRows());
            LOGGER.info("[{}] >> [公共导出] >> 普通列表或模板列表结束 >> 模板标题:{} ,总耗时:{} ms,导出行数:{},businessParamMap:{},downloadUrl:{}",
                    taskId, template.getTemplateTitle(), (end - start), exportModel.getRows(), businessParamMap, exportModel.getDownloadUrl());
        } catch (Exception e) {
            String stackTrace = GenExcelToolUtils.getStackTrace(e);
            LOGGER.error("[{}] >> [公共导出] >> 普通列表或模板列表结束 >> 模板标题:{} , 执行异常了 {}", taskId, template.getTemplateTitle(), stackTrace);
            //执行失败-更新任务表
            this.updateFail((System.currentTimeMillis() - start), taskId, this.dealExceptionMsg(e, stackTrace));
        } finally {
            //刷新任务缓存
            this.refreshTaskCache(taskId);
        }

    }


    /**
     * 分页导出
     */
    private GenExcelExportModel doPageExport(String taskId, ExportTemplateInfo template, Map<String, Object> queryParamMap, ExportFieldParseModel fieldParseModel) {
        AtomicInteger rows = new AtomicInteger(GenExcelNumberConstant.ZERO);
        int startRow = (int) queryParamMap.get(GenExcelQueryConstant.START_ROW);
        //任务临时文件目录
        String tempTaskFilePath = GenExcelPathConstant.getTempTaskFilePath(taskId);
        //excel临时文件目录
        String tempExcelFilePath = GenExcelPathConstant.getTempExcelFilePath(taskId);
        //获取-所有列规则Map
        Map<String, Map<String, Object>> fieldRuleMap = fieldParseModel.getFieldRuleMap();
        int page = 1;
        boolean hasNext;
        String downloadUrl;
        long startTime = System.currentTimeMillis();
        SqlSession sqlSession = null;
        try {
            //根据平台ID获取-当前数据配置
            SqlSessionFactory sqlSessionFactory = this.getCurrentDbSqlSessionFactory(taskId, template);
            //当前平台sqlSession
            sqlSession = sqlSessionFactory.openSession();
            do {
                //开始行号
                queryParamMap.put(GenExcelQueryConstant.START_ROW, startRow);
                long queryDbStartTime = System.currentTimeMillis();
                LOGGER.info("[{}] >> [公共导出] >> 异步导出列表-查询DB开始 >> 当前页码:{} , 已处理行数:{}", taskId, page, rows.intValue());
                //执行通用查询接口
                List<Map<String, Object>> queryByDbList = sqlSession.selectList(GenExcelMapperConstant.QUERY_STATEMENT_PATH, queryParamMap);
                long queryDbTime = System.currentTimeMillis();
                LOGGER.info("[{}] >> [公共导出] >> 异步导出列表-查询DB结束 >> 当前页码:{} , 耗时:{} ms ,返回行数:{}", taskId, page, (queryDbTime - queryDbStartTime), queryByDbList.size());
                if (CollectionUtils.isEmpty(queryByDbList)) {
                    hasNext = false;
                    continue;
                }
                //构建-导出结果集
                List<Map<String, Object>> exportList = this.buildExportList(queryByDbList, fieldRuleMap);
                long buildResTime = System.currentTimeMillis();
                LOGGER.info("[{}] >> [公共导出] >> 异步导出-构建导出结果结束 >> 当前页码:{} , 耗时:{} ms ", taskId, page, (buildResTime - queryDbTime));
                //生成excel临时文件全路径名
                String tempExcelFullFileName = GenExcelPathConstant.getTempExcelFullFileName(tempExcelFilePath, template.getExportFileName(), page);
                //写出excel临时文件
                this.doWriteTempExcel(template, tempExcelFullFileName, fieldParseModel.getExcelHeadMap(), exportList);
                LOGGER.info("[{}] >> [公共导出] >> 异步导出-写出临时文件结束 >> 当前页码:{} , 耗时:{} ms ", taskId, page, (System.currentTimeMillis() - buildResTime));
                //当前页累加
                page++;
                //记录导出总行数
                rows.getAndAdd(exportList.size());
                hasNext = CollectionUtils.isNotEmpty(queryByDbList);
                //开始行累加
                startRow = startRow + (int) queryParamMap.get(GenExcelQueryConstant.PAGE_SIZE);
            } while (hasNext);
            if (GenExcelNumberConstant.ZERO == rows.intValue()) {
                LOGGER.info("[{}] >> [公共导出] >> 无需要导出的数据 ", taskId);
                throw new GenExcelBusinessException("无需要导出的数据！");
            }
            LOGGER.info("[{}] >> [公共导出] >> 异步导出-写出所有临时文件完毕 >> 耗时:{} ms ", taskId, (System.currentTimeMillis() - startTime));
            //执行上传云存储-并返回下载链接
            downloadUrl = this.doDownloadFile(tempTaskFilePath, tempExcelFilePath, template);
        } finally {
            //关闭 sqlSession ,清除临时文件
            this.closeSqlSessionAndRemoveTempFile(sqlSession, tempTaskFilePath);
        }
        return new GenExcelExportModel(rows.intValue(), downloadUrl);
    }

    /**
     * 写出excel文件到临时目录
     *
     * @param tempExcelFullFileName excel临时文件全路径
     * @param headerMap             excel文件标题
     * @param exportList            excel导出内容结果集
     */
    private void doWriteTempExcel(ExportTemplateInfo template, String tempExcelFullFileName, Map<String, String> headerMap, List<Map<String, Object>> exportList) {
        //普通导出列表
        if (TemplateTypeEnum.ORDINARY_LIST.getValue().equals(template.getTemplateType())) {
            this.doWriteTempExcel(tempExcelFullFileName, headerMap, exportList);

        }
        //模板导出列表
        else if (TemplateTypeEnum.TEMPLATE_LIST.getValue().equals(template.getTemplateType())) {
            this.doWriteWithTemplateTempExcel(template, tempExcelFullFileName, exportList);
        }
    }

    /**
     * 写出excel文件到临时目录
     *
     * @param tempExcelFullFileName excel临时文件全路径
     * @param headerMap             excel文件标题
     * @param exportList            excel导出内容结果集
     */
    private void doWriteTempExcel(String tempExcelFullFileName, Map<String, String> headerMap, List<Map<String, Object>> exportList) {
        BigExcelWriter writer = ExcelUtil.getBigWriter(tempExcelFullFileName);
        writer.setHeaderAlias(headerMap);
        // 一次性写出内容，使用默认样式
        writer.write(exportList);
        // 关闭writer，释放内存
        writer.close();
    }

    /**
     * 根据模板-写出excel文件到临时目录
     *
     * @param tempExcelFullFileName excel临时文件全路径
     * @param exportList            excel导出内容结果集
     */
    private void doWriteWithTemplateTempExcel(ExportTemplateInfo template, String tempExcelFullFileName, List<Map<String, Object>> exportList) {
        //模板文件本地地址
        String templateLocalFileName = this.getTemplateFile(template);
        //excel临时文件路径
        String tempExcelPath = new File(tempExcelFullFileName).getParent();
        //父路径不存在则先创建
        if (!FileUtil.exist(tempExcelPath)) {
            FileUtil.mkdir(tempExcelPath);
        }
        //根据模板导出列表
        EasyExcel.write(tempExcelFullFileName).withTemplate(templateLocalFileName).sheet().doFill(exportList);
    }


}
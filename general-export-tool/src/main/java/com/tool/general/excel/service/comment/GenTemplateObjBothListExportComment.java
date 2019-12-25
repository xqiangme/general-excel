package com.tool.general.excel.service.comment;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import com.alibaba.excel.write.metadata.fill.FillConfig;
import com.tool.general.excel.common.constants.GenExcelMapperConstant;
import com.tool.general.excel.common.constants.GenExcelNumberConstant;
import com.tool.general.excel.common.constants.GenExcelPathConstant;
import com.tool.general.excel.common.constants.GenExcelQueryConstant;
import com.tool.general.excel.dao.bean.ExportTemplateInfo;
import com.tool.general.excel.common.exception.GenExcelBusinessException;
import com.tool.general.excel.common.model.support.GenExcelExportModel;
import com.tool.general.excel.parse.ExportFieldParseUtil;
import com.tool.general.excel.parse.model.ExportFieldParseModel;
import com.tool.general.excel.util.GenExcelStringPool;
import com.tool.general.excel.util.GenExcelToolUtils;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 模板导出对象或对象+列表-导出组件
 *
 * @author mengqiang
 */
@Component
public class GenTemplateObjBothListExportComment extends GenExcelBaeComment {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenTemplateObjBothListExportComment.class);

    /**
     * 根据模板导出对象或对象+列表接口
     *
     * @param taskId           任务ID
     * @param template         父模板
     * @param businessParamMap 业务查询条件
     * @author mengqiang
     * @date 2019-11-24
     */
    public void doGenExcelAsync(String taskId, ExportTemplateInfo template, Map<String, Object> businessParamMap) {

        long start = System.currentTimeMillis();
        try {

            //解析-父模板导出字段配置
            ExportFieldParseModel parentExportModel = ExportFieldParseUtil.getHeadAndFiledRule(this.getExportFieldsExpArray(template));
            //构建-父模板查询参数
            Map<String, Object> queryParamMap = this.buildQueryParamMap(taskId, template, businessParamMap);
            //执行导出
            GenExcelExportModel exportModel = this.doExport(taskId, template, queryParamMap, businessParamMap, parentExportModel);

            long end = System.currentTimeMillis();
            //执行成功-更新任务表
            this.updateSuccess((end - start), taskId, exportModel.getDownloadUrl(), exportModel.getRows());
            LOGGER.info("[{}] >> [公共导出] >> 根据模板导出对象OR列表结束 >> 模板标题:{} ,总耗时:{} ms,导出行数:{},businessParamMap:{},downloadUrl:{}",
                    taskId, template.getTemplateTitle(), (end - start), exportModel.getRows(), businessParamMap, exportModel.getDownloadUrl());
        } catch (Exception e) {
            String stackTrace = GenExcelToolUtils.getStackTrace(e);
            LOGGER.error("[{}] >> [公共导出] >> 根据模板导出对象OR列表结束 >> 模板标题:{} , 执行异常了 {}", taskId, template.getTemplateTitle(), stackTrace);
            //执行失败-更新任务表
            this.updateFail((System.currentTimeMillis() - start), taskId, this.dealExceptionMsg(e, stackTrace));
        } finally {
            //刷新任务缓存
            this.refreshTaskCache(taskId);
        }

    }

    /**
     * 根据模板导出
     */
    private GenExcelExportModel doExport(String taskId, ExportTemplateInfo template, Map<String, Object> queryParamMap,
                                         Map<String, Object> businessParamMap, ExportFieldParseModel parentExportModel) {
        AtomicInteger rows = new AtomicInteger(GenExcelNumberConstant.ZERO);
        //任务临时文件目录
        String tempTaskFilePath = GenExcelPathConstant.getTempTaskFilePath(taskId);
        //excel临时文件目录
        String tempExcelFilePath = GenExcelPathConstant.getTempExcelFilePath(taskId);
        //生成excel临时文件全路径名
        String tempExcelFullFileName = GenExcelPathConstant.getTempExcelFullFileName(tempExcelFilePath, template.getExportFileName(), 1);
        //若父文件夹不存在-创建父文件夹
        this.mkdirParentFolder(tempExcelFullFileName);
        //模板文件本地文件地址
        String templateLocalFile = this.getTemplateFile(template);

        String downloadUrl;
        long startTime = System.currentTimeMillis();
        SqlSession sqlSession = null;
        try {
            //根据平台ID获取-当前数据配置
            SqlSessionFactory sqlSessionFactory = this.getCurrentDbSqlSessionFactory(taskId, template);
            //当前平台sqlSession
            sqlSession = sqlSessionFactory.openSession();
            //执行通用查询接口
            Map<String, Object> parentObjFromDB = this.getParentObjFromDB(rows, sqlSession, queryParamMap);
            //构建-父级导出结果
            Map<String, Object> parentExportObj = this.buildExportObj(parentObjFromDB, parentExportModel.getFieldRuleMap());
            //构建-excel写出对象
            ExcelWriter excelWriter = EasyExcel.write(tempExcelFullFileName).withTemplate(templateLocalFile).build();
            WriteSheet writeSheet = EasyExcel.writerSheet().build();

            //是否存在子集
            ExportTemplateInfo childTemplate = getChildTemplate(template);
            if (null != childTemplate) {
                //存在子集-则写出子集列表
                this.exportChildList(taskId, childTemplate, sqlSession, businessParamMap, excelWriter, writeSheet, rows);
            }

            //写出父级主体信息
            excelWriter.fill(parentExportObj, writeSheet);
            //关闭流
            excelWriter.finish();
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
     * 获取子集模板
     */
    private ExportTemplateInfo getChildTemplate(ExportTemplateInfo template) {
        if (GenExcelStringPool.NUM_ZERO.equals(template.getTemplateChildId())) {
            return null;
        }
        //若存在子模板
        return exportTemplateInfoDAO.getByTemplateId(template.getTemplateChildId());
    }

    /**
     * 导出子集模板列表
     */
    private void exportChildList(String taskId, ExportTemplateInfo childTemplate, SqlSession sqlSession
            , Map<String, Object> businessParamMap, ExcelWriter excelWriter, WriteSheet writeSheet, AtomicInteger rows) {
        //解析-子模板导出字段配置
        ExportFieldParseModel childFieldParseModel = ExportFieldParseUtil.getHeadAndFiledRule(this.getExportFieldsExpArray(childTemplate));
        //构建-子模板查询参数
        Map<String, Object> childQueryParamMap = this.buildQueryParamMap(taskId, childTemplate, businessParamMap);

        int page = 1;
        boolean hasNext;
        FillConfig fillConfig = FillConfig.builder().forceNewRow(Boolean.TRUE).build();
        int startRow = (int) childQueryParamMap.get(GenExcelQueryConstant.START_ROW);
        //分页查询DB导出子集列表集合
        do {
            //开始行号
            childQueryParamMap.put(GenExcelQueryConstant.START_ROW, startRow);
            long queryDbStartTime = System.currentTimeMillis();
            LOGGER.info("[{}] >> [公共导出] >> 异步模板导出对象OR列表 - 查询DB开始 >> 当前页码:{} , 已处理行数:{}", taskId, page, rows.intValue());
            //执行通用查询接口
            List<Map<String, Object>> queryChildByDbList = sqlSession.selectList(GenExcelMapperConstant.QUERY_STATEMENT_PATH, childQueryParamMap);
            long queryDbTime = System.currentTimeMillis();
            LOGGER.info("[{}] >> [公共导出] >> 异步模板导出对象OR列表 - 查询DB结束 >> 当前页码:{} , 耗时:{} ms ,返回行数:{}", taskId, page, (System.currentTimeMillis() - queryDbStartTime), queryChildByDbList.size());

            if (CollectionUtils.isEmpty(queryChildByDbList)) {
                hasNext = false;
                continue;
            }
            //构建-导出结果集
            List<Map<String, Object>> exportList = this.buildExportList(queryChildByDbList, childFieldParseModel.getFieldRuleMap());
            //写出子集列表
            excelWriter.fill(exportList, fillConfig, writeSheet);
            LOGGER.info("[{}] >> [公共导出] >> 异步导出-构建导出结果结束 >> 当前页码:{} , 耗时:{} ms ", taskId, page, (System.currentTimeMillis() - queryDbTime));
            //当前页累加
            page++;
            //记录导出总行数
            rows.getAndAdd(exportList.size());
            hasNext = CollectionUtils.isNotEmpty(queryChildByDbList);
            //开始行累加
            startRow = startRow + (int) childQueryParamMap.get(GenExcelQueryConstant.PAGE_SIZE);
        } while (hasNext);
    }

    private Map<String, Object> getParentObjFromDB(AtomicInteger rows, SqlSession sqlSession, Map<String, Object> queryParamMap) {
        //执行通用查询接口
        List<Map<String, Object>> queryParentFromDbList = sqlSession.selectList(GenExcelMapperConstant.QUERY_STATEMENT_PATH, queryParamMap);
        if (CollectionUtils.isEmpty(queryParentFromDbList)) {
            throw new GenExcelBusinessException("导出集合为空！");
        }
        rows.getAndIncrement();
        return queryParentFromDbList.get(GenExcelNumberConstant.ZERO);
    }

}
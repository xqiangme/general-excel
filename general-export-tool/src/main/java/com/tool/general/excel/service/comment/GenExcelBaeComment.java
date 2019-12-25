package com.tool.general.excel.service.comment;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ZipUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.OSS;
import com.tool.general.excel.common.constants.GenExcelCacheConstant;
import com.tool.general.excel.common.constants.GenExcelNumberConstant;
import com.tool.general.excel.common.constants.GenExcelPathConstant;
import com.tool.general.excel.common.constants.GenExcelQueryConstant;
import com.tool.general.excel.common.enums.TaskExportStatusEnum;
import com.tool.general.excel.common.enums.TemplateRefreshFlagEnum;
import com.tool.general.excel.common.exception.GenExcelBaseException;
import com.tool.general.excel.common.exception.GenExcelBusinessException;
import com.tool.general.excel.common.model.support.GenExcelTaskCacheModel;
import com.tool.general.excel.config.GenExcelDataSourceConfig;
import com.tool.general.excel.config.GenExcelOssProperty;
import com.tool.general.excel.config.redis.RedisCache;
import com.tool.general.excel.dao.bean.ExportTaskInfo;
import com.tool.general.excel.dao.bean.ExportTemplateInfo;
import com.tool.general.excel.dao.mapper.ExportTaskInfoMapper;
import com.tool.general.excel.dao.mapper.ExportTemplateInfoMapper;
import com.tool.general.excel.util.*;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author mengqiang
 * s
 */
@Component
public class GenExcelBaeComment {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenExcelBaeComment.class);

    @Autowired
    protected RedisCache redisCache;

    @Autowired
    protected GenExcelOssProperty ossProperty;

    @Autowired
    private ExportTaskInfoMapper exportTaskInfoDAO;

    @Autowired
    protected ExportTemplateInfoMapper exportTemplateInfoDAO;

    @Autowired
    protected GenExcelDataSourceConfig genExcelSqlSessionFactory;

    protected final static int HTTP_DOWNLOAD_TIME_OUT = 1200000;

    /**
     * 获取模板文件
     *
     * @author mengqiang
     */
    protected String getTemplateFile(ExportTemplateInfo template) {

        if (GenExcelToolUtils.isBlank(template.getTemplateFileUrl())) {
            throw new GenExcelBusinessException("[公共导出] >> Gen-excel >> 模板文件下载地址不为空！");
        }

        //是否需要更新模板文件
        if (TemplateRefreshFlagEnum.UN_REFRESH.getValue().equals(template.getTemplateRefreshFlag())) {
            return this.downloadFileToLocal(template);
        }

        //本地模板文件存在
        if (GenExcelToolUtils.isNotBlank(template.getTemplateFileLocalPath()) &&
                FileUtil.exist(template.getTemplateFileLocalPath())) {
            return template.getTemplateFileLocalPath();
        }
        return this.downloadFileToLocal(template);
    }

    /**
     * 下载模板文件到本地
     */
    protected String downloadFileToLocal(ExportTemplateInfo template) {
        //临时文件下载锁
        String lockKey = GenExcelCacheConstant.getTemplateFileDownloadLockKey(template.getTemplateId());
        RLock redisLock = redisCache.getRedisLock(lockKey);
        try {
            //尝试加锁，等待时间3s, 锁超时时间60秒
            if (!redisLock.tryLock(GenExcelNumberConstant.THREE, GenExcelNumberConstant.SIXTY, TimeUnit.SECONDS)) {
                throw new GenExcelBusinessException("获取模板文件锁失败！");
            }


            String fileUrl = template.getTemplateFileUrl();
            //本地临时文件全路径名
            String fileLocalPath = GenExcelPathConstant.getTempLateFilFullPath(template.getTemplateId(), this.getFileSuffix(fileUrl));

            //下载到本地临时路径
            boolean downloadFlag = downloadFileToLocal(template.getTemplateFileUrl(), fileLocalPath);
            if (!downloadFlag) {
                throw new GenExcelBusinessException("模板文件下载失败！");
            }
            //删除原先的临时文件
            if (FileUtil.exist(template.getTemplateFileLocalPath())) {
                FileUtil.del(template.getTemplateFileLocalPath());
            }
            //更新表中模板文件信息
            exportTemplateInfoDAO.updateTemplateFileMsg(template.getTemplateId(), fileLocalPath, TemplateRefreshFlagEnum.ALREADY_REFRESH.getValue());
            return fileLocalPath;
        } catch (Exception e) {
            LOGGER.error("[公共导出] >> Gen-excel >> 模板文件下载失败 > fileUrl:{}, stack:{}", template.getTemplateFileUrl(), GenExcelToolUtils.getStackTrace(e));
            throw new GenExcelBusinessException("下载模板文件出错！");
        } finally {
            redisLock.unlock();
        }
    }

    /**
     * 任务执行-成功更新
     *
     * @param taskId 任务ID
     */
    public int updateSuccess(Long taskDual, String taskId, String downloadUrl, Integer taskRows) {
        return exportTaskInfoDAO.updateSuccess(taskDual, taskId, downloadUrl, taskRows, TaskExportStatusEnum.SUCCESS.getValue());
    }

    /**
     * 任务执行-失败更新
     *
     * @param taskId 任务ID
     */
    public int updateFail(Long taskDual, String taskId, String remarks) {
        return exportTaskInfoDAO.updateFail(taskDual, taskId, remarks, TaskExportStatusEnum.FAIL.getValue());
    }

    /**
     * 刷新任务缓存
     *
     * @param taskId 任务ID
     */
    public void refreshTaskCache(String taskId) {
        //查询最新任务
        ExportTaskInfo taskInfoDO = exportTaskInfoDAO.getByTaskId(taskId);
        GenExcelTaskCacheModel cacheModel = GenExcelBeanCopierUtil.copy(taskInfoDO, GenExcelTaskCacheModel.class);
        //任务缓存key
        String taskCacheKey = GenExcelCacheConstant.getTaskCacheKey(taskId);
        //刷新缓存
        redisCache.putString(taskCacheKey, JSON.toJSONString(cacheModel), GenExcelCacheConstant.TASK_CACHE_EXPIRED);
    }

    /**
     * 从文件URL中获取文件后缀
     */
    private String getFileSuffix(String fileUrl) {
        //分隔符 "/"
        String fileMsg = fileUrl.substring(fileUrl.lastIndexOf(GenExcelStringPool.SLASH) + 1);
        if (fileMsg.contains(GenExcelStringPool.QUESTION_MARK)) {
            fileMsg = fileMsg.substring(0, fileMsg.lastIndexOf(GenExcelStringPool.QUESTION_MARK));
        }
        if (fileMsg.contains(GenExcelStringPool.DOT)) {
            fileMsg = fileMsg.substring(fileMsg.lastIndexOf(GenExcelStringPool.DOT) + 1);
        }
        return fileMsg;
    }


    /**
     * 下载文件到本地
     *
     * @param fileUrl
     * @param localPath
     * @return
     */
    protected boolean downloadFileToLocal(String fileUrl, String localPath) {
        long currentTime = System.currentTimeMillis();
        long downloadSize;
        try {
            downloadSize = HttpRequest.get(fileUrl).timeout(HTTP_DOWNLOAD_TIME_OUT).executeAsync().writeBody(new File(localPath));
        } catch (Exception e) {
            LOGGER.info("[公共导出] >> Gen-excel >> 下载文件失败 fileUrl:{} , localPath:{}", fileUrl, localPath);
            throw new GenExcelBusinessException(" 下载文件失败！");
        }
        LOGGER.info("[公共导出] >> Gen-excel >> 下载临时存储路径={},请求参数={},耗时={}ms", localPath, fileUrl, System.currentTimeMillis() - currentTime);
        return downloadSize > 0;
    }

    /**
     * 构导出结果集
     *
     * @param queryByDbList 查询自DB的结果集
     * @param fieldRuleMap  字段规则配置Map
     */
    protected List<Map<String, Object>> buildExportList(List<Map<String, Object>> queryByDbList, Map<String, Map<String, Object>> fieldRuleMap) {
        //导出结果明细对象
        Map<String, Object> exportItem = null;
        //构建-导出结果集
        List<Map<String, Object>> exportList = new ArrayList<>(queryByDbList.size());
        //循环查询自DB的集合
        for (Map<String, Object> queryValue : queryByDbList) {
            //导出明细对象
            exportItem = new HashMap<>(queryValue.size());
            //处理导出对象信息
            this.dealExportObjMap(queryValue, fieldRuleMap, exportItem);
            //添加到导出结果集
            exportList.add(exportItem);
        }
        return exportList;
    }

    /**
     * 构导出结果对象
     *
     * @param queryByDbObj 查询自DB的结果对象
     * @param fieldRuleMap 字段规则配置Map
     */
    protected Map<String, Object> buildExportObj(Map<String, Object> queryByDbObj, Map<String, Map<String, Object>> fieldRuleMap) {
        //构建-导出结果集
        Map<String, Object> exportObjMap = new HashMap<>();
        return this.dealExportObjMap(queryByDbObj, fieldRuleMap, exportObjMap);
    }

    /**
     * 构导出结果对象-根据规则处理
     *
     * @param queryByDbObj 查询自DB的结果对象
     * @param fieldRuleMap 字段规则配置Map
     */
    private Map<String, Object> dealExportObjMap(Map<String, Object> queryByDbObj,
                                                 Map<String, Map<String, Object>> fieldRuleMap, Map<String, Object> exportObjMap) {
        //循环查询自DB集合中当对象Map(大小即字段属性长度)
        for (Map.Entry<String, Object> item : queryByDbObj.entrySet()) {
            //是否包含导出列
            if (!fieldRuleMap.containsKey(item.getKey())) {
                continue;
            }
            Object value = item.getValue();
            //当前列规则Map
            Map<String, Object> filedRuleItemMap = fieldRuleMap.get(item.getKey());
            //处理当前字段内容
            value = GenExcelValueRuleDealUtil.dealValue(value, filedRuleItemMap);
            exportObjMap.put(item.getKey(), value);
        }
        return exportObjMap;
    }

    /**
     * 执行上传文件到云存储，并返回下载链接
     *
     * @param tempTaskFilePath  任务临时文件地址
     * @param tempExcelFilePath excel临时文件地址
     * @param template          模板信息
     */
    protected String doDownloadFile(String tempTaskFilePath, String tempExcelFilePath, ExportTemplateInfo template) {
        //文件目录
        if (!FileUtil.isDirectory(tempExcelFilePath)) {
            throw new GenExcelBusinessException("任务临时文件夹 {0} 不存在,请检查配置", tempExcelFilePath);
        }
        List<String> excelFileList = FileUtil.listFileNames(tempExcelFilePath);
        if (CollectionUtils.isEmpty(excelFileList)) {
            throw new GenExcelBusinessException("任务临时文件夹 {0} 下无文件,请检查配置", tempExcelFilePath);
        }
        String fileName = template.getExportFileName();
        if (GenExcelToolUtils.isBlank(fileName)) {
            fileName = template.getTemplateTitle();
        }
        String uploadFileLocalPath;
        String uploadFileName;
        //上传压缩包文件
        if (excelFileList.size() > GenExcelNumberConstant.ONE) {
            uploadFileLocalPath = GenExcelPathConstant.getTempZipFullFileName(tempTaskFilePath, fileName);
            //压缩excel文件夹
            ZipUtil.zip(tempExcelFilePath, uploadFileLocalPath);
            //创建云存储服务器文件名 示例：（oss存储）文件名_yyyyMMddHHmmss+4位随机数
            uploadFileName = GenExcelPathConstant.getCloudZipFileName(template.getPlatformId(), template.getTemplateId(), fileName);
        } else {
            //上传单个excel文件
            String excelFileName = excelFileList.get(GenExcelNumberConstant.ZERO);
            //当前临时excel文件全路径
            uploadFileLocalPath = GenExcelPathConstant.getTempExcelFullFileName(tempExcelFilePath, excelFileName);
            //创建云存储服务器文件名 示例：（oss存储）文件名_yyyyMMddHHmmss+4位随机数
            uploadFileName = GenExcelPathConstant.getCloudExcelFileName(template.getPlatformId(), template.getTemplateId(), fileName);
        }
        //OSS客户端-上传
        OSS ossClient = GenExcelOssUtil.getAliOssConfig(ossProperty.getEndpoint(), ossProperty.getAccessId(), ossProperty.getAccessKey());
        GenExcelOssUtil.uploadFile(ossProperty.getBucket(), uploadFileName, uploadFileLocalPath, ossClient);

        //永久有效
        if (template.getDownloadUrlExpire() < GenExcelNumberConstant.ZERO) {
            return GenExcelOssUtil.getFileUrl(ossProperty.getEndpoint(), ossProperty.getBucket(), uploadFileName);
        }

        //自定义效期
        return GenExcelOssUtil.getExpiredFileUrl(ossProperty.getBucket(), uploadFileName, ossClient, template.getDownloadUrlExpire());
    }


    /**
     * 查询参数处理
     * 根据模板赋值
     * 1.查询字段 ;
     * 2.检索表名;
     * 3.排序方式 ;
     * 4.分页容量 ;
     */
    protected Map<String, Object> buildQueryParamMap(String taskId, ExportTemplateInfo template, Map<String, Object> queryParam) {
        //分隔符 "|"
        List<String> columnList = Arrays.asList(template.getTableColumns().split(GenExcelStringPool.PIPE_ESCAPE));
        LOGGER.info("[{}] >> [公共导出] >> 构建处理参数 >> 模板标题:{} 字段集:{}", taskId, template.getTemplateTitle(), columnList);

        int initMapSize = queryParam.size() + 16;
        Map<String, Object> paramMap = new LinkedHashMap<>(initMapSize);
        //表名
        String tableNames = this.replaceFirstKeywords(template.getTableNames(), GenExcelQueryConstant.FORM);
        //表名集
        paramMap.put(GenExcelQueryConstant.TABLE_NAMES, tableNames);
        //列名集
        paramMap.put(GenExcelQueryConstant.COLUMN_LIST, columnList);
        //检索条件参数
        paramMap.putAll(queryParam);
        //分页信息
        paramMap.put(GenExcelQueryConstant.START_ROW, GenExcelNumberConstant.ZERO);
        paramMap.put(GenExcelQueryConstant.PAGE_SIZE, template.getPageSize());

        //排序条件
        if (GenExcelToolUtils.isNotBlank(template.getOrderByExp())) {
            String orderByExp = this.replaceFirstKeywords(template.getOrderByExp(), GenExcelQueryConstant.ORDER_BY);
            paramMap.put(GenExcelQueryConstant.ORDER_BY_EXP, orderByExp);
        }

        //检索条件
        String conditionExp = null;
        if (GenExcelToolUtils.isNotBlank(template.getQueryCondition())) {
            //解析动态表达式SQL
            conditionExp = GenExcelXmlSqlParseUtil.getSqlFromXml(taskId, template.getQueryCondition(), paramMap);
        }

        paramMap.put(GenExcelQueryConstant.CONDITION_EXP, conditionExp);
        LOGGER.info("[{}] >> [公共导出] >> 构建处理参数 >> 模板标题:{} , SQL条件 > conditionExp:{}", taskId, template.getTemplateTitle(), conditionExp);
        return paramMap;
    }

    /**
     * 替换开头，关键字
     * 例如：from ,order by
     */
    private String replaceFirstKeywords(String content, String keywordsMatch) {
        if (GenExcelToolUtils.isBlank(content)) {
            return GenExcelStringPool.EMPTY;
        }
        content = content.trim();
        //包含小写关键字处理
        if (content.startsWith(keywordsMatch)) {
            content = content.replaceFirst(keywordsMatch, GenExcelStringPool.EMPTY);
        }
        //包含大写关键字处理
        if (content.startsWith(keywordsMatch.toUpperCase())) {
            content = content.replaceFirst(keywordsMatch.toUpperCase(), GenExcelStringPool.EMPTY);
        }

        return content + GenExcelStringPool.SPACE;
    }

    /**
     * 根据数据库编码，获取当前数据源-SqlSessionFactory;
     */
    protected SqlSessionFactory getCurrentDbSqlSessionFactory(String taskId, ExportTemplateInfo template) {
        if (null == genExcelSqlSessionFactory || GenExcelNumberConstant.ZERO == genExcelSqlSessionFactory.getSqlSessionFactoryMap().size()) {
            throw new GenExcelBusinessException("[公共导出] >> 未配置所属数据源sqlSessionFactory");
        }

        Map<String, SqlSessionFactory> factoryMap = genExcelSqlSessionFactory.getSqlSessionFactoryMap();
        if (!factoryMap.containsKey(template.getDbCode())) {
            throw new GenExcelBusinessException("[公共导出] >> 模板ID:{0} > 未配置所属数据源sqlSessionFactory!", template.getTemplateId());
        }
        LOGGER.info("[{}] >> [公共导出] >> 获取当前DB数据源sqlSessionFactory配置 >> templateId:{}, 模板:{}", taskId, template.getTemplateId(), template.getTemplateTitle());
        return factoryMap.get(template.getDbCode());
    }

    /**
     * 处理执行失败异常日志内容
     */
    protected String dealExceptionMsg(Exception e, String stackTrace) {
        String msg = stackTrace;
        if (e instanceof GenExcelBaseException) {
            msg = e.getMessage() == null ? GenExcelStringPool.EMPTY : e.getMessage();
        }
        if (null == msg) {
            return GenExcelStringPool.EMPTY;
        }

        //处理换行，与空格+长度处理
        msg = GenExcelToolUtils.replaceNewline(msg, GenExcelStringPool.EMPTY)
                .replaceAll(GenExcelStringPool.SPACE, GenExcelStringPool.EMPTY);
        //大于 1000 长度截取
        if (msg.length() > GenExcelNumberConstant.THOUSAND) {
            return msg.substring(GenExcelNumberConstant.ZERO, GenExcelNumberConstant.THOUSAND);
        }
        return msg;
    }

    /**
     * 获取excel列，若配置则取配置
     * 若未配置则取查询DB的字段名，需要去掉别名
     *
     * @param template 模板对象
     * @author mengqiang
     * @date 2019-12-07
     */
    protected String[] getExportFieldsExpArray(ExportTemplateInfo template) {
        //模板中导出字段配置
        if ((GenExcelToolUtils.isNotBlank(template.getExportFieldsExp()))) {
            return template.getExportFieldsExp().split(GenExcelStringPool.PIPE_ESCAPE);
        }

        //模板中不包含点
        if (!template.getTableColumns().contains(GenExcelStringPool.DOT)) {
            return template.getTableColumns().split(GenExcelStringPool.PIPE_ESCAPE);
        }

        //如果包含别名点，则去掉别名点
        String[] fieldsArray = template.getTableColumns().split(GenExcelStringPool.PIPE_ESCAPE);
        for (int i = 0; i < fieldsArray.length; i++) {
            String filed = fieldsArray[i];
            if (filed.contains(GenExcelStringPool.DOT)) {
                fieldsArray[i] = filed.substring(filed.lastIndexOf(GenExcelStringPool.DOT) + 1);
            } else {
                fieldsArray[i] = filed;
            }
        }
        return fieldsArray;
    }


    /**
     * 若父文件夹不存在-创建父文件夹
     */
    protected void mkdirParentFolder(String fullFileName) {
        //excel临时文件路径
        String tempExcelPath = new File(fullFileName).getParent();
        //父路径不存在则先创建
        if (!FileUtil.exist(tempExcelPath)) {
            FileUtil.mkdir(tempExcelPath);
        }
    }

    /**
     * 关闭 SqlSession
     * 移除临时文件
     */
    protected void closeSqlSessionAndRemoveTempFile(SqlSession sqlSession, String tempTaskFilePath) {
        //关闭sqlSession
        if (null != sqlSession) {
            sqlSession.close();
        }
        //无论是否出现异常均清除任务临时文件
        if (FileUtil.exist(tempTaskFilePath)) {
            FileUtil.del(tempTaskFilePath);
        }
    }

}
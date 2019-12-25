package com.tool.general.excel.service;

import com.alibaba.fastjson.JSON;
import com.tool.general.excel.dao.bean.ExportTaskInfo;
import com.tool.general.excel.dao.bean.ExportTemplateInfo;
import com.tool.general.excel.common.constants.GenExcelCacheConstant;
import com.tool.general.excel.common.constants.GenExcelNumberConstant;
import com.tool.general.excel.common.enums.GenExcelTemplateStatusEnum;
import com.tool.general.excel.common.enums.TaskExportStatusEnum;
import com.tool.general.excel.common.enums.TemplateTypeEnum;
import com.tool.general.excel.common.exception.GenExcelBaseException;
import com.tool.general.excel.common.exception.GenExcelBusinessException;
import com.tool.general.excel.common.model.param.GenExcelTaskAddParam;
import com.tool.general.excel.common.model.param.GenExcelTaskQueryParam;
import com.tool.general.excel.common.model.result.GenExcelTaskResult;
import com.tool.general.excel.common.model.support.GenExcelTaskCacheModel;
import com.tool.general.excel.common.model.support.GenExcelTaskExtInfoModel;
import com.tool.general.excel.config.redis.RedisCache;
import com.tool.general.excel.dao.mapper.ExportTaskInfoMapper;
import com.tool.general.excel.dao.mapper.ExportTemplateInfoMapper;
import com.tool.general.excel.service.comment.GenAsyncExcelComment;
import com.tool.general.excel.util.GenExcelBeanCopierUtil;
import com.tool.general.excel.util.GenExcelRandomNumberUtil;
import com.tool.general.excel.util.GenExcelStringPool;
import com.tool.general.excel.util.GenExcelToolUtils;
import org.redisson.api.RLock;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.TimeUnit;

/**
 * 根据DB配置-导出普通excel通用接口
 *
 * @author mengqiang
 */
@Service
public class GenExcelServiceImpl implements GenExcelService {

    /**
     * 日志
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(GenExcelServiceImpl.class);

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private GenAsyncExcelComment genExcelComment;

    @Autowired
    private ExportTaskInfoMapper exportTaskInfoDAO;

    @Autowired
    private ExportTemplateInfoMapper exportTemplateInfoDAO;


    /**
     * 根据DB配置模板-动态导出excel
     * 1.先创建任务
     * 2.异步分页导出
     *
     * @param addParam 模板ID
     * @return 任务ID
     */
    @Override
    public GenExcelTaskResult genExcel(GenExcelTaskAddParam addParam) {
        //查询模板
        ExportTemplateInfo template = exportTemplateInfoDAO.getByTemplateId(addParam.getTemplateId());
        //模板校验
        this.checkTemplate(template, addParam.getPlatformId(), addParam.getTemplateId());
        //新增导出任务
        String taskId = this.doAddTask(template, addParam.getReqParam());

        //异步执行导出
        genExcelComment.doGenExcelAsync(taskId, template, addParam.getReqParam());

        return new GenExcelTaskResult(taskId);
    }

    /**
     * 获取导出任务详情
     *
     * @param taskQueryParam 任务ID
     * @return
     */
    @Override
    public GenExcelTaskCacheModel getExportTask(GenExcelTaskQueryParam taskQueryParam) {
        String taskId = taskQueryParam.getTaskId();
        String cacheKey = GenExcelCacheConstant.getTaskCacheKey(taskId);
        String task = redisCache.getString(cacheKey);
        //缓存存在-直接返回
        if (GenExcelToolUtils.isNotBlank(task)) {
            return JSON.parseObject(task, GenExcelTaskCacheModel.class);
        }

        LOGGER.info("[{}] >> [公共导出] >> 缓存不存在 > 查询DB获取任务结果", taskId);
        //缓存不存在添加一个锁并查询DB
        String lockKey = GenExcelCacheConstant.getTaskLockKey(taskId);
        //获取redis同步锁
        RLock redisLock = redisCache.getRedisLock(lockKey);
        try {
            //尝试加锁，等待时间3s, 锁超时时间60秒
            if (!redisLock.tryLock(GenExcelNumberConstant.THREE, GenExcelNumberConstant.SIXTY, TimeUnit.SECONDS)) {
                throw new GenExcelBusinessException("任务处理中请稍后再试！");
            }

            //等待获得锁后-再次尝试缓存获取
            String taskInfo = redisCache.getString(cacheKey);
            if (GenExcelToolUtils.isNotBlank(taskInfo)) {
                return JSON.parseObject(taskInfo, GenExcelTaskCacheModel.class);
            }

            //查询任务
            ExportTaskInfo taskInfoDO = exportTaskInfoDAO.getByTaskId(taskId);
            if (null == taskInfoDO) {
                throw new GenExcelBusinessException("任务ID:{0} 任务不存在！", taskId);
            }

            GenExcelTaskCacheModel cacheModel = GenExcelBeanCopierUtil.copy(taskInfoDO, GenExcelTaskCacheModel.class);
            //添加到缓存-并返回
            redisCache.putString(cacheKey, JSON.toJSONString(cacheModel), GenExcelCacheConstant.TASK_CACHE_EXPIRED);
            return cacheModel;
        } catch (Exception e) {
            LOGGER.info("[{}] >> [公共导出] >> 获取任务结果异常了 stack:{}", taskId, GenExcelToolUtils.getStackTrace(e));
            if (e instanceof GenExcelBaseException) {
                throw new GenExcelBusinessException(e.getMessage());
            } else {
                throw new GenExcelBusinessException("获取任务失败请稍后再试！");
            }
        } finally {
            redisLock.unlock();
        }
    }

    /**
     * 新增导出任务
     */
    private String doAddTask(ExportTemplateInfo template, Object paramObj) {
        ExportTaskInfo taskInfo = new ExportTaskInfo();
        //任务ID
        taskInfo.setTaskId(GenExcelRandomNumberUtil.createTimeRandomNumber());
        //模板ID
        taskInfo.setTemplateId(template.getTemplateId());
        //平台方ID
        taskInfo.setPlatformId(template.getPlatformId());
        //任务标题
        taskInfo.setTaskTitle(template.getTemplateTitle());
        //任务状态：执行中
        taskInfo.setTaskStatus(TaskExportStatusEnum.IN_EXECUTE.getValue());
        //任务查询参数
        taskInfo.setTaskParam(GenExcelStringPool.EMPTY);
        if (!ObjectUtils.isEmpty(paramObj)) {
            taskInfo.setTaskParam(JSON.toJSONString(paramObj));
        }
        //任务扩展信息
        GenExcelTaskExtInfoModel extInfo = new GenExcelTaskExtInfoModel(template.getPageSize(), template.getDownloadUrlExpire());
        taskInfo.setExtInfo(JSON.toJSONString(extInfo));
        //下载链接
        taskInfo.setDownloadUrl(GenExcelStringPool.EMPTY);
        //新增任务
        exportTaskInfoDAO.insertTask(taskInfo);
        GenExcelTaskCacheModel cacheModel = GenExcelBeanCopierUtil.copy(taskInfo, GenExcelTaskCacheModel.class);
        //添加到缓存
        redisCache.putString(GenExcelCacheConstant.getTaskCacheKey(taskInfo.getTaskId()), JSON.toJSONString(cacheModel), 3600);

        return taskInfo.getTaskId();
    }

    /**
     * 模板前置校验
     *
     * @param template   模板
     * @param platformId 平台方ID
     * @param templateId 模板ID
     */
    private void checkTemplate(ExportTemplateInfo template, String platformId, String templateId) {
        if (null == template) {
            throw new GenExcelBusinessException("ID为{0}模板不存在", templateId);
        }
        if (!template.getPlatformId().equals(platformId)) {
            throw new GenExcelBusinessException("ID为{0}模板,平台归属关系错误!", templateId);
        }
        if (GenExcelTemplateStatusEnum.DISABLE.getStatus().equals(template.getTemplateStatus())) {
            throw new GenExcelBusinessException("ID为{0}模板,已停用!", templateId);
        }
        if (GenExcelToolUtils.isBlank(template.getDbCode())) {
            throw new GenExcelBusinessException("ID为{0}模板,数据库编码不能为空！", templateId);
        }
        if (GenExcelToolUtils.isBlank(template.getTableColumns())) {
            throw new GenExcelBusinessException("ID为{0}模板,查询表字段集不能为空！", templateId);
        }
        if (GenExcelToolUtils.isBlank(template.getTableNames())) {
            throw new GenExcelBusinessException("ID为{0}模板,查询表名集不能为空！", templateId);
        }
        if (GenExcelToolUtils.isBlank(template.getExportFileName())) {
            throw new GenExcelBusinessException("ID为{0}模板,导出文件名不为空！", templateId);
        }
        //是否需要模板下载URL
        if (TemplateTypeEnum.needTemplage(template.getTemplateType())) {
            if (GenExcelToolUtils.isBlank(template.getTemplateFileUrl())) {
                throw new GenExcelBusinessException("ID为{0}模板,根据模板导出,模板下载URL不能为空！", templateId);
            }
        }
    }
}
package com.tool.general.excel.common.constants;

/**
 * 缓存常量类
 *
 * @author mengqiang
 */
public class GenExcelCacheConstant {

    /**
     * 任务缓存，秒数
     */
    public static final long TASK_CACHE_EXPIRED = 3600;

    /**
     * 任务缓存key 前缀
     */
    private static final String TASK_CACHE_KEY_PREFIX = "gen.excel.task.";

    /**
     * 任务缓存锁 前缀
     */
    private static final String TASK_LOCK_CACHE_KEY_PREFIX = "gen.excel.task.lock.";

    /**
     * 模板文件下载锁 前缀
     */
    private static final String TEMPLATE_FILE_DOWNLOAD_LOCK_KEY = "gen.excel.template.download.lock.";


    public static String getTaskCacheKey(String taskId) {
        return TASK_CACHE_KEY_PREFIX + taskId;
    }

    public static String getTaskLockKey(String taskId) {
        return TASK_LOCK_CACHE_KEY_PREFIX + taskId;
    }


    public static String getTemplateFileDownloadLockKey(String templateId) {
        return TEMPLATE_FILE_DOWNLOAD_LOCK_KEY + templateId;
    }

}
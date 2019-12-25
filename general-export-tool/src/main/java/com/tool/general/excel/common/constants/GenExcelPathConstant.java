package com.tool.general.excel.common.constants;


import cn.hutool.core.date.DateUtil;
import com.tool.general.excel.util.GenExcelRandomNumberUtil;

import java.util.Date;

/**
 * 公共-地址常量
 *
 * @author mengqiang
 */
public class GenExcelPathConstant {

    /**
     * 系统用户目录路径
     */
    public final static String SYSTEM_USER_HOME_PATH = System.getProperties().getProperty("user.home");

    /**
     * 云存储- 文件存储路径
     */
    public final static String CLOUD_EXCEL_FILE_PATH = "gen-excel";

    /**
     * 临时文件目录
     */
    public static String getTempTaskFilePath(String taskId) {
        return SYSTEM_USER_HOME_PATH + "/files/temp/gen-excel/" + taskId;
    }

    /**
     * 模板文件目录-本地全路径
     */
    public static String getTempLateFilFullPath(String templateId, String suffix) {
        String fileNameNow = "template/" + GenExcelRandomNumberUtil.createTimeRandomNumber() + "." + suffix;
        return SYSTEM_USER_HOME_PATH + "/files/temp/gen-excel/template" + templateId + "/" + fileNameNow;
    }

    public static String getTempExcelFilePath(String taskId) {
        return getTempTaskFilePath(taskId) + "/excel";
    }

    public static String getTempExcelFullFileName(String tempExcelFilePath, String fileName) {
        return tempExcelFilePath + "/" + fileName;
    }

    public static String getTempExcelFullFileName(String tempExcelFilePath, String fileName, int page) {
        return tempExcelFilePath + "/" + fileName + "-" + page + ".xlsx";
    }

    public static String getTempZipFullFileName(String tempTaskFilePath, String fileName) {
        return tempTaskFilePath + "/" + fileName + ".zip";
    }

    /**
     * 云存储excel文件名
     *
     * @param platformId 平台方ID
     * @param templateId 模板ID
     */
    public static String getCloudExcelFileName(String platformId, String templateId, String fileName) {
        return String.format("%s/%s/%s/%s-%s%s.xlsx", CLOUD_EXCEL_FILE_PATH, platformId, templateId, fileName,
                DateUtil.format(new Date(), "yyyyMMddHHmmss"),
                GenExcelRandomNumberUtil.randomNumeric(6));
    }

    /**
     * 云存储excel压缩包文件名
     *
     * @param platformId 平台方ID
     * @param templateId 模板ID
     */
    public static String getCloudZipFileName(String platformId, String templateId, String fileName) {
        return String.format("%s/%s/%s/%s-%s%s.zip", CLOUD_EXCEL_FILE_PATH, platformId, templateId, fileName,
                DateUtil.format(new Date(), "yyyyMMddHHmmss"),
                GenExcelRandomNumberUtil.randomNumeric(6));
    }

}
package com.tool.general.excel.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * oss 配置类
 * 默认''
 *
 * @author mengqiang
 */
@Component
public class GenExcelOssProperty {

    /**
     * OSS accessid
     */
    private String accessId;

    /**
     * oss secret
     */
    private String accessKey;

    /**
     * oss endpoint
     */
    private String endpoint;

    /**
     * oss bucket
     */
    private String bucket;

    /**
     * 默认文件存储前缀
     */
    private String defaultDir;


    public String getAccessId() {
        return accessId;
    }

    public void setAccessId(String accessId) {
        this.accessId = accessId;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKey() {
        return accessKey;
    }

    public void setAccessKey(String accessKey) {
        this.accessKey = accessKey;
    }

    public String getBucket() {
        return bucket;
    }

    public void setBucket(String bucket) {
        this.bucket = bucket;
    }

    public String getDefaultDir() {
        return defaultDir;
    }

    public void setDefaultDir(String defaultDir) {
        this.defaultDir = defaultDir;
    }
}

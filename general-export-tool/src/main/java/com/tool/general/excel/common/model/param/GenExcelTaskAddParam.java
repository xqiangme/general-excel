package com.tool.general.excel.common.model.param;

import com.alibaba.fastjson.JSONObject;
import com.tool.general.excel.common.exception.GenExcelBusinessException;
import com.tool.general.excel.util.GenExcelToolUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务添加-参数
 *
 * @author mengqiang
 */
public class GenExcelTaskAddParam implements Serializable {

    private static final long serialVersionUID = -1L;
    /**
     * 平台方ID
     */
    private String platformId;

    /**
     * 模板ID
     */
    private String templateId;

    /**
     * 查询参数
     */
    private Map<String, Object> reqParam;

    public GenExcelTaskAddParam() {
    }

    public GenExcelTaskAddParam(String platformId, String templateId, Map<String, Object> reqParam) {
        this.platformId = platformId;
        this.templateId = templateId;
        this.reqParam = reqParam;
    }

    public static GenExcelTaskAddParam create(String platformId, String templateId, String paramContent) {
        if (GenExcelToolUtils.isBlank(platformId)) {
            throw new GenExcelBusinessException("平台方ID >> platformId 不能为空！");
        }
        if (GenExcelToolUtils.isBlank(templateId)) {
            throw new GenExcelBusinessException("模板ID >> templateId 不能为空！");
        }

        Map<String, Object> reqParam = new HashMap<>(0);
        if (GenExcelToolUtils.isNotBlank(paramContent)) {
            reqParam = JSONObject.parseObject(paramContent, Map.class);
        }

        return new GenExcelTaskAddParam(platformId, templateId, reqParam);
    }

    public String getPlatformId() {
        return platformId;
    }

    public void setPlatformId(String platformId) {
        this.platformId = platformId;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public Map<String, Object> getReqParam() {
        return reqParam;
    }

    public void setReqParam(Map<String, Object> reqParam) {
        this.reqParam = reqParam;
    }
}

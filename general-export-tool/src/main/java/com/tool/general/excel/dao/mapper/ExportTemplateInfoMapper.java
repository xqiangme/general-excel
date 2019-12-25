package com.tool.general.excel.dao.mapper;

import com.tool.general.excel.dao.bean.ExportTemplateInfo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;


/**
 * 导出模板表 Mapper
 */
@Repository
public interface ExportTemplateInfoMapper {

    /**
     * 根据模板ID查询模板
     *
     * @param templateId templateId
     * @return ExportTemplateInfoDO
     */
    ExportTemplateInfo getByTemplateId(@Param("templateId") String templateId);

    /**
     * 更新模板-本地临时文件信息
     *
     * @param templateId            templateId
     * @param templateFileLocalPath templateFileLocalPath
     * @param templateRefreshFlag   templateRefreshFlag
     * @return int
     */
    int updateTemplateFileMsg(@Param("templateId") String templateId, @Param("templateFileLocalPath") String templateFileLocalPath, @Param("templateRefreshFlag") Integer templateRefreshFlag);
}

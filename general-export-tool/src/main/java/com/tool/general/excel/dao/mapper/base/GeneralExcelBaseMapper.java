package com.tool.general.excel.dao.mapper.base;

import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

/**
 * 生成 excel 通用查询Mapper
 */
@Mapper
public interface GeneralExcelBaseMapper {

    /**
     * 高级查询
     *
     * @param paramMap
     * @return
     */
    List<Map<String, Object>> listByCondition(Map<String, Object> paramMap);
}

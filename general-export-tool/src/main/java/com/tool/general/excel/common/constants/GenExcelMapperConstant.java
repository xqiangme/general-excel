package com.tool.general.excel.common.constants;

/**
 * 其它常量
 *
 * @author mengqiang
 */
public class GenExcelMapperConstant {

    /**
     * 默认查询通道地址
     */
    public static final String QUERY_STATEMENT_PATH = "com.tool.general.excel.dao.mapper.base.GeneralExcelBaseMapper.listByCondition";

    /**
     * Mapper 类地址
     */
    public static final String MAPPER_ROOT_PACKAGE = "com.tool.general.excel.dao.mapper";

    /**
     * 公共查询Mapper 类地址
     */
    public static final String MAPPER_COMMON_PACKAGE = "com.tool.general.excel.dao.mapper.base";

    /**
     * Mapper xml 文件地址
     */
    public static final String XML_ROOT_LOCATIONS = "classpath*:/mybatis/genexcel/**/*Mapper.xml";

    /**
     * 公共查询-Mapper xml 文件地址
     */
    public static final String XML_COMMON_LOCATIONS = "classpath*:/mybatis/genexcel/base/**/*Mapper.xml";

}
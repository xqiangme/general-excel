package com.tool.general.excel.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据源配置类
 *
 * @author mengqiang
 */
@Component
public class GenExcelDataSourceConfig {
    /**
     * 当前平台数据库 sqlSessionFactory
     */
    private Map<String, SqlSessionFactory> sqlSessionFactoryMap;

    public static GenExcelDataSourceConfig initDefault() {
        return initDefault(16);
    }

    public static GenExcelDataSourceConfig initDefault(int size) {
        GenExcelDataSourceConfig genExcelSqlSessionFactory = new GenExcelDataSourceConfig();
        genExcelSqlSessionFactory.setSqlSessionFactoryMap(new HashMap<>(size));
        return genExcelSqlSessionFactory;
    }

    public GenExcelDataSourceConfig addSqlSessionFactory(String dbCode, SqlSessionFactory sqlSessionFactory) {
        if (null == sqlSessionFactoryMap) {
            sqlSessionFactoryMap = new HashMap<>(16);
        }
        sqlSessionFactoryMap.put(dbCode, sqlSessionFactory);
        return this;
    }

    public Map<String, SqlSessionFactory> getSqlSessionFactoryMap() {
        return sqlSessionFactoryMap;
    }

    public void setSqlSessionFactoryMap(Map<String, SqlSessionFactory> factoryMap) {
        this.sqlSessionFactoryMap = factoryMap;
    }
}
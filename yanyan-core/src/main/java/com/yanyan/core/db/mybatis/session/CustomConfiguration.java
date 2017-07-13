package com.yanyan.core.db.mybatis.session;

import com.yanyan.core.db.mybatis.binding.CustomMapperRegistry;
import org.apache.ibatis.binding.MapperRegistry;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;

/**
 * User: Saintcy
 * Date: 2017/3/29
 * Time: 14:43
 */
public class CustomConfiguration extends Configuration {
    protected CustomMapperRegistry mapperRegistry = new CustomMapperRegistry(this);

    public CustomConfiguration(Environment environment) {
        super(environment);
    }

    public CustomConfiguration() {
        super();
    }

    public MapperRegistry getMapperRegistry() {
        return mapperRegistry;
    }

    @Override
    public void addMappers(String packageName, Class<?> superType) {
        mapperRegistry.addMappers(packageName, superType);
    }

    @Override
    public void addMappers(String packageName) {
        mapperRegistry.addMappers(packageName);
    }

    @Override
    public <T> void addMapper(Class<T> type) {
        mapperRegistry.addMapper(type);
    }

    @Override
    public <T> T getMapper(Class<T> type, SqlSession sqlSession) {
        return mapperRegistry.getMapper(type, sqlSession);
    }

    @Override
    public boolean hasMapper(Class<?> type) {
        return mapperRegistry.hasMapper(type);
    }
}

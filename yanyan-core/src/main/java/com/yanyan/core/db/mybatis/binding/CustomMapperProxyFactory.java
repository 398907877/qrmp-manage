package com.yanyan.core.db.mybatis.binding;

import org.apache.ibatis.binding.MapperProxy;
import org.apache.ibatis.binding.MapperProxyFactory;
import org.apache.ibatis.session.SqlSession;

/**
 * User: Saintcy
 * Date: 2017/3/29
 * Time: 15:14
 */
public class CustomMapperProxyFactory<T> extends MapperProxyFactory<T> {
    public CustomMapperProxyFactory(Class<T> mapperInterface) {
        super(mapperInterface);
    }

    public T newInstance(SqlSession sqlSession) {
        final MapperProxy<T> mapperProxy = new CustomMapperProxy<T>(sqlSession, getMapperInterface(), getMethodCache());
        return newInstance(mapperProxy);
    }
}

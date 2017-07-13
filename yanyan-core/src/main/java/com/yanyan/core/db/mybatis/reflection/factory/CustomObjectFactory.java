package com.yanyan.core.db.mybatis.reflection.factory;

import com.yanyan.core.lang.Page;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;

/**
 * 自定义对象工厂，让返回值为Page类型的可以查询列表
 * User: Saintcy
 * Date: 2017/3/26
 * Time: 0:44
 */
public class CustomObjectFactory extends DefaultObjectFactory {
    @Override
    public <T> boolean isCollection(Class<T> type) {
        return super.isCollection(type) || Page.class.isAssignableFrom(type);
    }
}

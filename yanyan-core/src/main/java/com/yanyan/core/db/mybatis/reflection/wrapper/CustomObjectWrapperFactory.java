package com.yanyan.core.db.mybatis.reflection.wrapper;

import com.yanyan.core.lang.Page;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.wrapper.DefaultObjectWrapperFactory;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;

/**
 * 对象包裹工厂，让Page类可以有有包裹
 * 使用参见MetaObject构造函数
 * User: Saintcy
 * Date: 2017/3/26
 * Time: 10:24
 */
public class CustomObjectWrapperFactory extends DefaultObjectWrapperFactory {
    @Override
    public boolean hasWrapperFor(Object object) {
        if (object instanceof Page) {
            return true;
        }
        return super.hasWrapperFor(object);
    }

    @Override
    public ObjectWrapper getWrapperFor(MetaObject metaObject, Object object) {
        if (object instanceof Page) {
            return new PageWrapper(metaObject, (Page) object);
        }

        return super.getWrapperFor(metaObject, object);
    }
}

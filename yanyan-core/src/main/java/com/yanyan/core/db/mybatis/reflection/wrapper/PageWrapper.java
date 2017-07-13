package com.yanyan.core.db.mybatis.reflection.wrapper;

import com.yanyan.core.db.mybatis.PageList;
import com.yanyan.core.lang.Page;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.factory.ObjectFactory;
import org.apache.ibatis.reflection.property.PropertyTokenizer;
import org.apache.ibatis.reflection.wrapper.ObjectWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Page对象包裹
 * User: Saintcy
 * Date: 2017/3/26
 * Time: 10:27
 */
public class PageWrapper implements ObjectWrapper {

    private Page<Object> object;

    public PageWrapper(MetaObject metaObject, Page<Object> object) {
        this.object = object;
    }

    public Object get(PropertyTokenizer prop) {
        throw new UnsupportedOperationException();
    }

    public void set(PropertyTokenizer prop, Object value) {
        throw new UnsupportedOperationException();
    }

    public String findProperty(String name, boolean useCamelCaseMapping) {
        throw new UnsupportedOperationException();
    }

    public String[] getGetterNames() {
        throw new UnsupportedOperationException();
    }

    public String[] getSetterNames() {
        throw new UnsupportedOperationException();
    }

    public Class<?> getSetterType(String name) {
        throw new UnsupportedOperationException();
    }

    public Class<?> getGetterType(String name) {
        throw new UnsupportedOperationException();
    }

    public boolean hasSetter(String name) {
        throw new UnsupportedOperationException();
    }

    public boolean hasGetter(String name) {
        throw new UnsupportedOperationException();
    }


    public MetaObject instantiatePropertyValue(String name, PropertyTokenizer prop, ObjectFactory objectFactory) {
        throw new UnsupportedOperationException();
    }

    public boolean isCollection() {
        return true;
    }

    public void add(Object element) {
        throw new UnsupportedOperationException();
    }

    public <E> void addAll(List<E> element) {
        Page page;
        if (element instanceof Page) {
            page = (Page)element;
        } else if (element instanceof PageList) {
            PageList pageList = ((PageList) element);
            page = new Page();
            List<Object> rows = new ArrayList<Object>();
            rows.addAll(element);
            page.setRows(rows);
            page.setPageNo(pageList.getPageNo());
            page.setPageSize(pageList.getPageSize());
            page.setTotalCount(pageList.getTotalCount());
            page.setTotalPage(pageList.getTotalPage());
        } else {
            page = new Page();
            List<Object> rows = new ArrayList<Object>();
            rows.addAll(element);
            page.setRows(rows);
            page.setPageNo(1);
            page.setPageSize(rows.size());
            page.setTotalCount(rows.size());
            page.setTotalPage(1);
        }
        object.setPageNo(page.getPageNo());
        object.setPageSize(page.getPageSize());
        object.setTotalCount(page.getTotalCount());
        object.setTotalPage(page.getTotalPage());
        object.setRows(page.getRows());
    }

}

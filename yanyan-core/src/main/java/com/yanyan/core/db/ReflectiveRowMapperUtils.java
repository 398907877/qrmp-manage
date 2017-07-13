package com.yanyan.core.db;


import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapper;

import java.util.HashMap;
import java.util.Map;

/**
 * 通过反射方式将结果数据映射到对象里面
 * User: Saintcy
 * Date: 2015/7/6
 * Time: 16:08
 */
public class ReflectiveRowMapperUtils {
    private static final Map<Class, RowMapper> rowMapperMap = new HashMap<Class, RowMapper>();

    public static <T> RowMapper<T> getRowMapper(Class<T> mappedClass) {
        RowMapper rowMapper = rowMapperMap.get(mappedClass);
        if (rowMapper == null) {
            synchronized (rowMapperMap) {
                rowMapper = BeanPropertyRowMapper.newInstance(mappedClass);
                ((BeanPropertyRowMapper) rowMapper).setPrimitivesDefaultedForNullValue(true);//普通类型可以为null，默认0
                //((BeanPropertyRowMapper) rowMapper).setCheckFullyPopulated(true);//sql结果需要包含bean中的所有字段
            }
        }

        return rowMapper;
    }
}

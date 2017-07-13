package com.yanyan.core.db.mybatis;

import org.springframework.core.NamedThreadLocal;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;

/**
 * 为了在调用父接口时，可以得到子接口类名
 * MapperProxy.invoke()=>MapperMethod.execute()=>MethodSignature.convertArgsToSqlCommandParam()
 * 此方式在v3.4.2版本上进行定制的，如果在其他版本上可能会有问题，希望Mybatis能添加子接口传递参数给父接口的方法:-)
 * User: Saintcy
 * Date: 2017/3/29
 * Time: 13:51
 */
public class MybatisThreadLocal {
    protected static Map<Class, Map<String, Object>> propertiesMap = new HashMap<Class, Map<String, Object>>();
    protected static ThreadLocal<Class> interfaceThreadLocal = new NamedThreadLocal<Class>("mapperInterface");//当前调用的方法对象

    public static void addMapperInterface(Class mapperInterface) {
        interfaceThreadLocal.set(mapperInterface);
        //将接口静态字段加入属性中
        Map<String, Object> properties = new HashMap<String, Object>();
        Field[] fields = mapperInterface.getFields();
        if (fields != null) {
            for (Field field : fields) {
                if (Modifier.isStatic(field.getModifiers())) {//必须是静态字段
                    field.setAccessible(true);
                    try {
                        properties.put(field.getName(), field.get(mapperInterface));
                    } catch (Exception e) {

                    }
                }
            }
        }
        //将家口名称放入属性中
        properties.put("_FINAL_INTERFACE", mapperInterface.getName());
        propertiesMap.put(mapperInterface, properties);
    }

    public static Map<String, Object> getInterfaceVariables() {
        Map<String, Object> properties = new HashMap<String, Object>();
        Class clazz = interfaceThreadLocal.get();
        if (clazz != null) {
            properties.putAll(propertiesMap.get(clazz));
        }

        return properties;
    }
}

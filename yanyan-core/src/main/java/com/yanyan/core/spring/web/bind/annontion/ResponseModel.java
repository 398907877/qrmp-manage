package com.yanyan.core.spring.web.bind.annontion;


import java.lang.annotation.*;

/**
 * 返回模型数据，指定模型中返回的数据的关键字
 * User: Saintcy
 * Date: 2016/1/3
 * Time: 16:12
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ResponseModel {
    String [] value();
}
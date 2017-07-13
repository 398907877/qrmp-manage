package com.yanyan.temp.core.spring;

/**
 * 未登录错误
 * User: Saintcy
 * Date: 2016/1/31
 * Time: 20:23
 */
public class NotLoggedOnException extends RuntimeException {
    public NotLoggedOnException() {
        super("You do not have login.");
    }
}

package com.yanyan.temp.core.spring;

/**
 * 未授权错误
 * User: Saintcy
 * Date: 2016/1/31
 * Time: 20:26
 */
public class NotAuthorizedException extends RuntimeException {
    public NotAuthorizedException() {
        super("You do not have permission.");
    }
}

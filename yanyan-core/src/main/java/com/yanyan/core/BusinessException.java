package com.yanyan.core;

import org.apache.commons.lang3.StringUtils;

/**
 * 业务逻辑错误
 * User: Saintcy
 * Date: 2015/10/18
 * Time: 22:23
 */
public class BusinessException extends RuntimeException {
    protected String code;
    protected Object[] args;

    protected BusinessException() {
        super();
    }

    /**
     *
     * @param code 错误编码
     * @param args 错误消息参数，用于国际化时替换message中的变量，参考{@link org.springframework.context.MessageSource}
     * @param defaultMessage 默认消息
     * @param cause 异常原因
     */
    public BusinessException(String code, Object[] args, String defaultMessage, Throwable cause) {
        super(defaultMessage, cause);
        this.code = code;
        this.args = args;
    }

    public BusinessException(String code, String defaultMessage, Throwable cause) {
        this(code, null, defaultMessage, cause);
    }

    public BusinessException(int code, Object[] args, String defaultMessage, Throwable cause) {
        this(String.valueOf(code), args, defaultMessage, cause);
    }

    public BusinessException(int code, String defaultMessage, Throwable cause) {
        this(code, null, defaultMessage, cause);
    }

    public BusinessException(String code, Object[] args, String defaultMessage) {
        this(code, args, defaultMessage, null);
    }

    public BusinessException(String code, String defaultMessage) {
        this(code, null, defaultMessage, null);
    }


    public BusinessException(int code, Object[] args, String defaultMessage) {
        this(String.valueOf(code), args, defaultMessage, null);
    }

    public BusinessException(int code, String defaultMessage) {
        this(code, null, defaultMessage, null);
    }

    public BusinessException(String code, Object[] args, Throwable cause) {
        this(code, args, null, cause);
    }

    public BusinessException(String code, Throwable cause) {
        this(code, null, null, cause);
    }

    public BusinessException(String code, Object[] args) {
        this(code, args, null, null);
    }

    public BusinessException(String code) {
        this(code, null, null, null);
    }

    public BusinessException(int code, Object[] args, Throwable cause) {
        this(String.valueOf(code), args, cause);
    }

    public BusinessException(int code, Throwable cause) {
        this(code, null, null, cause);
    }

    public BusinessException(int code, Object[] args) {
        this(String.valueOf(code), args, null, null);
    }

    public BusinessException(int code) {
        this(String.valueOf(code));
    }

    public String getCode() {
        return code;
    }

    /**
     * TODO: to support international message
     *
     * @return
     */
    @Override
    public String getMessage() {
        return super.getMessage();
    }

    @Override
    public String getLocalizedMessage() {
        return super.getLocalizedMessage();
    }

    public boolean isErrorOf(String code) {
        return StringUtils.equals(code, this.code);
    }

    public boolean isErrorOf(int code) {
        return isErrorOf(String.valueOf(code));
    }

    @Override
    public String toString() {
        String s = getClass().getName();
        String message = getLocalizedMessage();
        return (message != null) ? (s + ": " + message) : (s + ": " + code);
    }
}

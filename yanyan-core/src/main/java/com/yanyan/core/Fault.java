package com.yanyan.core;

import com.yanyan.core.spring.ModelAndViewException;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 * 错误
 * User: Saintcy
 * Date: 2015/4/3
 * Time: 11:52
 */
public class Fault {
    public static final String SYSTEM_FAULT = "ERR_SYSTEM_FAILURE";
    private String faultCode;
    private String faultString;
    private Object faultDetail;
    private transient Throwable cause;

    public Fault() {
    }

    public Fault(Throwable e) {
        if (e instanceof ModelAndViewException) {
            ModelAndViewException mve = (ModelAndViewException) e;

            this.faultCode = mve.getCode();
            this.faultDetail = mve.getErrors();
        } else if (e instanceof BusinessException) {
            BusinessException be = (BusinessException) e;
            this.faultCode = be.getCode();
            this.faultDetail = be.getMessage();
        } else {
            this.faultCode = SYSTEM_FAULT;
            this.faultDetail = ExceptionUtils.getRootCauseMessage(e);
        }
        this.faultString = e.getMessage();

        cause = e;
    }

    public Fault(String faultCode, String faultString) {
        this.faultCode = faultCode;
        this.faultString = faultString;
    }

    public Fault(String faultCode, String faultString, String faultDetail) {
        this.faultCode = faultCode;
        this.faultString = faultString;
        this.faultDetail = faultDetail;
    }

    public String getFaultCode() {
        return faultCode;
    }

    public void setFaultCode(String faultCode) {
        this.faultCode = faultCode;
    }

    public String getFaultString() {
        return faultString;
    }

    public void setFaultString(String faultString) {
        this.faultString = faultString;
    }

    public Object getFaultDetail() {
        return faultDetail;
    }

    public void setFaultDetail(Object faultDetail) {
        this.faultDetail = faultDetail;
    }

    public Throwable getCause() {
        return this.cause;
    }

    @Override
    public String toString() {
        return "Fault{" +
                "faultCode='" + faultCode + '\'' +
                ", faultString='" + faultString + '\'' +
                ", faultDetail='" + faultDetail + '\'' +
                '}';
    }
}

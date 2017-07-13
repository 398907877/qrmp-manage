package com.yanyan.core.web;

import com.yanyan.core.BusinessException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.ui.ExtendedModelMap;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;

/**
 * 数据提交(http+json/xml)方式的数据响应
 * User: Saintcy
 * Date: 2016/5/31
 * Time: 9:53
 */
public class DataResponse<T> extends ExtendedModelMap {
    public static final String ERRCODE_SUCCESS = "0000";//处理成功
    public static final String ERRMSG_SUCCESS = "成功";
    public static final String ERRCODE_INVALID_APPID = "0001";//appid不合法
    public static final String ERRMSG_INVALID_APPID = "appid不合法";//appid不合法
    public static final String ERRCODE_SIGNATURE_VERIFY_FAIL = "0002";//签名验证失败
    public static final String ERRMSG_SIGNATURE_VERIFY_FAIL = "签名验证失败";//签名验证失败
    public static final String ERRCODE_UNAUTHORIZED = "0003";//没有权限
    public static final String ERRMSG_UNAUTHORIZED = "没有权限";//没有权限
    public static final String ERRCODE_INVALID_PARAM = "0004";//参数错误
    public static final String ERRMSG_INVALID_PARAM = "参数错误";//参数错误
    public static final String ERRCODE_TOKEN_VERIFY_FAIL = "0005";//非法token或已失效
    public static final String ERRMSG_TOKEN_VERIFY_FAIL = "非法token或已失效";//非法token或已失效
    public static final String ERRCODE_SERVICE_NOT_EXIST = "0006";//服务不存在
    public static final String ERRMSG_SERVICE_NOT_EXIST = "服务不存在";//服务不存在
    public static final String ERRCODE_UNDEFINED = "9999";//未知错误
    public static final String ERRMSG_UNDEFINED = "未知错误";//未知错误

    public static DataResponse failure(Throwable t) {
        String errcode = ERRMSG_UNDEFINED;
        String errmsg = StringUtils.isEmpty(t.getMessage()) ? t.toString() : t.getMessage();
        if (t instanceof ConstraintViolationException) {
            StringBuffer sb = new StringBuffer();
            Set<ConstraintViolation<?>> constraintViolations = ((ConstraintViolationException) t).getConstraintViolations();
            for (ConstraintViolation constraintViolation : constraintViolations) {
                sb.append(StringUtils.isEmpty(constraintViolation.getMessage()) ? constraintViolation.toString() : constraintViolation.getMessage()).append("\r\n");
            }
            errmsg = ERRMSG_INVALID_PARAM + ": " + sb.toString();
            errcode = ERRCODE_INVALID_PARAM;
        } else if (t instanceof IllegalArgumentException) {
            errcode = ERRCODE_INVALID_PARAM;
            errmsg = t.getMessage();
        } else if (t instanceof BusinessException) {
            errcode = ((BusinessException) t).getCode();
            errmsg = t.getMessage();
        }

        return failure(errcode, errmsg);
    }

    public static DataResponse failure(String errmsg) {
        return failure(ERRCODE_INVALID_PARAM, errmsg);
    }

    public static DataResponse failure(String errcode, String errmsg) {
        return new DataResponse(errcode, errmsg);
    }

    public static DataResponse success() {
        return new DataResponse(ERRCODE_SUCCESS, ERRMSG_SUCCESS);
    }

    public static DataResponse success(String name, Object data) {
        DataResponse serviceResponse = success();
        if (StringUtils.isEmpty(name)) {
            serviceResponse.addAttribute(data);
        } else {
            serviceResponse.addAttribute(name, data);
        }
        return serviceResponse;
    }

    public static DataResponse success(Object data) {
        return success(null, data);
    }

    public static DataResponse successWithResult(Object data) {
        return success("result", data);
    }

    private DataResponse(String errcode, String errmsg) {
        addAttribute("errcode", errcode);
        addAttribute("errmsg", errmsg);
        addAttribute("success", isSuccess());
    }

    public boolean isSuccess() {
        return StringUtils.equals((String) get("errcode"), ERRCODE_SUCCESS);
    }

    public String getMessage() {
        return (String) get("errmsg");
    }

    public <T> T getAttribute(String name) {
        return (T) get(name);
    }

    @Override
    public String toString() {
        return "DataResponse" + super.toString();
    }
}

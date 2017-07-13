package com.yanyan.data.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 登录Model
 * User: Saintcy
 * Date: 2015/6/4
 * Time: 11:10
 */
@Data
public class LoginModel implements Serializable {
    private String username;
    private String password;
    private boolean rememberMe;
    private String captcha;
    private String token;
}

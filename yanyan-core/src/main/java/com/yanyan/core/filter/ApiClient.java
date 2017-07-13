package com.yanyan.core.filter;

import lombok.Data;

/**
 * API调用者
 * User: Saintcy
 * Date: 2017/4/19
 * Time: 10:13
 */
@Data
public class ApiClient {
    private Long id;
    private String name;
    private String appId;
    private String appSecret;
    private String privateSecret;
}

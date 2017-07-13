package com.yanyan.data.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 密码Model
 * User: Saintcy
 * Date: 2016/6/7
 * Time: 15:59
 */
@Data
public class PasswordModel implements Serializable {
    private Long staff_id;
    private String old_password;
    private String new_password;
}

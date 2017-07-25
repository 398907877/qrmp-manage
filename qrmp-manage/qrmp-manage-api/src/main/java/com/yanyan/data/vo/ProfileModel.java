package com.yanyan.data.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 资料Model
 * User: Saintcy
 * Date: 2016/6/7
 * Time: 15:43
 */
@Data
public class ProfileModel implements Serializable {
    private Long id;
    private String name;
    private String cellphone;
    private String email;
}

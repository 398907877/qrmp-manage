package com.yanyan.data.domain.system.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * 人员角色
 * User: Saintcy
 * Date: 2016/5/20
 * Time: 11:13
 */
@Data
public class RoleVo implements Serializable {
    private Long id;
    private String code;
    private String name;
}

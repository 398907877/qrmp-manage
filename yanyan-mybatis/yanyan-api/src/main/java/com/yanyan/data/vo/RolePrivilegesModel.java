package com.yanyan.data.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 角色权限
 * User: Saintcy
 * Date: 2016/8/15
 * Time: 11:28
 */
@Data
public class RolePrivilegesModel implements Serializable {
    private long role_id;
    private List<Long> permissions;
}

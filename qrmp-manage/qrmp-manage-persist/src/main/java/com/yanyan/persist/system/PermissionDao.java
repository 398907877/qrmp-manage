package com.yanyan.persist.system;

import com.yanyan.data.domain.system.Permission;

import java.util.List;

/**
 * 权限数据存取对象
 * User: Saintcy
 * Date: 2016/5/16
 * Time: 17:33
 */
public interface PermissionDao {
    void insertPermission(Permission permission);

    void updatePermission(Permission permission);

    void deletePermission(Long id);

    void deletePermissionByResource(Long resource_id);

    Permission getPermission(Long id);

    List<Permission> getPermissionByResource(Long resource_id);
}

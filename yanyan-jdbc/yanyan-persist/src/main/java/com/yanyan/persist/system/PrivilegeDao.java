package com.yanyan.persist.system;


import com.yanyan.data.domain.system.Permission;
import com.yanyan.data.domain.system.Resource;

import java.util.List;

/**
 * 权限数据存取类
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
public interface PrivilegeDao {

    void revokeRolePermissions(Long role_id);
    void grantRolePermission(Long role_id, Long permission_id);

    void revokeStaffPermissions(Long staff_id);
    void grantStaffPermission(Long staff_id, Long permission_id);

    void revokeRolePrivilegesOfResource(Long resource_id);
    void revokeStaffPrivilegesOfResource(Long resource_id);
    void revokeRolePrivilegesOfRemovedPermissions(Long resource_id);
    void revokeStaffPrivilegesOfRemovedPermissions(Long resource_id);

    List<Permission> getStaffPrivileges(Long staff_id);
    List<Permission> getRolePrivileges(Long role_id);

    List<Permission> getAllPrivileges(Long staff_id);
    List<Resource> getAccessibleResources(Long staff_id);
}

package com.yanyan.service.system;


import com.yanyan.data.domain.system.Menu;
import com.yanyan.data.domain.system.Permission;

import java.util.List;

/**
 * 权限服务
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 17:23
 */
public interface PrivilegeService {
    /**
     * 赋予角色权限
     *
     * @param role_id
     * @param permissions
     */
    void grantRolePrivileges(Long role_id, List<Long> permissions);

    /**
     * 赋予人员权限
     *
     * @param staff_id
     * @param permissions
     */
    void grantStaffPrivileges(Long staff_id, List<Long> permissions);

    /**
     * 回收人员/角色的资源的所有授权
     *
     * @param resource_id
     */
    void revokePrivilegesOfResource(Long resource_id);

    /**
     * 回收人员/角色的资源中被移除的权限的所有授权
     *
     * @param resource_id 资源id
     */
    void revokePrivilegesOfRemovedPermissions(Long resource_id);

    /**
     * 获取人员权限授权（不包含角色的权限）
     *
     * @return
     */
    List<Permission> getStaffPrivileges(Long staff_id);

    /**
     * 获取角色权限授权
     *
     * @return
     */
    List<Permission> getRolePrivileges(Long role_id);

    /**
     * 获取用户的所有权限，包含角色和权限的
     *
     * @param staff_id
     * @return
     */
    List<String> getAllPrivileges(Long staff_id);

    /**
     * 获取人员可访问的菜单列表（包含角色和人员的权限合集）
     *
     * @param staff_id
     * @return
     */
    List<Menu> getAccessibleMenuList(Long staff_id);
}

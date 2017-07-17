package com.yanyan.service.system;


import com.yanyan.data.domain.system.Role;
import com.yanyan.data.query.system.RoleQuery;
import com.yanyan.core.lang.Page;

import java.util.List;

/**
 * 角色服务
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 16:17
 */
public interface RoleService {
    /**
     * 创建一个新角色
     *
     * @param role
     */
    long createRole(Role role);

    /**
     * 更新角色
     *
     * @param role
     */
    void updateRole(Role role);

    /**
     * 删除角色
     *
     * @param role_id
     */
    void deleteRole(Long role_id);

    /**
     * 获取角色
     *
     * @param role_id
     * @return
     */
    Role getRole(Long role_id);

    /**
     * 获取门户默认管理员
     *
     * @param portal_id
     * @return
     */
    Role getAdminRole(Long portal_id);

    /**
     * 获取门户默认管理员列表
     *
     * @param portal_id
     * @return
     */
    List<Role> getAdminRoleList(List<Long> portal_id);

    /**
     * 查询角色
     *
     * @param query 查询条件
     */
    Page<Role> findRole(RoleQuery query);

    /**
     * 获取门户角色列表
     * @param portal_id
     * @return
     */
    List<Role> getPortalRoleList(Long portal_id);

    /**
     * 检查编码是否唯一
     *
     * @param id
     * @param code
     * @return
     */
    boolean checkRoleCode(Long id, String code);
}

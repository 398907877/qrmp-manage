package com.yanyan.persist.system;

import com.yanyan.data.domain.system.Role;
import com.yanyan.data.query.system.RoleQuery;
import com.yanyan.core.lang.Page;

/**
 * 角色数据存取类
 * User: Saintcy
 * Date: 2015/8/24
 * Time: 17:33
 */
public interface RoleDao {

    void insertRole(Role role);

    void updateRole(Role role);

    void deleteRole(Long role_id);

    void unsetAdmin(Long portal_id);

    Role getRole(Long role_id);

    Page<Role> findRole(RoleQuery filter);
}

package com.yanyan.persist.system.impl;


import com.yanyan.data.domain.system.Permission;
import com.yanyan.data.domain.system.Resource;
import com.yanyan.persist.system.PrivilegeDao;
import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 授权数据存取类
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Repository
public class PrivilegeDaoImpl extends NamedParameterJdbcDaoSupport implements PrivilegeDao {
    private static final String REVOKE_ROLE_PERMISSIONS = "DELETE FROM s_role_permission WHERE role_id = :role_id";
    private static final String GRANT_ROLE_PERMISSION = "INSERT INTO s_role_permission(role_id, permission_id)VALUES(:role_id, :permission_id)";

    private static final String REVOKE_STAFF_PERMISSIONS = "DELETE FROM s_staff_permission WHERE staff_id = :staff_id";
    private static final String GRANT_STAFF_PERMISSION = "INSERT INTO s_staff_permission(staff_id, permission_id)VALUES(:staff_id, :permission_id)";

    private static final String REVOKE_ROLE_PRIVILEGES_OF_RESOURCE =
            "DELETE a FROM s_role_permission a, s_permission b\n" +
                    " WHERE a.permission_id = b.id\n" +
                    "   AND b.resource_id = :resource_id";
    private static final String REVOKE_STAFF_PRIVILEGES_OF_RESOURCE =
            "DELETE a FROM s_staff_permission a, s_permission b\n" +
                    " WHERE a.permission_id = b.id\n" +
                    "   AND b.resource_id = :resource_id";

    private static final String REVOKE_ROLE_PRIVILEGES_OF_REMOVED_PERMISSIONS =
            "DELETE FROM s_role_permission\n" +
                    " WHERE permission_id NOT IN\n" +
                    "       (SELECT id FROM s_permission)";
    private static final String REVOKE_STAFF_PRIVILEGES_OF_REMOVED_PERMISSIONS =
            "DELETE FROM s_staff_permission\n" +
                    " WHERE permission_id NOT IN\n" +
                    "       (SELECT id FROM s_permission)";

    private static final String GET_ROLE_PRIVILEGES =
            "SELECT b.id, b.resource_id,\n" +
                    "       (SELECT t.name FROM s_resource t WHERE t.id = b.resource_id) resource_name,\n" +
                    "       b.code, b.name, b.remarks, b.is_show, b.create_time, b.update_time\n" +
                    "  FROM s_role_permission a, s_permission b\n" +
                    " WHERE a.permission_id = b.id\n" +
                    "   AND a.role_id = :role_id";
    private static final String GET_STAFF_PRIVILEGES =
            "SELECT b.id, b.resource_id,\n" +
                    "       (SELECT t.name FROM s_resource t WHERE t.id = b.resource_id) resource_name,\n" +
                    "       b.code, b.name, b.remarks, b.is_show, b.create_time, b.update_time\n" +
                    "  FROM s_staff_permission a, s_permission b\n" +
                    " WHERE a.permission_id = b.id\n" +
                    "   AND a.staff_id = :staff_id";
    private static final String GET_ALL_PRIVILEGES =
            "SELECT b.id, b.resource_id,\n" +
                    "       (SELECT t.name FROM s_resource t WHERE t.id = b.resource_id) resource_name,\n" +
                    "       b.code, b.name, b.remarks, b.is_show, b.create_time, b.update_time\n" +
                    "  FROM s_staff_permission a, s_permission b\n" +
                    " WHERE a.permission_id = b.id\n" +
                    //"   AND b.is_show = 1\n" +
                    "   AND a.staff_id = :staff_id\n" +
                    "UNION\n" +
                    "SELECT b.id, b.resource_id,\n" +
                    "       (SELECT t.name FROM s_resource t WHERE t.id = b.resource_id) resource_name,\n" +
                    "       b.CODE, b.NAME, b.remarks, b.is_show, b.create_time, b.update_time\n" +
                    "  FROM s_role_permission a, s_permission b\n" +
                    " WHERE a.permission_id = b.id\n" +
                    //"   AND b.is_show = 1\n" +
                    "   AND a.role_id in\n" +
                    "       (SELECT role_id FROM s_staff_role WHERE staff_id = :staff_id)";
    private static final String GET_ACCESSIBLE_RESOURCE_LIST =
            "SELECT id, portal_id, code, name, parent_id, path, url, target, icon,\n" +
                    "       remarks, priority, is_show, create_time, update_time\n" +
                    "  FROM s_resource a\n" +
                    " WHERE a.id IN\n" +
                    "       (SELECT b.resource_id\n" +
                    "          FROM s_staff_permission a, s_permission b\n" +
                    "         WHERE a.permission_id = b.id\n" +
                    //"           AND b.is_show = 1\n" +
                    "           AND a.staff_id = :staff_id\n" +
                    "        UNION\n" +
                    "        SELECT b.resource_id\n" +
                    "          FROM s_role_permission a, s_permission b\n" +
                    "         WHERE a.permission_id = b.id\n" +
                    //"           AND b.is_show = 1\n" +
                    "           AND a.role_id IN\n" +
                    "               (SELECT role_id FROM s_staff_role WHERE staff_id = :staff_id))\n" +
                    " ORDER BY priority";

    public void revokeRolePermissions(Long role_id) {
        this.getNamedParameterJdbcTemplate().update(REVOKE_ROLE_PERMISSIONS, new ParameterBuilder("role_id", role_id).create());
    }

    public void grantRolePermission(Long role_id, Long permission_id) {
        this.getNamedParameterJdbcTemplate().update(GRANT_ROLE_PERMISSION, new ParameterBuilder().put("role_id", role_id).put("permission_id", permission_id).create());
    }

    public void revokeStaffPermissions(Long staff_id) {
        this.getNamedParameterJdbcTemplate().update(REVOKE_STAFF_PERMISSIONS, new ParameterBuilder("staff_id", staff_id).create());
    }

    public void grantStaffPermission(Long staff_id, Long permission_id) {
        this.getNamedParameterJdbcTemplate().update(GRANT_STAFF_PERMISSION, new ParameterBuilder().put("staff_id", staff_id).put("permission_id", permission_id).create());
    }

    public void revokeRolePrivilegesOfResource(Long resource_id) {
        this.getNamedParameterJdbcTemplate().update(REVOKE_ROLE_PRIVILEGES_OF_RESOURCE, new ParameterBuilder("resource_id", resource_id).create());
    }

    public void revokeStaffPrivilegesOfResource(Long resource_id) {
        this.getNamedParameterJdbcTemplate().update(REVOKE_STAFF_PRIVILEGES_OF_RESOURCE, new ParameterBuilder("resource_id", resource_id).create());
    }

    public void revokeRolePrivilegesOfRemovedPermissions(Long resource_id) {
        this.getNamedParameterJdbcTemplate().update(REVOKE_ROLE_PRIVILEGES_OF_REMOVED_PERMISSIONS, new ParameterBuilder("resource_id", resource_id).create());
    }

    public void revokeStaffPrivilegesOfRemovedPermissions(Long resource_id) {
        this.getNamedParameterJdbcTemplate().update(REVOKE_STAFF_PRIVILEGES_OF_REMOVED_PERMISSIONS, new ParameterBuilder("resource_id", resource_id).create());
    }

    public List<Permission> getStaffPrivileges(Long staff_id) {
        return this.getNamedParameterJdbcTemplate().query(GET_STAFF_PRIVILEGES, new ParameterBuilder("staff_id", staff_id).create(), ReflectiveRowMapperUtils.getRowMapper(Permission.class));
    }

    public List<Permission> getRolePrivileges(Long role_id) {
        return this.getNamedParameterJdbcTemplate().query(GET_ROLE_PRIVILEGES, new ParameterBuilder("role_id", role_id).create(), ReflectiveRowMapperUtils.getRowMapper(Permission.class));
    }

    public List<Permission> getAllPrivileges(Long staff_id) {
        return this.getNamedParameterJdbcTemplate().query(GET_ALL_PRIVILEGES, new ParameterBuilder("staff_id", staff_id).create(), ReflectiveRowMapperUtils.getRowMapper(Permission.class));
    }

    public List<Resource> getAccessibleResources(Long staff_id) {
        return this.getNamedParameterJdbcTemplate().query(GET_ACCESSIBLE_RESOURCE_LIST, new ParameterBuilder("staff_id", staff_id).create(), ReflectiveRowMapperUtils.getRowMapper(Resource.class));
    }
}

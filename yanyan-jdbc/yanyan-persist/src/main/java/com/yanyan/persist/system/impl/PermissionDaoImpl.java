package com.yanyan.persist.system.impl;

import com.yanyan.persist.system.PermissionDao;
import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.data.domain.system.Permission;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: Saintcy
 * Date: 2016/5/16
 * Time: 17:45
 */
@Repository
public class PermissionDaoImpl extends NamedParameterJdbcDaoSupport implements PermissionDao {

    private static final String INSERT_PERMISSION =
            "INSERT INTO s_permission\n" +
                    "  (id, resource_id, code, name, remarks, is_show, priority, create_time)\n" +
                    "VALUES\n" +
                    "  (:id, :resource_id, :code, :name, :remarks, :is_show, :priority, :create_time)";
    private static final String UPDATE_PERMISSION =
            "UPDATE s_permission\n" +
                    "   SET code = :code, name = :name, remarks = :remarks, is_show = :is_show, priority = :priority,\n" +
                    "       update_time = :update_time\n" +
                    " WHERE id = :id";
    private static final String DELETE_PERMISSION = "DELETE FROM s_permission WHERE id = :id";
    private static final String GET_PERMISSION = "SELECT id, resource_id, code, name, remarks, is_show, priority, create_time, update_time FROM s_permission a WHERE a.id = :permission_id order by a.priority";
    private static final String GET_PERMISSIONS = "SELECT id, resource_id, code, name, remarks, is_show, priority, create_time, update_time FROM s_permission a WHERE a.resource_id = :resource_id order by a.priority";
    private static final String CLEAR_PERMISSIONS = "DELETE FROM s_permission WHERE resource_id = :resource_id";

    public void insertPermission(Permission permission) {
        this.getNamedParameterJdbcTemplate().update(INSERT_PERMISSION, new ParameterBuilder(permission).create());
        permission.setId(this.getNamedParameterJdbcTemplate().queryForObject("select @@IDENTITY", Long.class));
    }

    public void updatePermission(Permission permission) {
        this.getNamedParameterJdbcTemplate().update(UPDATE_PERMISSION, new ParameterBuilder(permission).create());
    }

    public void deletePermission(Long permission_id) {
        this.getNamedParameterJdbcTemplate().update(DELETE_PERMISSION, new ParameterBuilder("id", permission_id).create());
    }

    public Permission getPermission(Long permission_id) {
        return this.getNamedParameterJdbcTemplate().queryForObject(GET_PERMISSION, new ParameterBuilder("permission_id", permission_id).create(), ReflectiveRowMapperUtils.getRowMapper(Permission.class));
    }

    public void deletePermissionByResource(Long resource_id) {
        this.getNamedParameterJdbcTemplate().update(CLEAR_PERMISSIONS, new ParameterBuilder("resource_id", resource_id).create());
    }

    public List<Permission> getPermissionByResource(Long resource_id) {
        return this.getNamedParameterJdbcTemplate().query(GET_PERMISSIONS, new ParameterBuilder("resource_id", resource_id).create(), ReflectiveRowMapperUtils.getRowMapper(Permission.class));
    }
}

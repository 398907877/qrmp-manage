package com.yanyan.persist.system.impl;


import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.NamedSqlBuilder;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Role;
import com.yanyan.data.query.system.RoleQuery;
import com.yanyan.persist.system.RoleDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 角色数据存取类
 * User: Saintcy
 * Date: 2015/8/24
 * Time: 17:33
 */
@Repository
public class RoleDaoImpl extends NamedParameterJdbcDaoSupport implements RoleDao {
    private static final String INSERT_ROLE =
            "INSERT INTO s_role\n" +
                    "  (id, portal_id, code, name, remarks, is_admin, is_show, priority, create_time)\n" +
                    "VALUES\n" +
                    "  (:id, :portal_id, :code, :name, :remarks, :is_admin, :is_show, :priority,\n" +
                    "   :create_time)";
    private static final String UPDATE_ROLE =
            "UPDATE s_role\n" +
                    "   SET code = :code, name = :name, remarks = :remarks, is_show = :is_show,\n" +
                    "       is_admin = :is_admin, priority = :priority, update_time = :update_time\n" +
                    " WHERE id = :id";
    private static final String UNSET_ADMIN = "UPDATE s_role SET is_admin = 0 WHERE portal_id = :portal_id";
    private static final String DELETE_ROLE = "UPDATE s_role SET is_del = 1 where id = :id";
    private static final String GET_ROLE_INFO =
            "SELECT id, portal_id, (select t.name from s_portal t where t.id = a.portal_id) portal_name, code, name,\n" +
                    "       remarks, is_admin, is_show, priority, create_time, update_time\n" +
                    "  FROM s_role a\n" +
                    " WHERE is_del = 0\n";

    public void insertRole(Role role) {
        Map<String, Object> parameters = new ParameterBuilder(role).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_ROLE, parameters);

        role.setId(this.getNamedParameterJdbcTemplate().queryForObject("select @@IDENTITY", Long.class));
    }

    public void updateRole(Role role) {
        Map<String, Object> parameters = new ParameterBuilder(role).create();
        this.getNamedParameterJdbcTemplate().update(UPDATE_ROLE, parameters);
    }

    public void deleteRole(Long role_id) {
        Map<String, Object> parameters = new ParameterBuilder("id", role_id).create();
        this.getNamedParameterJdbcTemplate().update(DELETE_ROLE, parameters);
    }

    public void unsetAdmin(Long portal_id){
        Map<String, Object> parameters = new ParameterBuilder("portal_id", portal_id).create();
        this.getNamedParameterJdbcTemplate().update(UNSET_ADMIN, parameters);
    }

    public Role getRole(Long role_id) {
        try {
            NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_ROLE_INFO);
            sqlBuilder.append(" and a.id = :id");
            Role role = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), new ParameterBuilder("id", role_id).create(), ReflectiveRowMapperUtils.getRowMapper(Role.class));

            return role;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Page<Role> findRole(RoleQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(GET_ROLE_INFO);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.portal_id = :portal_id",  query.getPortal_id());
            builder.appendIfNotEmpty(" and a.code = :code", query.getCode());
            builder.appendIfNotEmpty(" and a.name = :name", query.getName());
            builder.appendIfNotEmpty(" and a.is_show = :is_show", query.getIs_show());
            builder.appendIfNotEmpty(" and a.is_admin = :is_admin", query.getIs_admin());
            builder.appendIfNotEmpty(" and a.create_time >= :create_time_min", query.getCreate_time_min());
            builder.appendIfNotEmpty(" and a.create_time <= :create_time_max", query.getCreate_time_max());
            builder.appendIfNotEmpty(" and (a.code like concat('%', :keyword, '%') or a.name like concat('%', :keyword, '%'))", query.getKeyword());
        }
        builder.append(" order by a.priority, a.create_time desc");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(Role.class));
    }
}

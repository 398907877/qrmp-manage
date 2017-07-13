package com.yanyan.persist.system.impl;

import com.yanyan.data.domain.system.Portal;
import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.NamedSqlBuilder;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.core.lang.Page;
import com.yanyan.persist.system.PortalDao;
import com.yanyan.data.query.system.PortalQuery;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 门户数据操作类
 * User: Saintcy
 * Date: 2016/5/20
 * Time: 16:22
 */
@Repository
public class PortalDaoImpl extends NamedParameterJdbcDaoSupport implements PortalDao {
    private static final String INSERT_PORTAL =
            "INSERT INTO s_portal\n" +
                    "  (id, code, remarks, NAME, app_key, app_secret, priority, create_time)\n" +
                    "VALUES\n" +
                    "  (:id, :code, :remarks, :name, :app_key, :app_secret, :priority,\n" +
                    "   :create_time)";

    private static final String UPDATE_PORTAL =
            "UPDATE s_portal\n" +
                    "   SET code = :code, remarks = :remarks, name = :name, app_key = :app_key,\n" +
                    "       app_secret = :app_secret, priority = :priority,\n" +
                    "       update_time = :update_time\n" +
                    " WHERE id = :id";

    private static final String DISABLE_PORTAL = "update s_portal set status = 0 where id = :id";
    private static final String ENABLE_PORTAL = "update s_portal set status = 1 where id = :id";
    private static final String GET_PORTAL_INFO =
            "SELECT id, code, remarks, name, app_key, app_secret, status, priority, create_time,\n" +
                    "       update_time\n" +
                    "  FROM s_portal a" +
                    " WHERE 1 = 1\n";

    public void insertPortal(Portal portal) {
        Map<String, Object> parameters = new ParameterBuilder(portal).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_PORTAL, parameters);
        portal.setId(this.getNamedParameterJdbcTemplate().queryForObject("select @@IDENTITY", Long.class));
    }

    public void updatePortal(Portal portal) {
        Map<String, Object> parameters = new ParameterBuilder(portal).create();

        this.getNamedParameterJdbcTemplate().update(UPDATE_PORTAL, parameters);
    }

    public void disablePortal(Long portal_id) {
        Map<String, Object> parameters = new ParameterBuilder("id", portal_id).create();
        this.getNamedParameterJdbcTemplate().update(DISABLE_PORTAL, parameters);
    }

    public void enablePortal(Long portal_id) {
        Map<String, Object> parameters = new ParameterBuilder("id", portal_id).create();
        this.getNamedParameterJdbcTemplate().update(ENABLE_PORTAL, parameters);
    }

    public Portal getPortal(Long portal_id) {
        Map<String, Object> params = new ParameterBuilder("id", portal_id).create();
        NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_PORTAL_INFO).append(" and a.id = :id");

        Portal portal = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), params, ReflectiveRowMapperUtils.getRowMapper(Portal.class));
        return portal;
    }

    public Page<Portal> findPortal(PortalQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(GET_PORTAL_INFO);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.code = :code", query.getCode());
            builder.appendIfNotEmpty(" and a.app_key = :app_key", query.getApp_key());
            builder.appendIfNotEmpty(" and (a.name like concat('%', :keyword, '%') or a.code like concat('%', :keyword, '%') or a.app_key like concat('%', :keyword, '%'))", query.getKeyword());
        }
        builder.append(" order by a.priority, a.create_time");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(Portal.class));
    }
}

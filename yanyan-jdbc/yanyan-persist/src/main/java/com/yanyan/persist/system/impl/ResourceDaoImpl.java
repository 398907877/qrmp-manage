package com.yanyan.persist.system.impl;

import com.yanyan.core.db.NamedSqlBuilder;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Resource;
import com.yanyan.persist.impl.TreeDaoImpl;
import com.yanyan.persist.system.ResourceDao;
import com.yanyan.data.query.system.ResourceQuery;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 功能数据存取对象
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Repository
public class ResourceDaoImpl extends TreeDaoImpl implements ResourceDao {
    private static final String INSERT_RESOURCE =
            "INSERT INTO s_resource\n" +
                    "  (id, portal_id, code, name, parent_id, url, target, icon, remarks, priority,\n" +
                    "   is_show, create_time)\n" +
                    "VALUES\n" +
                    "  (:id, :portal_id, :code, :name, :parent_id, :url, :target, :icon, :remarks,\n" +
                    "   :priority, :is_show, :create_time)";

    private static final String UPDATE_RESOURCE =
            "UPDATE s_resource\n" +
                    "   SET code = :code, name = :name, parent_id = :parent_id,\n" +
                    "       url = :url, target = :target, icon = :icon, remarks = :remarks,\n" +
                    "       priority = :priority, is_show = :is_show,\n" +
                    "       update_time = :update_time\n" +
                    " WHERE id = :id";

    private static final String DELETE_RESOURCE = "delete from s_resource where id = :id";

    private static final String GET_RESOURCE_INFO =
            "SELECT id, portal_id, (select t.name from s_portal t where t.id = a.portal_id) portal_name, code, name, parent_id,\n" +
                    "      (select t.name from s_resource t where t.id = a.parent_id) parent_name,\n" +
                    "       path, level, url, target, icon, remarks, priority,\n" +
                    "       is_show, create_time, update_time, (select count(*) from s_resource t where t.parent_id = a.id) hasChildren\n" +
                    "  FROM s_resource a\n" +
                    " WHERE 1 = 1";

    public void insertResource(Resource resource) {
        Map<String, Object> parameters = new ParameterBuilder(resource).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_RESOURCE, parameters);
        resource.setId(this.getNamedParameterJdbcTemplate().queryForObject("select @@IDENTITY", Long.class));
    }

    public void updateResource(Resource resource) {
        Map<String, Object> parameters = new ParameterBuilder(resource).create();
        this.getNamedParameterJdbcTemplate().update(UPDATE_RESOURCE, parameters);
    }

    public void deleteResource(Long resource_id) {
        Map<String, Object> parameters = new ParameterBuilder("id", resource_id).create();
        this.getNamedParameterJdbcTemplate().update(DELETE_RESOURCE, parameters);
    }

    public Resource getResource(Long resource_id) {
        NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_RESOURCE_INFO);
        sqlBuilder.append(" and a.id = :id");
        Resource resource = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), new ParameterBuilder("id", resource_id).create(), ReflectiveRowMapperUtils.getRowMapper(Resource.class));

        return resource;
    }

    public Page<Resource> findResource(ResourceQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(GET_RESOURCE_INFO);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.code = :code", query.getCode());
            builder.appendIfNotEmpty(" and a.name = :name", query.getName());
            builder.appendIfNotEmpty(" and a.portal_id = :portal_id", query.getPortal_id());
            builder.appendIfNotEmpty(" and a.parent_id = :parent_id", query.getParent_id());
            builder.appendSetIfNotEmpty(" and a.is_show in (:is_show)", "is_show", StringUtils.split(query.getIs_show(), ","), true);
            builder.appendIfNotEmpty(" and (a.name like concat('%', :keyword, '%') or a.code like concat('%', :keyword, '%') or a.pinyin like concat('%', :keyword, '%') or a.pyabbr like concat('%', :keyword, '%'))", query.getKeyword());
        }
        builder.append(" order by a.parent_id, a.priority");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(Resource.class));
    }

    protected String getTableName(){
        return "s_resource";
    }

}

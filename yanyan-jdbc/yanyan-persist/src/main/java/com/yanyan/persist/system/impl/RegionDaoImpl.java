package com.yanyan.persist.system.impl;

import com.yanyan.persist.system.RegionDao;
import com.yanyan.core.db.NamedSqlBuilder;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Region;
import com.yanyan.persist.impl.TreeDaoImpl;
import com.yanyan.data.query.system.RegionQuery;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;

/**
 * 部门数据存取类
 * User: Saintcy
 * Date: 2015/8/24
 * Time: 17:33
 */
@Repository
public class RegionDaoImpl extends TreeDaoImpl implements RegionDao {
    private static final String INSERT_REGION =
            "INSERT INTO s_region\n" +
                    "  (id, CODE, NAME, pinyin, pyabbr, parent_id, priority, create_time)\n" +
                    "VALUES\n" +
                    "  (:id, :code, :name, :pinyin, :pyabbr, :parent_id, :priority, :create_time)";

    private static final String UPDATE_REGION =
            "UPDATE s_region\n" +
                    "   SET code = :code, name = :name, pinyin = :pinyin, pyabbr = :pyabbr,\n" +
                    "       parent_id = :parent_id, priority = :priority,\n" +
                    "       update_time = :update_time\n" +
                    " WHERE id = :id";

    private static final String DELETE_REGION = "delete from s_region where id = :id";
    private static final String GET_REGION_INFO =
            "SELECT id, code, name, pinyin, pyabbr, parent_id, path, level, priority,\n" +
                    "       (select t.name from s_region t where t.id = a.parent_id) parent_name,\n" +
                    "       create_time, update_time\n" +
                    "  FROM s_region a\n" +
                    " WHERE 1 = 1\n";

    public void insertRegion(Region region) {
        Map<String, Object> parameters = new ParameterBuilder(region).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_REGION, parameters);

        region.setId(this.getNamedParameterJdbcTemplate().queryForObject("select @@IDENTITY", Long.class));
    }

    public void updateRegion(Region region) {
        Map<String, Object> parameters = new ParameterBuilder(region)
                .put("update_time", new Date())
                .create();

        this.getNamedParameterJdbcTemplate().update(UPDATE_REGION, parameters);
    }

    public void deleteRegion(Long dept_id) {
        Map<String, Object> parameters = new ParameterBuilder("id", dept_id).create();
        this.getNamedParameterJdbcTemplate().update(DELETE_REGION, parameters);
    }

    public Region getRegion(Long dept_id) {
        try {
            NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_REGION_INFO);
            sqlBuilder.append(" and a.id = :id");

            Region region = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), new ParameterBuilder("id", dept_id).create(), ReflectiveRowMapperUtils.getRowMapper(Region.class));

            return region;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Page<Region> findRegion(RegionQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(GET_REGION_INFO);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.level = :level", query.getLevel());
            builder.appendIfNotEmpty(" and a.code = :code", query.getCode());
            builder.appendIfNotEmpty(" and a.name = :name", query.getName());
            builder.appendIfNotEmpty(" and a.parent_id = :parent_id", query.getParent_id());
            builder.appendIfNotEmpty(" and (a.name like concat('%', :keyword, '%') or REPLACE(a.pinyin, ' ', '') = concat('%', :keyword, '%') or a.pyabbr = concat('%', :keyword, '%'))", query.getKeyword());
        }
        builder.append(" order by a.priority, a.create_time desc");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(Region.class));
    }

    @Override
    protected String getTableName() {
        return "s_region";
    }

}
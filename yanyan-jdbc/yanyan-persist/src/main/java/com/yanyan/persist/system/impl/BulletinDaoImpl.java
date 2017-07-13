package com.yanyan.persist.system.impl;


import com.yanyan.data.query.system.BulletinQuery;
import com.yanyan.persist.system.BulletinDao;
import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.NamedSqlBuilder;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Bulletin;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Map;

/**
 * 公告数据存取类
 * User: Saintcy
 * Date: 2015/3/6
 * Time: 14:55
 */
@Repository
public class BulletinDaoImpl extends NamedParameterJdbcDaoSupport implements BulletinDao {
    private static final String INSERT_BULLETIN =
            "INSERT INTO s_bulletin\n" +
            "  (id, title, content, publish_time, effective_time, expiry_time, corp_id)\n" +
                    "VALUES\n" +
                    "  (:id, :title, :content, :publish_time, :effective_time,\n" +
                    "   :expiry_time, :corp_id)";

    private static final String UPDATE_BULLETIN =
            "UPDATE s_bulletin\n" +
            "   SET title = :title, content = :content,\n" +
            "       publish_time = :publish_time, effective_time = :effective_time,\n" +
            "       expiry_time = :expiry_time\n" +
            " WHERE id = :id";

    private static final String DELETE_BULLETIN = "update s_bulletin set status = 0 where id=:id";
    private static final String GET_BULLETIN_INFO =
            "SELECT id, title, content, publish_time, effective_time, expiry_time, status, corp_id\n" +
                    "  FROM s_bulletin a" +
                    " where a.status = 1 ";

    public void insertBulletin(Bulletin bulletin) {
        Map<String, Object> parameters = new ParameterBuilder(bulletin).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_BULLETIN, parameters);
        bulletin.setId(this.getNamedParameterJdbcTemplate().queryForObject("select @@IDENTITY", Long.class));
    }

    public void updateBulletin(Bulletin bulletin) {
        Map<String, Object> parameters = new ParameterBuilder(bulletin).create();

        this.getNamedParameterJdbcTemplate().update(UPDATE_BULLETIN, parameters);
    }

    public void deleteBulletin(Long id) {
        Map<String, Object> parameters = new ParameterBuilder("id", id).create();

        this.getNamedParameterJdbcTemplate().update(DELETE_BULLETIN, parameters);
    }

    public Bulletin getBulletin(Long notification_id) {
        NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_BULLETIN_INFO);
        sqlBuilder.append(" and a.id = :id");
        Bulletin bulletin = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), new ParameterBuilder("id", notification_id).create(), ReflectiveRowMapperUtils.getRowMapper(Bulletin.class));

        return bulletin;
    }

    public Page<Bulletin> findBulletin(BulletinQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(GET_BULLETIN_INFO);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.corp_id = :corp_id", query.getCorp_id());
            builder.appendIfNotEmpty(" and a.effective_time >= :effective_time_min", query.getEffective_time_min());
            builder.appendIfNotEmpty(" and a.effective_time >= :effective_time_max", query.getEffective_time_max());
            builder.appendSetIfNotEmpty(" and a.status in (:status)", "status", StringUtils.split(query.getStatus(), ","), true);
            builder.appendIfNotEmpty(" and (a.title like concat('%', :keyword, '%') or a.content like concat('%', :keyword, '%'))", query.getKeyword());
            if (query.getEffective() != null) {
                builder.append(" and a.expiry_time > :now", query.getEffective());
                builder.append(" and a.expiry_time < :now", !query.getEffective());
            }
        }
        builder.append(" order by a.effective_time desc");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).put("now", new Date()).create(), ReflectiveRowMapperUtils.getRowMapper(Bulletin.class));

    }
}

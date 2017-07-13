package com.yanyan.persist.system.impl;

import com.yanyan.data.domain.system.Corporation;
import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.NamedSqlBuilder;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.core.lang.Page;
import com.yanyan.data.query.system.CorporationQuery;
import com.yanyan.persist.system.CorporationDao;
import org.apache.commons.lang3.StringUtils;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 企业数据存取对象
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Repository
public class CorporationDaoImpl extends NamedParameterJdbcDaoSupport implements CorporationDao {
    private static final String INSERT_CORPORATION =
            "INSERT INTO s_corporation\n" +
                    "  (id, portal_id, NAME, english_name, province_id, city_id, county_id,\n" +
                    "   township_id, contact_man, address, postcode, contact_phone, fax, email, website,\n" +
                    "   introduction, create_time)\n" +
                    "VALUES\n" +
                    "  (:id, :portal_id, :name, :english_name, :province_id, :city_id,\n" +
                    "   :county_id, :township_id, :contact_man, :address, :postcode, :contact_phone, :fax, :email,\n" +
                    "   :website, :introduction, :create_time)";

    private static final String UPDATE_CORPORATION =
            "UPDATE s_corporation\n" +
                    "   SET name = :name, english_name = :english_name,\n" +
                    "       province_id = :province_id, city_id = :city_id,\n" +
                    "       county_id = :county_id, township_id = :township_id, contact_man = :contact_man,\n" +
                    "       address = :address, postcode = :postcode, contact_phone = :contact_phone,\n" +
                    "       fax = :fax, email = :email, website = :website,\n" +
                    "       introduction = :introduction, update_time = :update_time\n" +
                    " WHERE id = :id";

    private static final String DELETE_CORPORATION = "update s_corporation set is_del = 1 where id = :id";
    private static final String RESTORE_CORPORATION = "update s_corporation set is_del = 0 where id = :id";
    private static final String GET_CORPORATION_INFO = "select a.id, a.portal_id, (select t.name from s_portal t where t.id = a.portal_id) portal_name, a.name, a.english_name,\n" +
            "       a.province_id,\n" +
            "       (select t.name\n" +
            "           from s_region t\n" +
            "          where t.id = a.province_id) province_name,\n" +
            "       a.city_id,\n" +
            "       (select t.name\n" +
            "           from s_region t\n" +
            "          where t.id = a.city_id) city_name, a.county_id,\n" +
            "       (select t.name\n" +
            "           from s_region t\n" +
            "          where t.id = a.county_id) county_name, a.township_id,\n"+
            "       (select t.name\n" +
            "           from s_region t\n" +
            "          where t.id = a.township_id) township_name,\n" +
            "       a.contact_man, a.contact_phone,\n" +
            "       a.address, a.postcode, a.fax, a.email, a.website,\n" +
            "       a.introduction, a.is_del, a.create_time, a.update_time,\n" +
            "       (SELECT t.id FROM s_staff t WHERE t.corp_id = a.id AND is_admin = 1) AS admin_id,\n" +
            "       (SELECT t.account FROM s_staff t WHERE t.corp_id = a.id AND is_admin = 1) AS admin_account"+
            "  from s_corporation a\n" +
            " WHERE 1 = 1";

    public void insertCorporation(Corporation corporation) {
        Map<String, Object> parameters = new ParameterBuilder(corporation).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_CORPORATION, parameters);
        corporation.setId(this.getNamedParameterJdbcTemplate().queryForObject("select @@IDENTITY", Long.class));
    }

    public void updateCorporation(Corporation corporation) {
        Map<String, Object> parameters = new ParameterBuilder(corporation).create();

        this.getNamedParameterJdbcTemplate().update(UPDATE_CORPORATION, parameters);
    }

    public void deleteCorporation(Long corp_id) {
        Map<String, Object> parameters = new ParameterBuilder("id", corp_id).create();
        this.getNamedParameterJdbcTemplate().update(DELETE_CORPORATION, parameters);
    }

    public void restoreCorporation(Long corp_id) {
        Map<String, Object> parameters = new ParameterBuilder("id", corp_id).create();
        this.getNamedParameterJdbcTemplate().update(RESTORE_CORPORATION, parameters);
    }

    public Corporation getCorporation(Long corp_id) {
        try {
            NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_CORPORATION_INFO);
            sqlBuilder.append(" and a.id = :id");
            Corporation corporation = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), new ParameterBuilder("id", corp_id).create(), ReflectiveRowMapperUtils.getRowMapper(Corporation.class));

            return corporation;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Page<Corporation> findCorporation(CorporationQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(GET_CORPORATION_INFO);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.portal_id = :portal_id", query.getPortal_id());
            builder.appendIfNotEmpty(" and a.name = :name", query.getName());
            builder.appendIfNotEmpty(" and a.province_id = :province_id", query.getProvince_id());
            builder.appendIfNotEmpty(" and a.city_id = :city_id", query.getCity_id());
            builder.appendIfNotEmpty(" and a.county_id = :county_id", query.getCounty_id());
            builder.appendIfNotEmpty(" and a.township_id = :township_id", query.getTownship_id());
            builder.appendIfNotEmpty(" and a.create_time >= :create_time_min", query.getCreate_time_min());
            builder.appendIfNotEmpty(" and a.create_time <= :create_time_max", query.getCreate_time_max());
            builder.appendSetIfNotEmpty(" and a.is_del in (:status)", "status", StringUtils.split(query.getStatus(), ","), true);
            builder.appendIfNotEmpty(" and (a.name like concat('%', :keyword, '%') or a.contact_man like concat('%', :keyword, '%') or a.contact_phone like concat('%', :keyword, '%'))", query.getKeyword());
        }
        builder.append(" order by a.create_time desc");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(Corporation.class));
    }
}

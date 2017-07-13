package com.yanyan.persist.system.impl;

import com.yanyan.data.query.system.DictionaryEntryQuery;
import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.NamedSqlBuilder;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.DictionaryEntry;
import com.yanyan.data.domain.system.DictionaryGroup;
import com.yanyan.persist.system.DictionaryDao;
import com.yanyan.data.query.system.DictionaryGroupQuery;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * User: Saintcy
 * Date: 2016/8/12
 * Time: 10:13
 */
@Repository
public class DictionaryDaoImpl extends NamedParameterJdbcDaoSupport implements DictionaryDao {
    private static final String INSERT_DICTIONARY_GROUP =
            "INSERT INTO s_dictionary_group\n" +
                    "  (id, code, name, remarks, priority, create_time)\n" +
                    "VALUES\n" +
                    "  (:id, :code, :name, :remarks, :priority, :create_time)";
    private static final String UPDATE_DICTIONARY_GROUP =
            "UPDATE s_dictionary_group\n" +
                    "   SET code = :code, name = :name, remarks = :remarks,\n" +
                    "       priority = :priority, update_time = :update_time\n" +
                    " WHERE id = :id";
    private static final String DELETE_DICTIONARY_GROUP = "UPDATE s_dictionary_group SET is_del = 1 WHERE id = :id";
    private static final String GET_DICTIONARY_GROUP =
            "SELECT id, code, name, remarks, priority, create_time, update_time\n" +
                    "  FROM s_dictionary_group a\n" +
                    " WHERE a.is_del = 0\n";

    private static final String INSERT_DICTIONARY_ENTRY =
            "INSERT INTO s_dictionary\n" +
                    "  (id, code, name, value, group_id, remarks, priority, create_time)\n" +
                    "VALUES\n" +
                    "  (:id, :code, :name, :value, :group_id, :remarks, :priority, :create_time)";

    private static final String UPDATE_DICTIONARY_ENTRY =
            "UPDATE s_dictionary\n" +
                    "   SET code = :code, name = :name, value = :value, group_id = :group_id,\n" +
                    "       remarks = :remarks, priority = :priority,\n" +
                    "       update_time = :update_time\n" +
                    " WHERE id = :id";

    private static final String DELETE_DICTIONARY_ENTRY = "UPDATE s_dictionary SET is_del = 1 WHERE id = :id";
    private static final String DELETE_DICTIONARY_GROUP_ENTRY = "UPDATE s_dictionary SET is_del = 1 WHERE group_id = :group_id";
    private static final String GET_DICTIONARY_ENTRY =
            "SELECT id, code, name, value, group_id, (select t.name from s_dictionary_group t where t.id = a.group_id) group_name, remarks, priority, create_time,\n" +
                    "       update_time\n" +
                    "  FROM s_dictionary a\n" +
                    " WHERE a.is_del = 0\n";

    public void insertDictionaryGroup(DictionaryGroup group) {
        Map<String, Object> parameters = new ParameterBuilder(group).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_DICTIONARY_GROUP, parameters);

        group.setId(this.getNamedParameterJdbcTemplate().queryForObject("select @@IDENTITY", Long.class));
    }

    public void updateDictionaryGroup(DictionaryGroup group) {
        Map<String, Object> parameters = new ParameterBuilder(group).create();

        this.getNamedParameterJdbcTemplate().update(UPDATE_DICTIONARY_GROUP, parameters);
    }

    public void deleteDictionaryGroup(Long id) {
        Map<String, Object> parameters = new ParameterBuilder("id", id).create();
        this.getNamedParameterJdbcTemplate().update(DELETE_DICTIONARY_GROUP, parameters);
    }

    public DictionaryGroup getDictionaryGroup(Long id) {
        Map<String, Object> params = new ParameterBuilder("id", id).create();
        NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_DICTIONARY_GROUP).append(" and a.id = :id");

        DictionaryGroup group = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), params, ReflectiveRowMapperUtils.getRowMapper(DictionaryGroup.class));
        return group;
    }

    public DictionaryGroup getDictionaryGroupByCode(String code) {
        Map<String, Object> params = new ParameterBuilder("code", code).create();
        NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_DICTIONARY_GROUP).append(" and a.code = :code");

        DictionaryGroup group = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), params, ReflectiveRowMapperUtils.getRowMapper(DictionaryGroup.class));
        return group;
    }

    public Page<DictionaryGroup> findDictionaryGroup(DictionaryGroupQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(GET_DICTIONARY_GROUP);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.code = :code", query.getCode());
            builder.appendIfNotEmpty(" and (a.code like concat('%', :keyword, '%') or a.name like concat('%', :keyword, '%'))", query.getKeyword());
        }
        builder.append(" order by a.priority, a.create_time");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(DictionaryGroup.class));
    }

    public void insertDictionaryEntry(DictionaryEntry entry) {
        Map<String, Object> parameters = new ParameterBuilder(entry).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_DICTIONARY_ENTRY, parameters);

        entry.setId(this.getNamedParameterJdbcTemplate().queryForObject("select @@IDENTITY", Long.class));
    }

    public void updateDictionaryEntry(DictionaryEntry entry) {
        Map<String, Object> parameters = new ParameterBuilder(entry).create();

        this.getNamedParameterJdbcTemplate().update(UPDATE_DICTIONARY_ENTRY, parameters);
    }

    public void deleteDictionaryEntry(Long id) {
        Map<String, Object> parameters = new ParameterBuilder("id", id).create();
        this.getNamedParameterJdbcTemplate().update(DELETE_DICTIONARY_ENTRY, parameters);
    }

    public void deleteDictionaryEntryByGroup(Long group_id) {
        Map<String, Object> parameters = new ParameterBuilder("group_id", group_id).create();
        this.getNamedParameterJdbcTemplate().update(DELETE_DICTIONARY_GROUP_ENTRY, parameters);
    }

    public DictionaryEntry getDictionaryEntry(Long id) {
        Map<String, Object> params = new ParameterBuilder("id", id).create();
        NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_DICTIONARY_ENTRY).append(" and a.id = :id");

        DictionaryEntry entry = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), params, ReflectiveRowMapperUtils.getRowMapper(DictionaryEntry.class));
        return entry;
    }

    public DictionaryEntry getDictionaryEntryByCode(String code) {
        Map<String, Object> params = new ParameterBuilder("code", code).create();
        NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_DICTIONARY_ENTRY).append(" and a.code = :code");

        DictionaryEntry entry = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), params, ReflectiveRowMapperUtils.getRowMapper(DictionaryEntry.class));
        return entry;
    }

    public Page<DictionaryEntry> findDictionaryEntry(DictionaryEntryQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(GET_DICTIONARY_ENTRY);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.code = :code", query.getCode());
            builder.appendIfNotEmpty(" and a.group_id = :group_id", query.getGroup_id());
            builder.appendIfNotEmpty(" and (a.code like concat('%', :keyword, '%') or a.value like concat('%', :keyword, '%') or a.name like concat('%', :keyword, '%'))", query.getKeyword());
        }
        builder.append(" order by a.group_id, a.priority, a.create_time");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(DictionaryEntry.class));
    }
}
package com.yanyan.persist.codegen.impl;

import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.data.domain.console.Column;
import com.yanyan.data.domain.console.Schema;
import com.yanyan.data.domain.console.Table;
import com.yanyan.persist.codegen.SchemaDao;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * User: Saintcy
 * Date: 2016/10/26
 * Time: 17:18
 */
@Repository
public class SchemaDaoImpl extends NamedParameterJdbcDaoSupport implements SchemaDao {
    private static final String GET_SCHEMA = "SELECT schema_name name FROM information_schema.SCHEMATA WHERE schema_name NOT IN ('information_schema', 'mysql', 'performance_schema', 'sys') ORDER BY schema_name";
    private static final String GET_TABLE = "SELECT table_name name, table_comment comment FROM information_schema.TABLES WHERE table_schema = :schema";
    private static final String GET_COLUMN = "SELECT column_name name, data_type type, IFNULL(character_maximum_length, 0) + IFNULL(numeric_precision, 0) + IF(numeric_scale>0, 1, 0) /*+ IFNULL(datetime_precision, 0)*/ maxlen, column_comment comment FROM information_schema.COLUMNS WHERE table_schema = :schema";

    public List<Schema> getSchemaList() {
        return this.getNamedParameterJdbcTemplate().query(GET_SCHEMA, ReflectiveRowMapperUtils.getRowMapper(Schema.class));
    }

    public List<Table> getTableList(String schema) {
        Map<String, Object> parameters = new ParameterBuilder("schema", schema).create();
        return this.getNamedParameterJdbcTemplate().query(GET_TABLE, parameters, ReflectiveRowMapperUtils.getRowMapper(Table.class));
    }

    public Table getTable(String schema, String name) {
        Map<String, Object> parameters = new ParameterBuilder("schema", schema).put("name", name).create();
        return this.getNamedParameterJdbcTemplate().queryForObject(GET_TABLE + " AND table_name = :name", parameters, ReflectiveRowMapperUtils.getRowMapper(Table.class));
    }

    public List<Column> getColumnList(String schema, String table) {
        Map<String, Object> parameters = new ParameterBuilder("schema", schema).put("table", table).create();
        return this.getNamedParameterJdbcTemplate().query(GET_COLUMN + " AND table_name = :table", parameters, ReflectiveRowMapperUtils.getRowMapper(Column.class));
    }
}

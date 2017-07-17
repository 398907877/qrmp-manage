package com.yanyan.persist.codegen;

import com.yanyan.data.domain.console.Column;
import com.yanyan.data.domain.console.Schema;
import com.yanyan.data.domain.console.Table;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 业务数据结构数据存储
 * User: Saintcy
 * Date: 2016/10/26
 * Time: 17:20
 */
public interface SchemaDao {
    List<Schema> getSchemaList();
    List<Table> getTableList(String schema);
    Table getTable(@Param("schema") String schema, @Param("name") String name);
    List<Column> getColumnList(@Param("schema") String schema, @Param("table") String table);
}

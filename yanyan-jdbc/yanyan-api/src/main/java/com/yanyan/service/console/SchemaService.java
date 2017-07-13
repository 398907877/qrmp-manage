package com.yanyan.service.console;

import com.yanyan.data.domain.console.Column;
import com.yanyan.data.domain.console.Schema;
import com.yanyan.data.domain.console.Table;

import java.util.List;

/**
 * 表服务
 * User: Saintcy
 * Date: 2016/10/24
 * Time: 23:03
 */
public interface SchemaService {
    List<Schema> getSchemaList();
    List<Table> getTableList(String schema);
    Table getTable(String schema, String name);
    List<Column> getColumnList(String schema, String table);
}

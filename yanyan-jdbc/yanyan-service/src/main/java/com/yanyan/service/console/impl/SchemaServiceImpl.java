package com.yanyan.service.console.impl;

import com.yanyan.data.domain.console.Column;
import com.yanyan.data.domain.console.Schema;
import com.yanyan.data.domain.console.Table;
import com.yanyan.persist.codegen.SchemaDao;
import com.yanyan.service.BaseService;
import com.yanyan.service.console.SchemaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * User: Saintcy
 * Date: 2016/10/26
 * Time: 17:24
 */
@Service
public class SchemaServiceImpl extends BaseService implements SchemaService {
    @Autowired
    private SchemaDao schemaDao;

    public List<Schema> getSchemaList() {
        return schemaDao.getSchemaList();
    }

    public List<Table> getTableList(String schema) {
        return schemaDao.getTableList(schema);
    }

    public Table getTable(String schema, String name) {
        return schemaDao.getTable(schema, name);
    }

    public List<Column> getColumnList(String schema, String table) {
        return schemaDao.getColumnList(schema, table);
    }
}

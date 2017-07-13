package com.yanyan.core.db.dialect;

import com.yanyan.core.db.Dialect;

/**
 * Dialect for PostgreSQL
 *
 * User: Saintcy
 * Date: 2015/7/1
 * Time: 14:44
 */
public class PostgreSQLDialect extends Dialect {

    public PostgreSQLDialect(String name) {
        super(name);
    }

    @Override
    protected String getPageSqlInner(String sql, int offset, int limit) {
        StringBuffer buffer = new StringBuffer(sql.length() + 20).append(sql);
        buffer.append(" limit " + limit + " offset " + offset);
        return buffer.toString();
    }
}

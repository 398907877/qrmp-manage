package com.yanyan.core.db.dialect;

import com.yanyan.core.db.Dialect;

/**
 * Dialect for MySQL
 *
 * User: Saintcy
 * Date: 2015/7/1
 * Time: 14:44
 */
public class MySQLDialect extends Dialect {

    public MySQLDialect(String name) {
        super(name);
    }

    @Override
    protected String getPageSqlInner(String sql, int offset, int limit) {
        StringBuffer buffer = new StringBuffer(sql.length() + 20).append(sql);

        buffer.append(" limit " + offset + ", " + limit);

        return buffer.toString();
    }
}

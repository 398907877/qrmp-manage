package com.yanyan.core.db.dialect;


import com.yanyan.core.db.Dialect;

/**
 * A dialect compatible with the H2 database.
 *
 * User: Saintcy
 * Date: 2015/7/1
 * Time: 14:44
 */
public class H2Dialect extends Dialect {
    public H2Dialect(String name) {
        super(name);
    }

    @Override
    protected String getPageSqlInner(String sql, int offset, int limit) {
        return new StringBuffer(sql.length() + 40).
                append(sql).
                append(" limit " + limit + " offset " + offset).
                toString();
    }
}
package com.yanyan.core.db.dialect;

import com.yanyan.core.db.Dialect;

/**
 * Dialect for HSQLDB
 *
 * User: Saintcy
 * Date: 2015/7/1
 * Time: 14:44
 */
public class HSQLDialect extends Dialect {

    public HSQLDialect(String name) {
        super(name);
    }

    @Override
    protected String getPageSqlInner(String sql, int offset, int limit) {
        return new StringBuffer(sql.length() + 10)
                .append(sql)
                .insert(sql.toLowerCase().indexOf("select") + 6, " limit " + offset + " :" + limit)
                .toString();
    }

}

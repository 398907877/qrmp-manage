package com.yanyan.core.db.dialect;

import com.yanyan.core.db.Dialect;

/**
 * Dialect for Oracle
 *
 * User: Saintcy
 * Date: 2015/7/1
 * Time: 14:44
 */
public class OracleDialect extends Dialect {

    public OracleDialect(String name) {
        super(name);
    }

    @Override
    protected String getPageSqlInner(String sql, int offset, int limit) {
        sql = sql.trim();
        boolean isForUpdate = false;
        if (sql.toLowerCase().endsWith(" for update")) {
            sql = sql.substring(0, sql.length() - 11);
            isForUpdate = true;
        }

        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100);

        pagingSelect.append("select * from ( select row_.*, rownum rownum_ from ( ");

        pagingSelect.append(sql);

        pagingSelect.append(" ) row_ ) where rownum_ <= " + ((offset + 1) * limit) + " and rownum_ > " + (offset * limit));

        if (isForUpdate) {
            pagingSelect.append(" for update");
        }

        return pagingSelect.toString();
    }
}

package com.yanyan.core.db.dialect;

import com.yanyan.core.db.Dialect;

/**
 * A dialect compatible with the DB2 database.
 *
 * User: Saintcy
 * Date: 2015/7/1
 * Time: 14:44
 */
public class DB2Dialect extends Dialect {
    public DB2Dialect(String name) {
        super(name);
    }

    private static String getRowNumber(String sql) {
        StringBuffer rownumber = new StringBuffer(50)
                .append("rownumber() over(");

        int orderByIndex = sql.toLowerCase().indexOf("order by");

        if (orderByIndex > 0 && !hasDistinct(sql)) {
            rownumber.append(sql.substring(orderByIndex));
        }

        rownumber.append(") as rownumber_,");

        return rownumber.toString();
    }

    private static boolean hasDistinct(String sql) {
        return sql.toLowerCase().indexOf("select distinct") >= 0;
    }

    @Override
    protected String getPageSqlInner(String sql, int offset, int limit) {
        int startOfSelect = sql.toLowerCase().indexOf("select");

        StringBuffer pagingSelect = new StringBuffer(sql.length() + 100)
                .append(sql.substring(0, startOfSelect)) //add the comment
                .append("select * from ( select ") //nest the main query in an outer select
                .append(getRowNumber(sql)); //add the rownnumber bit into the outer query select list

        if (hasDistinct(sql)) {
            pagingSelect.append(" row_.* from ( ") //add another (inner) nested select
                    .append(sql.substring(startOfSelect)) //add the main query
                    .append(" ) as row_"); //close off the inner nested select
        } else {
            pagingSelect.append(sql.substring(startOfSelect + 6)); //add the main query
        }

        pagingSelect.append(" ) as temp_ where rownumber_ ");


        pagingSelect.append("between " + (offset + 1) + " and " + (offset + limit));


        return pagingSelect.toString();
    }
}

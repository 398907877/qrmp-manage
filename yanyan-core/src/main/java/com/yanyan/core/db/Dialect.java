package com.yanyan.core.db;

import com.yanyan.core.db.dialect.*;
import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.Function;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import javax.sql.DataSource;
import java.beans.PropertyDescriptor;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 数据库的Dialect,用于获取分页sql等
 * <p/>
 * User: Saintcy
 * Date: 2015/7/1
 * Time: 14:44
 */
public class Dialect {
    private static final List<SelectItem> COUNT_ITEM;
    private static final Alias TABLE_ALIAS;

    static {
        COUNT_ITEM = new ArrayList<SelectItem>();
        COUNT_ITEM.add(new SelectExpressionItem(new Column("count(*)")));

        TABLE_ALIAS = new Alias("table_count");
        TABLE_ALIAS.setUseAs(false);
    }

    //缓存已经修改过的sql
    private Map<String, String> CACHE = new ConcurrentHashMap<String, String>();
    private String name;

    /**
     * 根据驱动名称获取数据库方言类型
     * 可以传入jdbcUrl或driverName
     *
     * @param driverName
     * @return
     */
    public static Dialect createDialect(String driverName) {
        if (StringUtils.containsIgnoreCase(driverName, "mysql")) {//mysql
            return new MySQLDialect("mysql");
        } else if (StringUtils.containsIgnoreCase(driverName, "oracle")) {//oracle
            return new OracleDialect("oracle");
        } else if (StringUtils.containsIgnoreCase(driverName, "sqlserver")) {//sqlserver
            return new SQLServerDialect("sqlserver");
        } else if (StringUtils.containsIgnoreCase(driverName, "postgresql")) {//postgresql
            return new PostgreSQLDialect("postgresql");
        } else if (StringUtils.containsIgnoreCase(driverName, "db2")) {//sybase
            return new DB2Dialect("db2");
        } else if (StringUtils.containsIgnoreCase(driverName, "hsqldb")) {//hsqldb
            return new HSQLDialect("hsqldb");
        } else if (StringUtils.containsIgnoreCase(driverName, "h2")) {
            return new H2Dialect("h2");
        } else if (StringUtils.containsIgnoreCase(driverName, "sqlite")) {//sqlite
            return new Dialect("sqllite");
        } else if (StringUtils.containsIgnoreCase(driverName, "sybase")) {//sybase
            return new Dialect("sybase");
        } else if (StringUtils.containsIgnoreCase(driverName, "informix-sqli")) {//informix
            return new Dialect("informix");
        } else {
            return new Dialect("unknown");
        }
    }

    /**
     * 根据数据库元数据获取数据库方言类型
     *
     * @param databaseMetaData
     * @return
     * @throws SQLException
     */
    public static Dialect createDialect(DatabaseMetaData databaseMetaData) throws SQLException {
        return createDialect(databaseMetaData.getDriverName().toLowerCase());
    }

    /**
     * 根据数据源获取数据库方言类型
     *
     * @param dataSource
     * @return
     */
    public static Dialect createDialect(DataSource dataSource) {
        String jdbcUrl = getJdbcUrl(dataSource);

        if (StringUtils.contains(jdbcUrl, ":mysql:")) {//mysql
            return new MySQLDialect("mysql");
        } else if (StringUtils.contains(jdbcUrl, ":oracle:")) {//oracle
            return new OracleDialect("oracle");
        } else if (StringUtils.contains(jdbcUrl, ":sqlserver://")) {//sqlserver
            return new SQLServerDialect("sqlserver");
        } else if (StringUtils.contains(jdbcUrl, ":postgresql:")) {//postgresql
            return new PostgreSQLDialect("postgresql");
        } else if (StringUtils.contains(jdbcUrl, ":db2:")) {//sybase
            return new DB2Dialect("db2");
        } else if (StringUtils.contains(jdbcUrl, ":hsqldb:")) {//hsqldb
            return new HSQLDialect("hsqldb");
        } else if (StringUtils.contains(jdbcUrl, ":h2:")) {
            return new H2Dialect("h2");
        } else if (StringUtils.contains(jdbcUrl, ":sqlite:")) {//sqlite
            return new Dialect("sqllite");
        } else if (StringUtils.contains(jdbcUrl, ":sybase:")) {//sybase
            return new Dialect("sybase");
        } else if (StringUtils.contains(jdbcUrl, ":informix-sqli:")) {//informix
            return new Dialect("informix");
        } else {
            return new Dialect("unknown");
        }
    }

    public Dialect(String name) {
        this.name = name;
    }

    public static String getJdbcUrl(DataSource dataSource) {
        List cache = new ArrayList();
        return getJdbcUrlInner(dataSource, cache);
    }

    private static String getJdbcUrlInner(Object o, List cache) {
        BeanWrapper beanWrapper = new BeanWrapperImpl(o);
        PropertyDescriptor[] descriptor = beanWrapper.getPropertyDescriptors();
        for (int i = 0; i < descriptor.length; i++) {
            Class propertyType = descriptor[i].getPropertyType();
            //TODO: 有可能隐藏在某个非DataSource或String的内部变量内
            if (DataSource.class.isAssignableFrom(propertyType) || String.class.isAssignableFrom(propertyType)) {
                if (!beanWrapper.isReadableProperty(descriptor[i].getName())) continue;
                Object value = beanWrapper.getPropertyValue(descriptor[i].getName());
                if (value == null) continue;
                if (cache.contains(value)) continue;
                cache.add(value);
                if (value instanceof String) {
                    if (StringUtils.contains((String) value, "jdbc:")) {
                        return (String) value;
                    }
                } else {
                    String url = getJdbcUrlInner(value, cache);
                    if (url != null) {
                        return url;
                    }
                }
            }
        }

        return null;
    }

    public String getSequenceSql(String sequenceName) {
        return "select nextval('" + sequenceName + "') from dual";
    }

    public String getCountSql(String sql) {
        return getSmartCountSql(sql);
    }

    /**
     * 将sql转换为总记录数SQL
     *
     * @param sql      SQL语句
     * @param pageNo   页码
     * @param pageSize 每页条数
     * @return 总记录数的sql
     */
    public final String getPageSql(String sql, int pageNo, int pageSize) {
        return getPageSqlInner(sql, (pageNo - 1) * pageSize, pageSize);
    }

    protected String getPageSqlInner(String sql, int offset, int limit) {
        throw new UnsupportedOperationException(this.name + " do not support paging query");
    }

    /**
     * 获取简单的countSql
     *
     * @param sql
     * @return
     */
    private String getSimpleCountSql(String sql) {
        return "select count(1) from (" + sql + ") tmp_count";
    }

    /**
     * 获取智能的countSql
     *
     * @param sql
     * @return
     */
    private String getSmartCountSql(String sql) {
        if (CACHE.get(sql) != null) {
            return CACHE.get(sql);
        }
        //解析SQL
        Statement stmt = null;
        try {
            stmt = CCJSqlParserUtil.parse(sql);
        } catch (Throwable e) {
            //无法解析的用一般方法返回count语句
            String countSql = getSimpleCountSql(sql);
            CACHE.put(sql, countSql);
            return countSql;
        }
        Select select = (Select) stmt;
        SelectBody selectBody = select.getSelectBody();
        //处理body-去order by
        processSelectBody(selectBody);
        //处理with-去order by
        processWithItemsList(select.getWithItemsList());
        //处理为count查询
        sqlToCount(select);
        String result = select.toString();
        CACHE.put(sql, result);
        return result;
    }

    /**
     * 是否可以用简单的count查询方式
     *
     * @param select
     * @return
     */
    private boolean isSimpleCount(PlainSelect select) {
        //包含group by的时候不可以
        if (select.getGroupByColumnReferences() != null) {
            return false;
        }
        //包含distinct的时候不可以
        if (select.getDistinct() != null) {
            return false;
        }
        for (SelectItem item : select.getSelectItems()) {
            //select列中包含参数的时候不可以，否则会引起参数个数错误
            if (item.toString().contains("?")) {
                return false;
            }
            //如果查询列中包含函数，也不可以，函数可能会聚合列
            if (item instanceof SelectExpressionItem) {
                if (((SelectExpressionItem) item).getExpression() instanceof Function) {
                    return false;
                }
            }
        }
        return true;
    }


    /**
     * 将sql转换为count查询
     *
     * @param select
     */
    private void sqlToCount(Select select) {
        SelectBody selectBody = select.getSelectBody();
        // 是否能简化count查询
        if (selectBody instanceof PlainSelect && isSimpleCount((PlainSelect) selectBody)) {
            ((PlainSelect) selectBody).setSelectItems(COUNT_ITEM);
        } else {
            PlainSelect plainSelect = new PlainSelect();
            SubSelect subSelect = new SubSelect();
            subSelect.setSelectBody(selectBody);
            subSelect.setAlias(TABLE_ALIAS);
            plainSelect.setFromItem(subSelect);
            plainSelect.setSelectItems(COUNT_ITEM);
            select.setSelectBody(plainSelect);
        }
    }

    /**
     * 处理selectBody去除Order by
     *
     * @param selectBody
     */
    private void processSelectBody(SelectBody selectBody) {
        if (selectBody instanceof PlainSelect) {
            processPlainSelect((PlainSelect) selectBody);
        } else if (selectBody instanceof WithItem) {
            WithItem withItem = (WithItem) selectBody;
            if (withItem.getSelectBody() != null) {
                processSelectBody(withItem.getSelectBody());
            }
        } else {
            SetOperationList operationList = (SetOperationList) selectBody;
            if (operationList.getSelects() != null && operationList.getSelects().size() > 0) {
                List<SelectBody> selects = operationList.getSelects();
                for (SelectBody select : selects) {
                    if (select instanceof PlainSelect) {
                        processPlainSelect((PlainSelect) select);
                    }
                }
            }
            if (!orderByHashParameters(operationList.getOrderByElements())) {
                operationList.setOrderByElements(null);
            }
        }
    }

    /**
     * 处理PlainSelect类型的selectBody
     *
     * @param plainSelect
     */
    private void processPlainSelect(PlainSelect plainSelect) {
        if (!orderByHashParameters(plainSelect.getOrderByElements())) {
            plainSelect.setOrderByElements(null);
        }
        if (plainSelect.getFromItem() != null) {
            processFromItem(plainSelect.getFromItem());
        }
        if (plainSelect.getJoins() != null && plainSelect.getJoins().size() > 0) {
            List<Join> joins = plainSelect.getJoins();
            for (Join join : joins) {
                if (join.getRightItem() != null) {
                    processFromItem(join.getRightItem());
                }
            }
        }
    }

    /**
     * 处理WithItem
     *
     * @param withItemsList
     */
    private void processWithItemsList(List<WithItem> withItemsList) {
        if (withItemsList != null && withItemsList.size() > 0) {
            for (WithItem item : withItemsList) {
                processSelectBody(item.getSelectBody());
            }
        }
    }

    /**
     * 处理子查询
     *
     * @param fromItem
     */
    private void processFromItem(FromItem fromItem) {
        if (fromItem instanceof SubJoin) {
            SubJoin subJoin = (SubJoin) fromItem;
            if (subJoin.getJoin() != null) {
                if (subJoin.getJoin().getRightItem() != null) {
                    processFromItem(subJoin.getJoin().getRightItem());
                }
            }
            if (subJoin.getLeft() != null) {
                processFromItem(subJoin.getLeft());
            }
        } else if (fromItem instanceof SubSelect) {
            SubSelect subSelect = (SubSelect) fromItem;
            if (subSelect.getSelectBody() != null) {
                processSelectBody(subSelect.getSelectBody());
            }
        } else if (fromItem instanceof ValuesList) {

        } else if (fromItem instanceof LateralSubSelect) {
            LateralSubSelect lateralSubSelect = (LateralSubSelect) fromItem;
            if (lateralSubSelect.getSubSelect() != null) {
                SubSelect subSelect = lateralSubSelect.getSubSelect();
                if (subSelect.getSelectBody() != null) {
                    processSelectBody(subSelect.getSelectBody());
                }
            }
        }
        //Table时不用处理
    }

    /**
     * 判断Orderby是否包含参数，有参数的不能去
     *
     * @param orderByElements
     * @return
     */
    private boolean orderByHashParameters(List<OrderByElement> orderByElements) {
        if (orderByElements == null) {
            return false;
        }
        for (OrderByElement orderByElement : orderByElements) {
            if (orderByElement.toString().contains("?")) {
                return true;
            }
        }
        return false;
    }
}

package com.yanyan.core.db;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;

import java.util.*;

/**
 * SQL构建器
 * User: Saintcy
 * Date: 2015/7/3
 * Time: 12:00
 */
public class NamedSqlBuilder {
    protected static Logger logger = LoggerFactory.getLogger(NamedSqlBuilder.class);

    private StringBuffer sbSql = new StringBuffer();
    private static final String SPACE = " ";
    private static final String LINE_BREAK = "\n";


    public NamedSqlBuilder() {

    }

    /**
     * @param clause sql 子句
     */
    public NamedSqlBuilder(String clause) {
        append(clause);
    }


    /**
     * 增加语句
     *
     * @param clause sql片段
     * @return
     */
    public NamedSqlBuilder append(String clause) {
        append(clause, true);
        return this;
    }

    /**
     * 增加语句
     *
     * @param clause sql片段
     * @param append 是否增加
     * @return
     */
    public NamedSqlBuilder append(String clause, boolean append) {
        if (append) {
            this.sbSql.append(SPACE).append(clause).append(LINE_BREAK);
        }
        return this;
    }

    /**
     * 若参数值不为空，增加语句
     *
     * @param clause sql片段
     * @param values 判断值
     * @return
     */
    public NamedSqlBuilder appendIfNotEmpty(String clause, Object... values) {
        if (values != null) {
            for (Object value : values) {
                if (value == null) {
                    return this;
                } else if (value.getClass().isArray() && ((Object[]) value).length == 0) {
                    return this;
                } else if (value instanceof Collection && ((Collection) value).size() == 0) {
                    return this;
                } else if (value instanceof Map && ((Map) value).size() == 0) {
                    return this;
                } else if (StringUtils.isEmpty(value.toString())) {
                    return this;
                }
            }
        }

        append(clause);

        return this;
    }

    /**
     * 增加语句，用于数组时，增加自动替换in后面的参数为值
     *
     * @param clause
     * @param values
     * @return
     */
    public NamedSqlBuilder appendSetIfNotEmpty(String clause, String name, Object[] values) {
        return appendSetIfNotEmpty(clause, name, values, false);
    }

    /**
     * 增加语句，用于数组时，增加自动替换in后面的参数为值
     *
     * @param clause
     * @param values
     * @return
     */
    public NamedSqlBuilder appendSetIfNotEmpty(String clause, String name, Object[] values, boolean forceNumber) {
        if (values == null || values.length == 0) return this;
        StringBuffer sbSet = new StringBuffer();

        for (Object value : values) {
            if (value == null) {
                continue;
            } else if (forceNumber || isNumber(value)) {
                sbSet.append(value).append(",");
            } else {
                sbSet.append("'").append(value).append("'").append(",");
            }
        }

        if (sbSet.length() > 0) {
            sbSet.setLength(sbSet.length() - 1);//去掉最后一个逗号
            this.append(StringUtils.replace(clause, ":" + name, sbSet.toString()));
        }

        return this;
    }

    public NamedSqlBuilder appendSetIfNotEmpty(String clause, String name, Collection<?> values) {
        return appendSetIfNotEmpty(clause, name, values, false);
    }

    /**
     * 增加语句，用于列表时，增加自动增加替换in后面的值
     *
     * @param clause sql片段
     * @param name   参数名
     * @param values 参数值
     * @param number 数字类型
     * @return
     */
    public NamedSqlBuilder appendSetIfNotEmpty(String clause, String name, Collection<?> values, boolean number) {
        if (values == null || values.size() == 0) return this;
        StringBuffer sbSet = new StringBuffer();

        for (Object value : values) {
            if (value == null) {
                continue;
            } else if (number || isNumber(value)) {
                sbSet.append(value).append(",");
            } else {
                sbSet.append("'").append(value).append("'").append(",");
            }
        }

        if (sbSet.length() > 0) {
            sbSet.setLength(sbSet.length() - 1);//去掉最后一个逗号
            this.append(StringUtils.replace(clause, ":" + name, sbSet.toString()));
        }

        return this;
    }


    public List<String> getDeclaredParameterNames(String sql) {
        List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(NamedParameterUtils.parseSqlStatement(sql), new MapSqlParameterSource(new HashMap<String, Object>()));
        List<String> declaredParameterNames = new ArrayList<String>();
        for (SqlParameter parameter : declaredParameters) {
            declaredParameterNames.add(parameter.getName());
        }
        return declaredParameterNames;
    }


    public void clear() {
        this.sbSql.setLength(0);
    }

    @Deprecated
    public String getSql() {
        return this.sbSql.toString();
    }

    public String create() {
        return this.sbSql.toString();
    }

    @Override
    public String toString() {
        return sbSql.toString();
    }

    private boolean isNumber(Object value) {
        if (value == null) {
            return false;
        } else {
            return value.getClass() == Byte.TYPE || value.getClass() == Byte.class ||
                    value.getClass() == Short.TYPE || value.getClass() == Short.class ||
                    value.getClass() == Integer.TYPE || value.getClass() == Integer.class ||
                    value.getClass() == Long.TYPE || value.getClass() == Long.class ||
                    value.getClass() == Float.TYPE || value.getClass() == Float.class ||
                    value.getClass() == Double.TYPE || value.getClass() == Double.class;
        }
    }
}

package com.yanyan.core.db;

import com.yanyan.core.lang.Page;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterUtils;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.JdbcAccessor;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 相比原生的NamedParameterJdbcTemplate
 * 1、增加查询分页
 * 2、增加无参数查询
 * 3、queryObject无记录时返回null
 * User: Saintcy
 * Date: 2015/7/1
 * Time: 14:44
 */
public class NamedParameterJdbcTemplate extends org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate {
    protected static Logger logger = LoggerFactory.getLogger(NamedParameterJdbcTemplate.class);
    private static Map<String, Object> EMPTY_PARAM_MAP = new HashMap<String, Object>();
    private Dialect dialect;

    public NamedParameterJdbcTemplate(DataSource dataSource) {
        super(dataSource);
        dialect = Dialect.createDialect(dataSource);
    }

    public NamedParameterJdbcTemplate(JdbcOperations classicJdbcTemplate) {
        super(classicJdbcTemplate);
        dialect = Dialect.createDialect(((JdbcAccessor) classicJdbcTemplate).getDataSource());
    }

    protected Map<String, Object> buildSqlParameterList(String sql, Object parameter) {
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> declaredParameterNames = this.getDeclaredParameterNames(sql);
        BeanWrapper beanWrapper = new BeanWrapperImpl(parameter);
        for (String s : declaredParameterNames) {
            map.put(s, beanWrapper.getPropertyValue(s));
        }

        logger.debug("sql: {}", sql);
        logger.debug("parameter: {}", parameter);
        logger.debug("parameter map: {}", map);
        return map;
    }

    protected List<String> getDeclaredParameterNames(String sql) {
        List<SqlParameter> declaredParameters = NamedParameterUtils.buildSqlParameterList(this.getParsedSql(sql), new MapSqlParameterSource(new HashMap<String, Object>()));
        List<String> declaredParameterNames = new ArrayList<String>();
        for (SqlParameter parameter : declaredParameters) {
            declaredParameterNames.add(parameter.getName());
        }

        logger.debug("declared parameter names: {}", declaredParameterNames);
        return declaredParameterNames;
    }

    public <T> List<T> query(String sql, RowMapper<T> rowMapper) {
        return this.query(sql, EMPTY_PARAM_MAP, rowMapper);
    }

    public <T> T queryForObject(String sql, Class<T> requireType) throws DataAccessException {
        return this.queryForObject(sql, EMPTY_PARAM_MAP, requireType);
    }

    public List<Map<String, Object>> queryForList(String sql) {
        return this.queryForList(sql, EMPTY_PARAM_MAP);
    }

    public <T> Page<T> queryForPage(String sql, RowMapper<T> rowMapper) {
        return this.queryForPage(sql, EMPTY_PARAM_MAP, rowMapper);
    }

    public <T> Page<T> queryForPage(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) throws DataAccessException {
        Page<T> page = new Page<T>();
        int pageNo = paramSource.hasValue("pageNo") ? NumberUtils.toInt(String.valueOf(paramSource.getValue("pageNo"))) : 0;
        int pageSize = paramSource.hasValue("pageSize") ? NumberUtils.toInt(String.valueOf(paramSource.getValue("pageSize"))) : 0;
        boolean countTotal = paramSource.hasValue("countTotal") ? Boolean.parseBoolean(String.valueOf(paramSource.getValue("countTotal"))) : true;

        if (pageNo > 0 && pageSize > 0) {
            String pageSql = dialect.getPageSql(sql, pageNo, pageSize);
            page.setRows(this.query(pageSql, paramSource, rowMapper));
            if (countTotal) {
                String countSql = dialect.getCountSql(sql);
                page.setTotalCount(this.queryForObject(countSql, paramSource, Integer.class));
            } else {
                //如果本页满pageSize，则总数设置多1条，以便于客户端可以查询下一页，否则只返回当前条数
                page.setTotalCount(page.getRows().size() == pageSize ? pageNo * pageSize + 1 : (pageNo * (pageSize - 1)) + page.getRows().size());
            }
            page.setPageNo(pageNo);
            page.setPageSize(pageSize);
            page.setTotalPage((int) Math.ceil(page.getTotalCount() / (double) page.getPageSize()));
        } else {
            page.setRows(this.query(sql, paramSource, rowMapper));
            page.setTotalCount(page.getRows() != null ? page.getRows().size() : 0);
            page.setPageNo(1);
            page.setPageSize(page.getTotalCount());
            page.setTotalPage(1);
        }
        return page;
    }

    public <T> Page<T> queryForPage(String sql, Map<String, ?> paramMap, RowMapper<T> rowMapper) throws DataAccessException {
        return queryForPage(sql, new MapSqlParameterSource(paramMap), rowMapper);
    }

    public int update(String sql) {
        return this.update(sql, new HashMap<String, Object>());
    }

    @Override
    public <T> T queryForObject(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) throws DataAccessException {
        try {
            return super.queryForObject(sql, paramSource, rowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public long getSequence(String sequenceName) {
        return this.queryForObject(dialect.getSequenceSql(sequenceName), Long.class);
    }
}

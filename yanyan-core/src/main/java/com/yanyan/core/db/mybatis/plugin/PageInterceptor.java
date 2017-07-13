package com.yanyan.core.db.mybatis.plugin;

import com.yanyan.core.db.Dialect;
import com.yanyan.core.db.mybatis.PageList;
import com.yanyan.core.lang.PageQuery;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.*;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * MyBatis分页拦截器
 * User: Saintcy
 * Date: 2016/5/18
 * Time: 10:37
 */
@Intercepts({@Signature(
        type = Executor.class,
        method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})})
public class PageInterceptor implements Interceptor {
    private static final Logger log = LoggerFactory.getLogger(PageInterceptor.class);
    private static final List<ResultMapping> EMPTY_RESULT_MAPPINGS = new ArrayList<ResultMapping>(0);
    static int MAPPED_STATEMENT_INDEX = 0;
    static int PARAMETER_INDEX = 1;
    static int ROW_BOUNDS_INDEX = 2;
    static int RESULT_HANDLER_INDEX = 3;
    private Field additionalParametersField;

    public PageInterceptor() {
        try {
            //反射获取 BoundSql 中的 additionalParameters 属性
            additionalParametersField = BoundSql.class.getDeclaredField("additionalParameters");
            additionalParametersField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 拦截查询类的调用
     *
     * @param invocation
     * @return
     * @throws Throwable
     */
    public Object intercept(Invocation invocation) throws Throwable {
        final Executor executor = (Executor) invocation.getTarget();
        //得不到原始的接口方法信息，只能通过输入参数来判断是否分页了，这样也好，带有分页参数返回list也可以
        // final Class returnClass = invocation.getMethod().getReturnType();
        final Object[] queryArgs = invocation.getArgs();
        final MappedStatement ms = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        final Object parameter = queryArgs[PARAMETER_INDEX];
        final RowBounds rowBounds = (RowBounds) queryArgs[ROW_BOUNDS_INDEX];
        final ResultHandler resultHandler = (ResultHandler) queryArgs[RESULT_HANDLER_INDEX];
        //if (Page.class.isAssignableFrom(returnClass)) {//返回分页
        if (parameter != null && parameter instanceof PageQuery) {
            DatabaseMetaData databaseMetaData = executor.getTransaction().getConnection().getMetaData();
            Dialect dialect = Dialect.createDialect(databaseMetaData);

            PageList pageList = new PageList();

            boolean paging = false;//是否分页
            PageQuery pageQuery = (PageQuery) parameter;
            //如果有分页参数，则分页
            if (pageQuery.getPageNo() != null && pageQuery.getPageNo() > 0 && pageQuery.getPageSize() != null && pageQuery.getPageSize() > 0) {
                paging = true;
                BoundSql boundSql = ms.getBoundSql(parameter);
                queryArgs[0] = newPageMappedStatement(ms, boundSql, dialect, pageQuery.getPageNo(), pageQuery.getPageSize());
                List rows = (List) invocation.proceed();

                pageList.addAll(rows);
                if (pageQuery.getCountTotal()) {//统计总数
                    Object countResultList = executor.query(newCountMappedStatement(ms, boundSql, dialect), parameter, rowBounds, resultHandler);
                    int count = (Integer) ((List) countResultList).get(0);
                    pageList.setTotalCount(count);
                } else {
                    //如果本页满pageSize，则总数设置多1条，以便于客户端可以查询下一页，否则只返回当前条数
                    pageList.setTotalCount(pageList.size() == pageQuery.getPageSize() ? pageQuery.getPageNo() * pageQuery.getPageSize() + 1 : (pageQuery.getPageNo() * (pageQuery.getPageSize() - 1)) + pageList.size());
                }
                pageList.setPageNo(pageQuery.getPageNo());
                pageList.setPageSize(pageQuery.getPageSize());
                pageList.setTotalPage((int) Math.ceil(pageList.getTotalCount() / (double) pageList.getPageSize()));
            }

            if (!paging) {
                List rows = (List) invocation.proceed();
                pageList.setTotalPage(1);
                pageList.setTotalCount(rows == null ? 0 : rows.size());
                pageList.setPageNo(1);
                pageList.setPageSize(pageList.getTotalCount());
                pageList.addAll(rows);
            }

            return pageList;
        } else {
            return invocation.proceed();
        }
    }

    public Object plugin(Object target) {
        if (target instanceof Executor) {
            return Plugin.wrap(target, this);
        } else {
            return target;
        }
    }

    public void setProperties(Properties properties) {
        //PropertiesHelper propertiesHelper = new PropertiesHelper(properties);
    }

    public MappedStatement newPageMappedStatement(MappedStatement ms, BoundSql boundSql, Dialect dialect, int pageNo, int pageSize) throws IllegalAccessException {
        String pageSql = dialect.getPageSql(boundSql.getSql(), pageNo, pageSize);
        Map<String, Object> additionalParameters = (Map<String, Object>) additionalParametersField.get(boundSql);
        log.debug("Total page SQL [{}] ", pageSql);
        log.debug("Total page Parameters: {} ", boundSql.getParameterObject());
        log.debug("Total page Additional Parameters: {} ", additionalParameters);

        SqlSource sqlSource = new AdditionalStaticSqlSource(ms.getConfiguration(), pageSql, boundSql.getParameterMappings(), additionalParameters);

        return newMappedStatement(ms, sqlSource, ms.getResultMaps());
    }

    public MappedStatement newCountMappedStatement(MappedStatement ms, BoundSql boundSql, Dialect dialect) throws IllegalAccessException {
        String countSql = dialect.getCountSql(boundSql.getSql());
        Map<String, Object> additionalParameters = (Map<String, Object>) additionalParametersField.get(boundSql);
        log.debug("Total count SQL [{}] ", countSql);
        log.debug("Total count Parameters: {} ", boundSql.getParameterObject());
        log.debug("Total count Additional Parameters: {} ", additionalParameters);

        SqlSource sqlSource = new AdditionalStaticSqlSource(ms.getConfiguration(), countSql, boundSql.getParameterMappings(), additionalParameters);

        List<ResultMap> resultMaps = new ArrayList<ResultMap>();
        ResultMap resultMap = new ResultMap.Builder(ms.getConfiguration(), ms.getId(), Integer.class, EMPTY_RESULT_MAPPINGS).build();
        resultMaps.add(resultMap);

        return newMappedStatement(ms, sqlSource, resultMaps);
    }


    /**
     * 新建分页查询的MappedStatement
     *
     * @param ms
     * @param sqlSource
     * @param resultMaps
     * @return
     */
    private MappedStatement newMappedStatement(MappedStatement ms, SqlSource sqlSource, List<ResultMap> resultMaps) throws IllegalAccessException {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), sqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length != 0) {
            StringBuilder keyProperties = new StringBuilder();
            for (String keyProperty : ms.getKeyProperties()) {
                keyProperties.append(keyProperty).append(",");
            }
            keyProperties.delete(keyProperties.length() - 1, keyProperties.length());
            builder.keyProperty(keyProperties.toString());
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(resultMaps);
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());

        return builder.build();
    }

    public static class AdditionalStaticSqlSource implements SqlSource {
        private String sql;
        private List<ParameterMapping> parameterMappings;
        private Configuration configuration;
        private Map<String, Object> additionalParameters;

        public AdditionalStaticSqlSource(Configuration configuration, String sql) {
            this(configuration, sql, null, null);
        }

        public AdditionalStaticSqlSource(Configuration configuration, String sql, List<ParameterMapping> parameterMappings) {
            this(configuration, sql, parameterMappings, null);
        }

        public AdditionalStaticSqlSource(Configuration configuration, String sql, List<ParameterMapping> parameterMappings, Map<String, Object> additionalParameters) {
            this.sql = sql;
            this.parameterMappings = parameterMappings;
            this.configuration = configuration;
            this.additionalParameters = additionalParameters;
        }

        public BoundSql getBoundSql(Object parameterObject) {
            BoundSql boundSql = new BoundSql(configuration, sql, parameterMappings, parameterObject);
            if (additionalParameters != null) {
                //设置动态参数
                for (String key : additionalParameters.keySet()) {
                    boundSql.setAdditionalParameter(key, additionalParameters.get(key));
                }
            }
            return boundSql;
        }
    }
}

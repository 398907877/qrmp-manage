package com.yanyan.persist.system.impl;

import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.NamedSqlBuilder;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.OperationLog;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.data.query.system.OperationLogQuery;
import com.yanyan.persist.system.OperationLogDao;
import org.springframework.stereotype.Repository;

import java.util.Map;

/**
 * 操作数据存取对象
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Repository
public class OperationDaoImpl extends NamedParameterJdbcDaoSupport implements OperationLogDao {

    private static final String INSERT_OPERATION = "insert into s_operation_log\n" +
            "  (operation_id, operation_name, staff_id, corp_id, operate_time,\n" +
            "   operate_content)\n" +
            "values\n" +
            "  (:operation_id, :operation_name, :staff_id, :corp_id, :operate_time,\n" +
            "   :operate_content)";
    private static final String GET_OPERATION_INFO = "select a.operation_id, a.operation_name, a.staff_id, b.account staff_account,\n" +
            "       a.corp_id, c.name corp_name, a.operate_time, a.operate_content\n" +
            "  from s_operation_log a, s_staff b, s_corporation c\n" +
            " where a.staff_id = b.id\n" +
            "   and a.corp_id = c.id";

    public void insertOperationLog(OperationLog operation) {
        Map<String, Object> parameters = new ParameterBuilder(operation).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_OPERATION, parameters);
    }

    public OperationLog getOperationLog(Long id) {
        Map<String, Object> params = new ParameterBuilder("id", id).create();
        NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_OPERATION_INFO).append(" and a.id = :id");

        OperationLog operationLog = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), params, ReflectiveRowMapperUtils.getRowMapper(OperationLog.class));
        return operationLog;
    }

    public Page<OperationLog> findOperationLog(OperationLogQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(GET_OPERATION_INFO);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.corp_id = :corp_id", query.getCorp_id());
            builder.appendIfNotEmpty(" and a.staff_id = :staff_id", query.getStaff_id());
            builder.appendIfNotEmpty(" and a.operate_time >= :operate_time_min", query.getOperate_time_min());
            builder.appendIfNotEmpty(" and a.operate_time >= :operate_time_max", query.getOperate_time_max());
            builder.appendIfNotEmpty(" and a.operation_id = :operation_id", query.getOperation_id());
        }
        builder.append(" order by a.create_time desc");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(OperationLog.class));
    }
}

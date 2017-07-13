package com.yanyan.service.system;


import com.yanyan.data.query.system.OperationLogQuery;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.OperationLog;

/**
 * 操作服务
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 17:23
 */
public interface OperationLogService {

    /**
     * 写操作日志
     *
     * @param operation
     */
    void writeOperationLog(OperationLog operation);

    OperationLog getOperationLog(Long id);

    /**
     * 查询操作日志
     *
     * @param query 过滤条件
     */
    Page<OperationLog> queryOperationLog(OperationLogQuery query);
}

package com.yanyan.persist.system;


import com.yanyan.data.domain.system.OperationLog;
import com.yanyan.data.query.system.OperationLogQuery;
import com.yanyan.core.lang.Page;

/**
 * 操作数据存取对象
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
public interface OperationLogDao {

    void insertOperationLog(OperationLog operationLog);

    OperationLog getOperationLog(Long id);

    Page<OperationLog> findOperationLog(OperationLogQuery query);
}

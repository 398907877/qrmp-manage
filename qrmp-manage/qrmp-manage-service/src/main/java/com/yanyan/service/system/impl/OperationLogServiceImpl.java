package com.yanyan.service.system.impl;

import com.yanyan.data.query.system.OperationLogQuery;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.OperationLog;
import com.yanyan.persist.system.OperationLogDao;
import com.yanyan.service.BaseService;
import com.yanyan.service.system.OperationLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 操作管理
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Service
public class OperationLogServiceImpl extends BaseService implements OperationLogService {
    @Autowired
    private OperationLogDao operationLogDao;

    public Page<OperationLog> queryOperationLog(OperationLogQuery query) {
        return operationLogDao.findOperationLog(query);
    }

    public OperationLog getOperationLog(Long id) {
        return operationLogDao.getOperationLog(id);
    }

    public void writeOperationLog(OperationLog operation) {
        operationLogDao.insertOperationLog(operation);
    }

}
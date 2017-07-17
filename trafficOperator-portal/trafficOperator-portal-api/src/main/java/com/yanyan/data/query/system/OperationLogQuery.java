package com.yanyan.data.query.system;


import com.yanyan.core.lang.PageQuery;
import lombok.Data;

import java.util.Date;

/**
 * 操作过滤器
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 18:01
 */
@Data
public class OperationLogQuery extends PageQuery {
    private Long staff_id;
    private Long corp_id;
    private Date operate_time_min;
    private Date operate_time_max;
    private String operation_id;
}

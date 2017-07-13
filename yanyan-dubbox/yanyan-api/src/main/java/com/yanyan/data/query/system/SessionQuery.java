package com.yanyan.data.query.system;

import com.yanyan.core.lang.PageQuery;
import lombok.Data;

import java.util.Date;

/**
 * 会话查询过滤器
 * User: Saintcy
 * Date: 2015/8/24
 * Time: 16:47
 */
@Data
public class SessionQuery extends PageQuery {
    private Long staff_id;
    private Long corp_id;
    private Date login_time_min;
    private Date login_time_max;
    private String client_type;
    private String status;
    private String offline_type;
    private Boolean is_history;
}

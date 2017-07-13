package com.yanyan.data.domain.system;

import com.yanyan.core.serialize.exclusion.annotation.OutputOnly;
import com.yanyan.core.lang.BaseDomain;
import lombok.Data;

import java.util.Date;

/**
 * 登录信息
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Data
public class Session extends BaseDomain {
    private String id;
    private String client_type;
    private String client_agent;
    private String client_host;
    private String system_host;
    private Date login_time;
    private Date logout_time;
    private Date pulse_time;
    private Long staff_id;
    private String status;
    private String offline_type;

    /**Input Only Parameters*/
    /***/
    //No fields


    /**Output Only Parameters*/
    /***/
    @OutputOnly
    private String staff_name;
    @OutputOnly
    private String staff_account;

    /**Transient(Not Input and Output) Parameters*/
    /***/
    //No fields
}
package com.yanyan.data.query.system;

import com.yanyan.core.lang.PageQuery;
import lombok.Data;

import java.util.Date;

/**
 * 人员查询过滤器
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 17:04
 */
@Data
public class StaffQuery extends PageQuery {
    /**
     * 企业id
     */
    private Long corp_id;
    /**
     * 部门id
     */
    private Long dept_id;
    /**
     * 直属或从属部门id
     */
    private Long ancestor_dept_id;
    /**
     * 角色id
     */
    private Long role_id;
    /**
     * 用户名
     */
    private String account;
    /**
     * 姓名
     */
    private String name;
    /**
     * 手机号
     */
    private String cellphone;
    /**
     * email
     */
    private String email;
    /**
     * 关键字，用户名/姓名/手机号/email
     */
    private String keyword;
    /**
     * 创建时间起始
     */
    private Date create_time_min;
    /**
     * 创建时间截止
     */
    private Date create_time_max;
    /**
     * 是否管理员
     */
    private Integer is_admin;
    /**
     * 是否锁定
     */
    private Integer is_lock;
}

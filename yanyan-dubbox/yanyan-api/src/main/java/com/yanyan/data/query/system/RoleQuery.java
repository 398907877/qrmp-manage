package com.yanyan.data.query.system;

import com.yanyan.core.lang.PageQuery;
import lombok.Data;

import java.util.Date;

/**
 * 角色查询条件
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 16:17
 */
@Data
public class RoleQuery extends PageQuery {
    /**
     * 门户id，多个用逗号隔开
     */
    private Long portal_id;
    /**
     * 角色编码
     */
    private String code;
    /**
     * 角色名称
     */
    private String name;
    /**
     * 是否实现
     */
    private Integer is_show;
    /**
     * 是否管理员角色
     */
    private Integer is_admin;
    /**
     * 创建时间起始
     */
    private Date create_time_min;
    /**
     * 创建时间截止
     */
    private Date create_time_max;
    /**
     * 关键字，角色名称
     */
    private String keyword;
}

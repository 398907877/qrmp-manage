package com.yanyan.data.query.system;


import com.yanyan.core.lang.PageQuery;
import lombok.Data;

import java.util.Date;

/**
 * 部门过滤器
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Data
public class DepartmentQuery extends PageQuery {
    /**
     * 企业id
     */
    private Long corp_id;
    /**
     * 上级部门id
     */
    private Long parent_id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 创建时间开始
     */
    private Date create_time_min;
    /**
     * 创建时间截止
     */
    private Date create_time_max;
    /**
     * 关键字
     */
    private String keyword;
}

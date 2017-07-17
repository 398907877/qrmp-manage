package com.yanyan.data.query.system;

import com.yanyan.core.lang.PageQuery;
import lombok.Data;

/**
 * 地区查询
 * User: Saintcy
 * Date: 2016/8/23
 * Time: 15:45
 */
@Data
public class RegionQuery extends PageQuery {
    /**
     * 层级
     */
    private Short level;
    /**
     * 代码
     */
    private String code;
    /**
     * 上级部门id
     */
    private Long parent_id;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 关键字
     */
    private String keyword;
}

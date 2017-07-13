package com.yanyan.data.query.system;

import com.yanyan.core.lang.PageQuery;
import lombok.Data;

/**
 * 权限过滤器
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 17:33
 */
@Data
public class PermissionQuery extends PageQuery {
    /**
     * 门户类型
     */
    private String portal_id;
    /**
     * 功能类型，多个用逗号隔开
     */
    private String func_style;
    /**
     * 对象ID
     */
    private Long object_id;
    /**
     * 对象类型
     */
    private Integer object_type;
    /**
     * 功能ID
     */
    private String func_id;
}

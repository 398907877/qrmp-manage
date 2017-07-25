package com.yanyan.data.query.system;

import com.yanyan.core.lang.PageQuery;
import lombok.Data;

/**
 * 功能过滤器
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 21:41
 */
@Data
public class ResourceQuery extends PageQuery {
    /**
     * 资源编码
     */
    private String code;
    /**
     * 门户id
     */
    private Long portal_id;
    /**
     * 上级资源id
     */
    private Long parent_id;
    /**
     * 资源名称
     */
    private String name;
    /**
     * 是否显示。多个用逗号隔开
     */
    private String is_show;
    /**
     * 关键字。编码/名称
     */
    private String keyword;
    /**
     * 加载权限
     */
    private boolean loadPermission;
}

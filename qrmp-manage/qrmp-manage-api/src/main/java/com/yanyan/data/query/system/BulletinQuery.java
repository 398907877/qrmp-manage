package com.yanyan.data.query.system;


import com.yanyan.core.lang.PageQuery;
import lombok.Data;

import java.util.Date;

/**
 * 公告过滤器
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:36
 */
@Data
public class BulletinQuery extends PageQuery {
    /**
     * 企业id
     */
    private Long corp_id;
    /**
     * 是否有效
     */
    private Boolean effective;
    /**
     * 生效时间起始
     */
    private Date effective_time_min;
    /**
     * 生效时间截止
     */
    private Date effective_time_max;
    /**
     * 状态。多个用逗号隔开
     */
    private String status;
    /**
     * 关键字。标题/内容
     */
    private String keyword;
}

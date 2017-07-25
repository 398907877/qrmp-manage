package com.yanyan.data.query.system;

import com.yanyan.core.lang.PageQuery;
import lombok.Data;

import java.util.Date;

/**
 * 企业查询过滤器
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:59
 */
@Data
public class CorporationQuery extends PageQuery {
    /**
     * 门户id
     */
    private Long portal_id;
    /**
     * 企业名称
     */
    private String name;
    /**
     * 所在城市
     */
    private Long province_id;
    /**
     * 所在地市
     */
    private Long city_id;
    /**
     * 所在县市
     */
    private Long county_id;
    /**
     * 所在街道
     */
    private Long township_id;
    /**
     * 最小创建时间
     */
    private Date create_time_min;
    /**
     * 最大创建时间
     */
    private Date create_time_max;
    /**
     * 状态，多个用逗号隔开
     */
    private String status;
    /**
     * 关键字，名称、联系人、联系电话
     */
    private String keyword;
}

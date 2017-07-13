package com.yanyan.data.query.system;

import com.yanyan.core.lang.PageQuery;
import lombok.Data;

import java.util.Date;

/**
 * 提醒消息查询条件
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:40
 */
@Data
public class MessageQuery extends PageQuery {
    /**
     * 企业id
     */
    private Long corp_id;
    /**
     * 接收人员id
     */
    private Long to_staff_id;
    /**
     * 发送人员id
     */
    private Long from_staff_id;
    /**
     * 是否已经阅读
     */
    private Boolean read;
    /**
     * 推送时间起始
     */
    private Date post_time_min;
    /**
     * 推送时间截止
     */
    private Date post_time_max;
    /**
     * 状态。多个用逗号隔开
     */
    private String status;
    /**
     * 关键字。标题
     */
    private String keyword;
}

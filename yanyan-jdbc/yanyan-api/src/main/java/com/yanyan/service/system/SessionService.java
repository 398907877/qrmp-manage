package com.yanyan.service.system;

import com.yanyan.data.domain.system.Session;
import com.yanyan.data.query.system.SessionQuery;
import com.yanyan.core.lang.Page;

import java.util.Date;

/**
 * 会话服务
 * User: Saintcy
 * Date: 2016/3/30
 * Time: 17:09
 */
public interface SessionService {
    /**
     * 添加会话
     *
     * @return 登录会话信息
     */
    void createSession(Session session);

    /**
     * 更新会话
     *
     * @return 登录会话信息
     */
    void updateSession(Session session);

    /**
     * 删除会话
     *
     * @param session
     * @return
     */
    void deleteSession(Session session);

    /**
     * 删除会话
     *
     * @param session_id
     * @return
     */
    void deleteSession(String session_id);

    /**
     * 清除过期会话
     * @param max_pulse_time 最晚的心跳时间
     */
    void clearExpiredSessions(Date max_pulse_time);

    /**
     * 获取会话信息
     *
     * @return
     */
    Session getSession(String session_id);

    /**
     * 查询会话
     *
     * @param query 过滤条件
     */
    Page<Session> findSession(SessionQuery query);

}

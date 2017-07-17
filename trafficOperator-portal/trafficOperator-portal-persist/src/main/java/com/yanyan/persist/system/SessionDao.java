package com.yanyan.persist.system;


import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Session;
import com.yanyan.data.query.system.SessionQuery;

import java.util.Date;

/**
 * 用户登录数据存取对象
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
public interface SessionDao {
    /**
     * 添加会话
     *
     * @param session
     */
    void insertSession(Session session);

    /**
     * 更新会话
     *
     * @param session
     */
    void updateSession(Session session);

    /**
     * 删除会话
     *
     * @param session_id
     */
    void deleteSession(String session_id);

    /**
     * 归档会话
     *
     * @param session_id
     */
    void archiveSession(String session_id);

    /**
     * 标记过期会话
     *
     * @param max_pulse_time 最晚的心跳时间
     * @param max_pulse_time
     */
    void markExpiredSessions(Date max_pulse_time);

    /**
     * 归档过期会话
     */
    void archiveExpiredSessions();

    /**
     * 删除过期会话
     */
    void clearExpiredSessions();

    /**
     * 根据令牌获取会话信息
     *
     * @param session_id
     * @return
     */
    Session getSession(String session_id);

    /**
     * 根据人员id查找session
     *
     * @param staff_id
     * @return
     */
    Session getSessionByStaffId(Long staff_id);

    /**
     * 查询会话
     *
     * @param query
     * @return
     */
    Page<Session> findSession(SessionQuery query);
}

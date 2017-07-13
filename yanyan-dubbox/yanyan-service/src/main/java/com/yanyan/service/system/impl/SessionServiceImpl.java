package com.yanyan.service.system.impl;

import com.yanyan.data.domain.system.Session;
import com.yanyan.data.query.system.SessionQuery;
import com.yanyan.core.lang.Page;
import com.yanyan.persist.system.SessionDao;
import com.yanyan.service.BaseService;
import com.yanyan.service.system.SessionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 会话服务
 * User: Saintcy
 * Date: 2016/5/13
 * Time: 11:28
 */
@Service
public class SessionServiceImpl extends BaseService implements SessionService {
    @Autowired
    private SessionDao sessionDao;

    public void createSession(Session session) {
        sessionDao.insertSession(session);
    }

    public void updateSession(Session session) {
        if (getSession(session.getId()) == null) {
            sessionDao.insertSession(session);
        } else {
            sessionDao.updateSession(session);
        }
    }

    public void deleteSession(Session session) {
        sessionDao.updateSession(session);
        sessionDao.archiveSession(session.getId());
        sessionDao.deleteSession(session.getId());
    }

    public void deleteSession(String session_id) {
        sessionDao.deleteSession(session_id);
    }

    public void clearExpiredSessions(Date max_pulse_time) {
        sessionDao.markExpiredSessions(max_pulse_time);
        sessionDao.archiveExpiredSessions();
        sessionDao.clearExpiredSessions();
    }

    public Session getSession(String session_id) {
        return sessionDao.getSession(session_id);
    }

    public Page<Session> findSession(SessionQuery query) {
        return sessionDao.findSession(query);
    }
}

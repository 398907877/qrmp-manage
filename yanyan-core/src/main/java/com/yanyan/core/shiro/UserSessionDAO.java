package com.yanyan.core.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.EnterpriseCacheSessionDAO;

import java.io.Serializable;

/**
 * 用户会话管理
 * User: Saintcy
 * Date: 2016/4/2
 * Time: 22:41
 */
public class UserSessionDAO extends EnterpriseCacheSessionDAO {
    private ShiroService shiroService;

    public void setShiroService(ShiroService shiroService) {
        this.shiroService = shiroService;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable id = super.doCreate(session);
        shiroService.addSession(session);
        return id;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        Session session = super.doReadSession(sessionId);
        if (session == null) {
            session = shiroService.getSession(String.valueOf(sessionId));
        }
        return session;
    }

    @Override
    protected void doUpdate(Session session) {
        shiroService.updateSession(session);
    }

    @Override
    protected void doDelete(Session session) {
        shiroService.deleteSession(session);
    }
}

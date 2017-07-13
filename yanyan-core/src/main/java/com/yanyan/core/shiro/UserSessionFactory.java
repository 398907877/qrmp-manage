package com.yanyan.core.shiro;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionContext;
import org.apache.shiro.session.mgt.SessionFactory;

/**
 * User: Saintcy
 * Date: 2016/4/4
 * Time: 20:59
 */
public class UserSessionFactory implements SessionFactory {
    public Session createSession(SessionContext initData) {
        if (initData != null) {
            String host = initData.getHost();
            if (host != null) {
                return new UserSession(host);
            }
        }
        return new UserSession();
    }
}

package com.yanyan.core.shiro;


import org.apache.shiro.session.Session;
import org.apache.shiro.session.SessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 监听会话
 * User: Saintcy
 * Date: 2016/3/24
 * Time: 10:51
 */
public class ShiroSessionListener implements SessionListener {
    private static final Logger log = LoggerFactory.getLogger(ShiroSessionListener.class);

    public void onStart(Session session) {//会话创建时触发
        log.info("Session Created：" + session.getId());
    }

    public void onExpiration(Session session) {//会话过期时触发
        log.info("Session Expired：" + session.getId());
    }

    public void onStop(Session session) {//退出/会话过期时触发
        log.info("Session Stopped：" + session.getId());
    }
}
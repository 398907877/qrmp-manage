package com.yanyan.core.shiro;

import org.apache.shiro.session.mgt.SimpleSession;

/**
 * 用户会话
 * User: Saintcy
 * Date: 2016/4/4
 * Time: 21:00
 */
public class UserSession extends SimpleSession {
    /**
     * 属性是否改变 优化session数据同步
     */
    private transient boolean attributeChanged = false;

    public UserSession() {

    }

    public UserSession(String host) {
        super(host);
    }

    public boolean isAttributeChanged() {
        return attributeChanged;
    }

    /**
     * 设置属性是否变更，由应用程序自行设置
     * @param attributeChanged
     */
    public void setAttributeChanged(boolean attributeChanged) {
        this.attributeChanged = attributeChanged;
    }
}

package com.yanyan.web;


import com.yanyan.core.shiro.ShiroConstants;
import com.yanyan.core.shiro.UserSession;
import com.yanyan.core.util.HttpUtils;
import com.yanyan.data.domain.system.Staff;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;

import javax.servlet.http.HttpServletRequest;

/**
 * session工具
 * User: Saintcy
 * Date: 2016/5/29
 * Time: 15:49
 */
public class SessionUtils {
    /**
     * 会话字段名：用户ID
     */
    public static String SESSION_STAFF_ID = "shiro_session_staff_id";
    /**
     * 会话字段名：用户姓名
     */
    public static final String SESSION_STAFF_NAME = "shiro_session_staff_name";
    /**
     * 会话字段名：用户名
     */
    public static final String SESSION_STAFF_ACCOUNT = "shiro_session_username";
    /**
     * 会话字段名：客户端类型
     */
    public static String SESSION_CLIENT_TYPE = "shiro_session_client_type";
    /**
     * 会话字段名：客户端代理
     */
    public static String SESSION_CLIENT_AGENT = "shiro_session_client_agent";
    /**
     * 会话字段名：客户端ip/imei
     */
    public static String SESSION_CLIENT_HOST = "shiro_session_client_host";
    /**
     * 会话字段名：服务端ip
     */
    public static String SESSION_SYSTEM_HOST = "shiro_session_system_host";

    /**
     * 会话字段名：企业id
     */
    public static String SESSION_CORP_ID = "shiro_session_corp_id";

    /**
     * 会话字段名：门户id
     */
    public static String SESSION_PORTAL_ID = "shiro_session_portal_id";

    /**
     * 会话字段名：门户编码
     */
    public static String SESSION_PORTAL_CODE = "shiro_session_portal_code";

    private static Session getSession() {
        Subject subject = SecurityUtils.getSubject();
        if (subject.getSession() != null) {
            return subject.getSession();
        } else {
            throw new UnauthenticatedException();//未登录
        }
    }

    public static void initSession(Staff staff, HttpServletRequest request) {
        Session session = getSession();
        session.setAttribute(SessionUtils.SESSION_CLIENT_TYPE, HttpUtils.getClient_type(request));
        session.setAttribute(SessionUtils.SESSION_CLIENT_AGENT, HttpUtils.getClient_agent(request));
        session.setAttribute(SessionUtils.SESSION_CLIENT_HOST, HttpUtils.getSystem_host(request));
        session.setAttribute(SessionUtils.SESSION_SYSTEM_HOST, HttpUtils.getClientIp(request));
        session.setAttribute(ShiroConstants.SESSION_STATUS, ShiroConstants.SessionStatus.ONLINE);
        session.setAttribute(SessionUtils.SESSION_STAFF_NAME, staff.getName());
        session.setAttribute(SessionUtils.SESSION_STAFF_ACCOUNT, staff.getAccount());
        session.setAttribute(SessionUtils.SESSION_CORP_ID, staff.getCorp_id());
        session.setAttribute(SessionUtils.SESSION_PORTAL_ID, staff.getPortal_id());
        session.setAttribute(SessionUtils.SESSION_PORTAL_CODE, staff.getPortal_code());
        session.setAttribute(SessionUtils.SESSION_STAFF_ID, staff.getId());//最后写入，写入数据库时以此字段作判断，以免与会话验证器冲突导致写入数据库信息不全

        if (session instanceof UserSession) {
            ((UserSession) session).setAttributeChanged(true);
        }
    }

    public static long getStaffId() {
        return getAttribute(SESSION_STAFF_ID);
    }

    public static String getStaffName() {
        return getAttribute(SESSION_STAFF_NAME);
    }

    public static String getStaffAccount() {
        return getAttribute(SESSION_STAFF_ACCOUNT);
    }

    public static String getClientType() {
        return getAttribute(SESSION_CLIENT_TYPE);
    }

    public static String getClientAgent() {
        return getAttribute(SESSION_CLIENT_AGENT);
    }

    public static String getClientHost() {
        return getAttribute(SESSION_CLIENT_HOST);
    }

    public static String getSystemHost() {
        return getAttribute(SESSION_SYSTEM_HOST);
    }

    public static long getPortalId() {
        return getAttribute(SESSION_PORTAL_ID);
    }

    public static String getPortalCode() {
        return getAttribute(SESSION_PORTAL_CODE);
    }

    public static long getCorpId() {
        return getAttribute(SESSION_CORP_ID);
    }

    public static <T> T getAttribute(String key) {
        return (T) getSession().getAttribute(key);
    }

    public static void setAttribute(String key, Object value) {
        getSession().setAttribute(key, value);
    }
}

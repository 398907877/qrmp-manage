package com.yanyan.core.shiro;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.SessionDAO;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.AccessControlFilter;
import org.apache.shiro.web.util.WebUtils;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * 检查session是否有效
 * User: Saintcy
 * Date: 2016/2/17
 * Time: 17:45
 */
public class InspectSessionFilter extends AccessControlFilter {

    /**
     * 正常退出的重定向地址
     */
    private String logOutUrl;
    /**
     * 超时退出后重定向的地址
     */
    private String timeOutUrl;
    /**
     * 强制退出后重定向的地址
     */
    private String forceOutUrl;
    /**
     * 超过会话个数后重定向的地址
     */
    private String kickOutUrl;

    private SessionDAO sessionDAO;

    public String getLogOutUrl() {
        return logOutUrl;
    }

    public void setLogOutUrl(String logOutUrl) {
        this.logOutUrl = logOutUrl;
    }

    public String getTimeOutUrl() {
        return timeOutUrl;
    }

    public void setTimeOutUrl(String timeOutUrl) {
        this.timeOutUrl = timeOutUrl;
    }

    public String getForceOutUrl() {
        return forceOutUrl;
    }

    public void setForceOutUrl(String forceOutUrl) {
        this.forceOutUrl = forceOutUrl;
    }

    public String getKickOutUrl() {
        return kickOutUrl;
    }

    public void setKickOutUrl(String kickOutUrl) {
        this.kickOutUrl = kickOutUrl;
    }

    public void setSessionDAO(SessionDAO sessionDAO) {
        this.sessionDAO = sessionDAO;
    }

    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) throws Exception {
        Subject subject = getSubject(request, response);
        if (subject == null || subject.getSession() == null) {
            return true;
        }
        Session session = sessionDAO.readSession(subject.getSession().getId());
        if (session != null) {
            if (session instanceof UserSession) {
                UserSession userSession = (UserSession) session;
                if (userSession.isExpired()) {
                    session.setAttribute(ShiroConstants.SESSION_STATUS, ShiroConstants.SessionStatus.OFFLINE);
                    session.setAttribute(ShiroConstants.SESSION_OFFLINE_TYPE, ShiroConstants.SessionOfflineType.TIMEOUT);
                } else if (!userSession.isValid()) {
                    session.setAttribute(ShiroConstants.SESSION_STATUS, ShiroConstants.SessionStatus.OFFLINE);
                    session.setAttribute(ShiroConstants.SESSION_OFFLINE_TYPE, ShiroConstants.SessionOfflineType.LOGOUT);
                } else {
                    ShiroConstants.SessionStatus status = (ShiroConstants.SessionStatus) session.getAttribute(ShiroConstants.SESSION_STATUS);
                    if (status == null) {
                        session.setAttribute(ShiroConstants.SESSION_STATUS, ShiroConstants.SessionStatus.ONLINE);
                    }
                }
            }

            if (session.getAttribute(ShiroConstants.SESSION_OFFLINE_TYPE) == ShiroConstants.SessionOfflineType.LOGOUT) {
                request.setAttribute("redirectUrl", StringUtils.defaultString(logOutUrl, getLoginUrl()));
                return false;
            } else if (session.getAttribute(ShiroConstants.SESSION_OFFLINE_TYPE) == ShiroConstants.SessionOfflineType.TIMEOUT) {
                request.setAttribute("redirectUrl", StringUtils.defaultString(timeOutUrl, getLoginUrl()));
                return false;
            } else if (session.getAttribute(ShiroConstants.SESSION_OFFLINE_TYPE) == ShiroConstants.SessionOfflineType.FORCEOUT) {
                request.setAttribute("redirectUrl", StringUtils.defaultString(forceOutUrl, getLoginUrl()));
                return false;
            } else if (session.getAttribute(ShiroConstants.SESSION_OFFLINE_TYPE) == ShiroConstants.SessionOfflineType.KICKOUT) {
                request.setAttribute("redirectUrl", StringUtils.defaultString(kickOutUrl, getLoginUrl()));
                return false;
            }
            //session.touch();session访问时间变更，需同步到数据库
            sessionDAO.update(session);
        }
        return true;
    }


    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
        Subject subject = getSubject(request, response);
        if (subject != null) {
            subject.logout();
        }
        saveRequestAndRedirectToLogin(request, response);
        return true;
    }


    protected void redirectToLogin(ServletRequest request, ServletResponse response) throws IOException {
        WebUtils.issueRedirect(request, response, (String) request.getAttribute("redirectUrl"));
    }
}

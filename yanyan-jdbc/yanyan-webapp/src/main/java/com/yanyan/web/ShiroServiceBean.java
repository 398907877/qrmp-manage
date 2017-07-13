package com.yanyan.web;

import com.google.gson.Gson;
import com.yanyan.core.shiro.*;
import com.yanyan.core.util.DateUtils;
import com.yanyan.core.util.GenericsUtils;
import com.yanyan.core.util.JwtUtil;
import com.yanyan.core.util.PasswordUtils;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.data.domain.system.vo.RoleVo;
import com.yanyan.service.system.PrivilegeService;
import com.yanyan.service.system.SessionService;
import com.yanyan.service.system.StaffService;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.SessionFactory;
import org.apache.shiro.session.mgt.SimpleSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Shiro与应用程序的接口
 * User: Saintcy
 * Date: 2016/3/30
 * Time: 17:28
 */
@Slf4j
@Component("shiroService")
public class ShiroServiceBean implements ShiroService {
    protected static final long MAX_SYNC_INTERVAL = 5 * 60 * 1000;//最大同步间隔
    @Autowired
    private SessionFactory userSessionFactory;
    @Autowired
    private StaffService staffService;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private SessionService sessionService;
    @Autowired
    private Gson gson;
    @Value("${token.signature.key}")
    private String tokenKey;
    @Value("${token.maxAge}")
    private long tokenMaxAge;

    public Set<String> findStringRoles(String username) {
        Set<String> roleSet = new HashSet();
        Staff staff = staffService.getStaffByAccount(username);
        if (staff != null) {
            for (RoleVo role : staff.getRoles()) {
                roleSet.add(role.getCode());
            }
        }
        return roleSet;
    }

    public Set<String> findStringPermissions(String username) {
        Set<String> privilegeSet = new HashSet();
        Staff staff = staffService.getStaffByAccount(username);
        if (staff != null) {
            privilegeSet.addAll(privilegeService.getAllPrivileges(staff.getId()));

        }
        return privilegeSet;
    }

    public User findUser(String username) {
        Staff staff = staffService.getStaffByAccount(username);
        if (staff == null) return null;
        User user = new User();
        user.setUsername(staff.getAccount());
        user.setPassword(staff.getPassword());
        user.setSalt(staff.getSalt());
        user.setLocked(staff.getIs_lock() == 1);
        return user;
    }

    public boolean validatePassword(String inputPassword, byte[] salt, String storedPassword) {
        return PasswordUtils.validatePassword(inputPassword, salt, storedPassword);
    }

    public void addSession(Session session) {
        if (session instanceof UserSession) {
            ((UserSession) session).setAttributeChanged(true);
        }
        saveSession(session);
    }

    public void updateSession(Session session) {
        saveSession(session);
    }

    public void deleteSession(Session session) {
        com.yanyan.data.domain.system.Session model = toModel(session);
        model.setLogout_time(new Date());
        sessionService.deleteSession(model);
    }

    public Session getSession(String sessionId) {
        log.debug("Get Session: {}", sessionId);
        com.yanyan.data.domain.system.Session model = sessionService.getSession(sessionId);
        if (model != null) {
            log.debug("Find Session: {}", model);
            SimpleSession session = (SimpleSession) userSessionFactory.createSession(null);
            session.setId(sessionId);
            session.setStartTimestamp(model.getLogin_time());
            //session.setLastAccessTime(model.getPulse_time());
            session.setLastAccessTime(new Date());//更新最后访问时间
            session.setHost(model.getClient_host());
            session.setAttribute(ShiroConstants.SESSION_STATUS, ShiroConstants.SessionStatus.valueOf(model.getStatus()));
            session.setAttribute(SessionUtils.SESSION_STAFF_ACCOUNT, model.getStaff_account());
            session.setAttribute(SessionUtils.SESSION_STAFF_NAME, model.getStaff_name());
            session.setAttribute(SessionUtils.SESSION_STAFF_ID, model.getStaff_id());
            session.setAttribute(SessionUtils.SESSION_CLIENT_TYPE, model.getClient_type());
            session.setAttribute(SessionUtils.SESSION_CLIENT_AGENT, model.getClient_agent());
            session.setAttribute(SessionUtils.SESSION_CLIENT_HOST, model.getClient_host());
            session.setAttribute(SessionUtils.SESSION_SYSTEM_HOST, model.getSystem_host());
            Staff staff = staffService.getStaff(model.getStaff_id());
            session.setAttribute(SessionUtils.SESSION_CORP_ID, staff.getCorp_id());
            session.setAttribute(SessionUtils.SESSION_PORTAL_ID, staff.getPortal_id());

            return session;
        } else {//说明有过期数据
            log.info("Clear Expired Sessions.");
            sessionService.clearExpiredSessions(DateUtils.addDays(new Date(), -1));//清除过期的session，todo：注入过期时间？
            return null;//总是返回null，如果session丢失，则删除session
        }
    }

    protected void saveSession(Session session) {
        if (session instanceof UserSession) {
            UserSession userSession = (UserSession) session;
            if (session.getAttribute(SessionUtils.SESSION_STAFF_ID) != null) {//需是已经登录的会话，否则不同步
                Long lastSyncTime = (Long) session.getAttribute("lastSyncTime");
                if (lastSyncTime == null) {
                    lastSyncTime = 0L;
                }
                Long now = System.currentTimeMillis();
                if (userSession.isAttributeChanged() || now - lastSyncTime > MAX_SYNC_INTERVAL) {//如果属性被标记为变更或超过同步间隔，则需要同步
                    sessionService.updateSession(toModel(session));
                    session.setAttribute("lastSyncTime", session.getLastAccessTime().getTime());
                    userSession.setAttributeChanged(false);
                }
            }
        }
    }

    private com.yanyan.data.domain.system.Session toModel(Session session) {
        com.yanyan.data.domain.system.Session model = new com.yanyan.data.domain.system.Session();
        model.setId(String.valueOf(session.getId()));
        model.setStaff_id(NumberUtils.toLong(String.valueOf(session.getAttribute(SessionUtils.SESSION_STAFF_ID))));
        model.setClient_type(String.valueOf(session.getAttribute(SessionUtils.SESSION_CLIENT_TYPE)));
        model.setClient_agent(String.valueOf(session.getAttribute(SessionUtils.SESSION_CLIENT_AGENT)));
        model.setClient_host(String.valueOf(session.getAttribute(SessionUtils.SESSION_CLIENT_HOST)));
        model.setSystem_host(String.valueOf(session.getAttribute(SessionUtils.SESSION_SYSTEM_HOST)));
        model.setLogin_time(session.getStartTimestamp());
        model.setPulse_time(session.getLastAccessTime());
        ShiroConstants.SessionStatus status = (ShiroConstants.SessionStatus) session.getAttribute(ShiroConstants.SESSION_STATUS);
        if (status != null) {
            model.setStatus(String.valueOf(status));
        }
        ShiroConstants.SessionOfflineType offlineType = (ShiroConstants.SessionOfflineType) session.getAttribute(ShiroConstants.SESSION_OFFLINE_TYPE);
        if (offlineType != null) {
            model.setOffline_type(String.valueOf(offlineType));
        }

        return model;
    }

    public StatelessToken getToken(HttpServletRequest request) {
        String digest = request.getHeader("token");
        if (StringUtils.isEmpty("token")) {
            throw new IllegalArgumentException("token不能为空");
        }

        Claims claims;
        try {
            claims = JwtUtil.parseJWT(gson, tokenKey, digest);
        } catch (Exception e) {
            throw new IllegalArgumentException("token格式错误", e);
        }
        String subject = claims.getSubject();
        Map<String, String> session;
        try {
            session = gson.fromJson(subject, GenericsUtils.getMapType(String.class, String.class, Map.class));
        } catch (Exception e) {
            throw new IllegalArgumentException("token中的subject格式错误", e);
        }

        StatelessToken token = new JwtStatelessToken(session.get("account"), digest, claims, session);

        return token;
    }

    public boolean validateToken(StatelessToken token) {
        if (token instanceof JwtStatelessToken) {
            JwtStatelessToken jwt = (JwtStatelessToken) token;
            long expTime = jwt.claims.getExpiration().getTime();
            long curTime = new Date().getTime();
            if (expTime < curTime) {
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public void refreshToken(StatelessToken token, HttpServletRequest request, HttpServletResponse response) {
        if (token instanceof JwtStatelessToken) {
            JwtStatelessToken jwt = (JwtStatelessToken) token;
            try {
                response.setHeader("token", JwtUtil.createJWT(gson, tokenKey, jwt.claims.getId(), jwt.claims.getSubject(), tokenMaxAge));
            } catch (Exception e) {
                throw new IllegalArgumentException("token写入错误", e);
            }
        }
    }

    public static class JwtStatelessToken extends StatelessToken {
        private Claims claims;
        private Map<String, String> session;

        public JwtStatelessToken(String username, String digest, Claims claims, Map<String, String> session) {
            super(username, digest);
            this.claims = claims;
            this.session = session;
        }
    }
}

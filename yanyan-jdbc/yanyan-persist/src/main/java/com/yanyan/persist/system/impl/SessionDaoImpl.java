package com.yanyan.persist.system.impl;

import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.NamedSqlBuilder;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Session;
import com.yanyan.data.query.system.SessionQuery;
import com.yanyan.persist.system.SessionDao;
import org.springframework.stereotype.Repository;

import java.util.Date;

/**
 * User: Saintcy
 * Date: 2016/4/4
 * Time: 17:50
 */
@Repository
public class SessionDaoImpl extends NamedParameterJdbcDaoSupport implements SessionDao {
    private static final String INSERT_SESSION =
            "INSERT INTO s_session\n" +
                    "  (id, staff_id, client_type, client_agent, client_host, system_host,\n" +
                    "   login_time, logout_time, pulse_time, status, offline_type)\n" +
                    "VALUES\n" +
                    "  (:id, :staff_id, :client_type, :client_agent, :client_host, :system_host,\n" +
                    "   :login_time, :logout_time, :pulse_time, :status, :offline_type)";
    private static final String UPDATE_SESSION =
            "UPDATE s_session\n" +
                    "   SET staff_id = :staff_id, client_type = :client_type,\n" +
                    "       client_agent = :client_agent, client_host = :client_host,\n" +
                    "       system_host = :system_host, login_time = :login_time,\n" +
                    "       logout_time = :logout_time, pulse_time = :pulse_time,\n" +
                    "       status = :status, offline_type = :offline_type\n" +
                    " WHERE id = :id";
    private static final String ARCHIVE_SESSION = "INSERT INTO s_session_his\n" +
            "  (id, staff_id, client_type, client_agent, client_host, system_host,\n" +
            "   login_time, logout_time, pulse_time, status, offline_type)\n" +
            "SELECT id, staff_id, client_type, client_agent, client_host, system_host,\n" +
            "       login_time, logout_time, pulse_time, STATUS, offline_type\n" +
            "  FROM s_session\n" +
            " WHERE id = :id";

    private static final String DELETE_SESSION = "DELETE FROM s_session WHERE id = :id";
    private static final String GET_SESSION_INFO =
            "SELECT a.id, a.staff_id, b.name staff_name, b.account staff_account, a.client_type, a.client_agent,\n" +
                    "       a.client_host, a.system_host, a.login_time, a.logout_time,\n" +
                    "       a.pulse_time, a.status, a.offline_type\n" +
                    "  FROM s_session a, s_staff b\n" +
                    " WHERE a.staff_id = b.id";
    private static final String GET_SESSION_HIS_INFO =
            "SELECT a.id, a.staff_id, b.name staff_name, b.account staff_account, a.client_type, a.client_agent,\n" +
                    "       a.client_host, a.system_host, a.login_time, a.logout_time,\n" +
                    "       a.pulse_time, a.status, a.offline_type\n" +
                    "  FROM s_session_his a, s_staff b\n" +
                    " WHERE a.staff_id = b.id";
    private static final String MARK_EXPIRED_SESSIONS = "UPDATE s_session SET status = 'OFFLINE', offline_type = 'TIMEOUT' WHERE pulse_time <= :max_pulse_time";
    private static final String ARCHIVE_EXPIRED_SESSIONS = "INSERT INTO s_session_his\n" +
            "  (id, staff_id, client_type, client_agent, client_host, system_host,\n" +
            "   login_time, logout_time, pulse_time, status, offline_type)\n" +
            "SELECT id, staff_id, client_type, client_agent, client_host, system_host,\n" +
            "       login_time, logout_time, pulse_time, STATUS, offline_type\n" +
            "  FROM s_session\n" +
            " WHERE status = 'OFFLINE'";
    private static final String CLEAR_EXPIRED_SESSIONS = "DELETE FROM s_session WHERE status = 'OFFLINE'";


    public void insertSession(Session session) {
        this.getNamedParameterJdbcTemplate().update(INSERT_SESSION, new ParameterBuilder(session).create());
    }

    public void updateSession(Session session) {
        this.getNamedParameterJdbcTemplate().update(UPDATE_SESSION, new ParameterBuilder(session).create());
    }

    public void archiveSession(String session_id) {
        this.getNamedParameterJdbcTemplate().update(ARCHIVE_SESSION, new ParameterBuilder("id", session_id).create());
    }

    public void deleteSession(String session_id) {
        this.getNamedParameterJdbcTemplate().update(DELETE_SESSION, new ParameterBuilder("id", session_id).create());
    }

    public void markExpiredSessions(Date max_pulse_time) {
        this.getNamedParameterJdbcTemplate().update(MARK_EXPIRED_SESSIONS, new ParameterBuilder("max_pulse_time", max_pulse_time).create());
    }

    public void archiveExpiredSessions() {
        this.getNamedParameterJdbcTemplate().update(ARCHIVE_EXPIRED_SESSIONS);
    }

    public void clearExpiredSessions() {
        this.getNamedParameterJdbcTemplate().update(CLEAR_EXPIRED_SESSIONS);
    }

    public Session getSession(String session_id) {
        return this.getNamedParameterJdbcTemplate().queryForObject(GET_SESSION_INFO + " and a.id = :id", new ParameterBuilder("id", session_id).create(), ReflectiveRowMapperUtils.getRowMapper(Session.class));
    }

    public Session getSessionByStaffId(Long staff_id) {
        return this.getNamedParameterJdbcTemplate().queryForObject(GET_SESSION_INFO + " and a.staff_id = :staff_id", new ParameterBuilder("staff_id", staff_id).create(), ReflectiveRowMapperUtils.getRowMapper(Session.class));
    }

    public Page<Session> findSession(SessionQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(query.getIs_history() != null && query.getIs_history() == true ? GET_SESSION_HIS_INFO : GET_SESSION_INFO);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.staff_id = :staff_id", query.getStaff_id());
            builder.appendIfNotEmpty(" and b.corp_id = :corp_id", query.getCorp_id());
            builder.appendIfNotEmpty(" and a.login_time >= :login_time_min", query.getLogin_time_min());
            builder.appendIfNotEmpty(" and a.login_time <= :login_time_max", query.getLogin_time_max());
            builder.appendIfNotEmpty(" and a.client_type = :client_type", query.getClient_type());
            builder.appendIfNotEmpty(" and a.status = :status", query.getStatus());
            builder.appendIfNotEmpty(" and a.offline_type = :offline_type", query.getStatus());
        }
        builder.append(" order by a.login_time desc");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(Session.class));

    }
}

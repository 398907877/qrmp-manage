package com.yanyan.persist.system.impl;

import com.yanyan.core.db.NamedParameterJdbcDaoSupport;
import com.yanyan.core.db.NamedSqlBuilder;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.data.domain.system.vo.RoleVo;
import com.yanyan.data.query.system.StaffQuery;
import com.yanyan.persist.system.StaffDao;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 人员数据存取对象
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Repository
public class StaffDaoImpl extends NamedParameterJdbcDaoSupport implements StaffDao {
    private static final String INSERT_STAFF =
            "INSERT INTO s_staff\n" +
                    "  (id, corp_id, account, cellphone, name, pyabbr, pinyin,\n" +
                    "   email, is_admin, dept_id, duty, priority, create_time)\n" +
                    "VALUES\n" +
                    "  (:id, :corp_id, :account, :cellphone, :name, :pyabbr, :pinyin,\n" +
                    "   :email, :is_admin, :dept_id, :duty, :priority, :create_time)";

    private static final String UPDATE_STAFF =
            "UPDATE s_staff\n" +
                    "   SET account = :account, cellphone = :cellphone, name = :name,\n" +
                    "       pyabbr = :pyabbr, pinyin = :pinyin, email = :email,\n" +
                    "       dept_id = :dept_id, duty = :duty, priority = :priority, update_time = :update_time\n" +
                    " WHERE id = :id";

    private static final String UPDATE_PASSWORD = "UPDATE s_staff SET password = :password, salt = :salt, password_update_time = :password_update_time WHERE id = :id";

    private static final String LOCK_STAFF = "UPDATE s_staff SET is_lock = 1 WHERE id = :id";
    private static final String UNLOCK_STAFF = "UPDATE s_staff SET is_lock = 0 WHERE id = :id";
    private static final String DELETE_STAFF = "UPDATE s_staff SET is_del = 1 WHERE id = :id";

    private static final String GET_STAFF_INFO =
            "SELECT a.id, a.corp_id, a.account, a.cellphone, a.salt, a.name, a.pyabbr,\n" +
                    "       a.pinyin, a.password_update_time, a.email, a.password, a.is_lock,\n" +
                    "       a.dept_id, (select t.name from s_department t where t.id = a.dept_id) dept_name,\n" +
                    "       a.is_admin, a.priority, a.create_time, a.update_time, b.name corp_name, b.portal_id,\n" +
                    "       (select t.code from s_portal t where t.id = b.portal_id) portal_code\n"+
                    "  FROM s_staff a, s_corporation b\n" +
                    " WHERE a.corp_id = b.id\n" +
                    "   AND a.is_del = 0\n" +
                    "   AND b.is_del = 0\n";

    private static final String IS_MANAGER = "SELECT count(*) FROM s_manager WHERE staff_id = :staff_id AND dept_id = :dept_id";

    private static final String CLEAR_STAFF_ROLES = "DELETE FROM s_staff_role WHERE staff_id = :staff_id";
    private static final String INSERT_STAFF_ROLE = "INSERT INTO s_staff_role(role_id, staff_id)VALUES(:role_id, :staff_id)";
    private static final String GET_STAFF_ROLES = "SELECT a.role_id id, b.code, b.name FROM s_staff_role a, s_role b WHERE a.role_id = b.id AND a.staff_id = :staff_id";

    public void insertStaff(Staff staff) {
        Map<String, Object> parameters = new ParameterBuilder(staff).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_STAFF, parameters);
        staff.setId(this.getNamedParameterJdbcTemplate().queryForObject("select @@IDENTITY", Long.class));
    }

    public void updateStaff(Staff staff) {
        Map<String, Object> parameters = new ParameterBuilder(staff).create();

        this.getNamedParameterJdbcTemplate().update(UPDATE_STAFF, parameters);
    }

    public void lockStaff(Long staff_id) {
        Map<String, Object> parameters = new ParameterBuilder("id", staff_id).create();
        this.getNamedParameterJdbcTemplate().update(LOCK_STAFF, parameters);
    }

    public void unlockStaff(Long staff_id) {
        Map<String, Object> parameters = new ParameterBuilder("id", staff_id).create();
        this.getNamedParameterJdbcTemplate().update(UNLOCK_STAFF, parameters);
    }

    public void deleteStaff(Long staff_id) {
        Map<String, Object> parameters = new ParameterBuilder("id", staff_id).create();
        this.getNamedParameterJdbcTemplate().update(DELETE_STAFF, parameters);
    }

    public void changePassword(Long staff_id, String password, String salt, Date update_time) {
        Map<String, Object> parameters = new ParameterBuilder()
                .put("id", staff_id)
                .put("password", password)
                .put("salt", salt)
                .put("password_update_time", update_time)
                .create();

        this.getNamedParameterJdbcTemplate().update(UPDATE_PASSWORD, parameters);
    }

    public Staff getStaff(Long staff_id) {
        Map<String, Object> params = new ParameterBuilder("id", staff_id).create();
        NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_STAFF_INFO).append(" and a.id = :id");

        Staff staff = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), params, ReflectiveRowMapperUtils.getRowMapper(Staff.class));
        return staff;
    }

    public Staff getStaffByAccount(String account) {
        Map<String, Object> params = new ParameterBuilder()
                .put("account", account)
                .create();
        NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_STAFF_INFO);
        sqlBuilder.append(" and a.account = :account");

        Staff staff = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), params, ReflectiveRowMapperUtils.getRowMapper(Staff.class));

        return staff;
    }

    public Staff getStaffByCellphone(String cellphone) {
        Map<String, Object> params = new ParameterBuilder()
                .put("cellphone", cellphone)
                .create();
        NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_STAFF_INFO);
        sqlBuilder.append(" and a.cellphone = :cellphone");

        Staff staff = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), params, ReflectiveRowMapperUtils.getRowMapper(Staff.class));

        return staff;
    }

    public Staff getStaffByEmail(String email) {
        Map<String, Object> params = new ParameterBuilder()
                .put("email", email)
                .create();
        NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_STAFF_INFO);
        sqlBuilder.append(" and a.email = :email");

        Staff staff = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), params, ReflectiveRowMapperUtils.getRowMapper(Staff.class));

        return staff;
    }

    public Page<Staff> findStaff(StaffQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(GET_STAFF_INFO);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.account = :account", query.getAccount());
            builder.appendIfNotEmpty(" and a.name = :name", query.getName());
            builder.appendIfNotEmpty(" and b.email = :email", query.getEmail());
            builder.appendIfNotEmpty(" and a.cellphone = :cellphone", query.getCellphone());
            builder.appendIfNotEmpty(" and a.create_time >= :create_time_min", query.getCreate_time_min());
            builder.appendIfNotEmpty(" and a.create_time >= :create_time_max", query.getCreate_time_max());
            builder.appendIfNotEmpty(" and a.is_lock = :is_lock", query.getIs_lock());
            builder.appendIfNotEmpty(" and a.corp_id = :corp_id", query.getCorp_id());
            builder.appendIfNotEmpty(" and a.dept_id = :dept_id", query.getDept_id());
            builder.appendIfNotEmpty(" and a.dept_id in (select t.id from s_department t where t.id = :ancestor_dept_id or t.path like concat((select t.path from s_department t where t.id = :ancestor_dept_id), :ancestor_dept_id, '/%'))", query.getAncestor_dept_id());
            builder.appendIfNotEmpty(" and a.staff_id in (select t.staff_id from s_staff_role t where t.role_id = :role_id)", query.getRole_id());
            builder.appendIfNotEmpty(" and a.is_admin = :is_admin", query.getIs_admin());
            builder.appendIfNotEmpty(" and (a.account like concat('%', :keyword, '%') or a.name like concat('%', :keyword, '%') or a.cellphone like concat('%', :keyword, '%') or a.email like concat('%', :keyword, '%') or REPLACE(a.pinyin, ' ', '') = concat('%', :keyword, '%') or a.pyabbr = concat('%', :keyword, '%'))", query.getKeyword());
        }
        builder.append(" order by a.priority, a.create_time desc");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(Staff.class));
    }

    public boolean isDirectManager(Long dept_id, Long staff_id) {
        return this.getNamedParameterJdbcTemplate().queryForObject(IS_MANAGER, new ParameterBuilder().put("staff_id", staff_id).put("dept_id", dept_id).create(), Long.class) > 0;
    }

    public void clearStaffRoles(Long staff_id) {
        this.getNamedParameterJdbcTemplate().update(CLEAR_STAFF_ROLES, new ParameterBuilder("staff_id", staff_id).create());
    }

    public void insertStaffRole(Long staff_id, Long role_id) {
        this.getNamedParameterJdbcTemplate().update(INSERT_STAFF_ROLE, new ParameterBuilder().put("staff_id", staff_id).put("role_id", role_id).create());
    }

    public List<RoleVo> getStaffRoles(Long staff_id) {
        return this.getNamedParameterJdbcTemplate().query(GET_STAFF_ROLES, new ParameterBuilder("staff_id", staff_id).create(), ReflectiveRowMapperUtils.getRowMapper(RoleVo.class));
    }
}

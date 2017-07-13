package com.yanyan.persist.system.impl;

import com.yanyan.core.db.NamedSqlBuilder;
import com.yanyan.core.db.ParameterBuilder;
import com.yanyan.core.db.ReflectiveRowMapperUtils;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Department;
import com.yanyan.data.domain.system.vo.StaffVo;
import com.yanyan.data.query.system.DepartmentQuery;
import com.yanyan.persist.impl.TreeDaoImpl;
import com.yanyan.persist.system.DepartmentDao;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 部门数据存取类
 * User: Saintcy
 * Date: 2015/8/24
 * Time: 17:33
 */
@Repository
public class DepartmentDaoImpl extends TreeDaoImpl implements DepartmentDao {
    private static final String INSERT_DEPARTMENT =
            "INSERT INTO s_department\n" +
                    "  (id, corp_id, parent_id, name, pyabbr, pinyin, remarks, priority, create_time)\n" +
                    "VALUES\n" +
                    "  (:id, :corp_id, :parent_id, :name, :pyabbr, :pinyin, :remarks, :priority, :create_time)";

    private static final String UPDATE_DEPARTMENT =
            "UPDATE s_department\n" +
                    "   SET parent_id = :parent_id, name = :name, pyabbr = :pyabbr, pinyin = :pinyin,\n" +
                    "       priority = :priority, remarks = :remarks, update_time = :update_time\n" +
                    " WHERE id = :id";

    private static final String DELETE_DEPARTMENT = "DELETE FROM s_department WHERE id = :id";

    private static final String GET_DEPARTMENT_INFO = "SELECT id, corp_id,\n" +
            "       (select t.name from s_corporation t where t.id = a.corp_id) corp_name,\n" +
            "       parent_id,\n" +
            "       (select t.name from s_department t where t.id = a.parent_id) parent_name,\n" +
            "       (select count(*) from s_department t where t.parent_id = a.id) hasChildren,\n" +
            "       path, level, name, pinyin, pyabbr, remarks, priority, create_time, update_time\n" +
            "  FROM s_department a\n" +
            " where 1 = 1\n";
    private static final String INSERT_MANAGER =
            "INSERT INTO s_manager (dept_id, staff_id) VALUES (:dept_id, :staff_id)";
    private static final String CLEAR_MANAGERS = "DELETE FROM s_manager WHERE dept_id = :dept_id";
    private static final String GET_MANAGERS =
            "SELECT id, name, account\n" +
                    "  FROM s_manager a, s_staff b\n" +
                    " WHERE a.staff_id = b.id\n" +
                    "   AND a.dept_id = :dept_id";

    public void insertDepartment(Department department) {
        Map<String, Object> parameters = new ParameterBuilder(department).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_DEPARTMENT, parameters);

        department.setId(this.getNamedParameterJdbcTemplate().queryForObject("select @@IDENTITY", Long.class));
    }

    public void updateDepartment(Department department) {
        Map<String, Object> parameters = new ParameterBuilder(department)
                .put("update_time", new Date())
                .create();

        this.getNamedParameterJdbcTemplate().update(UPDATE_DEPARTMENT, parameters);
    }

    public void deleteDepartment(Long dept_id) {
        Map<String, Object> parameters = new ParameterBuilder("id", dept_id).create();
        this.getNamedParameterJdbcTemplate().update(DELETE_DEPARTMENT, parameters);
    }

    public Department getDepartment(Long dept_id) {
        try {
            NamedSqlBuilder sqlBuilder = new NamedSqlBuilder(GET_DEPARTMENT_INFO);
            sqlBuilder.append(" and a.id = :id");

            Department department = this.getNamedParameterJdbcTemplate().queryForObject(sqlBuilder.toString(), new ParameterBuilder("id", dept_id).create(), ReflectiveRowMapperUtils.getRowMapper(Department.class));

            return department;
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public Page<Department> findDepartment(DepartmentQuery query) {
        NamedSqlBuilder builder = new NamedSqlBuilder(GET_DEPARTMENT_INFO);
        if (query != null) {
            builder.appendIfNotEmpty(" and a.corp_id = :corp_id", query.getCorp_id());
            builder.appendIfNotEmpty(" and a.name = :name", query.getName());
            builder.appendIfNotEmpty(" and a.parent_id = :parent_id", query.getParent_id());
            builder.appendIfNotEmpty(" and a.create_time >= :create_time_min", query.getCreate_time_min());
            builder.appendIfNotEmpty(" and a.create_time >= :create_time_max", query.getCreate_time_max());
            builder.appendIfNotEmpty(" and (a.name like concat('%', :keyword, '%') or REPLACE(a.pinyin, ' ', '') = concat('%', :keyword, '%') or a.pyabbr = concat('%', :keyword, '%'))", query.getKeyword());
        }
        builder.append(" order by a.priority, a.create_time desc");

        return this.getNamedParameterJdbcTemplate().queryForPage(builder.getSql(), new ParameterBuilder(query).create(), ReflectiveRowMapperUtils.getRowMapper(Department.class));
    }

    public void insertManager(Long dept_id, Long staff_id){
        Map<String, Object> parameters = new ParameterBuilder().put("dept_id", dept_id).put("staff_id", staff_id).create();
        this.getNamedParameterJdbcTemplate().update(INSERT_MANAGER, parameters);
    }

    public void clearManagers(Long dept_id){
        Map<String, Object> parameters = new ParameterBuilder().put("dept_id", dept_id).create();
        this.getNamedParameterJdbcTemplate().update(CLEAR_MANAGERS, parameters);
    }

    public List<StaffVo> getManagers(Long dept_id){
        Map<String, Object> parameters = new ParameterBuilder().put("dept_id", dept_id).create();
        return this.getNamedParameterJdbcTemplate().query(GET_MANAGERS, parameters, ReflectiveRowMapperUtils.getRowMapper(StaffVo.class));
    }

    @Override
    protected String getTableName() {
        return "s_department";
    }

}
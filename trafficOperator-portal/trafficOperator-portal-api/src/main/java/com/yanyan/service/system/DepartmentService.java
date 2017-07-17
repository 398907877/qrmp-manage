package com.yanyan.service.system;


import com.yanyan.data.domain.system.Department;
import com.yanyan.core.lang.Page;
import com.yanyan.data.query.system.DepartmentQuery;

import java.util.List;

/**
 * 部门服务
 * User: Saintcy
 * Date: 2015/3/4
 * Time: 17:25
 */
public interface DepartmentService {
    /**
     * 创建新部门
     *
     * @param department
     * @return
     */
    long createDepartment(Department department);

    /**
     * 更新部门
     *
     * @param department
     */
    void updateDepartment(Department department);

    /**
     * 删除部门
     *
     * @param dept_id
     */
    void deleteDepartment(Long dept_id);

    /**
     * 获得部门
     *
     * @param dept_id
     * @return
     */
    Department getDepartment(Long dept_id);

    /**
     * 查询部门
     *
     * @param query
     * @return
     */
    Page<Department> findDepartment(DepartmentQuery query);

    /**
     * 获取企业的所有部门
     * @param corp_id
     * @return
     */
    List<Department> getCorporationDepartmentList(Long corp_id);
}

package com.yanyan.service.system.impl;

import com.yanyan.core.BusinessException;
import com.yanyan.core.lang.Page;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.core.spring.validator.group.Update;
import com.yanyan.core.util.NumberUtils;
import com.yanyan.data.domain.system.Department;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.data.query.system.DepartmentQuery;
import com.yanyan.data.query.system.StaffQuery;
import com.yanyan.persist.TreeDao;
import com.yanyan.persist.system.DepartmentDao;
import com.yanyan.service.AbstractTreeService;
import com.yanyan.service.system.DepartmentService;
import com.yanyan.service.system.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 部门服务
 * User: Saintcy
 * Date: 2015/3/4
 * Time: 17:31
 */
@Service
public class DepartmentServiceImpl extends AbstractTreeService implements DepartmentService {
    @Autowired
    private DepartmentDao departmentDao;
    @Autowired
    private StaffService staffService;

    public long createDepartment(Department department) {
        validate(department, Create.class);

        departmentDao.insertDepartment(department);
        createPath(department.getId(), department.getParent_id());

        addManagers(department.getId(), department.getManager_id());

        return department.getId();
    }

    public void updateDepartment(Department department) {
        validate(department, Update.class);
        updatePath(department.getId(), department.getParent_id(), departmentDao.getPath(department.getId()));
        departmentDao.updateDepartment(department);
        departmentDao.clearManagers(department.getId());
        addManagers(department.getId(), department.getManager_id());
    }

    private void addManagers(Long dept_id, List<Long> manager_ids) {
        if (manager_ids != null) {
            for (Long staff_id : manager_ids) {
                departmentDao.insertManager(dept_id, staff_id);
            }
        }
    }

    public void validate(Department department) {
        super.validate(department);

        //验证名称是否重复
        DepartmentQuery departmentQuery = new DepartmentQuery();
        departmentQuery.setCorp_id(department.getCorp_id());
        departmentQuery.setName(department.getName());
        departmentQuery.setParent_id(department.getParent_id());
        departmentQuery.one();

        Page<Department> departmentPage = departmentDao.findDepartment(departmentQuery);

        if (departmentPage.getTotalCount() > 0 && NumberUtils.equals(department.getId(), departmentPage.getFirstRow().getId())) {
            throw new BusinessException("department.name.existed", new Object[]{department.getName()}, "部门名称[" + department.getName() + "]已经存在！");
        }
    }

    public void deleteDepartment(Long dept_id) {
        if (departmentDao.hasChildren(dept_id)) {
            throw new BusinessException("department.has.children", "部门含有子部门不能删除");
        }
        StaffQuery query = new StaffQuery();
        query.setDept_id(dept_id);
        query.one();
        Page<Staff> staffPage = staffService.findStaff(query);
        if (!staffPage.isEmpty()) {
            throw new BusinessException("department.has.staff", "部门含有人员不能删除");
        }
        departmentDao.deleteDepartment(dept_id);
        departmentDao.clearManagers(dept_id);
    }

    public Department getDepartment(Long dept_id) {
        Department department = departmentDao.getDepartment(dept_id);
        if (department != null) {
            department.setManagers(departmentDao.getManagers(dept_id));
        }
        return department;
    }

    /*public Department getRootDepartment(long corporation_id) {
        //查找根部门
        DepartmentQuery departmentQuery = new DepartmentQuery();
        departmentQuery.setCorp_id(corporation_id);
        departmentQuery.setParent_id(0L);
        departmentQuery.one();

        Page<Department> departmentPage = departmentDao.findDepartment(departmentQuery);
        if (!departmentPage.isEmpty()) {
            return departmentPage.getFirstRow();
        } else {
            return null;
        }
    }*/

    public Page<Department> findDepartment(DepartmentQuery query) {
        Page<Department> page = departmentDao.findDepartment(query);
        for (Department department : page.getRows()) {
            department.setManagers(departmentDao.getManagers(department.getId()));
        }
        return page;
    }

    public List<Department> getCorporationDepartmentList(Long corp_id) {
        DepartmentQuery departmentQuery = new DepartmentQuery();
        departmentQuery.setCorp_id(corp_id);
        return findDepartment(departmentQuery).getRows();
    }

    @Override
    protected TreeDao getTreeDao() {
        return departmentDao;
    }
}


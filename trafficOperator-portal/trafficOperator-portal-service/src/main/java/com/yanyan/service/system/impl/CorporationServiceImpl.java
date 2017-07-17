package com.yanyan.service.system.impl;


import com.yanyan.data.domain.system.Role;
import com.yanyan.core.lang.Page;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.core.spring.validator.group.Update;
import com.yanyan.data.domain.system.Corporation;
import com.yanyan.data.domain.system.Department;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.persist.system.CorporationDao;
import com.yanyan.data.query.system.CorporationQuery;
import com.yanyan.service.BaseService;
import com.yanyan.service.system.CorporationService;
import com.yanyan.service.system.DepartmentService;
import com.yanyan.service.system.RoleService;
import com.yanyan.service.system.StaffService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 企业管理
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Service
public class CorporationServiceImpl extends BaseService implements CorporationService {
    @Autowired
    private CorporationDao corporationDao;
    @Autowired
    private StaffService staffService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private DepartmentService departmentService;

    public long createCorporation(Corporation corporation) {
        validate(corporation, Create.class);
        corporationDao.insertCorporation(corporation);

        //添加企业默认部门
        Department department = new Department();
        department.setCorp_id(corporation.getId());
        department.setParent_id(0L);
        department.setName("默认部门");
        department.setRemarks("默认部门");
        long dept_id = departmentService.createDepartment(department);

        //查找所属门户的各种默认角色
        Role adminRole = roleService.getAdminRole(corporation.getPortal_id());
        List<Long> role_id = new ArrayList<Long>();
        if (adminRole != null) {
            role_id.add(adminRole.getId());
        }

        //添加管理员账号
        Staff staff = new Staff();
        staff.setAccount(corporation.getAdmin_account());
        staff.setName("企业管理员");
        staff.setPinyin("Qi Ye Guan Li Yuan");
        staff.setPyabbr("QYGLY");
        staff.setCellphone(corporation.getContact_phone());
        staff.setEmail(corporation.getEmail());
        staff.setCorp_id(corporation.getId());
        staff.setRole_id(role_id);
        staff.setIs_admin(1);//设为默认管理员账号
        staff.setDept_id(dept_id);
        staff.setPassword(corporation.getAdmin_password());

        staffService.createStaff(staff);

        return corporation.getId();
    }

    public void updateCorporation(Corporation corporation) {
        //验证企业信息
        validate(corporation, Update.class);

        corporationDao.updateCorporation(corporation);
    }


    public void deleteCorporation(Long corp_id) {
         corporationDao.deleteCorporation(corp_id);
    }

    public void restoreCorporation(Long corp_id) {
        corporationDao.restoreCorporation(corp_id);
    }

    public Corporation getCorporation(Long corp_id) {
        Corporation corporation = corporationDao.getCorporation(corp_id);

        return corporation;
    }

    public Page<Corporation> findCorporation(CorporationQuery query) {
        return corporationDao.findCorporation(query);
    }
}
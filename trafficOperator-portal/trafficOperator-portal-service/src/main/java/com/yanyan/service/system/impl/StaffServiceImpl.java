package com.yanyan.service.system.impl;

import com.yanyan.Configs;
import com.yanyan.core.BusinessException;
import com.yanyan.core.lang.Page;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.core.spring.validator.group.Update;
import com.yanyan.core.util.NumberUtils;
import com.yanyan.core.util.PasswordUtils;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.data.domain.system.vo.RoleVo;
import com.yanyan.data.query.system.StaffQuery;
import com.yanyan.persist.system.DepartmentDao;
import com.yanyan.persist.system.StaffDao;
import com.yanyan.service.BaseService;
import com.yanyan.service.system.StaffService;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.codec.Hex;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 人员管理
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Service
public class StaffServiceImpl extends BaseService implements StaffService {
    @Autowired
    private StaffDao staffDao;
    @Autowired
    private DepartmentDao departmentDao;

    public long createStaff(Staff staff) {
        validate(staff, Create.class);
        staffDao.insertStaff(staff);
        setStaffRoles(staff.getId(), staff.getRole_id());
        initStaffPassword(staff.getId());

        return staff.getId();
    }

    public void updateStaff(Staff staff) {
        validate(staff, Update.class);
        staffDao.updateStaff(staff);
        staffDao.clearStaffRoles(staff.getId());
        setStaffRoles(staff.getId(), staff.getRole_id());
    }

    private void setStaffRoles(Long staff_id, List<Long> role_ids) {
        if (role_ids != null) {
            for (Long role_id : role_ids) {
                staffDao.insertStaffRole(staff_id, role_id);
            }
        }
    }

    private void validate(Staff staff, Class<?>... groups) {
        super.validate(staff, groups);

        if (!checkStaffAccount(staff.getId(), staff.getAccount())) {
            throw new BusinessException("staff.account.existed", new Object[]{staff.getAccount()}, "用户名[" + staff.getAccount() + "]已经存在！");
        }

        if (!checkStaffCellphone(staff.getId(), staff.getCellphone())) {
            throw new BusinessException("staff.cellphone.existed", new Object[]{staff.getCellphone()}, "手机号[" + staff.getCellphone() + "]已经存在！");
        }

        /*if (!checkStaffEmail(staff.getId(), staff.getEmail())) {
            throw new BusinessException("staff.email.existed", new Object[]{staff.getEmail()}, "邮箱地址[" + staff.getEmail() + "]已经存在！");
        }*/
    }

    public void lockStaff(Long staff_id) {
        staffDao.lockStaff(staff_id);
    }

    public void unlockStaff(Long staff_id) {
        staffDao.unlockStaff(staff_id);
    }

    public void deleteStaff(Long staff_id) {
        staffDao.deleteStaff(staff_id);
    }

    public void initStaffPassword(Long staff_id) {
        byte[] salt = PasswordUtils.generate8ByteSalt();
        staffDao.changePassword(staff_id, PasswordUtils.encryptPassword(Configs.DEFAULT_PASSWORD, salt), Hex.encodeToString(salt), null);
    }

    public String randomStaffPassword(Long staff_id) {
        byte[] salt = PasswordUtils.generate8ByteSalt();
        String password = RandomStringUtils.random(6, true, true);
        staffDao.changePassword(staff_id, PasswordUtils.encryptPassword(password, salt), Hex.encodeToString(salt), null);

        return password;
    }

    public void changeStaffPassword(Long staff_id, String new_password, String old_password) {
        Staff staff = staffDao.getStaff(staff_id);
        if (PasswordUtils.validatePassword(old_password, Hex.decode(staff.getSalt()), staff.getPassword())) {
            byte[] salt = PasswordUtils.generate8ByteSalt();
            String encryptedPassword = PasswordUtils.encryptPassword(new_password, salt);
            staffDao.changePassword(staff_id, encryptedPassword, Hex.encodeToString(salt), new Date());
        } else {
            throw new BusinessException("staff.old.password.incorrect", "原始密码不正确");
        }
    }

    public Staff getStaff(Long staff_id) {
        Staff staff = staffDao.getStaff(staff_id);
        if (staff != null) {
            getStaffRoles(staff);
        }

        return staff;
    }

    public Staff getStaffByAccount(String account) {
        Staff staff = staffDao.getStaffByAccount(account);
        if (staff != null) {
            getStaffRoles(staff);
        }

        return staff;
    }

    public Staff getStaffByCellphone(String cellphone) {
        Staff staff = staffDao.getStaffByCellphone(cellphone);
        if (staff != null) {
            getStaffRoles(staff);
        }

        return staff;
    }

    public Staff getStaffByEmail(String email) {
        Staff staff = staffDao.getStaffByEmail(email);
        if (staff != null) {
            getStaffRoles(staff);
        }

        return staff;
    }

    private void getStaffRoles(Staff staff) {
        staff.setRoles(staffDao.getStaffRoles(staff.getId()));
        if (staff.getRoles() != null) {
            List<Long> role_id = new ArrayList<Long>();
            for (RoleVo role : staff.getRoles()) {
                role_id.add(role.getId());
            }
            staff.setRole_id(role_id);
        }
    }

    public Staff getAdminStaff(Long corporation_id) {
        StaffQuery staffQuery = new StaffQuery();
        staffQuery.setCorp_id(corporation_id);
        staffQuery.setIs_admin(1);
        staffQuery.one();

        Page<Staff> staffPage = findStaff(staffQuery);
        if (staffPage.isEmpty()) {
            return null;
        } else {
            return getStaff(staffPage.getFirstRow().getId());
        }
    }

    public boolean hasStaffOfRole(Long role_id) {
        StaffQuery staffQuery = new StaffQuery();
        staffQuery.setRole_id(role_id);
        staffQuery.one();
        Page<Staff> staffPage = findStaff(staffQuery);
        return !staffPage.isEmpty();
    }

    public Page<Staff> findStaff(StaffQuery query) {
        return staffDao.findStaff(query);
    }

    public List<Staff> findDescendantStaffList(Long dept_id) {
        StaffQuery query = new StaffQuery();
        query.setAncestor_dept_id(dept_id);
        query.setCountTotal(false);

        return findStaff(query).getRows();
    }

    public boolean checkStaffAccount(Long id, String account) {
        if (StringUtils.isEmpty(account)) return true;
        StaffQuery staffQuery = new StaffQuery();
        staffQuery.setAccount(account);
        staffQuery.one();

        Page<Staff> staffPage = staffDao.findStaff(staffQuery);
        return staffPage.getTotalCount() <= 0 || NumberUtils.equals(id, staffPage.getFirstRow().getId());
    }

    public boolean checkStaffCellphone(Long id, String cellphone) {
        if (StringUtils.isEmpty(cellphone)) return true;
        StaffQuery staffQuery = new StaffQuery();
        staffQuery.setCellphone(cellphone);
        staffQuery.one();

        Page<Staff> staffPage = staffDao.findStaff(staffQuery);
        return staffPage.getTotalCount() <= 0 || NumberUtils.equals(id, staffPage.getFirstRow().getId());
    }

    public boolean checkStaffEmail(Long id, String email) {
        if (StringUtils.isEmpty(email)) return true;
        StaffQuery staffQuery = new StaffQuery();
        staffQuery.setEmail(email);
        staffQuery.one();

        Page<Staff> staffPage = staffDao.findStaff(staffQuery);
        return staffPage.getTotalCount() <= 0 || NumberUtils.equals(id, staffPage.getFirstRow().getId());
    }

    public List<Staff> getDepartmentChildStaffList(Long dept_id) {
        StaffQuery query = new StaffQuery();
        query.setDept_id(dept_id);
        return findStaff(query).getRows();
    }

    public boolean isDirectManager(Long dept_id, Long staff_id) {
        return staffDao.isDirectManager(dept_id, staff_id);
    }

    public boolean isManager(Long dept_id, Long staff_id) {
        boolean isManager = staffDao.isDirectManager(dept_id, staff_id);
        if (!isManager) {
            List<Long> ancestors = departmentDao.getAncestors(dept_id);
            int i = 0;
            while (!isManager) {//找是不是上级部门直属主管
                isManager = isDirectManager(ancestors.get(i), staff_id);
            }
        }

        return isManager;
    }
}
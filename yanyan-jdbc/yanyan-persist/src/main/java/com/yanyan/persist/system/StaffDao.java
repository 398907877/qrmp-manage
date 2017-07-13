package com.yanyan.persist.system;

import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.data.domain.system.vo.RoleVo;
import com.yanyan.data.query.system.StaffQuery;

import java.util.Date;
import java.util.List;

/**
 * 人员数据存取对象
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
public interface StaffDao {
    void insertStaff(Staff staff);

    void updateStaff(Staff staff);

    void deleteStaff(Long id);

    void lockStaff(Long id);

    void unlockStaff(Long id);

    void changePassword(Long staff_id, String password, String salt, Date update_time);

    Staff getStaff(Long id);

    Staff getStaffByAccount(String account);

    Staff getStaffByCellphone(String cellphone);

    Staff getStaffByEmail(String email);

    Page<Staff> findStaff(StaffQuery query);

    boolean isDirectManager(Long dept_id, Long staff_id);

    void clearStaffRoles(Long staff_id);

    void insertStaffRole(Long staff_id, Long role_id);

    List<RoleVo> getStaffRoles(Long staff_id);
}

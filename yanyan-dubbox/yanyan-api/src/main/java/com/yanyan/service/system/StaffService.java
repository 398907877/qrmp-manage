package com.yanyan.service.system;


import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.data.query.system.StaffQuery;

import java.util.List;

/**
 * 人员服务
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 16:17
 */
public interface StaffService {
    /**
     * 创建新人员
     *
     * @param staff
     * @return
     */
    long createStaff(Staff staff);

    /**
     * 更新人员信息
     *
     * @param staff
     */
    void updateStaff(Staff staff);

    /**
     * 锁定人员
     *
     * @param staff_id
     */
    void lockStaff(Long staff_id);

    /**
     * 解锁人员
     *
     * @param staff_id
     */
    void unlockStaff(Long staff_id);

    /**
     * 删除人员
     *
     * @param staff_id
     */
    void deleteStaff(Long staff_id);

    /**
     * 初始化密码
     *
     * @param staff_id
     */
    void initStaffPassword(Long staff_id);

    /**
     * 随机生成密码，必须在验证用户身份合法后，比如手机号+验证码认证通过
     *
     * @param staff_id
     * @return 返回重置后的密码
     */
    String randomStaffPassword(Long staff_id);

    /**
     * 修改密码
     *
     * @param staff_id
     * @param new_password
     * @param old_password
     */
    void changeStaffPassword(Long staff_id, String new_password, String old_password);

    /**
     * 获取人员
     *
     * @param staff_id
     */
    Staff getStaff(Long staff_id);

    /**
     * 是否有role_id角色的人员
     *
     * @param role_id
     * @return
     */
    boolean hasStaffOfRole(Long role_id);

    /**
     * 查询人员
     *
     * @param query 过滤条件集合
     */
    Page<Staff> findStaff(StaffQuery query);

    /**
     * 查询部门及子部门的人员
     *
     * @param dept_id
     * @return
     */
    List<Staff> findDescendantStaffList(Long dept_id);

    /**
     * 根据用户名找人员
     *
     * @param account
     * @return
     */
    Staff getStaffByAccount(String account);

    /**
     * 根据手机号找人员
     *
     * @param cellphone
     * @return
     */
    Staff getStaffByCellphone(String cellphone);

    /**
     * 根据email找人员
     *
     * @param email
     * @return
     */
    Staff getStaffByEmail(String email);

    /**
     * 获取企业管理员
     *
     * @param corporation_id
     * @return
     */
    Staff getAdminStaff(Long corporation_id);

    /**
     * 检查账号是否唯一
     *
     * @param id
     * @param account
     * @return
     */
    boolean checkStaffAccount(Long id, String account);

    /**
     * 检查手机号是否唯一
     *
     * @param id
     * @param cellphone
     * @return
     */
    boolean checkStaffCellphone(Long id, String cellphone);

    /**
     * 检查Email是否唯一
     *
     * @param id
     * @param email
     * @return
     */
    boolean checkStaffEmail(Long id, String email);

    /**
     * 获取部门直属人员
     *
     * @return
     */
    List<Staff> getDepartmentChildStaffList(Long dept_id);

    /**
     * 是否是部门主管
     *
     * @param dept_id
     * @param staff_id
     * @return
     */
    boolean isDirectManager(Long dept_id, Long staff_id);

    /**
     * 是否是部门主管
     *
     * @param dept_id
     * @param staff_id
     * @return
     */
    boolean isManager(Long dept_id, Long staff_id);
}

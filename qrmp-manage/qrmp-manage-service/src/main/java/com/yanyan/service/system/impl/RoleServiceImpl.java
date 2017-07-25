package com.yanyan.service.system.impl;


import com.yanyan.core.util.NumberUtils;
import com.yanyan.data.domain.system.Role;
import com.yanyan.data.query.system.RoleQuery;
import com.yanyan.service.system.StaffService;
import com.yanyan.core.BusinessException;
import com.yanyan.core.lang.Page;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.core.spring.validator.group.Update;
import com.yanyan.persist.system.RoleDao;
import com.yanyan.service.BaseService;
import com.yanyan.service.system.RoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * 角色服务
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Service
public class RoleServiceImpl extends BaseService implements RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private StaffService staffService;

    public long createRole(Role role) {
        validate(role, Create.class);
        if (role.getIs_admin() == 1) {//若为默认管理员，则取消其他默认的管理员
            roleDao.unsetAdmin(role.getPortal_id());
        }

        roleDao.insertRole(role);

        return role.getId();
    }

    public void updateRole(Role role) {
        validate(role, Update.class);
        if (role.getIs_admin() == 1) {//若为默认管理员，则取消其他默认的管理员
            roleDao.unsetAdmin(role.getPortal_id());
        }
        roleDao.updateRole(role);
    }

    private void validate(Role role, Class<?>... groups) {
        super.validate(role, groups);//验证字段
        //检查角色编码是否重复
        if (!checkRoleCode(role.getId(), role.getCode())) {
            throw new BusinessException("role.code.existed", new Object[]{role.getCode()}, "角色编码[" + role.getCode() + "]已经存在！");
        }
    }

    public void deleteRole(Long role_id) {
        if (staffService.hasStaffOfRole(role_id)) {
            throw new BusinessException("role.in.use", "存在该角色的人员，无法删除！");
        }

        roleDao.deleteRole(role_id);
    }

    public Role getRole(Long role_id) {
        return roleDao.getRole(role_id);
    }

    public Role getAdminRole(Long portal_id) {
        RoleQuery roleQuery = new RoleQuery();
        roleQuery.setPortal_id(portal_id);
        roleQuery.setIs_admin(1);
        roleQuery.one();

        Page<Role> rolePage = findRole(roleQuery);

        if (!rolePage.isEmpty()) {
            return rolePage.getFirstRow();
        } else {
            return null;
        }
    }

    public List<Role> getAdminRoleList(List<Long> portal_id) {
        List<Role> roleList = new ArrayList<Role>();
        for (Long id : portal_id) {
            Role role = getAdminRole(id);
            if (role != null) {
                roleList.add(role);
            }
        }
        return roleList;
    }

    public Page<Role> findRole(RoleQuery query) {
        return roleDao.findRole(query);
    }

    public List<Role> getPortalRoleList(Long portal_id){
        RoleQuery roleQuery = new RoleQuery();
        roleQuery.setPortal_id(portal_id);

        return findRole(roleQuery).getRows();
    }

    public boolean checkRoleCode(Long id, String code) {
        if (StringUtils.isEmpty(code)) return true;
        RoleQuery roleQuery = new RoleQuery();
        roleQuery.setCode(code);
        roleQuery.one();

        Page<Role> rolePage = roleDao.findRole(roleQuery);
        return rolePage.getTotalCount() <= 0 || NumberUtils.equals(id, rolePage.getFirstRow().getId());
    }
}
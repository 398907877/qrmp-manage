package com.yanyan.service.system.impl;


import com.yanyan.service.system.ResourceService;
import com.yanyan.data.domain.system.Menu;
import com.yanyan.data.domain.system.Permission;
import com.yanyan.data.domain.system.Resource;
import com.yanyan.persist.system.PrivilegeDao;
import com.yanyan.service.BaseService;
import com.yanyan.service.system.PrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 权限服务
 * User: Saintcy
 * Date: 2015/3/5
 * Time: 23:15
 */
@Service
public class PrivilegeServiceImpl extends BaseService implements PrivilegeService {
    @Autowired
    private PrivilegeDao privilegeDao;
    @Autowired
    private ResourceService resourceService;

    public void grantRolePrivileges(Long role_id, List<Long> permissions) {
        privilegeDao.revokeRolePermissions(role_id);
        if (permissions != null) {
            for (Long permission_id : permissions) {
                privilegeDao.grantRolePermission(role_id, permission_id);
            }
        }
    }

    public void grantStaffPrivileges(Long staff_id, List<Long> permissions) {
        privilegeDao.revokeStaffPermissions(staff_id);
        if (permissions != null) {
            for (Long permission_id : permissions) {
                privilegeDao.grantStaffPermission(staff_id, permission_id);
            }
        }
    }

    public void revokePrivilegesOfResource(Long resource_id) {
        privilegeDao.revokeRolePrivilegesOfResource(resource_id);
        privilegeDao.revokeStaffPrivilegesOfResource(resource_id);
    }

    public void revokePrivilegesOfRemovedPermissions(Long resource_id) {
        privilegeDao.revokeRolePrivilegesOfRemovedPermissions(resource_id);
        privilegeDao.revokeStaffPrivilegesOfRemovedPermissions(resource_id);
    }

    public List<Permission> getStaffPrivileges(Long staff_id) {
        return privilegeDao.getStaffPrivileges(staff_id);
    }

    public List<Permission> getRolePrivileges(Long role_id) {
        return privilegeDao.getRolePrivileges(role_id);
    }

    public List<String> getAllPrivileges(Long staff_id) {
        List<String> stringPermissions = new ArrayList<String>();
        List<Permission> permissions = privilegeDao.getAllPrivileges(staff_id);
        Map<Long, Resource> resourceMap = new HashMap<Long, Resource>();
        if (permissions != null) {
            for (Permission permission : permissions) {
                String stringPermission = permission.getCode();
                Long parent_resource_id = permission.getResource_id();
                while (parent_resource_id != null && parent_resource_id != 0) {
                    Resource resource = resourceMap.get(parent_resource_id);
                    if (resource == null) {
                        resource = resourceService.getResource(parent_resource_id);
                        if (resource == null) {
                            break;
                        }
                    }
                    resourceMap.put(resource.getId(), resource);
                    parent_resource_id = resource.getParent_id();
                    stringPermission = resource.getCode() + ":" + stringPermission;
                }
                stringPermissions.add(stringPermission);
            }
        }
        resourceMap.clear();

        return stringPermissions;
    }

    public List<Menu> getAccessibleMenuList(Long staff_id) {
        List<Resource> resources = privilegeDao.getAccessibleResources(staff_id);
        List<Menu> menus = findChildMenus(0L, resources);

        return menus;
    }

    private List<Menu> findChildMenus(Long id, List<Resource> resources) {
        List<Menu> menus = new ArrayList<Menu>();
        if (resources != null) {
            for (Resource resource : resources) {
                if (resource.getParent_id() == id) {
                    Menu menu = new Menu(resource.getId(), resource.getCode(), resource.getName(), resource.getIcon(), resource.getUrl(), resource.getTarget(), resource.getIs_show() != 1);
                    menu.setChildren(findChildMenus(menu.getId(), resources));
                    menus.add(menu);
                }
            }
        }
        return menus;
    }
}

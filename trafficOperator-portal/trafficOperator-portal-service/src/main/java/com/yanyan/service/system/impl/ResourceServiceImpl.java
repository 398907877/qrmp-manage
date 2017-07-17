package com.yanyan.service.system.impl;

import com.yanyan.core.BusinessException;
import com.yanyan.core.lang.Page;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.core.spring.validator.group.Update;
import com.yanyan.core.util.NumberUtils;
import com.yanyan.data.domain.system.Permission;
import com.yanyan.data.domain.system.Resource;
import com.yanyan.data.query.system.ResourceQuery;
import com.yanyan.persist.TreeDao;
import com.yanyan.persist.system.PermissionDao;
import com.yanyan.persist.system.ResourceDao;
import com.yanyan.service.AbstractTreeService;
import com.yanyan.service.system.PrivilegeService;
import com.yanyan.service.system.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 资源管理
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
@Service
public class ResourceServiceImpl extends AbstractTreeService implements ResourceService {
    @Autowired
    private ResourceDao resourceDao;
    @Autowired
    private PermissionDao permissionDao;
    @Autowired
    private PrivilegeService privilegeService;

    public long createResource(Resource resource) {
        validate(resource, Create.class);
        resourceDao.insertResource(resource);
        if (resource.getPermissions() != null) {
            for (Permission permission : resource.getPermissions()) {
                permission.setResource_id(resource.getId());
                permissionDao.insertPermission(permission);
            }
        }

        createPath(resource.getId(), resource.getParent_id());

        return resource.getId();
    }

    public void updateResource(Resource resource) {
        validate(resource, Update.class);

        permissionDao.deletePermissionByResource(resource.getId());
        if (resource.getPermissions() != null) {
            for (Permission permission : resource.getPermissions()) {
                permission.setResource_id(resource.getId());
                //if (permission.getId() == null) {
                permissionDao.insertPermission(permission);
                //} else {//依然是insert，有id自动会insert原来的id
                //    permissionDao.updatePermission(permission);
                //}
            }
        }

        updatePath(resource.getId(), resource.getParent_id(), resourceDao.getPath(resource.getId()));
        resourceDao.updateResource(resource);
        //权限若减少，则权限的授权需要回收
        privilegeService.revokePrivilegesOfRemovedPermissions(resource.getId());
    }

    private void validate(Resource resource, Class<?> groups) {
        super.validate(resource, groups);

        if (!checkResourceCode(resource.getId(), resource.getPortal_id(), resource.getCode())) {
            throw new BusinessException("resource.code.existed", new Object[]{resource.getCode()}, "编码[" + resource.getCode() + "]已经存在！");
        }
    }

    public void deleteResource(Long resource_id) {
        if (resourceDao.hasChildren(resource_id)) {
            throw new BusinessException("resource.has.children", "资源含有子资源不能删除");
        }
        permissionDao.deletePermissionByResource(resource_id);
        resourceDao.deleteResource(resource_id);
        privilegeService.revokePrivilegesOfResource(resource_id);
    }

    public Resource getResource(Long resource_id) {
        Resource resource = resourceDao.getResource(resource_id);
        if (resource != null) {
            resource.setPermissions(permissionDao.getPermissionByResource(resource_id));
        }
        return resource;
    }

    public Page<Resource> findResource(ResourceQuery query) {
        Page<Resource> page = resourceDao.findResource(query);
        if (!page.isEmpty() && query != null && query.isLoadPermission()) {
            for (Resource resource : page.getRows()) {
                resource.setPermissions(permissionDao.getPermissionByResource(resource.getId()));
            }
        }

        return page;
    }

    public List<Resource> getPortalResourceList(Long portal_id) {
        ResourceQuery resourceQuery = new ResourceQuery();
        resourceQuery.setPortal_id(portal_id);
        resourceQuery.setLoadPermission(true);

        return findResource(resourceQuery).getRows();
    }

    public boolean checkResourceCode(Long id, Long portal_id, String code) {
        ResourceQuery resourceQuery = new ResourceQuery();
        resourceQuery.setPortal_id(portal_id);
        resourceQuery.setCode(code);
        resourceQuery.one();

        Page<Resource> resourcePage = resourceDao.findResource(resourceQuery);
        return resourcePage.getTotalCount() <= 0 || NumberUtils.equals(id, resourcePage.getFirstRow().getId());
    }

    @Override
    protected TreeDao getTreeDao() {
        return resourceDao;
    }
}
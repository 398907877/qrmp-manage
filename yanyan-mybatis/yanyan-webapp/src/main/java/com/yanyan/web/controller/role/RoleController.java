package com.yanyan.web.controller.role;

import com.google.common.collect.Lists;
import com.yanyan.core.util.NumberUtils;
import com.yanyan.data.domain.system.Permission;
import com.yanyan.data.domain.system.Portal;
import com.yanyan.data.domain.system.Resource;
import com.yanyan.data.domain.system.Role;
import com.yanyan.data.query.system.RoleQuery;
import com.yanyan.data.vo.RolePrivilegesModel;
import com.yanyan.data.vo.TreeNode;
import com.yanyan.service.system.PortalService;
import com.yanyan.service.system.PrivilegeService;
import com.yanyan.service.system.ResourceService;
import com.yanyan.service.system.RoleService;
import com.yanyan.web.controller.util.PortalUtils;
import com.yanyan.core.web.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 角色
 * User: Saintcy
 * Date: 2016/8/11
 * Time: 15:42
 */
@Slf4j
@Controller
@RequestMapping("/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @Autowired
    private PortalService portalService;
    @Autowired
    private PrivilegeService privilegeService;
    @Autowired
    private ResourceService resourceService;

    @RequestMapping("/list")
    public String list(@ModelAttribute("query") RoleQuery query, Model model) {
        List<Portal> portals = portalService.findPortal(null).getRows();
        model.addAttribute("portals", portals);

        if (CollectionUtils.isNotEmpty(portals)) {
            if (PortalUtils.indexOf(portals, query.getPortal_id()) < 0) {//没传入或传入的不对，则默认选中第一个
                query.setPortal_id(portals.get(0).getId());
            }

            model.addAttribute("page", roleService.findRole(query));
        }

        return "/role/list";
    }

    @RequestMapping("/find")
    @ResponseBody
    public Model find(@RequestBody RoleQuery query) {
        try {
            return DataResponse.success("page", roleService.findRole(query));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/create")
    public String create(@RequestParam("portal_id") Long portal_id, Model model) {
        Role role = new Role();
        Portal portal = portalService.getPortal(portal_id);
        role.setPortal_id(portal.getId());
        role.setPortal_name(portal.getName());
        role.setIs_admin(0);
        model.addAttribute("role", role);
        List<Portal> portals = portalService.findAllPortalList();
        model.addAttribute("portals", portals);

        return "/role/form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model create(@RequestBody Role role) {
        try {
            return DataResponse.success("id", roleService.createRole(role));
        } catch (Exception e) {
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/update")
    public String update(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("role", roleService.getRole(id));
        List<Portal> portals = portalService.findAllPortalList();
        model.addAttribute("portals", portals);

        return "/role/form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model update(@RequestBody Role role) {
        try {
            roleService.updateRole(role);
            return DataResponse.success();
        } catch (Exception e) {
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model delete(@PathVariable Long id) {
        try {
            roleService.deleteRole(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/check_code", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model checkCode(@RequestParam(value = "id", required = false) Long id, @RequestParam("code") String code) {
        if (roleService.checkRoleCode(id, code)) {
            return DataResponse.success();
        } else {
            return DataResponse.failure("编码[" + code + "]已存在");
        }
    }

    @RequestMapping("/grant")
    public String grant(@RequestParam(value = "id") Long id, Model model) {
        Role role = roleService.getRole(id);
        model.addAttribute("role", role);
        List<Resource> resources = resourceService.getPortalResourceList(role.getPortal_id());
        List<Permission> permissions = privilegeService.getRolePrivileges(id);

        model.addAttribute("permissions", buildPermissionTree(0, resources, permissions));
        List<Portal> portals = portalService.findAllPortalList();
        model.addAttribute("portals", portals);

        return "/role/grant";
    }

    private List<TreeNode> buildPermissionTree(long parent_resource_id, List<Resource> resources, List<Permission> permissions) {
        List<TreeNode> nodes = Lists.newArrayList();
        for (Resource resource : resources) {
            if (resource.getParent_id().longValue() == parent_resource_id) {
                TreeNode node = new TreeNode();
                node.setId(resource.getId() + "");
                node.setName(resource.getName());
                node.setIcon(resource.getIcon());

                //加载下一级
                List<TreeNode> childNodes = buildPermissionTree(resource.getId(), resources, permissions);

                //加载权限
                if (resource.getPermissions() != null) {
                    int i = 0;
                    for (Permission permission : resource.getPermissions()) {
                        if (StringUtils.equals(permission.getCode(), "view")) {//把查看合并到父菜单，注意view权限需要摆在第一个，并且每个菜单都至少有一个view
                            node.setId("" + permission.getId());
                            node.setChecked(hasPermission(permission, permissions));
                        } else {
                            TreeNode node2 = new TreeNode();
                            node2.setId("" + permission.getId());
                            node2.setName(permission.getName());
                            node2.setChecked(hasPermission(permission, permissions));

                            childNodes.add(i++, node2);//插入到前面
                        }
                    }
                }

                node.setChildren(childNodes);

                nodes.add(node);
            }
        }

        return nodes;
    }

    private boolean hasPermission(Permission permission, List<Permission> permissions) {
        for (Permission ownedPermission : permissions) {
            if (NumberUtils.equals(permission.getId(), ownedPermission.getId())) {
                return true;
            }
        }

        return false;
    }

    @RequestMapping(value = "/grant", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model grant(@RequestBody RolePrivilegesModel rolePrivileges) {
        try {
            privilegeService.grantRolePrivileges(rolePrivileges.getRole_id(), rolePrivileges.getPermissions());
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }
}

package com.yanyan.web.controller.resource;

import com.yanyan.data.domain.system.Permission;
import com.yanyan.data.domain.system.Portal;
import com.yanyan.data.domain.system.Resource;
import com.yanyan.data.query.system.ResourceQuery;
import com.yanyan.service.system.PortalService;
import com.yanyan.service.system.ResourceService;
import com.yanyan.web.controller.util.PortalUtils;
import com.yanyan.core.web.DataResponse;
import com.yanyan.core.util.TreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Saintcy
 * Date: 2016/8/5
 * Time: 15:20
 */
@Slf4j
@Controller
@RequestMapping("/resource")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;
    @Autowired
    private PortalService portalService;

    @RequestMapping("/list")
    public String list(@ModelAttribute("query") ResourceQuery query, @RequestParam(value = "expand_to", required = false) Long expandTo, Model model) {
        List<Portal> portals = portalService.findPortal(null).getRows();
        model.addAttribute("portals", portals);

        if (query.getParent_id() == null) {
            query.setParent_id(0L);
        }
        if (CollectionUtils.isNotEmpty(portals)) {
            if (PortalUtils.indexOf(portals, query.getPortal_id()) < 0) {//没传入或传入的不对，则默认选中第一个
                query.setPortal_id(portals.get(0).getId());
            }

            model.addAttribute("page", resourceService.findResource(query));
        }

        if (expandTo != null) {
            Resource resource = resourceService.getResource(expandTo);
            if (resource != null) {
                model.addAttribute("path", resource.getPath());
            }
        }

        return "/resource/list";
    }

    @RequestMapping("/find")
    @ResponseBody
    public Model find(@RequestBody ResourceQuery query) {
        try {
            return DataResponse.success("page", resourceService.findResource(query));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/create")
    public String create(@RequestParam("portal_id") Long portal_id, @RequestParam(value = "parent_id", required = false) Long parent_id, Model model) {
        Resource resource = new Resource();
        resource.setParent_id(parent_id);
        Portal portal = portalService.getPortal(portal_id);
        resource.setPortal_id(portal.getId());
        resource.setPortal_name(portal.getName());
        resource.setIs_show(1);

        //默认添加增删改查权限
        List<Permission> permissions = new ArrayList<Permission>();
        Permission view = new Permission();//查
        view.setName("查看");
        view.setCode("view");
        view.setIs_show(1);
        permissions.add(view);
        Permission create = new Permission();//增
        create.setName("增加");
        create.setCode("create");
        create.setIs_show(1);
        permissions.add(create);
        Permission update = new Permission();//改
        update.setName("修改");
        update.setCode("update");
        update.setIs_show(1);
        permissions.add(update);
        resource.setPermissions(permissions);
        Permission delete = new Permission();//删
        delete.setName("删除");
        delete.setCode("delete");
        delete.setIs_show(1);
        permissions.add(delete);

        model.addAttribute("resource", resource);
        model.addAttribute("resources", TreeUtils.sortAsTree(resourceService.getPortalResourceList(resource.getPortal_id()), 0));
        model.addAttribute("portals", portalService.findAllPortalList());

        return "/resource/form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model create(@RequestBody Resource resource) {
        try {
            return DataResponse.success("id", resourceService.createResource(resource));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/update")
    public String update(@RequestParam Long id, Model model) {
        Resource resource = resourceService.getResource(id);
        model.addAttribute("resource", resource);
        model.addAttribute("resources", TreeUtils.sortAsTree(resourceService.getPortalResourceList(resource.getPortal_id()), 0));
        model.addAttribute("portals", portalService.findAllPortalList());

        return "/resource/form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model update(@RequestBody Resource resource) {
        try {
            resourceService.updateResource(resource);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model delete(@PathVariable Long id) {
        try {
            resourceService.deleteResource(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/prioritize/{id}/{priority}")
    public Model prioritize(@PathVariable("id") Long id, @PathVariable("priority") Integer priority) {
        try {
            resourceService.updatePriority(id, priority);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/check_code", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model checkCode(@RequestParam(value = "id", required = false) Long id, @RequestParam("portal_id") Long portal_id, @RequestParam("code") String
            code) {
        if (resourceService.checkResourceCode(id, portal_id, code)) {
            return DataResponse.success();
        } else {
            return DataResponse.failure("编码[" + code + "]已存在");
        }
    }
}

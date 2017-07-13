package com.yanyan.web.controller.department;

import com.yanyan.data.domain.system.Department;
import com.yanyan.data.query.system.DepartmentQuery;
import com.yanyan.service.system.DepartmentService;
import com.yanyan.service.system.StaffService;
import com.yanyan.web.SessionUtils;
import com.yanyan.core.web.DataResponse;
import com.yanyan.core.util.TreeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

/**
 * 部门
 * User: Saintcy
 * Date: 2016/8/5
 * Time: 15:20
 */
@Slf4j
@Controller
@RequestMapping("/department")
public class DepartmentController {
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private StaffService staffService;

    @RequestMapping("/list")
    public String list(@ModelAttribute("query") DepartmentQuery query, @RequestParam(value = "expand_to", required = false) Long expandTo, Model model) {
        if (query.getParent_id() == null) {
            query.setParent_id(0L);
        }
        if (query.getCorp_id() == null) {
            query.setCorp_id(SessionUtils.getCorpId());
        }
        model.addAttribute("page", departmentService.findDepartment(query));

        if (expandTo != null) {
            Department department = departmentService.getDepartment(expandTo);
            if (department != null) {
                model.addAttribute("path", department.getPath());
            }
        }

        return "/department/list";
    }

    @RequestMapping("/find")
    @ResponseBody
    public Model find(@RequestBody DepartmentQuery query) {
        try {
            if (query.getCorp_id() == null) {
                query.setCorp_id(SessionUtils.getCorpId());
            }
            return DataResponse.success("page", departmentService.findDepartment(query));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/create")
    public String create(@RequestParam(value = "parent_id", required = false) Long parent_id, Model model) {
        Department department = new Department();
        department.setParent_id(parent_id);
        department.setCorp_id(SessionUtils.getCorpId());
        model.addAttribute("department", department);
        model.addAttribute("departments", TreeUtils.sortAsTree(departmentService.getCorporationDepartmentList(SessionUtils.getCorpId()), 0));
        model.addAttribute("managers", staffService.getDepartmentChildStaffList(parent_id));//默认推荐上一级的直属人员作为主管

        return "/department/form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model create(@RequestBody Department department) {
        try {
            return DataResponse.success("id", departmentService.createDepartment(department));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/update")
    public String update(@RequestParam Long id, Model model) {
        Department department = departmentService.getDepartment(id);
        model.addAttribute("department", department);
        model.addAttribute("departments", TreeUtils.sortAsTree(departmentService.getCorporationDepartmentList(SessionUtils.getCorpId()), 0));
        //默认推荐上一级与本身的直属人员作为主管
        model.addAttribute("managers", CollectionUtils.union(staffService.getDepartmentChildStaffList(department.getParent_id()), staffService.getDepartmentChildStaffList(department.getId())));

        return "/department/form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model update(@RequestBody Department department) {
        try {
            departmentService.updateDepartment(department);
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
            departmentService.deleteDepartment(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }
}

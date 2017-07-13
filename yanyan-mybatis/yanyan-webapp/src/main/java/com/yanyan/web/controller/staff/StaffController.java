package com.yanyan.web.controller.staff;

import com.yanyan.Configs;
import com.yanyan.core.util.TreeUtils;
import com.yanyan.core.web.DataResponse;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.data.query.system.StaffQuery;
import com.yanyan.service.system.DepartmentService;
import com.yanyan.service.system.RoleService;
import com.yanyan.service.system.StaffService;
import com.yanyan.web.SessionUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 人员
 * User: Saintcy
 * Date: 2016/8/17
 * Time: 9:32
 */
@Slf4j
@Controller
@RequestMapping("/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;
    @Autowired
    private DepartmentService departmentService;
    @Autowired
    private RoleService roleService;

    @RequestMapping("/list")
    public String list(@ModelAttribute("query") StaffQuery query, Model model) {
        query.defaultPageParam();

        if (query.getCorp_id() == null) {
            query.setCorp_id(SessionUtils.getCorpId());
        }
        model.addAttribute("page", staffService.findStaff(query));
        model.addAttribute("DEFAULT_PASSWORD", Configs.DEFAULT_PASSWORD);

        return "/staff/list";
    }

    @RequestMapping("/find")
    @ResponseBody
    public Model find(@RequestBody StaffQuery query) {
        try {
            if (query.getCorp_id() == null) {
                query.setCorp_id(SessionUtils.getCorpId());
            }
            return DataResponse.success("page", staffService.findStaff(query));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/create")
    public String create(Model model) {
        Staff staff = new Staff();
        staff.setCorp_id(SessionUtils.getCorpId());
        model.addAttribute("staff", staff);
        model.addAttribute("departments", TreeUtils.sortAsTree(departmentService.getCorporationDepartmentList(staff.getCorp_id()), 0));
        model.addAttribute("roles", roleService.getPortalRoleList(SessionUtils.getPortalId()));
        model.addAttribute("DEFAULT_PASSWORD", Configs.DEFAULT_PASSWORD);

        return "/staff/form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model create(@RequestBody Staff staff) {
        try {
            staff.setPassword(Configs.DEFAULT_PASSWORD);
            return DataResponse.success("id", staffService.createStaff(staff));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/update")
    public String update(@RequestParam(value = "id") Long id, Model model) {
        Staff staff = staffService.getStaff(id);
        model.addAttribute("staff", staff);
        model.addAttribute("departments", TreeUtils.sortAsTree(departmentService.getCorporationDepartmentList(SessionUtils.getCorpId()), 0));
        model.addAttribute("roles", roleService.getPortalRoleList(SessionUtils.getPortalId()));
        model.addAttribute("DEFAULT_PASSWORD", Configs.DEFAULT_PASSWORD);

        return "/staff/form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model update(@RequestBody Staff staff) {
        try {
            staffService.updateStaff(staff);
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
            staffService.deleteStaff(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/lock/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model lock(@PathVariable Long id) {
        try {
            staffService.lockStaff(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/unlock/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model unlock(@PathVariable Long id) {
        try {
            staffService.unlockStaff(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/reset/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model resetPassword(@PathVariable Long id) {
        try {
            staffService.initStaffPassword(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/check_account", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model checkAccount(@RequestParam(value = "id", required = false) Long id, @RequestParam("account") String account) {
        if (staffService.checkStaffAccount(id, account)) {
            return DataResponse.success();
        } else {
            return DataResponse.failure("用户名[" + account + "]已存在");
        }
    }

    @RequestMapping(value = "/check_cellphone", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model checkCellphone(@RequestParam(value = "id", required = false) Long id, @RequestParam("cellphone") String cellphone) {
        if (staffService.checkStaffAccount(id, cellphone)) {
            return DataResponse.success();
        } else {
            return DataResponse.failure("手机号[" + cellphone + "]已存在");
        }
    }

    @RequestMapping(value = "/check_email", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model checkEmail(@RequestParam(value = "id", required = false) Long id, @RequestParam("email") String email) {
        if (staffService.checkStaffEmail(id, email)) {
            return DataResponse.success();
        } else {
            return DataResponse.failure("邮箱[" + email + "]已存在");
        }
    }
}
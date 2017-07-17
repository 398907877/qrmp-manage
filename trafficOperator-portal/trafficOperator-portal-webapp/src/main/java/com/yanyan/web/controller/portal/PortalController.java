package com.yanyan.web.controller.portal;

import com.yanyan.data.domain.system.Portal;
import com.yanyan.data.query.system.PortalQuery;
import com.yanyan.service.system.PortalService;
import com.yanyan.core.web.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 门户
 * User: Saintcy
 * Date: 2016/6/24
 * Time: 9:57
 */
@Slf4j
@Controller
@RequestMapping("/portal")
public class PortalController {
    @Autowired
    private PortalService portalService;

    @RequestMapping("/list")
    public String list(@ModelAttribute("query") PortalQuery query, Model model) {
        query.defaultPageParam();
        model.addAttribute("page", portalService.findPortal(query));

        return "/portal/list";
    }

    @RequestMapping("/find")
    @ResponseBody
    public Model find(@RequestBody PortalQuery query) {
        try {
            return DataResponse.success("page", portalService.findPortal(query));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/create")
    public String create(Model model) {
        return "/portal/form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model create(@RequestBody Portal portal) {
        try {
            return DataResponse.success("id", portalService.createPortal(portal));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/update")
    public String update(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("portal", portalService.getPortal(id));

        return "/portal/form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model update(@RequestBody Portal portal) {
        try {
            portalService.updatePortal(portal);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/disable/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model disable(@PathVariable Long id) {
        try {
            portalService.disablePortal(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/enable/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model enable(@PathVariable Long id) {
        try {
            portalService.enablePortal(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/check_code", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model checkCode(@RequestParam(value = "id", required = false) Long id, @RequestParam("code") String code) {
        if (portalService.checkPortalCode(id, code)) {
            return DataResponse.success();
        } else {
            return DataResponse.failure("编码[" + code + "]已存在");
        }
    }

    @RequestMapping(value = "/check_app_key", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model checkAppKey(@RequestParam(value = "id", required = false) Long id, @RequestParam("app_key") String appKey) {
        if (portalService.checkPortalAppKey(id, appKey)) {
            return DataResponse.success();
        } else {
            return DataResponse.failure("APP KEY[" + appKey + "]已存在");
        }
    }
}

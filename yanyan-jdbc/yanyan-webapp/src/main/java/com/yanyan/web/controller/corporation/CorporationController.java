package com.yanyan.web.controller.corporation;

import com.yanyan.Configs;
import com.yanyan.data.domain.system.Corporation;
import com.yanyan.data.domain.system.Portal;
import com.yanyan.data.query.system.CorporationQuery;
import com.yanyan.service.system.CorporationService;
import com.yanyan.service.system.PortalService;
import com.yanyan.service.system.RegionService;
import com.yanyan.core.web.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 企业
 * User: Saintcy
 * Date: 2016/6/24
 * Time: 9:57
 */
@Slf4j
@Controller
@RequestMapping("/corporation")
public class CorporationController {
    @Autowired
    private CorporationService corporationService;
    @Autowired
    private PortalService portalService;
    @Autowired
    private RegionService regionService;
    private Map<String, Portal> portalMap = new ConcurrentHashMap<String, Portal>();

    @RequestMapping("/{portal_code}/list")
    public String list(@ModelAttribute("query") CorporationQuery query, @PathVariable("portal_code") String portal_code, Model model) {
        query.defaultPageParam();
        query.setPortal_id(portalMap.get(portal_code).getId());
        model.addAttribute("page", corporationService.findCorporation(query));

        return "/corporation/list";
    }

    @RequestMapping("/{portal_code}/find")
    @ResponseBody
    public Model find(@PathVariable("portal_code") String portal_code, @RequestBody CorporationQuery query) {
        try {
            query.setPortal_id(portalMap.get(portal_code).getId());
            return DataResponse.success("page", corporationService.findCorporation(query));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/{portal_code}/create")
    public String create(@PathVariable("portal_code") String portal_code, Model model) {
        Portal portal = portalMap.get(portal_code);
        Corporation corporation = new Corporation();
        corporation.setPortal_id(portal.getId());
        corporation.setPortal_name(portal.getName());

        model.addAttribute("corporation", corporation);

        model.addAttribute("provinceOptions", regionService.getProvinceList(1L));

        return "/corporation/form";
    }

    @RequestMapping(value = "/{portal_code}/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model create(@PathVariable("portal_code") String portal_code, @RequestBody Corporation corporation) {
        try {
            corporation.setAdmin_password(Configs.DEFAULT_PASSWORD);
            return DataResponse.success("id", corporationService.createCorporation(corporation));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/{portal_code}/update")
    public String update(@PathVariable("portal_code") String portal_code, @RequestParam(value = "id") Long id, Model model) {
        Corporation corporation = corporationService.getCorporation(id);
        model.addAttribute("corporation", corporation);

        model.addAttribute("provinceOptions", regionService.getProvinceList(1L));
        model.addAttribute("cityOptions", regionService.getCityList(corporation.getProvince_id()));
        model.addAttribute("countyOptions", regionService.getCountyList(corporation.getCity_id()));
        model.addAttribute("townshipOptions", regionService.getTownshipList(corporation.getCounty_id()));

        return "/corporation/form";
    }

    @RequestMapping(value = "/{portal_code}/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model update(@PathVariable("portal_code") String portal_code, @RequestBody Corporation corporation) {
        try {
            corporationService.updateCorporation(corporation);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/{portal_code}/delete/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model delete(@PathVariable("portal_code") String portal_code, @PathVariable Long id) {
        try {
            corporationService.deleteCorporation(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @ModelAttribute("portal")
    public Portal getPortal(@PathVariable("portal_code") String portal_code) {
        Portal portal = portalMap.get(portal_code);

        if (portal == null) {
            portal = portalService.getPortalByCode(portal_code);
            if (portal == null) {
                throw new IllegalArgumentException("不存在编码为[" + portal_code + "]的门户");
            }
            portalMap.put(portal.getCode(), portal);
        }

        return portal;
    }
}

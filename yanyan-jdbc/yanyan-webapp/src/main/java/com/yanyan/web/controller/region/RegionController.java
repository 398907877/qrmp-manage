package com.yanyan.web.controller.region;

import com.google.common.collect.Lists;
import com.yanyan.core.util.NumberUtils;
import com.yanyan.service.system.RegionService;
import com.yanyan.data.domain.system.Region;
import com.yanyan.data.query.system.RegionQuery;
import com.yanyan.core.web.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 部门
 * User: Saintcy
 * Date: 2016/8/5
 * Time: 15:20
 */
@Slf4j
@Controller
@RequestMapping("/region")
public class RegionController {
    @Autowired
    private RegionService regionService;

    @RequestMapping("/list")
    public String list(@ModelAttribute("query") RegionQuery query, Model model) {
        if (query.getParent_id() == null) {
            query.setParent_id(0L);
        } else {
            List<Region> parents = Lists.newArrayList();
            long parent_id = query.getParent_id();
            while (parent_id != 0) {
                Region region = regionService.getRegion(parent_id);
                parents.add(0, region);
                parent_id = region.getParent_id();
            }

            model.addAttribute("parents", parents);
        }

        model.addAttribute("page", regionService.findRegion(query));
        return "/region/list";
    }

    @RequestMapping("/find")
    @ResponseBody
    public Model find(@RequestBody RegionQuery query) {
        try {
            return DataResponse.success("page", regionService.findRegion(query));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/create")
    public String create(@RequestParam(value = "parent_id", required = false) Long parent_id, Model model) {
        Region region = new Region();
        region.setParent_id(parent_id);
        model.addAttribute("region", region);

        List<Region> parents = Lists.newArrayList();
        long pid = region.getParent_id();
        while (pid != 0) {
            Region region1 = regionService.getRegion(pid);
            parents.add(0, region1);
            if (NumberUtils.equals(region1.getId(), region.getParent_id())) {
                region.setParent_name(region1.getName());
            }

            pid = region1.getParent_id();
        }

        model.addAttribute("parents", parents);

        return "/region/form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model create(@RequestBody Region region) {
        try {
            return DataResponse.success("id", regionService.createRegion(region));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/update")
    public String update(@RequestParam Long id, Model model) {
        Region region = regionService.getRegion(id);
        model.addAttribute("region", region);

        List<Region> parents = Lists.newArrayList();
        long pid = region.getParent_id();
        while (pid != 0) {
            Region region1 = regionService.getRegion(pid);
            parents.add(0, region1);
            pid = region1.getParent_id();
        }

        model.addAttribute("parents", parents);

        return "/region/form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model update(@RequestBody Region region) {
        try {
            regionService.updateRegion(region);
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
            regionService.deleteRegion(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/check_code", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model checkCode(@RequestParam(value = "id", required = false) Long id, @RequestParam("code") String code) {
        if (regionService.checkRegionCode(id, code)) {
            return DataResponse.success();
        } else {
            return DataResponse.failure("编码[" + code + "]已存在");
        }
    }
}

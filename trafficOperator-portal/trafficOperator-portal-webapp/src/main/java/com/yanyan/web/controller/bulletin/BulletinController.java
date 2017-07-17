package com.yanyan.web.controller.bulletin;

import com.yanyan.core.util.NumberUtils;
import com.yanyan.data.domain.system.Bulletin;
import com.yanyan.data.query.system.BulletinQuery;
import com.yanyan.service.system.BulletinService;
import com.yanyan.web.SessionUtils;
import com.yanyan.core.web.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

/**
 * 人员
 * User: Saintcy
 * Date: 2016/8/17
 * Time: 9:32
 */
@Slf4j
@Controller
@RequestMapping("/bulletin")
public class BulletinController {
    @Autowired
    private BulletinService bulletinService;

    @RequestMapping("/list")
    public String list(@ModelAttribute("query") BulletinQuery query, Model model) {
        query.defaultPageParam();

        if (query.getCorp_id() == null) {
            query.setCorp_id(SessionUtils.getCorpId());
        }
        model.addAttribute("page", bulletinService.findBulletin(query));

        return "/bulletin/list";
    }

    @RequestMapping("/find")
    @ResponseBody
    public Model find(@RequestBody BulletinQuery query) {
        try {
            if (query.getCorp_id() == null) {
                query.setCorp_id(SessionUtils.getCorpId());
            }
            return DataResponse.success("page", bulletinService.findBulletin(query));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/info")
    public String info(@RequestParam(value = "id") Long id, Model model) {
        Bulletin bulletin = bulletinService.getBulletin(id);
        model.addAttribute("bulletin", bulletin);

        return "/bulletin/info";
    }

    @RequestMapping("/get/{id}")
    @ResponseBody
    public Model get(@PathVariable("id") Long id) {
        try {
            return DataResponse.success("bulletin", bulletinService.getBulletin(id));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/create")
    public String create(Model model) {
        Bulletin bulletin = new Bulletin();
        bulletin.setCorp_id(SessionUtils.getCorpId());
        model.addAttribute("bulletin", bulletin);

        return "/bulletin/form";
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public Model create(@RequestBody Bulletin bulletin) {
        try {
            bulletin.setCorp_id(NumberUtils.defaultIfEmpty(bulletin.getCorp_id(), SessionUtils.getCorpId()));
            if (bulletin.getEffective_time() == null) {
                bulletin.setEffective_time(new Date());
            }
            return DataResponse.success("id", bulletinService.createBulletin(bulletin));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/update")
    public String update(@RequestParam(value = "id") Long id, Model model) {
        Bulletin bulletin = bulletinService.getBulletin(id);
        model.addAttribute("bulletin", bulletin);

        return "/bulletin/form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public Model update(@RequestBody Bulletin bulletin) {
        try {
            bulletinService.updateBulletin(bulletin);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST)
    @ResponseBody
    public Model delete(@PathVariable Long id) {
        try {
            bulletinService.deleteBulletin(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }
}
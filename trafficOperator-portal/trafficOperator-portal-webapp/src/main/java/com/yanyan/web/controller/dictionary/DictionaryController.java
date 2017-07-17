package com.yanyan.web.controller.dictionary;

import com.yanyan.data.domain.system.DictionaryEntry;
import com.yanyan.data.domain.system.DictionaryGroup;
import com.yanyan.data.query.system.DictionaryEntryQuery;
import com.yanyan.data.query.system.DictionaryGroupQuery;
import com.yanyan.service.system.DictionaryService;
import com.yanyan.core.web.DataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

/**
 * 数据字典
 * User: Saintcy
 * Date: 2016/8/12
 * Time: 10:10
 */
@Slf4j
@Controller
@RequestMapping("/dictionary")
public class DictionaryController {
    @Autowired
    private DictionaryService dictionaryService;

    @RequestMapping("/entry/list")
    public String entryList(@ModelAttribute("query") DictionaryEntryQuery query, Model model) {
        query.defaultPageParam();
        model.addAttribute("page", dictionaryService.findEntry(query));

        return "/dictionary/entry/list";
    }

    @RequestMapping("/entry/find")
    @ResponseBody
    public Model entryFind(@RequestBody DictionaryEntryQuery query) {
        try {
            return DataResponse.success("page", dictionaryService.findEntry(query));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/entry/create")
    public String entryCreate(Model model) {
        model.addAttribute("groups", dictionaryService.findGroup(null).getRows());
        return "/dictionary/entry/form";
    }

    @RequestMapping(value = "/entry/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model entryCreate(@RequestBody DictionaryEntry entry) {
        try {
            return DataResponse.success("id", dictionaryService.createEntry(entry));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/entry/update")
    public String entryUpdate(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("groups", dictionaryService.findGroup(null).getRows());
        model.addAttribute("entry", dictionaryService.getEntry(id));

        return "/dictionary/entry/form";
    }

    @RequestMapping(value = "/entry/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model entryUpdate(@RequestBody DictionaryEntry entry) {
        try {
            dictionaryService.updateEntry(entry);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/entry/delete/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model entryDelete(@PathVariable Long id) {
        try {
            dictionaryService.deleteEntry(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/entry/check_code", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model entryCheckCode(@RequestParam(value = "id", required = false) Long id, @RequestParam("code") String code) {
        if (dictionaryService.checkEntryCode(id, code)) {
            return DataResponse.success();
        } else {
            return DataResponse.failure("编码[" + code + "]已存在");
        }
    }

    @RequestMapping("/group/list")
    public String groupList(@ModelAttribute("query") DictionaryGroupQuery query, Model model) {
        query.defaultPageParam();
        model.addAttribute("page", dictionaryService.findGroup(query));

        return "/dictionary/group/list";
    }

    @RequestMapping("/group/find")
    @ResponseBody
    public Model groupFind(@RequestBody DictionaryGroupQuery query) {
        try {
            return DataResponse.success("page", dictionaryService.findGroup(query));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/group/create")
    public String groupCreate(Model model) {
        return "/dictionary/group/form";
    }

    @RequestMapping(value = "/group/create", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model groupCreate(@RequestBody DictionaryGroup group) {
        try {
            return DataResponse.success("id", dictionaryService.createGroup(group));
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping("/group/update")
    public String groupUpdate(@RequestParam(value = "id") Long id, Model model) {
        model.addAttribute("group", dictionaryService.getGroup(id));

        return "/dictionary/group/form";
    }

    @RequestMapping(value = "/group/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model groupUpdate(@RequestBody DictionaryGroup group) {
        try {
            dictionaryService.updateGroup(group);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/group/delete/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model groupDelete(@PathVariable Long id) {
        try {
            dictionaryService.deleteGroup(id);
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }

    @RequestMapping(value = "/group/check_code", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model groupCheckCode(@RequestParam(value = "id", required = false) Long id, @RequestParam("code") String code) {
        if (dictionaryService.checkGroupCode(id, code)) {
            return DataResponse.success();
        } else {
            return DataResponse.failure("编码[" + code + "]已存在");
        }
    }
}

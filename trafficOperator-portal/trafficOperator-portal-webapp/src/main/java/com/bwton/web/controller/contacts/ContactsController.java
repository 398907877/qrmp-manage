package com.bwton.web.controller.contacts;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.bwton.service.contacts.ContactsVoService;
import com.bwton.vo.operatorManagement.OperatorInformation;
import com.bwton.vo.operatorManagement.OperatorInformationExample;

import com.yanyan.core.web.DataResponse;

import com.yanyan.service.system.RegionService;


import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author wujiajun
 * @date 2017
 * @desc 运营商联系人的 控制器
 *
 */
@Slf4j
@Data
@Controller
@RequestMapping("contacts")
public class ContactsController {

	@Autowired
	private ContactsVoService contactsVoService;

	@Autowired
	private RegionService regionService;

	@RequestMapping("/list")
	public String list(@ModelAttribute(value = "query", name = "query") OperatorInformationExample query, Model model) {

		return "/operatorManagement/list";
	}

	@RequestMapping("/form")
	public String form(Model model) {
		return "/operatorManagement/form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public Model form(@RequestBody OperatorInformation OperatorInformation) {

		try {

			return DataResponse.success("id", 1L);
		} catch (Exception e) {
			log.error("", e);
			return DataResponse.failure(e);
		}
	}
	
	
	
	


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model delete(@PathVariable Long id) {
        try {
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }
    
    
    
    
    
    
    
    
    

    @RequestMapping("/update")
    public String update(@RequestParam(value = "id") Long id, Model model) {

        return "/operatorManagement/form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model update(@RequestBody OperatorInformation operatorInformation) {
        try {
        	
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
	
	
	
	
	

}

package com.bwton.web.controller.operatorManagement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bwton.service.operatorManagement.OperatorInformationService;
import com.bwton.vo.operatorManagement.OperatorInformation;
import com.bwton.vo.operatorManagement.OperatorInformationExample;
import com.yanyan.core.lang.Page;
import com.yanyan.data.query.system.BulletinQuery;
import com.yanyan.web.SessionUtils;
import com.yanyan.web.controller.bulletin.BulletinController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/operatorManagement")
public class OperatorManagementController {
	
	
	@Autowired
	private OperatorInformationService   operInfService;
	

    @RequestMapping("/list")
    public String list( Model model) {
     //   query.defaultPageParam();
    	
    	
    	OperatorInformationExample  ex= new OperatorInformationExample();
    	
    	
    	Page<OperatorInformation> ops=  operInfService.selectByExample(ex);
    	
    	model.addAttribute("page", ops);

    	System.out.println(ops);
    	
    	
    	
    	
    

        return "/operatorManagement/list";
    }

    
    @RequestMapping("/form")
    public String form( Model model) {
     //   query.defaultPageParam();
    	
    	

    	
    	
    	
    	
    

        return "/operatorManagement/form";
    }
	

}

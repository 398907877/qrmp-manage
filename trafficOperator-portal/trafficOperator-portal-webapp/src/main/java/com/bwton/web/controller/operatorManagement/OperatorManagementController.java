package com.bwton.web.controller.operatorManagement;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.yanyan.data.query.system.BulletinQuery;
import com.yanyan.web.SessionUtils;
import com.yanyan.web.controller.bulletin.BulletinController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/operatorManagement")
public class OperatorManagementController {
	
	
	
	

    @RequestMapping("/list")
    public String list( Model model) {
     //   query.defaultPageParam();

        return "/operatorManagement/list";
    }

	

}

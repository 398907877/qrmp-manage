package com.bwton.qrmp.manage.web.controller.operatorManagement;

import java.util.Date;
import java.util.List;

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

import com.bwton.qrmp.manage.service.contacts.ContactsVoService;
import com.bwton.qrmp.manage.service.operatorManagement.OperatorInformationService;
import com.bwton.qrmp.manage.vo.contacts.ContactsVo;
import com.bwton.qrmp.manage.vo.contacts.ContactsVoExample;
import com.bwton.qrmp.manage.vo.operatorManagement.OperatorInformation;
import com.bwton.qrmp.manage.vo.operatorManagement.OperatorInformationExample;
import com.bwton.qrmp.manage.vo.operatorManagement.OperatorInformationExample.Criteria;
import com.yanyan.Configs;
import com.yanyan.core.lang.Page;
import com.yanyan.core.util.TreeUtils;
import com.yanyan.core.web.DataResponse;
import com.yanyan.data.domain.system.Corporation;
import com.yanyan.data.domain.system.Region;
import com.yanyan.data.domain.system.Staff;
import com.yanyan.data.query.system.RegionQuery;
import com.yanyan.service.system.RegionService;
import com.yanyan.web.SessionUtils;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * 
 * @author wujiajun
 * @date 2017
 * @desc to yxs
 *
 */
@Slf4j
@Data
@Controller
@RequestMapping("/operatorManagement")
public class OperatorManagementController {

	@Autowired
	private OperatorInformationService operInfService;

	@Autowired
	private RegionService regionService;
	
	
	
    @Autowired
    private ContactsVoService contactsVoService;

	@RequestMapping("/list")
	public String list(@ModelAttribute(value = "query", name = "query") OperatorInformationExample query, Model model) {
		query.defaultPageParam();

		System.out.println(query);

		Criteria cq = query.createCriteria();

		if (query.getKeyword() != null) {

			cq.andOperatorNameLike("%" + query.getKeyword() + "%");
		}

		Page<OperatorInformation> ops = operInfService.selectByExample(query);

		RegionQuery requery = new RegionQuery();

		for (OperatorInformation op : ops.getRows()) {
			requery.setCode("");

			String[] argg = op.getLocation().split("\\|");

			if (argg.length < 2) {

				continue;
			}
			op.setProvince_id(argg[0]);

			op.setCity_id(argg[1]);

			String provinceId = op.getProvince_id();
			requery.setCode(provinceId);
			Page<Region> pg1 = regionService.findRegion(requery);

			String provinceName = null;
			if (pg1.getRows().size() >= 1) {

				provinceName = pg1.getRows().get(0).getName();
			}

			String cityId = op.getCity_id();

			requery.setCode(cityId);
			Page<Region> pg2 = regionService.findRegion(requery);
			String cityName = null;
			if (pg1.getRows().size() >= 1) {
				cityName = pg2.getRows().get(0).getName();
			}

			op.setLocation(provinceName + "|" + cityName);

		}

		model.addAttribute("page", ops);

		return "/operatorManagement/list";
	}

	@RequestMapping("/form")
	public String form(Model model) {

		Corporation corporation = new Corporation();

		model.addAttribute("corporation", corporation);

		model.addAttribute("provinceOptions", regionService.getProvinceList(1L));

		return "/operatorManagement/form";
	}

	@RequestMapping(value = "/form", method = RequestMethod.POST, consumes = { MediaType.APPLICATION_JSON_VALUE,
			MediaType.APPLICATION_XML_VALUE })
	@ResponseBody
	public Model form(@RequestBody OperatorInformation OperatorInformation) {

		try {

			String province_id = OperatorInformation.getProvince_id();
			String city_id = OperatorInformation.getCity_id();

			OperatorInformation.setLocation(province_id + "|" + city_id);
			OperatorInformation.setLastModifyTime(new Date());
			OperatorInformation.setLastModifyUser(SessionUtils.getStaffName());

			return DataResponse.success("id", operInfService.insert(OperatorInformation));
		} catch (Exception e) {
			log.error("", e);
			return DataResponse.failure(e);
		}
	}
	
	
	
	


    @RequestMapping(value = "/delete/{id}", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model delete(@PathVariable Long id) {
        try {
            operInfService.deleteByPrimaryKey(id.intValue());
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }
    
    
    
    
    
    
    
    
    

    @RequestMapping("/update")
    public String update(@RequestParam(value = "id") Long id, Model model) {
       // Staff staff = staffService.getStaff(id);
      //  model.addAttribute("staff", staff);
    //    model.addAttribute("departments", TreeUtils.sortAsTree(departmentService.getCorporationDepartmentList(SessionUtils.getCorpId()), 0));
     //   model.addAttribute("roles", roleService.getPortalRoleList(SessionUtils.getPortalId()));
    	
    	
    	
		Corporation corporation = new Corporation();

		model.addAttribute("corporation", corporation);

		//1.根据表的 location  的分隔符  获取到
		// 然后分别set进去
		
		
		
        model.addAttribute("provinceOptions", regionService.getProvinceList(1L));
        
        
      //  model.addAttribute("cityOptions", regionService.getCityList(corporation.getProvince_id()));
    	
    	
    	OperatorInformation   operatorInformation=  operInfService.selectByPrimaryKey(id.intValue());
    	
    	
    	String  [] los=  operatorInformation.getLocation().split("\\|");
    	
    	if(los.length>=2) {
            model.addAttribute("cityOptions", regionService.getCityList((Long.valueOf(los[0]))));
            
        	
        	operatorInformation.setProvince_id(los[0]);
        	operatorInformation.setCity_id(los[1]);
        	
            
    	}
    	

    	
    	

        model.addAttribute("operatorInformation",operatorInformation);
        
        
        
        ContactsVoExample conQuery= new ContactsVoExample();
        
        com.bwton.qrmp.manage.vo.contacts.ContactsVoExample.Criteria conCq=   conQuery.createCriteria();
        
        conCq.andOpinIdEqualTo(operatorInformation.getId());
        
        List<ContactsVo> conVos= contactsVoService.selectByExample(conQuery).getRows();
        
        
        model.addAttribute("linkmans", conVos);

    	
       // model.addAttribute("DEFAULT_PASSWORD", Configs.DEFAULT_PASSWORD);

        return "/operatorManagement/form";
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ResponseBody
    public Model update(@RequestBody OperatorInformation operatorInformation) {
        try {
        //    staffService.updateStaff(staff);
        	
        	
        	log.debug("wujiajun");
        	String province_id = operatorInformation.getProvince_id();
			String city_id = operatorInformation.getCity_id();

			operatorInformation.setLocation(province_id + "|" + city_id);
			operatorInformation.setLastModifyTime(new Date());
			operatorInformation.setLastModifyUser(SessionUtils.getStaffName());
        	
        	operInfService.updateByPrimaryKey(operatorInformation);
        	
        	
            return DataResponse.success();
        } catch (Exception e) {
            log.error("", e);
            return DataResponse.failure(e);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
	
	
	
	
	

}

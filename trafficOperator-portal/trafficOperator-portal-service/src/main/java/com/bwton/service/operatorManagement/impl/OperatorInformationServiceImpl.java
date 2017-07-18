package com.bwton.service.operatorManagement.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bwton.persist.operatorManagement.OperatorInformationDao;
import com.bwton.service.operatorManagement.OperatorInformationService;
import com.bwton.vo.operatorManagement.OperatorInformation;
import com.bwton.vo.operatorManagement.OperatorInformationExample;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Department;
import com.yanyan.service.BaseService;


@Service
public class OperatorInformationServiceImpl extends BaseService implements OperatorInformationService {

	
    @Autowired
    private OperatorInformationDao operatorInformationDao;
	
	
	@Override
	public int deleteByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return operatorInformationDao.deleteByPrimaryKey(id);
	}

	@Override
	public int insert(OperatorInformation record) {
		// TODO Auto-generated method stub
		return operatorInformationDao.insert(record);
	}

	@Override
	public int insertSelective(OperatorInformation record) {
		// TODO Auto-generated method stub
		return  operatorInformationDao.insertSelective(record) ;
	}

	@Override
	public Page<OperatorInformation> selectByExample(OperatorInformationExample example) {
		// TODO Auto-generated method stub
		
        Page<OperatorInformation> page = operatorInformationDao.selectByExample(example);
//        for (OperatorInformation operatorInformation : page.getRows()) {
//            department.setManagers(departmentDao.getManagers(department.getId()));
//        }

        
        
        
		return  page;
				
				
				
	}

	@Override
	public OperatorInformation selectByPrimaryKey(Integer id) {
		// TODO Auto-generated method stub
		return operatorInformationDao.selectByPrimaryKey(id);
	}

	@Override
	public int updateByPrimaryKeySelective(OperatorInformation record) {
		// TODO Auto-generated method stub
		return operatorInformationDao.updateByPrimaryKeySelective(record);
	}

	@Override
	public int updateByPrimaryKey(OperatorInformation record) {
		// TODO Auto-generated method stub
		return operatorInformationDao.updateByPrimaryKey(record);
	}

	@Override
	public long countByExample(OperatorInformationExample example) {
		// TODO Auto-generated method stub
		return operatorInformationDao.countByExample(example);
	}

	@Override
	public int deleteByExample(OperatorInformationExample example) {
		// TODO Auto-generated method stub
		return   operatorInformationDao.deleteByExample(example);
	}

	@Override
	public int updateByExampleSelective(OperatorInformation record, OperatorInformationExample example) {
		// TODO Auto-generated method stub
		return operatorInformationDao.updateByExampleSelective(record, example);
	}

	@Override
	public int updateByExample(OperatorInformation record, OperatorInformationExample example) {
		// TODO Auto-generated method stub
		return  operatorInformationDao.updateByExample(record, example);
	}

	
	
	
	
}
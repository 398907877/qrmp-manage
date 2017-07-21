package com.bwton.service.contacts.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bwton.persist.contacts.ContactsVoDao;
import com.bwton.service.contacts.ContactsVoService;
import com.bwton.vo.contacts.ContactsVo;
import com.bwton.vo.contacts.ContactsVoExample;
import com.yanyan.core.lang.Page;
import com.yanyan.service.BaseService;

/**
 * 
 * @author wujiajun
 * @desc  运营商的联系人信息
 *
 */
@Service
public class ContactsVoServiceImpl  extends BaseService implements ContactsVoService{
	
	
	
    @Autowired
    private ContactsVoDao contactsVoDao;
	
	
	
	@Override
	public long countByExample(ContactsVoExample example) {
		// TODO Auto-generated method stub
		return contactsVoDao.countByExample(example);
	}

	@Override
	public int deleteByExample(ContactsVoExample example) {
		// TODO Auto-generated method stub
		return contactsVoDao.deleteByExample(example);
	}

	@Override
	public int insert(ContactsVo record) {
		// TODO Auto-generated method stub
		return contactsVoDao.insert(record);
	}

	@Override
	public int insertSelective(ContactsVo record) {
		// TODO Auto-generated method stub
		return contactsVoDao.insertSelective(record);
	}

	@Override
	public Page<ContactsVo> selectByExample(ContactsVoExample example) {
		// TODO Auto-generated method stub
		return contactsVoDao.selectByExample(example);
	}

	@Override
	public int updateByExampleSelective(ContactsVo record, ContactsVoExample example) {
		// TODO Auto-generated method stub
		return   contactsVoDao.updateByExampleSelective(record, example);
	}

	@Override
	public int updateByExample(ContactsVo record, ContactsVoExample example) {
		// TODO Auto-generated method stub
		return contactsVoDao.updateByExample(record, example);
	} 

}

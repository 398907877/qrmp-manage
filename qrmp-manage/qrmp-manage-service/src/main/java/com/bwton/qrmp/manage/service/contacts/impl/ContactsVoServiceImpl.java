package com.bwton.qrmp.manage.service.contacts.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bwton.qrmp.manage.persist.contacts.ContactsVoDao;
import com.bwton.qrmp.manage.service.contacts.ContactsVoService;
import com.bwton.qrmp.manage.vo.contacts.ContactsVo;
import com.bwton.qrmp.manage.vo.contacts.ContactsVoExample;
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

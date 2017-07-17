package com.yanyan.service.system.impl;

import com.yanyan.persist.system.BulletinDao;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Bulletin;
import com.yanyan.data.query.system.BulletinQuery;
import com.yanyan.service.BaseService;
import com.yanyan.service.system.BulletinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * 公告服务
 * User: Saintcy
 * Date: 2015/3/6
 * Time: 14:44
 */
@Service
public class BulletinServiceImpl extends BaseService implements BulletinService {
    @Autowired
    private BulletinDao bulletinDao;

    public long createBulletin(Bulletin bulletin) {
        bulletin.setPublish_time(new Date());
        bulletinDao.insertBulletin(bulletin);

        return bulletin.getId();
    }

    public void updateBulletin(Bulletin bulletin) {
        bulletin.setPublish_time(new Date());
        bulletinDao.updateBulletin(bulletin);
    }

    public void deleteBulletin(Long id) {
        bulletinDao.deleteBulletin(id);
    }

    public Bulletin getBulletin(Long id) {
        return bulletinDao.getBulletin(id);
    }

    public Page<Bulletin> findBulletin(BulletinQuery query) {
        return bulletinDao.findBulletin(query);
    }
}

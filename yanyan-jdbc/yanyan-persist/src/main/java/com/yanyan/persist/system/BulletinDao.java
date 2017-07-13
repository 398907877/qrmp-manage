package com.yanyan.persist.system;


import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Bulletin;
import com.yanyan.data.query.system.BulletinQuery;

/**
 * 公告数据存取类
 * User: Saintcy
 * Date: 2015/3/6
 * Time: 14:55
 */
public interface BulletinDao {

    void insertBulletin(Bulletin bulletin);

    void updateBulletin(Bulletin bulletin);

    void deleteBulletin(Long bulletin_id);

    Bulletin getBulletin(Long bulletin_id);

    Page<Bulletin> findBulletin(BulletinQuery query);
}

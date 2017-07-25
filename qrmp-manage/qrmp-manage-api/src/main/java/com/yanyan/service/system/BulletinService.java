package com.yanyan.service.system;


import com.yanyan.data.query.system.BulletinQuery;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Bulletin;

/**
 * 公告管理
 * User: Saintcy
 * Date: 2015/3/6
 * Time: 14:07
 */
public interface BulletinService {
    /**
     * 创建新公告
     *
     * @param bulletin
     * @return
     */
    long createBulletin(Bulletin bulletin);

    /**
     * 更新公告
     *
     * @param bulletin
     */
    void updateBulletin(Bulletin bulletin);

    /**
     * 删除公告
     *
     * @param bulletin_id
     */
    void deleteBulletin(Long bulletin_id);

    /**
     * 获得公告
     *
     * @param bulletin_id
     * @return
     */
    Bulletin getBulletin(Long bulletin_id);

    /**
     * 查询公告
     *
     * @param query
     * @return
     */
    Page<Bulletin> findBulletin(BulletinQuery query);
}

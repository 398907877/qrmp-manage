package com.yanyan.service.system;

import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Portal;
import com.yanyan.data.query.system.PortalQuery;

import java.util.List;

/**
 * 门户服务
 * User: Saintcy
 * Date: 2016/5/3
 * Time: 15:25
 */
public interface PortalService {
    /**
     * 创建门户
     *
     * @param portal
     * @return
     */
    long createPortal(Portal portal);

    /**
     * 更新门户
     *
     * @param portal
     */
    void updatePortal(Portal portal);

    /**
     * 注销门户
     *
     * @param portal_id
     */
    void disablePortal(Long portal_id);

    /**
     * 启用门户
     *
     * @param portal_id
     */
    void enablePortal(Long portal_id);

    /**
     * 获得门户
     *
     * @param portal_id
     * @return
     */
    Portal getPortal(Long portal_id);

    /**
     * 查询门户
     *
     * @param query
     * @return
     */
    Page<Portal> findPortal(PortalQuery query);

    /**
     * 由编码获取门户
     *
     * @param code
     * @return
     */
    Portal getPortalByCode(String code);

    /**
     * 获取所有的门户
     *
     * @return
     */
    List<Portal> findAllPortalList();

    /**
     * 检查编码是否唯一
     *
     * @param id
     * @param code
     * @return
     */
    boolean checkPortalCode(Long id, String code);

    /**
     * 检查APP KEY是否唯一
     *
     * @param id
     * @param appKey
     * @return
     */
    boolean checkPortalAppKey(Long id, String appKey);
}

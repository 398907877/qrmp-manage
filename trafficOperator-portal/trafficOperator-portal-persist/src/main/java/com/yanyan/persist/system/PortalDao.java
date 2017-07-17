package com.yanyan.persist.system;

import com.yanyan.data.domain.system.Portal;
import com.yanyan.core.lang.Page;
import com.yanyan.data.query.system.PortalQuery;

/**
 * 门户DAO
 * User: Saintcy
 * Date: 2016/5/18
 * Time: 16:24
 */
public interface PortalDao {
    void insertPortal(Portal portal);
    void updatePortal(Portal portal);
    void disablePortal(Long portal_id);
    void enablePortal(Long portal_id);
    Portal getPortal(Long portal_id);
    Page<Portal> findPortal(PortalQuery query);
}

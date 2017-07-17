package com.yanyan.web.controller.util;

import com.yanyan.data.domain.system.Portal;

import java.util.List;

/**
 * 门户工具类
 * User: Saintcy
 * Date: 2016/12/23
 * Time: 17:06
 */
public class PortalUtils {
    /**
     * 根据id从列表中定位门户
     *
     * @param portals
     * @param id
     * @return
     */
    public static int indexOf(List<Portal> portals, Long id) {
        if (id == null) {
            return -1;
        }
        int i = 0;
        for (Portal portal : portals) {
            if (id.longValue() == portal.getId().longValue()) {
                return i;
            }
        }

        return -1;
    }
}

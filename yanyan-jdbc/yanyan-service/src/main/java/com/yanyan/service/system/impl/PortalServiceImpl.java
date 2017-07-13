package com.yanyan.service.system.impl;

import com.yanyan.core.BusinessException;
import com.yanyan.core.filter.ApiClient;
import com.yanyan.core.filter.ApiClientService;
import com.yanyan.core.lang.Page;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.core.spring.validator.group.Update;
import com.yanyan.core.util.NumberUtils;
import com.yanyan.data.domain.system.Portal;
import com.yanyan.data.query.system.PortalQuery;
import com.yanyan.persist.system.PortalDao;
import com.yanyan.service.BaseService;
import com.yanyan.service.system.PortalService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 门户服务类
 * User: Saintcy
 * Date: 2016/5/20
 * Time: 16:26
 */
@Service("portalService")
public class PortalServiceImpl extends BaseService implements PortalService, ApiClientService {
    @Autowired
    private PortalDao portalDao;

    public long createPortal(Portal portal) {
        validate(portal, Create.class);
        portalDao.insertPortal(portal);
        return portal.getId();
    }

    public void updatePortal(Portal portal) {
        validate(portal, Update.class);
        portalDao.updatePortal(portal);
    }

    protected void validate(Portal portal, Class<?>... groups) {
        super.validate(portal, groups);

        if (!checkPortalCode(portal.getId(), portal.getCode())) {
            throw new BusinessException("portal.code.existed", new Object[]{portal.getCode()}, "编码[" + portal.getCode() + "]已经存在！");
        }

        if (!checkPortalAppKey(portal.getId(), portal.getApp_key())) {
            throw new BusinessException("portal.app_key.existed", new Object[]{portal.getApp_key()}, "APP KEY[" + portal.getApp_key() + "]已经存在！");
        }
    }

    public void disablePortal(Long portal_id) {
        portalDao.disablePortal(portal_id);
    }

    public void enablePortal(Long portal_id) {
        portalDao.enablePortal(portal_id);
    }

    public Portal getPortal(Long portal_id) {
        return portalDao.getPortal(portal_id);
    }

    public Page<Portal> findPortal(PortalQuery query) {
        return portalDao.findPortal(query);
    }

    public Portal getPortalByCode(String code) {
        PortalQuery query = new PortalQuery();
        query.setCode(code);
        query.one();
        Page<Portal> portalPage = findPortal(query);
        if (portalPage.getTotalCount() > 0) {
            return portalPage.getFirstRow();
        } else {
            return null;
        }
    }

    public Portal getPortalByAppId(String appId) {
        PortalQuery query = new PortalQuery();
        query.setApp_key(appId);
        query.one();
        Page<Portal> portalPage = findPortal(query);
        if (portalPage.getTotalCount() > 0) {
            return portalPage.getFirstRow();
        } else {
            return null;
        }
    }

    public List<Portal> findAllPortalList() {
        return findPortal(null).getRows();
    }

    public boolean checkPortalCode(Long id, String code) {
        if (StringUtils.isEmpty(code)) return true;
        PortalQuery portalQuery = new PortalQuery();
        portalQuery.setCode(code);
        portalQuery.one();

        Page<Portal> portalPage = portalDao.findPortal(portalQuery);
        return portalPage.getTotalCount() <= 0 || NumberUtils.equals(id, portalPage.getFirstRow().getId());
    }

    public boolean checkPortalAppKey(Long id, String appKey) {
        if (StringUtils.isEmpty(appKey)) return true;
        PortalQuery portalQuery = new PortalQuery();
        portalQuery.setApp_key(appKey);
        portalQuery.one();

        Page<Portal> portalPage = portalDao.findPortal(portalQuery);
        return portalPage.getTotalCount() <= 0 && NumberUtils.equals(id, portalPage.getFirstRow().getId());
    }

    public ApiClient getClientByAppId(String appId) {
        Portal portal = getPortalByAppId(appId);
        if (portal != null) {
            ApiClient client = new ApiClient();
            client.setId(portal.getId());
            client.setName(portal.getName());
            client.setAppId(portal.getApp_key());
            client.setAppSecret(portal.getApp_secret());
            client.setPrivateSecret(portal.getPrivate_secret());
            return client;
        }
        return null;
    }

    public boolean hasPermission(String appId, String permission) {
        return true;
    }

    public String getClientIdKey() {
        return "portalId";
    }
}

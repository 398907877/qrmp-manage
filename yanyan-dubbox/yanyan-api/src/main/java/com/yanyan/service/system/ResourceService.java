package com.yanyan.service.system;


import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Resource;
import com.yanyan.data.query.system.ResourceQuery;
import com.yanyan.service.TreeService;

import java.util.List;

/**
 * 资源服务
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 17:23
 */
public interface ResourceService extends TreeService {
    /**
     * 建一个资源
     *
     * @param resource
     */
    long createResource(Resource resource);

    /**
     * 更新资源
     *
     * @param resource
     */
    void updateResource(Resource resource);

    /**
     * 删除资源
     *
     * @param resource_id
     */
    void deleteResource(Long resource_id);

    /**
     * 获取资源
     *
     * @param resource_id
     */
    Resource getResource(Long resource_id);

    /**
     * 查询资源
     *
     * @param query
     */
    Page<Resource> findResource(ResourceQuery query);

    /**
     * 获取门户资源列表
     * @param portal_id
     * @return
     */
    List<Resource> getPortalResourceList(Long portal_id);

    /**
     * 检查编码是否唯一
     *
     * @param id
     * @param portal_id
     * @param code
     * @return
     */
    boolean checkResourceCode(Long id, Long portal_id, String code);
}

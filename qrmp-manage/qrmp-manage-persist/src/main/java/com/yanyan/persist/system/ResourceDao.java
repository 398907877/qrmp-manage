package com.yanyan.persist.system;

import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Resource;
import com.yanyan.data.query.system.ResourceQuery;
import com.yanyan.persist.TreeDao;

/**
 * 资源数据存取对象
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 23:10
 */
public interface ResourceDao extends TreeDao {
    String _TABLE_NAME = "s_resource";

    void insertResource(Resource resource);

    void updateResource(Resource resource);

    void deleteResource(Long resource_id);

    Resource getResource(Long resource_id);

    Page<Resource> findResource(ResourceQuery query);
}

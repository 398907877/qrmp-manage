package com.yanyan.persist.system;

import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Region;
import com.yanyan.persist.TreeDao;
import com.yanyan.data.query.system.RegionQuery;

/**
 * 地区
 * User: Saintcy
 * Date: 2015/8/24
 * Time: 17:33
 */
public interface RegionDao extends TreeDao {
    String TABLE_NAME = "s_region";

    void insertRegion(Region region);

    void updateRegion(Region region);

    void deleteRegion(Long region_id);

    Region getRegion(Long region_id);

    Page<Region> findRegion(RegionQuery query);
}
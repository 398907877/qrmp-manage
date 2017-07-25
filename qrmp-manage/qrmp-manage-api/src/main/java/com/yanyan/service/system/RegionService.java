package com.yanyan.service.system;


import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.Region;
import com.yanyan.data.query.system.RegionQuery;

import java.util.List;

/**
 * 区域服务
 * User: Saintcy
 * Date: 2016/5/3
 * Time: 15:21
 */
public interface RegionService {
    /**
     * 创建新区域
     *
     * @param region
     * @return
     */
    long createRegion(Region region);

    /**
     * 更新区域
     *
     * @param region
     */
    void updateRegion(Region region);

    /**
     * 删除区域
     *
     * @param region_id
     */
    void deleteRegion(Long region_id);

    /**
     * 获得区域
     *
     * @param region_id
     * @return
     */
    Region getRegion(Long region_id);

    /**
     * 查询区域
     *
     * @param query
     * @return
     */
    Page<Region> findRegion(RegionQuery query);

    /**
     * 获取国家列表
     *
     * @return
     */
    List<Region> getCountryList();

    /**
     * 根据国家id获取省份列表
     *
     * @return
     */
    List<Region> getProvinceList(Long country_id);

    /**
     * 根据省份id获取地市列表
     *
     * @param province_id
     * @return
     */
    List<Region> getCityList(Long province_id);

    /**
     * 根据地市id获取县市列表
     *
     * @param city_id
     * @return
     */
    List<Region> getCountyList(Long city_id);

    /**
     * 根据县市id获取街道列表
     *
     * @param county_id
     * @return
     */
    List<Region> getTownshipList(Long county_id);

    /**
     * 检查编码是否唯一
     *
     * @param id
     * @param code
     * @return
     */
    boolean checkRegionCode(Long id, String code);
}

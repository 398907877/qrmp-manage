package com.yanyan.service.system.impl;

import com.google.common.collect.Lists;
import com.yanyan.core.util.NumberUtils;
import com.yanyan.persist.system.RegionDao;
import com.yanyan.core.BusinessException;
import com.yanyan.core.lang.Page;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.core.spring.validator.group.Update;
import com.yanyan.data.domain.system.Region;
import com.yanyan.persist.TreeDao;
import com.yanyan.data.query.system.RegionQuery;
import com.yanyan.service.AbstractTreeService;
import com.yanyan.service.system.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地区服务
 * User: Saintcy
 * Date: 2016/8/23
 * Time: 16:03
 */
@Service
public class RegionServiceImpl extends AbstractTreeService implements RegionService {
    @Autowired
    private RegionDao regionDao;

    public long createRegion(Region region) {
        validate(region, Create.class);

        regionDao.insertRegion(region);
        createPath(region.getId(), region.getParent_id());
        return region.getId();
    }

    public void updateRegion(Region region) {
        validate(region, Update.class);
        updatePath(region.getId(), region.getParent_id(), regionDao.getPath(region.getId()));
        regionDao.updateRegion(region);
    }

    public void deleteRegion(Long region_id) {
        if (regionDao.hasChildren(region_id)) {
            throw new BusinessException("region.has.children", "地区含有子地区不能删除");
        }
        regionDao.deleteRegion(region_id);
    }

    public Region getRegion(Long region_id) {
        return regionDao.getRegion(region_id);
    }

    public Page<Region> findRegion(RegionQuery query) {
        return regionDao.findRegion(query);
    }

    public List<Region> getCountryList(){
        RegionQuery query = new RegionQuery();
        query.setParent_id(0L);
        query.setLevel((short)1);
        query.setCountTotal(false);

        return regionDao.findRegion(query).getRows();
    }

    public List<Region> getProvinceList(Long country_id) {
        if (country_id == null) return Lists.newArrayList();
        RegionQuery query = new RegionQuery();
        query.setParent_id(country_id);
        query.setLevel((short)2);
        query.setCountTotal(false);

        return regionDao.findRegion(query).getRows();
    }

    public List<Region> getCityList(Long province_id) {
        if (province_id == null) return Lists.newArrayList();
        RegionQuery query = new RegionQuery();
        query.setParent_id(province_id);
        query.setLevel((short)3);
        query.setCountTotal(false);

        return regionDao.findRegion(query).getRows();
    }

    public List<Region> getCountyList(Long city_id) {
        if (city_id == null) return Lists.newArrayList();
        RegionQuery query = new RegionQuery();
        query.setParent_id(city_id);
        query.setLevel((short)4);
        query.setCountTotal(false);

        return regionDao.findRegion(query).getRows();
    }

    public List<Region> getTownshipList(Long county_id) {
        if (county_id == null) return Lists.newArrayList();
        RegionQuery query = new RegionQuery();
        query.setParent_id(county_id);
        query.setLevel((short)5);
        query.setCountTotal(false);

        return regionDao.findRegion(query).getRows();
    }

    public boolean checkRegionCode(Long id, String code) {
        RegionQuery regionQuery = new RegionQuery();
        regionQuery.setCode(code);
        regionQuery.one();

        Page<Region> regionPage = regionDao.findRegion(regionQuery);
        return regionPage.getTotalCount() <= 0 || NumberUtils.equals(id, regionPage.getFirstRow().getId());
    }

    @Override
    protected TreeDao getTreeDao() {
        return regionDao;
    }
}

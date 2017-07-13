package com.yanyan.service.system.impl;

import com.google.common.collect.Lists;
import com.yanyan.core.util.NumberUtils;
import com.yanyan.data.domain.system.DictionaryEntry;
import com.yanyan.data.domain.system.DictionaryGroup;
import com.yanyan.data.query.system.DictionaryEntryQuery;
import com.yanyan.data.query.system.DictionaryGroupQuery;
import com.yanyan.persist.system.DictionaryDao;
import com.yanyan.service.system.DictionaryService;
import com.yanyan.core.BusinessException;
import com.yanyan.core.lang.Page;
import com.yanyan.core.spring.validator.group.Create;
import com.yanyan.core.spring.validator.group.Update;
import com.yanyan.service.BaseService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 数据字典
 * User: Saintcy
 * Date: 2016/8/12
 * Time: 10:11
 */
@Service
public class DictionaryServiceImpl extends BaseService implements DictionaryService {
    @Autowired
    private DictionaryDao dictionaryDao;
    @Resource(name = "serviceCacheManager")
    private CacheManager cacheManager;

    public long createEntry(DictionaryEntry entry) {
        validate(entry, Create.class);
        dictionaryDao.insertDictionaryEntry(entry);
        return entry.getId();
    }

    public void updateEntry(DictionaryEntry entry) {
        validate(entry, Update.class);
        DictionaryEntry oldEntry = getEntry(entry.getId());
        try {
            getCache().evict(oldEntry.getCode());//从缓存移除
        } catch (Exception e) {

        }
        dictionaryDao.updateDictionaryEntry(entry);

    }

    protected void validate(DictionaryEntry entry, Class<?>... groups) {
        super.validate(entry, groups);

        if (!checkEntryCode(entry.getId(), entry.getCode())) {
            throw new BusinessException("dictionary.entry.code.existed", new Object[]{entry.getCode()}, "编码[" + entry.getCode() + "]已经存在！");
        }
    }

    public void deleteEntry(Long entry_id) {
        DictionaryEntry entry = getEntry(entry_id);
        dictionaryDao.deleteDictionaryEntry(entry_id);
        try {
            getCache().evict(entry.getCode());//从缓存移除
        } catch (Exception e) {

        }
    }

    public DictionaryEntry getEntry(Long entry_id) {
        return dictionaryDao.getDictionaryEntry(entry_id);
    }

    public Page<DictionaryEntry> findEntry(DictionaryEntryQuery query) {
        return dictionaryDao.findDictionaryEntry(query);
    }

    public boolean checkEntryCode(Long id, String code) {
        if (StringUtils.isEmpty(code)) return true;
        DictionaryEntryQuery entryQuery = new DictionaryEntryQuery();
        entryQuery.setCode(code);
        entryQuery.one();

        Page<DictionaryEntry> entryPage = dictionaryDao.findDictionaryEntry(entryQuery);
        return entryPage.getTotalCount() <= 0 || (id != null && entryPage.getFirstRow().getId().longValue() == id);
    }

    public long createGroup(DictionaryGroup group) {
        validate(group, Create.class);
        dictionaryDao.insertDictionaryGroup(group);
        return group.getId();
    }

    public void updateGroup(DictionaryGroup group) {
        validate(group, Update.class);
        dictionaryDao.updateDictionaryGroup(group);
        getCache().clear();//清空缓存
    }

    protected void validate(DictionaryGroup group, Class<?>... groups) {
        super.validate(group, groups);

        if (!checkGroupCode(group.getId(), group.getCode())) {
            throw new BusinessException("dictionary.group.code.existed", new Object[]{group.getCode()}, "编码[" + group.getCode() + "]已经存在！");
        }
    }

    public void deleteGroup(Long group_id) {
        dictionaryDao.deleteDictionaryGroup(group_id);
        dictionaryDao.deleteDictionaryEntryByGroup(group_id);
        getCache().clear();//清空缓存
    }

    public DictionaryGroup getGroup(Long group_id) {
        return dictionaryDao.getDictionaryGroup(group_id);
    }

    public Page<DictionaryGroup> findGroup(DictionaryGroupQuery query) {
        return dictionaryDao.findDictionaryGroup(query);
    }

    public boolean checkGroupCode(Long id, String code) {
        if (StringUtils.isEmpty(code)) return true;
        DictionaryGroupQuery groupQuery = new DictionaryGroupQuery();
        groupQuery.setCode(code);
        groupQuery.one();

        Page<DictionaryGroup> groupPage = dictionaryDao.findDictionaryGroup(groupQuery);
        return groupPage.getTotalCount() <= 0 || NumberUtils.equals(id, groupPage.getFirstRow().getId());
    }

    public String getValue(String code) {
        DictionaryEntry entry = getEntry(code);
        if (entry == null) {
            throw new BusinessException("dictionary.entry.code.not.exist", new Object[]{code}, "编码[" + code + "]不存在！");
        }

        return entry.getValue();
    }

    public DictionaryEntry getEntry(String code) {
        DictionaryEntry entry = getCache().get(code, DictionaryEntry.class);
        if (entry == null) {//放入缓存
            entry = dictionaryDao.getDictionaryEntryByCode(code);
            getCache().put(code, entry);
        }
        return dictionaryDao.getDictionaryEntryByCode(code);
    }

    //TODO: 按分组的是否需要缓存？
    public List<DictionaryEntry> getEntryList(String group_code) {
        DictionaryGroup group = dictionaryDao.getDictionaryGroupByCode(group_code);

        if (group != null) {
            DictionaryEntryQuery query = new DictionaryEntryQuery();
            query.setGroup_id(group.getId());
            return dictionaryDao.findDictionaryEntry(query).getRows();
        } else {
            return Lists.newArrayList();
        }
    }

    private Cache getCache() {
        return cacheManager.getCache("service-dictionaryCache");
    }
}

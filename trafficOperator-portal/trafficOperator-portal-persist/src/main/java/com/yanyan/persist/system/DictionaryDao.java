package com.yanyan.persist.system;

import com.yanyan.data.domain.system.DictionaryEntry;
import com.yanyan.data.domain.system.DictionaryGroup;
import com.yanyan.data.query.system.DictionaryEntryQuery;
import com.yanyan.data.query.system.DictionaryGroupQuery;
import com.yanyan.core.lang.Page;

/**
 * 数据字典
 * User: Saintcy
 * Date: 2016/8/12
 * Time: 10:12
 */
public interface DictionaryDao {

    void insertDictionaryGroup(DictionaryGroup dictionary);
    void updateDictionaryGroup(DictionaryGroup dictionary);
    void deleteDictionaryGroup(Long id);
    DictionaryGroup getDictionaryGroup(Long id);
    DictionaryGroup getDictionaryGroupByCode(String code);
    Page<DictionaryGroup> findDictionaryGroup(DictionaryGroupQuery query);

    void insertDictionaryEntry(DictionaryEntry dictionary);
    void updateDictionaryEntry(DictionaryEntry dictionary);
    void deleteDictionaryEntry(Long id);
    void deleteDictionaryEntryByGroup(Long group_id);
    DictionaryEntry getDictionaryEntry(Long id);
    DictionaryEntry getDictionaryEntryByCode(String code);
    Page<DictionaryEntry> findDictionaryEntry(DictionaryEntryQuery query);
}

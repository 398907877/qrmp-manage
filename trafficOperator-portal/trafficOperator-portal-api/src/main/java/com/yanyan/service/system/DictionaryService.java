package com.yanyan.service.system;


import com.yanyan.data.query.system.DictionaryEntryQuery;
import com.yanyan.core.lang.Page;
import com.yanyan.data.domain.system.DictionaryEntry;
import com.yanyan.data.domain.system.DictionaryGroup;
import com.yanyan.data.query.system.DictionaryGroupQuery;

import java.util.List;

/**
 * 数据字典服务
 * User: Saintcy
 * Date: 2015/8/21
 * Time: 17:23
 */
public interface DictionaryService {

    /**
     * 创建条目
     *
     * @param entry
     */
    long createEntry(DictionaryEntry entry);

    /**
     * 保存条目
     *
     * @param entry
     */
    void updateEntry(DictionaryEntry entry);

    /**
     * 删除条目
     *
     * @param entry_id
     */
    void deleteEntry(Long entry_id);

    /**
     * 根据关键字获取条目
     *
     * @param entry_id 关键字
     * @return
     */
    DictionaryEntry getEntry(Long entry_id);

    /**
     * 查询条目列表
     *
     * @param query
     * @return
     */
    Page<DictionaryEntry> findEntry(DictionaryEntryQuery query);

    /**
     * 检查编码是否唯一
     * @param id
     * @param code
     * @return
     */
    boolean checkEntryCode(Long id, String code);

    /**
     * 创建分组
     *
     * @param group
     */
    long createGroup(DictionaryGroup group);

    /**
     * 保存分组
     *
     * @param group
     */
    void updateGroup(DictionaryGroup group);

    /**
     * 删除分组
     *
     * @param group_id
     */
    void deleteGroup(Long group_id);

    /**
     * 根据关键字获取分组
     *
     * @param group_id
     * @return
     */
    DictionaryGroup getGroup(Long group_id);

    /**
     * 查询分组列表
     *
     * @param query
     * @return
     */
    Page<DictionaryGroup> findGroup(DictionaryGroupQuery query);


    /**
     * 检查编码是否唯一
     * @param id
     * @param code
     * @return
     */
    boolean checkGroupCode(Long id, String code);

    /**
     * 根据关键字获取值
     *
     * @param code 关键字
     */
    String getValue(String code);

    /**
     * 根据关键字获取条目
     *
     * @param code 关键字
     * @return
     */
    DictionaryEntry getEntry(String code);

    /**
     * 根据组名获取一组条目
     *
     * @param group 组
     * @return
     */
    List<DictionaryEntry> getEntryList(String group);
}

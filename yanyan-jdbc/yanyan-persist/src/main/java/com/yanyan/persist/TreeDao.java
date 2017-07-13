package com.yanyan.persist;

import java.util.List;

/**
 * 树形DAO
 * 主要实现对扩展字段path的维护，以便于业务查询子孙
 * User: Saintcy
 * Date: 2016/5/21
 * Time: 23:45
 */
public interface TreeDao {

    /**
     * 根据父id获取孩子的path
     *
     * @param parent_id
     * @return
     */
    String generatePath(Long parent_id);

    /**
     * 获取路径
     *
     * @param id
     * @return
     */
    String getPath(Long id);

    /**
     * 更新路径
     *
     * @param id
     * @param path
     */
    void updatePath(Long id, String path);

    /**
     * 更新子孙路径
     *
     * @param id
     * @param newPath
     * @param oldPath
     */
    void updateDescendantsPath(Long id, String newPath, String oldPath);

    /**
     * 更新顺序号
     * @param id
     * @param priority
     */
    void updatePriority(Long id, Integer priority);

    /**
     * 获取祖先id列表
     * @param id
     * @return
     */
    List<Long> getAncestors(Long id);

    /**
     * 获取子孙id列表
     *
     * @param id
     * @return
     */
    List<Long> getDescendants(Long id);

    /**
     * 是否有孩子
     *
     * @param id
     * @return
     */
    boolean hasChildren(Long id);
}

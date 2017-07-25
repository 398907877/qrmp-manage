package com.yanyan.persist;

import org.apache.ibatis.annotations.Param;

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
    String generatePath(@Param("parent_id") Long parent_id);

    /**
     * 获取路径
     *
     * @param id
     * @return
     */
    String getPath(@Param("id") Long id);

    /**
     * 更新路径
     *
     * @param id
     * @param path
     */
    void updatePath(@Param("id") Long id, @Param("path") String path);

    /**
     * 更新子孙路径
     *
     * @param id
     * @param newPath
     * @param oldPath
     */
    void updateDescendantsPath(@Param("id") Long id, @Param("new_path") String newPath, @Param("old_path") String oldPath);

    /**
     * 更新顺序号
     * @param id
     * @param priority
     */
    void updatePriority(@Param("id") Long id, @Param("priority") Integer priority);

    /**
     * 获取祖先id列表
     * @param id
     * @return
     */
    List<Long> getAncestors(@Param("id") Long id);

    /**
     * 获取子孙id列表
     *
     * @param id
     * @return
     */
    List<Long> getDescendants(@Param("id") Long id);

    /**
     * 是否有孩子
     *
     * @param id
     * @return
     */
    boolean hasChildren(@Param("id") Long id);
}

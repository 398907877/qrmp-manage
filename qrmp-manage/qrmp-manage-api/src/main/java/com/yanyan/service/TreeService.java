package com.yanyan.service;

/**
 * 树形服务
 * User: Saintcy
 * Date: 2016/11/28
 * Time: 14:10
 */
public interface TreeService {
    /**
     * 创建路径字段
     *
     * @param id
     * @param parent_id
     */
    //void createPath(long id, long parent_id);

    /**
     * 更新路径字段
     *
     * @param id
     * @param parent_id
     * @param oldPath
     */
    //void updatePath(long id, long parent_id, String oldPath);

    /**
     * 更新树节点的排序
     * @param id
     * @param priority
     */
    void updatePriority(long id, int priority);

    /**
     * 移动数节点位置
     * @param src_id 要移动的节点
     * @param dest_id 移动到的目标节点
     */
    //void moveTo(long src_id, long dest_id);
}

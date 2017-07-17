package com.yanyan.service;

import com.yanyan.persist.TreeDao;
import org.apache.commons.lang3.StringUtils;

/**
 * 树形对象服务抽象类
 * User: Saintcy
 * Date: 2016/5/22
 * Time: 8:56
 */
public abstract class AbstractTreeService extends BaseService {

    /**
     * 创建路径字段
     *
     * @param id
     * @param parent_id
     */
    protected void createPath(long id, long parent_id) {
        getTreeDao().updatePath(id, getTreeDao().generatePath(parent_id));
    }

    /**
     * 更新路径字段
     *
     * @param id
     * @param parent_id
     * @param oldPath
     */
    protected void updatePath(long id, long parent_id, String oldPath) {
        //String oldPath = getTreeDao().getPath(id);
        String newPath = getTreeDao().generatePath(parent_id);
        if (!StringUtils.equals(oldPath, newPath)) {
            getTreeDao().updatePath(id, newPath);
            getTreeDao().updateDescendantsPath(id, newPath, oldPath);
        }
    }

    /**
     * 更新树节点的排序
     * @param id
     * @param priority
     */
    public void updatePriority(long id, int priority) {
        getTreeDao().updatePriority(id, priority);
    }

    protected abstract TreeDao getTreeDao();
}

package com.yanyan.core.util;


import org.apache.commons.beanutils.PropertyUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * User: Saintcy
 * Date: 2016/8/17
 * Time: 17:06
 */
public class TreeUtils {
    /**
     * 按树的显示顺序排序：
     * 1
     * --11
     * ----111
     * ----112
     * --12
     * ----121
     * ----122
     * 2
     * --21
     * --22
     * 3
     * --31
     * --32
     *
     * @param nodeList 节点列表，必须是按pid,priority排序好的
     * @param parentId 上级节点id
     * @param <T>      具有id, parent_id, priority三个属性的类
     *                 Tips: 因为每个领域对象对每个字段进行验证的内容message不一样，所以没有采用继承的方式，而是采用反射读取字段
     * @return
     */
    public static final <T> List<T> sortAsTree(List<T> nodeList, long parentId) {
        List<T> tree = new ArrayList<T>();
        try {
            for (T node : nodeList) {
                if (((Long) PropertyUtils.getProperty(node, "parent_id")).longValue() == parentId) {
                    tree.add(node);
                    tree.addAll(sortAsTree(nodeList, (Long) PropertyUtils.getProperty(node, "id")));
                }
            }

            /*Collections.sort(tree, new Comparator<T>() {
                public int compare(T o1, T o2) {
                    try {
                        return (Integer) PropertyUtils.getProperty(o1, "priority") - (Integer) PropertyUtils.getProperty(o1, "priority");
                    } catch (Exception e) {
                        log.error("", e);
                    }
                    return 0;
                }
            });*/
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tree;
    }
}

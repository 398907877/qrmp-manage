package com.yanyan.data.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 树节点
 * User: Saintcy
 * Date: 2016/8/15
 * Time: 19:50
 */
@Data
public class TreeNode implements Serializable {
    private String id;
    private String name;
    private Integer priority;
    private boolean checked;
    private String icon;
    private List<TreeNode> children;
}

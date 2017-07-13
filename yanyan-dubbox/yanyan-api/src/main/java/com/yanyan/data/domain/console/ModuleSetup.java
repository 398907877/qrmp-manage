package com.yanyan.data.domain.console;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 代码生成设置
 * User: Saintcy
 * Date: 2016/10/25
 * Time: 10:14
 */
@Data
public class ModuleSetup implements Serializable {
    /**
     * 主表
     */
    private String tableName;
    /**
     * 子表
     */
    private List<ModuleSetup> slaveTable;
    /**
     * 包名
     */
    private String packageName;
    /**
     * 模块名，多级用"."隔开，如 system.user.permission
     */
    private String moduleName;
    /**
     * 功能名
     */
    private String name;
    /**
     * 功能标题
     */
    private String title;
    /**
     * 功能描述
     */
    private String desc;
    /**
     * 功能作者
     */
    private String author;
    /**
     * 字段列表
     */
    private List<Field> fields;
    /**
     * 外键，与主表关联
     */
    private String foreignKey;
    /**
     * 字段是否采用驼峰式
     */
    private Boolean isCamelCase;
    /**
     * 是否标志删除字段
     */
    private Boolean isFlagDelete;
    /**
     * 是否分页
     */
    private Boolean isPaginate;
}

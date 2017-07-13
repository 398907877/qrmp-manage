package com.yanyan.data.domain.console;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 字段
 * User: Saintcy
 * Date: 2016/10/25
 * Time: 10:36
 */
@Data
public class Field implements Serializable {
    /**
     * 所在表名
     */
    private String tableName;
    /**
     * 所在字段
     */
    private String columnName;
    /**
     * 若是字段不在主表内，则需要指定关联的字段[[本表字段1, 主表字段1],[本表字段2, 主表字段2],[本表字段2, 主表字段2]],
     */
    private List<String[]> references;
    /**
     * 字段名
     */
    private String name;
    /**
     * 字段类型
     */
    private String type;
    /**
     * 字段标题
     */
    private String title;
    /**
     * 字段描述
     */
    private String desc;
    /**
     * 约束，json格式，例如：{name: "Length", message: "{min}-{max}", max: 10, min: 1}
     */
    private List<String> constraints;
    /**
     * 传输类型：InputOnly/OutputOnly/NoTransfer/InputOutput(in and out)/None
     */
    private String transfer;
    /**
     * 检查唯一
     */
    private Boolean checkUnique;
    /**
     * 是否在插入列中
     */
    private Boolean isInsert;
    /**
     * 是否在更新列中
     */
    private Boolean isUpdate;
    /**
     * 是否在查询列中
     */
    private Boolean isSelect;
    /**
     * 是否作为查询条件 0无 1 精确= 2范围between 3集合in 4 模糊like
     */
    private Integer queryType;
    /**
     * 是否关键字
     */
    private Boolean isKeyword;
    /**
     * 是否作为排序字段 asc/desc
     */
    private String orderBy;
}

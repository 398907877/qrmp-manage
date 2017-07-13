package com.yanyan.core.db.mybatis;

import lombok.Data;

import java.util.ArrayList;

/**
 * 分页对象的包装，以骗过mybatis，以为是集合类型
 * 因为mybatis的selectList必须返回List类型
 * 所有Page相关的字段设置为transient，避免序列化器输出
 * User: Saintcy
 * Date: 2017/3/26
 * Time: 10:06
 */
@Data
public class PageList<T> extends ArrayList<T> {
    private transient int totalCount;//总条数
    private transient int pageNo;//当前页码,从1开始
    private transient int pageSize;//每页记录数
    private transient int totalPage;//总页数


    @Override
    public String toString() {
        return "PageList{" +
                "totalCount=" + totalCount +
                ", pageNo=" + pageNo +
                ", pageSize=" + pageSize +
                ", totalPage=" + totalPage +
                "} " + super.toString();
    }
}

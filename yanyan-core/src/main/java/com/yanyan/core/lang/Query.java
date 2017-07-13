package com.yanyan.core.lang;

import java.io.Serializable;
import java.util.Arrays;

/**
 * 查询过滤器
 * User: Saintcy
 * Date: 2015/8/24
 * Time: 11:01
 */
public class Query implements Serializable {
    protected String fields;//返回的字段集合，多个用逗号隔开
    protected String[] order_by;//排序字段

    public String[] getOrder_by() {
        return order_by;
    }

    public void setOrder_by(String[] order_by) {
        this.order_by = order_by;
    }

    //*******************--兼容驼峰模式--********************//

    public String getFields() {
        return fields;
    }

    public void setFields(String fields) {
        this.fields = fields;
    }

    public String[] getOrderBy() {
        return order_by;
    }

    public void setOrderBy(String[] orderBy) {
        this.order_by = orderBy;
    }

    /*public void reset() {
        try {
            BeanUtils.copyProperties(this.getClass().newInstance(), this);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }*/

    @Override
    public String toString() {
        return "Query{" +
                "fields='" + fields + '\'' +
                ", orderBy=" + Arrays.toString(order_by) +
                '}';
    }
}

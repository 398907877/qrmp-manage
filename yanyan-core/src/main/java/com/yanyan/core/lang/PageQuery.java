package com.yanyan.core.lang;

import java.io.Serializable;

/**
 * 分页查询条件
 * User: Saintcy
 * Date: 2015/7/9
 * Time: 13:02
 */
public class PageQuery extends Query implements Serializable {
    private Integer page_no;
    private Integer page_size;
    private Boolean count_total = true;//计算总数，默认总是计算，除非设置为false

    public PageQuery() {
    }

    public PageQuery(Integer pageNo, Integer pageSize) {
        this.setPageNo(pageNo);
        this.setPageSize(pageSize);
    }

    public PageQuery(Integer pageNo, Integer pageSize, Boolean countTotal) {
        this(pageNo, pageSize);
        this.count_total = countTotal;
    }

    /**
     * 如果没有设定值，则设定默认值
     */
    public void defaultPageParam() {
        defaultPageParam(null, null);
    }

    /**
     * 如果没有设定值，则设定默认值
     *
     * @param defaultPageNo
     * @param defaultPageSize
     */
    public void defaultPageParam(Integer defaultPageNo, Integer defaultPageSize) {
        defaultPageParam(defaultPageNo, defaultPageSize, null);
    }

    /**
     * 如果没有设定值，则设定默认值
     *
     * @param defaultPageNo
     * @param defaultPageSize
     * @param defaultCountTotal
     */
    public void defaultPageParam(Integer defaultPageNo, Integer defaultPageSize, Boolean defaultCountTotal) {
        if (this.page_no == null || this.page_no <= 0) {
            this.page_no = (defaultPageNo == null || defaultPageNo <= 0 ? 1 : defaultPageNo);
        }
        if (this.page_size == null || this.page_size <= 0) {
            this.page_size = (defaultPageSize == null || defaultPageSize <= 0 ? 10 : defaultPageSize);
        }
        if (this.count_total == null) {
            this.count_total = (defaultCountTotal == null ? true : defaultCountTotal);
        }
    }

    /**
     * 查询一行，用于查找唯一匹配的数据
     */
    public void one() {
        this.page_no = 1;
        this.page_size = 1;
        this.count_total = false;//此时无需查询总条数
    }

    public Integer getPage_no() {
        return page_no;
    }

    public void setPage_no(Integer page_no) {
        this.page_no = page_no;
    }

    public Integer getPage_size() {
        return page_size;
    }

    public void setPage_size(Integer page_size) {
        this.page_size = page_size;
    }

    public Boolean getCount_total() {
        return count_total;
    }

    public void setCount_total(Boolean count_total) {
        this.count_total = count_total;
    }

    //********************** -- 兼容驼峰模式 --  ************************//
    public Integer getPageNo() {
        return page_no;
    }

    public void setPageNo(Integer pageNo) {
        this.page_no = pageNo;
    }

    public Integer getPageSize() {
        return page_size;
    }

    public void setPageSize(Integer pageSize) {
        this.page_size = pageSize;
    }

    public Boolean getCountTotal() {
        return count_total;
    }

    public void setCountTotal(Boolean countTotal) {
        this.count_total = countTotal;
    }

    @Override
    public String toString() {
        return "PageQuery{" +
                "pageNo=" + page_no +
                ", pageSize=" + page_size +
                ", countTotal=" + count_total +
                "} " + super.toString();
    }
}

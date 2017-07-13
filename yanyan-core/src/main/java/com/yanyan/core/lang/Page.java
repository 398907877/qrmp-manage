package com.yanyan.core.lang;

import java.io.Serializable;
import java.util.List;

/**
 * Title: 分页
 *
 * @author Saintcy Don
 * @version 1.0
 */
public class Page<T> implements Serializable {
    private int total_count;//总条数
    private int page_no;//当前页码,从1开始
    private int page_size;//每页记录数
    private int total_page;//总页数
    private List<T> rows;//记录列表

    public Page() {
    }

    public int getTotal_count() {
        return total_count;
    }

    public void setTotal_count(int total_count) {
        this.total_count = total_count;
    }

    public int getPage_no() {
        return page_no;
    }

    public void setPage_no(int page_no) {
        this.page_no = page_no;
    }

    public int getPage_size() {
        return page_size;
    }

    public void setPage_size(int page_size) {
        this.page_size = page_size;
    }

    public int getTotal_page() {
        return total_page;
    }

    public void setTotal_page(int total_page) {
        this.total_page = total_page;
    }

    //************************ 兼容驼峰模式 **************************//

    public int getTotalCount() {
        return total_count;
    }

    public void setTotalCount(int totalCount) {
        this.total_count = totalCount;
    }

    public int getPageNo() {
        return page_no;
    }

    public void setPageNo(int pageNo) {
        this.page_no = pageNo;
    }

    public int getPageSize() {
        return page_size;
    }

    public void setPageSize(int pageSize) {
        this.page_size = pageSize;
    }

    public int getTotalPage() {
        return total_page;
    }

    public void setTotalPage(int totalPage) {
        this.total_page = totalPage;
    }

    public List<T> getRows() {
        return rows;
    }

    public void setRows(List<T> rows) {
        this.rows = rows;
    }

    public T getRow(int i) {
        if (rows != null) {
            return rows.get(i);
        } else {
            return null;
        }
    }

    public T getFirstRow() {
        return isEmpty() ? null : getRow(0);
    }

    public T getLastRow() {
        return getRow(rows == null ? 0 : rows.size());
    }

    public boolean isEmpty() {
        return rows == null || rows.size() == 0;
    }

    @Override
    public String toString() {
        return "Page{" +
                "total_count=" + total_count +
                ", page_no=" + page_no +
                ", page_size=" + page_size +
                ", total_page=" + total_page +
                ", rows=" + rows +
                '}';
    }
}

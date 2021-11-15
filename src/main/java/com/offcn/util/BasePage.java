package com.offcn.util;

public class BasePage {
    /**
     * 当前页码
     */
    private int page;
    /**
     * 当前页条数
     */
    private int rows;
    /**
     * 查询数据的起始位置
     */
    private int limitStart;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getLimitStart() {
        return limitStart;
    }

    public void setLimitStart(int limitStart) {
        this.limitStart = limitStart;
    }

    @Override
    public String toString() {
        return "BasePage{" +
                "page=" + page +
                ", rows=" + rows +
                ", limitStart=" + limitStart +
                '}';
    }
}

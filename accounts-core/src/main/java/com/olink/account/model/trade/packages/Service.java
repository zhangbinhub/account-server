package com.olink.account.model.trade.packages;

/**
 * Created by Shepherd on 2016-08-04.
 * 接口参数
 */
public class Service {

    private String action;//调用接口

    private int page;//当前页

    private int count;//每页数量

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

}

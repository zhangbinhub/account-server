package com.olink.account.trade.servers.result;

import com.olink.account.base.impl.BaseServerResult;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/12/4.
 * 二级商户接口返回
 */
public class BusinessResult extends BaseServerResult {

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getShopno() {
        return shopno;
    }

    public void setShopno(String shopno) {
        this.shopno = shopno;
    }

    public double getAmont() {
        return amont.doubleValue();
    }

    public void setAmont(double amont) {
        this.amont = BigDecimal.valueOf(amont);
    }

    public double getRate() {
        return rate.doubleValue();
    }

    public void setRate(double rate) {
        this.rate = BigDecimal.valueOf(rate);
    }

    public String getFinishdate() {
        return finishdate;
    }

    public void setFinishdate(String finishdate) {
        this.finishdate = finishdate;
    }

    private String orderno;

    private String shopno;

    private BigDecimal amont;

    private BigDecimal rate;

    private String finishdate;

}

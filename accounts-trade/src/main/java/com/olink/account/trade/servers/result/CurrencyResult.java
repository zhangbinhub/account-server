package com.olink.account.trade.servers.result;

import com.olink.account.base.impl.BaseServerResult;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/10/4.
 * 自有币种交易接口返回
 */
public class CurrencyResult extends BaseServerResult {

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getCurrencyname() {
        return currencyname;
    }

    public void setCurrencyname(String currencyname) {
        this.currencyname = currencyname;
    }

    public double getAmont() {
        return amont.doubleValue();
    }

    public void setAmont(double amont) {
        this.amont = BigDecimal.valueOf(amont);
    }

    public double getSuramont() {
        return suramont.doubleValue();
    }

    public void setSuramont(double suramont) {
        this.suramont = BigDecimal.valueOf(suramont);
    }

    public String getFinishdate() {
        return finishdate;
    }

    public void setFinishdate(String finishdate) {
        this.finishdate = finishdate;
    }

    private String custid;

    private String orderno;

    private String currencyname;

    private BigDecimal amont;

    private BigDecimal suramont;

    private String finishdate;

}

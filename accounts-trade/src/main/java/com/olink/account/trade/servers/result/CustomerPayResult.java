package com.olink.account.trade.servers.result;

import com.olink.account.base.impl.BaseServerResult;
import net.sf.json.JSONObject;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/9/6.
 * C户交易接口返回报文，body
 */
public class CustomerPayResult extends BaseServerResult {

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getRececustid() {
        return rececustid;
    }

    public void setRececustid(String rececustid) {
        this.rececustid = rececustid;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public double getAmont() {
        return amont.doubleValue();
    }

    public void setAmont(double amont) {
        this.amont = BigDecimal.valueOf(amont);
    }

    public double getTotalbalance() {
        return totalbalance.doubleValue();
    }

    public void setTotalbalance(double totalbalance) {
        this.totalbalance = BigDecimal.valueOf(totalbalance);
    }

    public double getAvailablebalance() {
        return availablebalance.doubleValue();
    }

    public void setAvailablebalance(double availablebalance) {
        this.availablebalance = BigDecimal.valueOf(availablebalance);
    }

    public double getBeforebalance() {
        return beforebalance.doubleValue();
    }

    public void setBeforebalance(double beforebalance) {
        this.beforebalance = BigDecimal.valueOf(beforebalance);
    }

    public double getAfterbalance() {
        return afterbalance.doubleValue();
    }

    public void setAfterbalance(double afterbalance) {
        this.afterbalance = BigDecimal.valueOf(afterbalance);
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

    private String rececustid;

    private String orderno;

    private BigDecimal amont;

    private BigDecimal totalbalance;

    private BigDecimal availablebalance;

    private BigDecimal beforebalance;

    private BigDecimal afterbalance;

    private BigDecimal suramont;

    private String finishdate;

}

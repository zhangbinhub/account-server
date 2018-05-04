package com.olink.account.trade.servers.result;

import com.olink.account.base.impl.BaseServerResult;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/10/8.
 * C户交易接口返回（后台）
 */
public class BackCustomerResult extends BaseServerResult {

    public double getAmont() {
        return amont.doubleValue();
    }

    public void setAmont(double amont) {
        this.amont = BigDecimal.valueOf(amont);
    }

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

    public String getFinishdate() {
        return finishdate;
    }

    public void setFinishdate(String finishdate) {
        this.finishdate = finishdate;
    }

    private String custid;

    private String orderno;

    private BigDecimal amont;

    private String finishdate;

}

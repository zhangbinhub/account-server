package com.olink.account.trade.servers.result;

import com.olink.account.base.impl.BaseServerResult;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/12/4.
 * 内部交易返回
 */
public class BackInnerResult extends BaseServerResult {

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

    public String getFinishdate() {
        return finishdate;
    }

    public void setFinishdate(String finishdate) {
        this.finishdate = finishdate;
    }

    private String orderno;

    private BigDecimal amont;

    private String finishdate;

}

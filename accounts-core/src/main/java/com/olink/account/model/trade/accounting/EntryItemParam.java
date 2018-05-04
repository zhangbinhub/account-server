package com.olink.account.model.trade.accounting;

import com.olink.account.enumration.BalanceDirectEnum;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/12/12.
 * 会计科目流水参数
 */
public class EntryItemParam {

    public double getAmont() {
        return amont.doubleValue();
    }

    public void setAmont(double amont) {
        this.amont = BigDecimal.valueOf(amont);
    }

    public String getItemcode() {
        return itemcode;
    }

    public void setItemcode(String itemcode) {
        this.itemcode = itemcode;
    }

    public String getSubitemcode() {
        return subitemcode;
    }

    public void setSubitemcode(String subitemcode) {
        this.subitemcode = subitemcode;
    }

    public BalanceDirectEnum getBalanceDirect() throws EnumValueUndefinedException {
        return BalanceDirectEnum.getEnum(balanceDirect);
    }

    public void setBalanceDirect(BalanceDirectEnum balanceDirect) {
        this.balanceDirect = balanceDirect.getValue();
    }

    /**
     * 发生额
     */
    private BigDecimal amont;

    /**
     * 第三级科目编号
     */
    private String itemcode;

    /**
     * 科目下立子账户序号
     */
    private String subitemcode;

    /**
     * 记账类型：1-借，2-贷
     */
    private int balanceDirect;

}

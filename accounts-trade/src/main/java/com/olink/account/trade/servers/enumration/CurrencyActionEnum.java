package com.olink.account.trade.servers.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/4.
 * 自有币种接口列表
 */
public enum CurrencyActionEnum {

    /**
     * 申请入账
     */
    cust_currency_applyrecharge("cust_currency_applyrecharge"),

    /**
     * 确认入账
     */
    cust_currency_recharge("cust_currency_recharge"),

    /**
     * 取消入账
     */
    cust_currency_canclerecharge("cust_currency_canclerecharge"),

    /**
     * 申请消费
     */
    cust_currency_applypayment("cust_currency_applypayment"),

    /**
     * 确认消费
     */
    cust_currency_payment("cust_currency_payment"),

    /**
     * 取消消费
     */
    cust_currency_canclepayment("cust_currency_canclepayment"),

    /**
     * 申请退款
     */
    cust_currency_applyrefund("cust_currency_applyrefund"),

    /**
     * 确认退款
     */
    cust_currency_refund("cust_currency_refund"),

    /**
     * 取消退款
     */
    cust_currency_canclerefund("cust_currency_canclerefund");

    private String value;

    private static Map<String, CurrencyActionEnum> map;

    static {
        map = new HashMap<>();
        for (CurrencyActionEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    CurrencyActionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CurrencyActionEnum getEnum(String value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(CustActionEnum.class, value);
        }
    }
}

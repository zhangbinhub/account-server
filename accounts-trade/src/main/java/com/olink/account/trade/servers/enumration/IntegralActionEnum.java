package com.olink.account.trade.servers.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/7.
 * 积分接口列表
 */
public enum IntegralActionEnum {

    /**
     * 积分消费申请
     */
    cust_integral_applypayment("cust_integral_applypayment"),

    /**
     * 积分消费确认
     */
    cust_integral_payment("cust_integral_payment"),

    /**
     * 积分消费取消
     */
    cust_integral_canclepayment("cust_integral_canclepayment"),

    /**
     * 积分退款申请
     */
    cust_integral_applyrefund("cust_integral_applyrefund"),

    /**
     * 积分退款确认
     */
    cust_integral_refund("cust_integral_refund"),

    /**
     * 积分退款取消
     */
    cust_integral_canclerefund("cust_integral_canclerefund");

    private String value;

    private static Map<String, IntegralActionEnum> map;

    static {
        map = new HashMap<>();
        for (IntegralActionEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    IntegralActionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static IntegralActionEnum getEnum(String value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(IntegralActionEnum.class, value);
        }
    }
}

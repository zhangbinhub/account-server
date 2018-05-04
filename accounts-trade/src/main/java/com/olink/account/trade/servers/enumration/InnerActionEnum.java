package com.olink.account.trade.servers.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/12/4.
 * 内部交易action
 */
public enum InnerActionEnum {

    /**
     * B户余额结算申请
     */
    back_business_applyBalanceSettlement("back_business_applyBalanceSettlement"),

    /**
     * B户余额结算确认
     */
    back_business_BalanceSettlement("back_business_BalanceSettlement"),

    /**
     * B户余额结算取消
     */
    back_business_cancleBalanceSettlement("back_business_cancleBalanceSettlement"),

    /**
     * B户积分结算申请
     */
    back_business_applyIntegralSettlement("back_business_applyIntegralSettlement"),

    /**
     * B户积分结算确认
     */
    back_business_IntegralSettlement("back_business_IntegralSettlement"),

    /**
     * B户积分结算取消
     */
    back_business_cancleIntegralSettlement("back_business_cancleIntegralSettlement"),

    /**
     * 特殊交易申请
     */
    back_applyInternalTrade("back_applyInternalTrade"),

    /**
     * 特殊交易审核
     */
    back_reviewInternalTrade("back_reviewInternalTrade"),

    /**
     * 特殊交易确认
     */
    back_InternalTrade("back_InternalTrade"),

    /**
     * 特殊交易取消
     */
    back_cancleInternalTrade("back_cancleInternalTrade"),

    /**
     * 通用记账交易
     */
    back_business_normaltrade("back_business_normaltrade");

    private String value;

    private static Map<String, InnerActionEnum> map;

    static {
        map = new HashMap<>();
        for (InnerActionEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    InnerActionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static InnerActionEnum getEnum(String value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(InnerActionEnum.class, value);
        }
    }

}

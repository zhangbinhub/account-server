package com.olink.account.trade.servers.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/12/4.
 * 二级商户接口列表
 */
public enum BusinessActionEnum {

    /**
     * 二级商户结算申请
     */
    shop_applysettlement("shop_applysettlement"),

    /**
     * 二级商户结算确认
     */
    shop_settlement("shop_settlement"),

    /**
     * 二级商户结算取消
     */
    shop_canclesettlement("shop_canclesettlement"),

    /**
     * B户通用记账交易
     */
    business_normaltrade("business_normaltrade");

    private String value;

    private static Map<String, BusinessActionEnum> map;

    static {
        map = new HashMap<>();
        for (BusinessActionEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    BusinessActionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BusinessActionEnum getEnum(String value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(BusinessActionEnum.class, value);
        }
    }

}

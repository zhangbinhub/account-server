package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/8.
 * C户绑定类型
 */
public enum CBindTypeEnum {

    AliPay("ALIPAY", "支付宝"),

    WeiXin("WEIXIN", "微信号"),

    BankCard("BANKCARD", "银行卡");

    private String name;

    private String value;

    private static Map<String, CBindTypeEnum> map;

    static {
        map = new HashMap<>();
        for (CBindTypeEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    CBindTypeEnum(String value, String name) {
        this.value = value.toUpperCase();
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public String getValue() {
        return this.value;
    }

    public Boolean equals(String value) {
        return this.value.equals(value.toUpperCase());
    }

    public static CBindTypeEnum getEnum(String value) throws EnumValueUndefinedException {
        if (map.containsKey(value.toUpperCase())) {
            return map.get(value.toUpperCase());
        } else {
            throw new EnumValueUndefinedException(CBindTypeEnum.class, value);
        }
    }
}

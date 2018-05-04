package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/18.
 * B户绑定类型
 */
public enum BBindTypeEnum {

    AliPay("ALIPAY", "支付宝"),

    WeiXinPublic("WEIXINPUBLIC", "微信PUBLIC"),

    WeiXinOpen("WEIXINOPEN", "微信OPEN"),

    BankCard("BANKCARD", "银行卡");

    private String name;

    private String value;

    private static Map<String, BBindTypeEnum> map;

    static {
        map = new HashMap<>();
        for (BBindTypeEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    BBindTypeEnum(String value, String name) {
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

    public static BBindTypeEnum getEnum(String value) throws EnumValueUndefinedException {
        if (map.containsKey(value.toUpperCase())) {
            return map.get(value.toUpperCase());
        } else {
            throw new EnumValueUndefinedException(BBindTypeEnum.class, value);
        }
    }
}

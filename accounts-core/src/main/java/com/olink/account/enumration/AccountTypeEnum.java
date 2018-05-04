package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/9/18.
 * 虚拟账户类型
 */
public enum AccountTypeEnum {

    All("00", "ALL", "总余额账户"),

    Change("01", "CHANGE", "可用余额账户"),

    Red("02", "RED", "红包账户"),

    Coupon("03", "COUPON", "优惠券账户"),

    Integral("04", "INTEGRAL", "积分账户");

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    private String code;

    private String type;

    private String name;

    private static Map<String, AccountTypeEnum> map;

    static {
        map = new HashMap<>();
        for (AccountTypeEnum type : values()) {
            map.put(type.getType(), type);
        }
    }

    AccountTypeEnum(String code, String type, String name) {
        this.code = code;
        this.type = type.toUpperCase();
        this.name = name;
    }

    public Boolean equals(String type) {
        return this.type.equals(type.toUpperCase());
    }

    public static AccountTypeEnum getEnum(String type) throws EnumValueUndefinedException {
        if (map.containsKey(type.toUpperCase())) {
            return map.get(type.toUpperCase());
        } else {
            throw new EnumValueUndefinedException(AccountTypeEnum.class, type);
        }
    }
}

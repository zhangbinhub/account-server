package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/11/4.
 * 余额类型
 */
public enum AmontTypeEnum {

    balance(1),//余额

    money(2);//折算金额

    private Integer value;

    private static Map<Integer, AmontTypeEnum> map;

    static {
        map = new HashMap<>();
        for (AmontTypeEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    AmontTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static AmontTypeEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(AmontTypeEnum.class, value);
        }
    }

}

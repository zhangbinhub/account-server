package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/31.
 * 发生额计算模式
 */
public enum CalculateModeEnum {

    calculate(1),//计算

    convert(2);//兑换

    private Integer value;

    private static Map<Integer, CalculateModeEnum> map;

    static {
        map = new HashMap<>();
        for (CalculateModeEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    CalculateModeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static CalculateModeEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(CalculateModeEnum.class, value);
        }
    }

}

package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shepherd on 2016-08-26.
 * 是否默认
 */
public enum DefaultEnum {

    isdefault(1),

    notdefault(0);

    private Integer value;

    private static Map<Integer, DefaultEnum> map;

    static {
        map = new HashMap<>();
        for (DefaultEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    DefaultEnum(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static DefaultEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(DefaultEnum.class, value);
        }
    }
}

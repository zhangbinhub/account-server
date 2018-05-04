package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shepherd on 2016-08-24.
 * 状态枚举类
 */
public enum StatusEnum {

    prohibit(0),//停用

    activate(1),//启用

    registering(2);//注册中

    private Integer value;

    private static Map<Integer, StatusEnum> map;

    static {
        map = new HashMap<>();
        for (StatusEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    StatusEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static StatusEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(StatusEnum.class, value);
        }
    }
}

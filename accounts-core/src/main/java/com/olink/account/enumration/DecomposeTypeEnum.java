package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/25.
 * 发生额分解类型
 */
public enum DecomposeTypeEnum {

    outer(1),//价外

    inner(2);//价内

    private Integer value;

    private static Map<Integer, DecomposeTypeEnum> map;

    static {
        map = new HashMap<>();
        for (DecomposeTypeEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    DecomposeTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static DecomposeTypeEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(DecomposeTypeEnum.class, value);
        }
    }

}

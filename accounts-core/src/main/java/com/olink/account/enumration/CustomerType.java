package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2017/5/22.
 * 客户类型
 */
public enum CustomerType {

    business(1),

    customer(0);

    private Integer value;

    private static Map<Integer, CustomerType> map;

    static {
        map = new HashMap<>();
        for (CustomerType type : values()) {
            map.put(type.getValue(), type);
        }
    }

    CustomerType(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static CustomerType getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(CustomerType.class, value);
        }
    }

}

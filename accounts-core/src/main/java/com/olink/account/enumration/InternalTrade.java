package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.interfaces.IEnumValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/12/8.
 * 是否是内部特殊交易
 */
public enum InternalTrade implements IEnumValue {

    No("不是", 0),

    Yes("是", 1);

    private String name;

    private Integer value;

    private static Map<Integer, InternalTrade> map;

    static {
        map = new HashMap<>();
        for (InternalTrade type : values()) {
            map.put(type.getValue(), type);
        }
    }

    InternalTrade(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Boolean equals(Integer integer) {
        return this.value.equals(integer);
    }

    public static InternalTrade getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(InternalTrade.class, value);
        }
    }

}

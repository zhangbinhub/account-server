package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.interfaces.IEnumValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/11/24.
 * 内部交易类型
 */
public enum TradeTypeEnum implements IEnumValue {

    normal("普通交易", 0),

    recovery("冲正交易", 1);

    private String name;

    private Integer value;

    private static Map<Integer, TradeTypeEnum> map;

    static {
        map = new HashMap<>();
        for (TradeTypeEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    TradeTypeEnum(String name, Integer value) {
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

    public static TradeTypeEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(TradeTypeEnum.class, value);
        }
    }

}

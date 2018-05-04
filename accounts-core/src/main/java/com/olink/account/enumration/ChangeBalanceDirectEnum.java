package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/21.
 * 虚拟账户余额变动方向
 */
public enum ChangeBalanceDirectEnum {

    Plus(1),//加

    Minus(2);//减

    private Integer value;

    private static Map<Integer, ChangeBalanceDirectEnum> map;

    static {
        map = new HashMap<>();
        for (ChangeBalanceDirectEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    ChangeBalanceDirectEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static ChangeBalanceDirectEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(ChangeBalanceDirectEnum.class, value);
        }
    }
}

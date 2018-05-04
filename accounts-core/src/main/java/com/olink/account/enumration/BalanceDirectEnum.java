package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/20.
 * 会计科目余额方向
 */
public enum BalanceDirectEnum {

    debits(1),//借

    credits(2);//贷

    private Integer value;

    private static Map<Integer, BalanceDirectEnum> map;

    static {
        map = new HashMap<>();
        for (BalanceDirectEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    BalanceDirectEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static BalanceDirectEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(BalanceDirectEnum.class, value);
        }
    }

}

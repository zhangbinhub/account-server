package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/11/6.
 * 发生额分解规则类型
 */
public enum AmontRuleType {

    amont(1),//订单额

    actamont(2),//订单金额

    difference(3),//差额

    extamont1(4),//扩展额1

    extamont2(5),//扩展额2

    extamont3(6),//扩展额3

    extamont4(7),//扩展额4

    extamont5(8),//扩展额5

    extamont6(9),//扩展额6

    extamont7(10),//扩展额7

    extamont8(11),//扩展额8

    extamont9(12),//扩展额9

    extamont10(13);//扩展额10

    private Integer value;

    private static Map<Integer, AmontRuleType> map;

    static {
        map = new HashMap<>();
        for (AmontRuleType type : values()) {
            map.put(type.getValue(), type);
        }
    }

    AmontRuleType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static AmontRuleType getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(AmontRuleType.class, value);
        }
    }

}

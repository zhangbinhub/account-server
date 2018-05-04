package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/25.
 * 发生额计算方式
 */
public enum CalculateTypeEnum {

    quota(1),//定额

    ratio(2),//比例

    step(3),//阶梯

    fullCut(4),//满减

    balance(5),//余额

    orderAmont(6);//订单额

    private Integer value;

    private static Map<Integer, CalculateTypeEnum> map;

    static {
        map = new HashMap<>();
        for (CalculateTypeEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    CalculateTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static CalculateTypeEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(CalculateTypeEnum.class, value);
        }
    }

}

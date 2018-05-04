package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/10.
 * 订单审核结果
 */
public enum ReviewResultEnum {

    notpass(0),//不通过

    pass(1);//通过

    private Integer value;

    private static Map<Integer, ReviewResultEnum> map;

    static {
        map = new HashMap<>();
        for (ReviewResultEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    ReviewResultEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static ReviewResultEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(ReviewResultEnum.class, value);
        }
    }
}

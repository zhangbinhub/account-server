package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/12/15.
 * 积分参数
 */
public enum IntergralParamTypeEnum {

    /**
     * 积分价值比例
     */
    ratio(1);

    private Integer value;

    private static Map<Integer, IntergralParamTypeEnum> map;

    static {
        map = new HashMap<>();
        for (IntergralParamTypeEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    IntergralParamTypeEnum(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static IntergralParamTypeEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(IntergralParamTypeEnum.class, value);
        }
    }

}

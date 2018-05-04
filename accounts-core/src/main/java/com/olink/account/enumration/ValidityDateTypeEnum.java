package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/12/15.
 * 有效期类型
 */
public enum ValidityDateTypeEnum {

    /**
     * 会计日期
     */
    accountdate(0),

    /**
     * 系统日期
     */
    sysdate(1);

    private Integer value;

    private static Map<Integer, ValidityDateTypeEnum> map;

    static {
        map = new HashMap<>();
        for (ValidityDateTypeEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    ValidityDateTypeEnum(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static ValidityDateTypeEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(ValidityDateTypeEnum.class, value);
        }
    }

}

package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/8.
 * 是否逻辑删除
 */
public enum DeleteEnum {

    isDelete(1),

    notDelete(0);

    private Integer value;

    private static Map<Integer, DeleteEnum> map;

    static {
        map = new HashMap<>();
        for (DeleteEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    DeleteEnum(int value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static DeleteEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(DeleteEnum.class, value);
        }
    }
}

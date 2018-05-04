package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/12/29.
 * 批量任务执行方式
 */
public enum BatchTaskType {

    /**
     * 顺序执行
     */
    sequentially(1),

    /**
     * 独立执行
     */
    Independent(2);

    private Integer value;

    private static Map<Integer, BatchTaskType> map;

    static {
        map = new HashMap<>();
        for (BatchTaskType type : values()) {
            map.put(type.getValue(), type);
        }
    }

    BatchTaskType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static BatchTaskType getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(BatchTaskType.class, value);
        }
    }

}

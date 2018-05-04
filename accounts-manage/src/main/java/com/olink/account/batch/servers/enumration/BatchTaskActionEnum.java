package com.olink.account.batch.servers.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2017/1/5.
 * 批量任务接口列表
 */
public enum BatchTaskActionEnum {

    /**
     * 手动执行单个任务
     */
    batchtask_excute("batchtask_excute");

    private String value;

    private static Map<String, BatchTaskActionEnum> map;

    static {
        map = new HashMap<>();
        for (BatchTaskActionEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    BatchTaskActionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static BatchTaskActionEnum getEnum(String value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(BatchTaskActionEnum.class, value);
        }
    }

}

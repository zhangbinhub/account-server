package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/25.
 * 处理结果状态
 */
public enum ResultStatusEnum {

    success(1, "success"),

    failed(-1, "failed");

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    private Integer code;

    private String name;

    private static Map<Integer, ResultStatusEnum> map;

    static {
        map = new HashMap<>();
        for (ResultStatusEnum type : values()) {
            map.put(type.getCode(), type);
        }
    }

    ResultStatusEnum(Integer code, String name) {
        this.code = code;
        this.name = name.toLowerCase();
    }

    public Boolean equals(Integer code) {
        return this.code.equals(code);
    }

    public static ResultStatusEnum getEnum(Integer code) throws EnumValueUndefinedException {
        if (map.containsKey(code)) {
            return map.get(code);
        } else {
            throw new EnumValueUndefinedException(ResultStatusEnum.class, code);
        }
    }

}

package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.interfaces.IEnumValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/12/29.
 * 批处理任务状态
 */
public enum BatchTaskStatus implements IEnumValue {

    faild("失败", -1),

    begin("开始", 1),

    processing("处理中", 2),

    igone("忽略", 3),

    success("成功", 4);

    private String name;

    private Integer value;

    private static Map<Integer, BatchTaskStatus> map;

    static {
        map = new HashMap<>();
        for (BatchTaskStatus type : values()) {
            map.put(type.getValue(), type);
        }
    }

    BatchTaskStatus(String name, Integer value) {
        this.name = name;
        this.value = value;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static BatchTaskStatus getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(BatchTaskStatus.class, value);
        }
    }

}

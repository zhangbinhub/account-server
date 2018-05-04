package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/11/2.
 * 小数处理方式
 */
public enum DecimalProcessModeEnum {

    Up(1, BigDecimal.ROUND_UP),//入

    Down(2, BigDecimal.ROUND_DOWN),//舍

    Ceiling(3, BigDecimal.ROUND_CEILING),//正无穷大

    Floor(4, BigDecimal.ROUND_FLOOR),//负无穷大

    Half_UP(5, BigDecimal.ROUND_HALF_UP),//四舍五入

    Half_DOWN(6, BigDecimal.ROUND_HALF_DOWN),//五舍六入

    Half_EVEN(7, BigDecimal.ROUND_HALF_EVEN);//银行家舍入（四舍六入；五，左奇入）

    private Integer value;

    private Integer mode;

    private static Map<Integer, DecimalProcessModeEnum> map;

    static {
        map = new HashMap<>();
        for (DecimalProcessModeEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    DecimalProcessModeEnum(Integer value, Integer mode) {
        this.value = value;
        this.mode = mode;
    }

    public Integer getValue() {
        return value;
    }

    public Integer getMode() {
        return mode;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static DecimalProcessModeEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(DecimalProcessModeEnum.class, value);
        }
    }

}

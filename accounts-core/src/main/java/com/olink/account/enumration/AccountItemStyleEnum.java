package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/11/30.
 * 会计科目类型
 */
public enum AccountItemStyleEnum {

    assets(1),//资产类

    liabilities(2),//负债类

    common(3),//共同类

    equity(4),//所有者权益类

    cost(5),//成本类

    profitandloss(6);//损益类

    private Integer value;

    private static Map<Integer, AccountItemStyleEnum> map;

    static {
        map = new HashMap<>();
        for (AccountItemStyleEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    AccountItemStyleEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static AccountItemStyleEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(AccountItemStyleEnum.class, value);
        }
    }

}

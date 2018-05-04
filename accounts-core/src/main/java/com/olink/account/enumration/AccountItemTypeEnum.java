package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/11/30.
 * 会计科目类型
 */
public enum AccountItemTypeEnum {

    debits(1),//借

    credits(2),//贷

    common(3);//共同

    private Integer value;

    private static Map<Integer, AccountItemTypeEnum> map;

    static {
        map = new HashMap<>();
        for (AccountItemTypeEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    AccountItemTypeEnum(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static AccountItemTypeEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(AccountItemTypeEnum.class, value);
        }
    }

}

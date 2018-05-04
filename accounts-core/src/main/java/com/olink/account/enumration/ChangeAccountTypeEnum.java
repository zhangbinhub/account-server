package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/21.
 * 余额变动虚拟账户类型
 */
public enum ChangeAccountTypeEnum {

    Account(0, "账务平台B户"),

    Business(1, "B类账户"),

    DisBusiness(2, "目标B类账户"),

    Cust(3, "C类账户"),

    DisCust(4, "目标C类账户");

    private Integer value;

    private String descript;

    private static Map<Integer, ChangeAccountTypeEnum> map;

    static {
        map = new HashMap<>();
        for (ChangeAccountTypeEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    ChangeAccountTypeEnum(Integer value, String descript) {
        this.value = value;
        this.descript = descript;
    }

    public Integer getValue() {
        return value;
    }

    public String getDescript() {
        return descript;
    }

    public Boolean equals(Integer value) {
        return this.value.equals(value);
    }

    public static ChangeAccountTypeEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(ChangeAccountTypeEnum.class, value);
        }
    }

}

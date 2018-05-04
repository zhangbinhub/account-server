package com.olink.account.trade.servers.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/12/9.
 * 信息维护action
 */
public enum CustMngActionEnum {

    /**
     * C户注册
     */
    cust_register("cust_register"),

    /**
     * C户手机号变更
     */
    cust_telephone_modify("cust_telephone_modify"),

    /**
     * C户绑定信息
     */
    cust_bindinfo_add("cust_bindinfo_add"),

    /**
     * C户信息删除
     */
    cust_bindinfo_del("cust_bindinfo_del"),

    /**
     * 绑定信息设置为默认
     */
    cust_bindinfo_defualt("cust_bindinfo_default"),

    /**
     * C户设置支付密码 暂定余额支付使用
     */
    cust_pay_password_set("cust_pay_password_set"),

    /**
     * C户支付密码修改
     */
    cust_pay_password_modify("cust_pay_password_modify");

    private String value;

    private static Map<String, CustMngActionEnum> map;

    static {
        map = new HashMap<>();
        for (CustMngActionEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    CustMngActionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CustMngActionEnum getEnum(String value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(CustMngActionEnum.class, value);
        }
    }
}

package com.olink.account.trade.servers.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/9/5.
 * C户查询接口列表
 */
public enum CustActionEnum {

    /**
     * C户详细信息 通过custid查询
     */
    cust_desc_custid("cust_desc_custid"),

    /**
     * 通过C户电话号码获取C户信息
     */
    cust_desc_telephone("cust_desc_telephone"),

    /**
     * 通过C户客户号获取可用的C户绑定信息
     */
    cust_desc_bindinfo("cust_desc_bindinfo"),

    /**
     * 获取可用的C户绑定类型
     */
    cust_bindinfo_type("cust_bindinfo_type"),

    /**
     * 检测是否需要设置支付密码
     */
    cust_pay_password_check("cust_pay_password_check"),

    /**
     * 支付密码验证（正确性验证）
     */
    cust_pay_password_validate("cust_pay_password_validate"),

    /**
     * 获取可用的B户收款账号类型
     */
    cust_pay_business_bindinfo_type("cust_pay_business_bindinfo_type"),

    /**
     * 获取运营方收款账号信息
     */
    cust_pay_business_receipt_account("cust_pay_business_receipt_account");

    private String value;

    private static Map<String, CustActionEnum> map;

    static {
        map = new HashMap<>();
        for (CustActionEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    CustActionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CustActionEnum getEnum(String value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(CustActionEnum.class, value);
        }
    }
}

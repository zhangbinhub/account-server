package com.olink.account.trade.servers.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/7.
 * C户交易接口列表
 */
public enum CustPayActionEnum {

    /**
     * C户充值入账
     */
    cust_pay_recharge("cust_pay_recharge"),

    /**
     * C户支付到B户（申请）
     */
    cust_pay_applypayment("cust_pay_applypayment"),

    /**
     * C户支付到B户（确认）
     */
    cust_pay_payment("cust_pay_payment"),

    /**
     * C户支付到B户（取消）
     */
    cust_pay_canclepayment("cust_pay_canclepayment"),

    /**
     * C户退款（申请）
     */
    cust_pay_applyrefund("cust_pay_applyrefund"),

    /**
     * C户退款（确认）
     */
    cust_pay_refund("cust_pay_refund"),

    /**
     * C户退款（确认）
     */
    cust_pay_canclerefund("cust_pay_canclerefund"),

    /**
     * C户转账申请
     */
    cust_pay_applytransfer("cust_pay_applytransfer"),

    /**
     * C户转账确认
     */
    cust_pay_transfer("cust_pay_transfer"),

    /**
     * C户转账取消
     */
    cust_pay_cancletransfer("cust_pay_cancletransfer"),

    /**
     * C户余额提现申请
     */
    cust_pay_applycash("cust_pay_applycash"),

    /**
     * C户余额提现确认
     */
    back_cust_cash("back_cust_cash"),

    /**
     * C户余额提现取消
     */
    back_cust_canclecash("back_cust_canclecash");

    private String value;

    private static Map<String, CustPayActionEnum> map;

    static {
        map = new HashMap<>();
        for (CustPayActionEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    CustPayActionEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static CustPayActionEnum getEnum(String value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(CustPayActionEnum.class, value);
        }
    }
}

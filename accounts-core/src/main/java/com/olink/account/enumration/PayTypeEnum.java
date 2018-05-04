package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.interfaces.IEnumValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/9/12.
 * 支付（充值）类型
 */
public enum PayTypeEnum implements IEnumValue {

    alipay("alipay", 1, "支付宝"),

    alimobilepay("alimobilepay", 2, "支付宝移动支付"),

    alidirectpay("alidirectpay", 3, "支付宝即时到账"),

    aliwappay("aliwappay", 4, "支付宝wap支付"),

    alitradebybuyerpay("alitradebybuyerpay", 5, "支付宝担保交易"),

    tenpay("tenpay", 6, "财付通"),

    weixinpay("weixinpay", 7, "微信支付"),

    weixinapppay("weixinapppay", 8, "微信APP支付"),

    weixinJSAPIpay("weixinJSAPIpay", 9, "微信JSAPI支付"),

    scrcupay("scrcupay", 10, "农信支付"),

    balance("balance", 11, "余额支付"),

    hsh("hsh", 12, "惠支付"),

    otherspay("otherspay", 13, "其他方式");

    private String name;

    private Integer value;

    private String description;

    private static Map<String, PayTypeEnum> nameMap;

    private static Map<Integer, PayTypeEnum> valueMap;

    static {
        nameMap = new HashMap<>();
        valueMap = new HashMap<>();
        for (PayTypeEnum type : values()) {
            nameMap.put(type.getName(), type);
            valueMap.put(type.getValue(), type);
        }
    }

    PayTypeEnum(String name, Integer value, String description) {
        this.name = name.toLowerCase();
        this.value = value;
        this.description = description;
    }

    @Override
    public Integer getValue() {
        return this.value;
    }

    @Override
    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    @Override
    public Boolean equals(Integer integer) {
        return this.value.equals(integer);
    }

    public Boolean equals(String name) {
        return this.name.toLowerCase().equals(name.toLowerCase());
    }

    public PayTypeEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (valueMap.containsKey(value)) {
            return valueMap.get(value);
        } else {
            throw new EnumValueUndefinedException(PayTypeEnum.class, value);
        }
    }

    public static PayTypeEnum getEnum(String name) throws EnumValueUndefinedException {
        if (nameMap.containsKey(name.toLowerCase())) {
            return nameMap.get(name.toLowerCase());
        } else {
            throw new EnumValueUndefinedException(PayTypeEnum.class, name);
        }
    }
}

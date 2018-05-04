package com.olink.account.enumration;

import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.interfaces.IEnumValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/9/12.
 * 订单状态
 */
public enum OrderStatusEnum implements IEnumValue {

    other("未知订单状态", 0),

    create("订单创建", 10),

    paySuccess("支付完成", 11),

    reviewing("审核中", 12),

    success("订单完成", 13),

    failed("订单失败", 14),

    cancle("订单取消", 15),

    applyRefund("申请退款", 20),

    applyRefundSuccess("已申请退款", 21),

    refunding("退款中", 22),

    refundSuccess("已退款", 23),

    refundFail("退款失败", 24),

    refundCancle("退款取消", 25),

    applyAdjust("申请核算", 30),

    applyAdjustSuccess("申请核算成功", 31),

    adjusting("核算中", 32),

    adjustSuccess("已核算", 33),

    adjustFail("核算失败", 34),

    adjustCancle("核算取消", 35);

    private String name;

    private Integer value;

    private static Map<Integer, OrderStatusEnum> map;

    static {
        map = new HashMap<>();
        for (OrderStatusEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    OrderStatusEnum(String name, Integer value) {
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
    public Boolean equals(Integer integer) {
        return this.value.equals(integer);
    }

    public static OrderStatusEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(OrderStatusEnum.class, value);
        }
    }
}

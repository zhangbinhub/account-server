package com.olink.account.enumration;

import com.olink.account.model.trade.order.*;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.interfaces.IEnumValue;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/9/13.
 * 订单类型
 * 内部特殊交易不进行配置
 */
public enum OrderTypeEnum implements IEnumValue {

    BalancePay(BalancePayOrder.class, "余额支付订单", 101),

    BalanceRefund(BalanceRefundOrder.class, "余额退款订单", 102),

    BalanceRecharge(BalanceRechargeOrder.class, "余额充值订单", 103),

    BalanceTransfer(BalanceTransferOrder.class, "余额转账订单", 104),

    BalanceCash(BalanceCashOrder.class, "余额提现订单", 105),

    BalanceSettlement(BalanceSettlementOrder.class, "余额结算订单", 106),

    NonBalancePay(NonBalancePayOrder.class, "非余额支付订单", 201),

    NonBalanceRefund(NonBalanceRefundOrder.class, "非余额退款订单", 202),

    NonBalanceTransfer(NonBalanceTransferOrder.class, "非余额转账订单", 204),

    IntegralPay(IntegralPayOrder.class, "积分消费订单", 301),

    IntegralRefund(IntegralRefundOrder.class, "积分退款订单", 302),

    IntegralRecharge(IntegralRechargeOrder.class, "积分获取订单", 303),

    IntegralSettlement(IntegralSettlementOrder.class, "积分结算订单", 306),

    CustomCurrencyPay(CustomCurrencyPayOrder.class, "电子券消费订单", 401),

    CustomCurrencyRefund(CustomCurrencyRefundOrder.class, "电子券退款订单", 402),

    CustomCurrencyRecharge(CustomCurrencyRechargeOrder.class, "电子券入账订单", 403),

    ShopSettlement(ShopSettlementOrder.class, "二级商户结算订单", 506),

    NormalTrade(NormalTradeOrder.class, "B户通用记账交易订单", 8099);

    private Class<? extends OrderBase> cls;

    private String name;

    private Integer value;

    private static Map<Integer, OrderTypeEnum> map;

    static {
        map = new HashMap<>();
        for (OrderTypeEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    OrderTypeEnum(Class<? extends OrderBase> cls, String name, Integer value) {
        this.cls = cls;
        this.name = name;
        this.value = value;
    }

    public Class<? extends OrderBase> getOrderClass() {
        return this.cls;
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

    public static OrderTypeEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(OrderStatusEnum.class, value);
        }
    }

}

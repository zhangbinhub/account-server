package com.olink.account.trade.dao.impl;

import com.olink.account.base.impl.BaseTradeDao;
import com.olink.account.trade.dao.ICurrencyDao;
import com.olink.account.enumration.OrderStatusEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.order.CustomCurrencyPayOrder;
import com.olink.account.model.trade.order.CustomCurrencyRechargeOrder;
import com.olink.account.model.trade.order.CustomCurrencyRefundOrder;
import pers.acp.tools.common.DBConTools;

/**
 * Created by zhangbin on 2016/10/5.
 * 自有币种持久化
 */
public class CurrencyDao extends BaseTradeDao implements ICurrencyDao {

    public CurrencyDao() {
        super();
    }

    public CurrencyDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public boolean doApplyCurrencyRecharge(CustomCurrencyRechargeOrder customCurrencyRechargeOrder) throws ServerException {
        return customCurrencyRechargeOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(customCurrencyRechargeOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doCurrencyRecharge(CustomCurrencyRechargeOrder customCurrencyRechargeOrder) throws ServerException {
        customCurrencyRechargeOrder.setStatus(OrderStatusEnum.success);
        return customCurrencyRechargeOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleCurrencyRecharge(String orderNo) throws ServerException {
        CustomCurrencyRechargeOrder customCurrencyRechargeOrder = (CustomCurrencyRechargeOrder) CustomCurrencyRechargeOrder.doViewByLock(orderNo, CustomCurrencyRechargeOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(customCurrencyRechargeOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyCurrencyPayment(CustomCurrencyPayOrder customCurrencyPayOrder) throws ServerException {
        return customCurrencyPayOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(customCurrencyPayOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doCurrencyPayment(CustomCurrencyPayOrder customCurrencyPayOrder) throws ServerException {
        customCurrencyPayOrder.setStatus(OrderStatusEnum.success);
        return customCurrencyPayOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleCurrencyPayment(String orderNo) throws ServerException {
        CustomCurrencyPayOrder customCurrencyPayOrder = (CustomCurrencyPayOrder) CustomCurrencyPayOrder.doViewByLock(orderNo, CustomCurrencyPayOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(customCurrencyPayOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyCurrencyRefund(CustomCurrencyRefundOrder customCurrencyRefundOrder) throws ServerException {
        return customCurrencyRefundOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(customCurrencyRefundOrder, OrderStatusEnum.reviewing);
    }

    @Override
    public boolean doCurrencyRefund(CustomCurrencyRefundOrder customCurrencyRefundOrder) throws ServerException {
        customCurrencyRefundOrder.setStatus(OrderStatusEnum.success);
        return customCurrencyRefundOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleCurrencyRefund(String orderNo) throws ServerException {
        CustomCurrencyRefundOrder customCurrencyRefundOrder = (CustomCurrencyRefundOrder) CustomCurrencyRefundOrder.doViewByLock(orderNo, CustomCurrencyRefundOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(customCurrencyRefundOrder, OrderStatusEnum.cancle);
    }

}

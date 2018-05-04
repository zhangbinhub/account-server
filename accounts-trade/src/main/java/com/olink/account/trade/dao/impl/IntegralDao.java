package com.olink.account.trade.dao.impl;

import com.olink.account.base.impl.BaseTradeDao;
import com.olink.account.trade.dao.IIntegralDao;
import com.olink.account.enumration.OrderStatusEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.order.IntegralPayOrder;
import com.olink.account.model.trade.order.IntegralRechargeOrder;
import com.olink.account.model.trade.order.IntegralRefundOrder;
import com.olink.account.model.trade.order.IntegralSettlementOrder;
import pers.acp.tools.common.DBConTools;

/**
 * Created by zhangbin on 2016/10/7.
 * 积分持久化
 */
public class IntegralDao extends BaseTradeDao implements IIntegralDao {

    public IntegralDao() {
        super();
    }

    public IntegralDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public boolean doApplyIntegralRecharge(IntegralRechargeOrder integralRechargeOrder) throws ServerException {
        integralRechargeOrder.setRececustid(integralRechargeOrder.getCustid());
        return integralRechargeOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(integralRechargeOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doRecharge(IntegralRechargeOrder integralRechargeOrder) throws ServerException {
        integralRechargeOrder.setStatus(OrderStatusEnum.success);
        return integralRechargeOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleIntegralRecharge(String orderNo) throws ServerException {
        IntegralRechargeOrder integralRechargeOrder = (IntegralRechargeOrder) IntegralRechargeOrder.doViewByLock(orderNo, IntegralRechargeOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(integralRechargeOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyPayment(IntegralPayOrder integralPayOrder) throws ServerException {
        integralPayOrder.setRecebusinessid(integralPayOrder.getBusinessid());
        return integralPayOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(integralPayOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doPayment(IntegralPayOrder integralPayOrder) throws ServerException {
        integralPayOrder.setStatus(OrderStatusEnum.success);
        return integralPayOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCanclePayment(String orderNo) throws ServerException {
        IntegralPayOrder integralPayOrder = (IntegralPayOrder) IntegralPayOrder.doViewByLock(orderNo, IntegralPayOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(integralPayOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyRefund(IntegralRefundOrder integralRefundOrder) throws ServerException {
        integralRefundOrder.setRececustid(integralRefundOrder.getCustid());
        return integralRefundOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(integralRefundOrder, OrderStatusEnum.reviewing);
    }

    @Override
    public boolean doRefund(IntegralRefundOrder integralRefundOrder) throws ServerException {
        integralRefundOrder.setStatus(OrderStatusEnum.success);
        return integralRefundOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleRefund(String orderNo) throws ServerException {
        IntegralRefundOrder integralRefundOrder = (IntegralRefundOrder) IntegralRefundOrder.doViewByLock(orderNo, IntegralRefundOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(integralRefundOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyIntegralSettlement(IntegralSettlementOrder integralSettlementOrder) throws ServerException {
        integralSettlementOrder.setRecebusinessid(integralSettlementOrder.getBusinessid());
        return integralSettlementOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(integralSettlementOrder, OrderStatusEnum.reviewing);
    }

    @Override
    public boolean doIntegralSettlement(IntegralSettlementOrder integralSettlementOrder) throws ServerException {
        integralSettlementOrder.setStatus(OrderStatusEnum.success);
        return integralSettlementOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleIntegralSettlement(String orderNo, String userid) throws ServerException {
        IntegralSettlementOrder integralSettlementOrder = (IntegralSettlementOrder) IntegralSettlementOrder.doViewByLock(orderNo, IntegralSettlementOrder.class, null, dbTools.getDbcon());
        integralSettlementOrder.setUserid(userid);
        integralSettlementOrder.setStatus(OrderStatusEnum.cancle);
        return integralSettlementOrder.doUpdateOrder(dbTools.getDbcon());
    }

}

package com.olink.account.trade.dao.impl;

import com.olink.account.base.impl.BaseTradeDao;
import com.olink.account.model.trade.order.*;
import com.olink.account.trade.dao.IMoneyDao;
import com.olink.account.enumration.*;
import com.olink.account.exception.ServerException;
import pers.acp.tools.common.DBConTools;

/**
 * Created by Shepherd on 2016-08-25.
 * C户持久化
 */
public class MoneyDao extends BaseTradeDao implements IMoneyDao {

    public MoneyDao() {
        super();
    }

    public MoneyDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public boolean doApplyRecharge(BalanceRechargeOrder balanceRechargeOrder) throws ServerException {
        balanceRechargeOrder.setRececustid(balanceRechargeOrder.getCustid());
        return balanceRechargeOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(balanceRechargeOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doRecharge(BalanceRechargeOrder balanceRechargeOrder) throws ServerException {
        balanceRechargeOrder.setStatus(OrderStatusEnum.success);
        return balanceRechargeOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleRecharge(String orderNo) throws ServerException {
        BalanceRechargeOrder balanceRechargeOrder = (BalanceRechargeOrder) BalanceRechargeOrder.doViewByLock(orderNo, BalanceRechargeOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(balanceRechargeOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyBalancePay(BalancePayOrder balancePayOrder) throws ServerException {
        balancePayOrder.setRecebusinessid(balancePayOrder.getBusinessid());
        return balancePayOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(balancePayOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doBalancePay(BalancePayOrder balancePayOrder) throws ServerException {
        balancePayOrder.setStatus(OrderStatusEnum.success);
        return balancePayOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleBalancePay(String orderNo) throws ServerException {
        BalancePayOrder balancePayOrder = (BalancePayOrder) BalancePayOrder.doViewByLock(orderNo, BalancePayOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(balancePayOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyNonBalancePay(NonBalancePayOrder nonBalancePayOrder) throws ServerException {
        return nonBalancePayOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(nonBalancePayOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doNonBalancePay(NonBalancePayOrder nonBalancePayOrder) throws ServerException {
        nonBalancePayOrder.setStatus(OrderStatusEnum.success);
        return nonBalancePayOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleNonBalancePay(String orderNo) throws ServerException {
        NonBalancePayOrder nonBalancePayOrder = (NonBalancePayOrder) NonBalancePayOrder.doViewByLock(orderNo, NonBalancePayOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(nonBalancePayOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyBalancePayRefund(BalanceRefundOrder balanceRefundOrder) throws ServerException {
        balanceRefundOrder.setRececustid(balanceRefundOrder.getCustid());
        return balanceRefundOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(balanceRefundOrder, OrderStatusEnum.reviewing);
    }

    @Override
    public boolean doBalancePayRefund(BalanceRefundOrder balanceRefundOrder) throws ServerException {
        balanceRefundOrder.setStatus(OrderStatusEnum.success);
        return balanceRefundOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleBalancePayRefund(String orderNo) throws ServerException {
        BalanceRefundOrder balanceRefundOrder = (BalanceRefundOrder) BalanceRefundOrder.doViewByLock(orderNo, BalanceRefundOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(balanceRefundOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyNonBalancePayRefund(NonBalanceRefundOrder nonBalanceRefundOrder) throws ServerException {
        return nonBalanceRefundOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(nonBalanceRefundOrder, OrderStatusEnum.reviewing);
    }

    @Override
    public boolean doNonBalancePayRefund(NonBalanceRefundOrder nonBalanceRefundOrder) throws ServerException {
        nonBalanceRefundOrder.setStatus(OrderStatusEnum.success);
        return nonBalanceRefundOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleNonBalancePayRefund(String orderNo) throws ServerException {
        NonBalanceRefundOrder nonBalanceRefundOrder = (NonBalanceRefundOrder) NonBalanceRefundOrder.doViewByLock(orderNo, NonBalanceRefundOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(nonBalanceRefundOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyBalanceTransfer(BalanceTransferOrder balanceTransferOrder) throws ServerException {
        return balanceTransferOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(balanceTransferOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doApplyNonBalanceTransfer(NonBalanceTransferOrder nonBalanceTransferOrder) throws ServerException {
        return nonBalanceTransferOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(nonBalanceTransferOrder, OrderStatusEnum.paySuccess);
    }

    @Override
    public boolean doBalanceTransfer(BalanceTransferOrder balanceTransferOrder) throws ServerException {
        balanceTransferOrder.setStatus(OrderStatusEnum.success);
        return balanceTransferOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doNonBalanceTransfer(NonBalanceTransferOrder nonBalanceTransferOrder) throws ServerException {
        nonBalanceTransferOrder.setStatus(OrderStatusEnum.success);
        return nonBalanceTransferOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleBalanceTransfer(String orderNo) throws ServerException {
        BalanceTransferOrder balanceTransferOrder = (BalanceTransferOrder) BalanceTransferOrder.doViewByLock(orderNo, BalanceTransferOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(balanceTransferOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doCancleNonBalanceTransfer(String orderNo) throws ServerException {
        NonBalanceTransferOrder nonBalanceTransferOrder = (NonBalanceTransferOrder) NonBalanceTransferOrder.doViewByLock(orderNo, NonBalanceTransferOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(nonBalanceTransferOrder, OrderStatusEnum.cancle);
    }

    @Override
    public boolean doApplyBalanceCash(BalanceCashOrder balanceCashOrder) throws ServerException {
        return balanceCashOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(balanceCashOrder, OrderStatusEnum.reviewing);
    }

    @Override
    public boolean doBalanceCash(BalanceCashOrder balanceCashOrder) throws ServerException {
        balanceCashOrder.setStatus(OrderStatusEnum.success);
        return balanceCashOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleBalanceCash(String orderNo, String userid) throws ServerException {
        BalanceCashOrder balanceCashOrder = (BalanceCashOrder) BalanceCashOrder.doViewByLock(orderNo, BalanceCashOrder.class, null, dbTools.getDbcon());
        balanceCashOrder.setUserid(userid);
        balanceCashOrder.setStatus(OrderStatusEnum.cancle);
        return balanceCashOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doApplyBalanceSettlement(BalanceSettlementOrder balanceSettlementOrder) throws ServerException {
        balanceSettlementOrder.setRecebusinessid(balanceSettlementOrder.getBusinessid());
        return balanceSettlementOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(balanceSettlementOrder, OrderStatusEnum.reviewing);
    }

    @Override
    public boolean doBalanceSettlement(BalanceSettlementOrder balanceSettlementOrder) throws ServerException {
        balanceSettlementOrder.setStatus(OrderStatusEnum.success);
        return balanceSettlementOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleBalanceSettlement(String orderNo, String userid) throws ServerException {
        BalanceSettlementOrder balanceSettlementOrder = (BalanceSettlementOrder) BalanceSettlementOrder.doViewByLock(orderNo, BalanceSettlementOrder.class, null, dbTools.getDbcon());
        balanceSettlementOrder.setUserid(userid);
        balanceSettlementOrder.setStatus(OrderStatusEnum.cancle);
        return balanceSettlementOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doApplyShopSettlement(ShopSettlementOrder shopSettlementOrder) throws ServerException {
        return shopSettlementOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(shopSettlementOrder, OrderStatusEnum.reviewing);
    }

    @Override
    public boolean doShopSettlement(ShopSettlementOrder shopSettlementOrder) throws ServerException {
        shopSettlementOrder.setStatus(OrderStatusEnum.success);
        return shopSettlementOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleShopSettlement(String orderNo) throws ServerException {
        ShopSettlementOrder shopSettlementOrder = (ShopSettlementOrder) ShopSettlementOrder.doViewByLock(orderNo, ShopSettlementOrder.class, null, dbTools.getDbcon());
        shopSettlementOrder.setStatus(OrderStatusEnum.cancle);
        return shopSettlementOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doApplyNormalTrade(NormalTradeOrder normalTradeOrder) throws ServerException {
        return normalTradeOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(normalTradeOrder, OrderStatusEnum.reviewing);
    }

    @Override
    public boolean doNormalTrade(NormalTradeOrder normalTradeOrder) throws ServerException {
        normalTradeOrder.setStatus(OrderStatusEnum.success);
        return normalTradeOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleNormalTrade(String orderNo) throws ServerException {
        NormalTradeOrder normalTradeOrder = (NormalTradeOrder) NormalTradeOrder.doViewByLock(orderNo, NormalTradeOrder.class, null, dbTools.getDbcon());
        return doChangeOrderSatus(normalTradeOrder, OrderStatusEnum.cancle);
    }

}

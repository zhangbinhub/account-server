package com.olink.account.trade.dao.impl;

import com.olink.account.base.impl.BaseTradeDao;
import com.olink.account.trade.dao.IAccountDao;
import com.olink.account.enumration.OrderStatusEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.order.InternalTradeOrder;
import pers.acp.tools.common.DBConTools;

/**
 * Created by zhangbin on 2016/11/23.
 * 账务系统持久化
 */
public class AccountDao extends BaseTradeDao implements IAccountDao {

    public AccountDao() {
        super();
    }

    public AccountDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public boolean doApplyInternalTrade(InternalTradeOrder internalTradeOrder) throws ServerException {
        return internalTradeOrder.doCreateOrder(dbTools.getDbcon()) && doChangeOrderSatus(internalTradeOrder, OrderStatusEnum.reviewing);
    }

    @Override
    public boolean doInternalTrade(InternalTradeOrder internalTradeOrder) throws ServerException {
        internalTradeOrder.setStatus(OrderStatusEnum.success);
        return internalTradeOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doCancleInternalTrade(String orderNo, String userid) throws ServerException {
        InternalTradeOrder internalTradeOrder = (InternalTradeOrder) InternalTradeOrder.doViewByLock(orderNo, InternalTradeOrder.class, null, dbTools.getDbcon());
        internalTradeOrder.setUserid(userid);
        internalTradeOrder.setStatus(OrderStatusEnum.cancle);
        return internalTradeOrder.doUpdateOrder(dbTools.getDbcon());
    }

    @Override
    public boolean doUpdateInternalTrade(InternalTradeOrder internalTradeOrder) throws ServerException {
        return internalTradeOrder.doUpdateOrder(dbTools.getDbcon());
    }

}

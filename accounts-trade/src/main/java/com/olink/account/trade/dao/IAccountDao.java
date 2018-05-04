package com.olink.account.trade.dao;

import com.olink.account.base.IBaseTradeDao;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.order.InternalTradeOrder;

/**
 * Created by zhangbin on 2016/11/23.
 * 账务系统持久化接口
 */
public interface IAccountDao extends IBaseTradeDao {

    /**
     * 内部交易申请
     *
     * @param internalTradeOrder 内部交易订单
     * @return true|false
     */
    boolean doApplyInternalTrade(InternalTradeOrder internalTradeOrder) throws ServerException;

    /**
     * 内部交易
     *
     * @param internalTradeOrder 内部交易订单
     * @return true|false
     */
    boolean doInternalTrade(InternalTradeOrder internalTradeOrder) throws ServerException;

    /**
     * 内部交易取消
     *
     * @param orderNo 内部交易订单号
     * @return true|false
     */
    boolean doCancleInternalTrade(String orderNo, String userid) throws ServerException;

    /**
     * 内部交易审核
     *
     * @param internalTradeOrder 内部交易订单
     * @return true|false
     */
    boolean doUpdateInternalTrade(InternalTradeOrder internalTradeOrder) throws ServerException;

}

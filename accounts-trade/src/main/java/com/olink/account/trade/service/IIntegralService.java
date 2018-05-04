package com.olink.account.trade.service;

import com.olink.account.base.IBaseTradeService;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.order.IntegralPayOrder;
import com.olink.account.model.trade.order.IntegralRechargeOrder;
import com.olink.account.model.trade.order.IntegralRefundOrder;
import com.olink.account.model.trade.order.IntegralSettlementOrder;

/**
 * Created by zhangbin on 2016/10/7.
 * 积分服务
 */
public interface IIntegralService extends IBaseTradeService {

    /**
     * 申请入账
     *
     * @param integralRechargeOrder 积分入账订单
     * @return true|false
     */
    boolean doApplyRecharge(IntegralRechargeOrder integralRechargeOrder) throws ServerException;

    /**
     * 确认入账
     *
     * @param integralRechargeOrder 积分入账订单
     * @return true|false
     */
    boolean doRecharge(IntegralRechargeOrder integralRechargeOrder) throws ServerException;

    /**
     * 取消入账
     *
     * @param orderNo 积分入账订单号
     * @return true|false
     */
    boolean doCancleRecharge(String orderNo) throws ServerException;

    /**
     * 申请消费
     *
     * @param integralPayOrder 积分消费订单
     * @return true|false
     */
    boolean doApplyPayment(IntegralPayOrder integralPayOrder) throws ServerException;

    /**
     * 确认消费
     *
     * @param integralPayOrder 积分消费订单
     * @return true|false
     */
    boolean doPayment(IntegralPayOrder integralPayOrder) throws ServerException;

    /**
     * 取消消费
     *
     * @param orderNo 积分消费订单号
     * @return true|false
     */
    boolean doCanclePayment(String orderNo) throws ServerException;

    /**
     * 申请退款
     *
     * @param integralRefundOrder 退款订单
     * @return true|false
     */
    boolean doApplyRefund(IntegralRefundOrder integralRefundOrder) throws ServerException;

    /**
     * 确认退款
     *
     * @param integralRefundOrder 退款订单
     * @return true|false
     */
    boolean doRefund(IntegralRefundOrder integralRefundOrder) throws ServerException;

    /**
     * 取消退款
     *
     * @param orderNo 退款订单号
     * @return true|false
     */
    boolean doCancleRefund(String orderNo) throws ServerException;

    /**
     * B户积分结算申请
     *
     * @param integralSettlementOrder B户积分结算订单
     * @return true|false
     */
    boolean doApplyIntegralSettlement(IntegralSettlementOrder integralSettlementOrder) throws ServerException;

    /**
     * B户积分结算
     *
     * @param integralSettlementOrder B户积分结算订单
     * @return true|false
     */
    boolean doIntegralSettlement(IntegralSettlementOrder integralSettlementOrder) throws ServerException;

    /**
     * B户积分结算取消
     *
     * @param orderNo B户积分结算订单号
     * @param content 审核意见
     * @param userid  操作员id
     * @return true|false
     */
    boolean doCancleIntegralSettlement(String orderNo, String content, String userid) throws ServerException;

}

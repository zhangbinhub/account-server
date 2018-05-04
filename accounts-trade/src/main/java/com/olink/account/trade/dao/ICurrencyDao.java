package com.olink.account.trade.dao;

import com.olink.account.base.IBaseTradeDao;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.order.CustomCurrencyPayOrder;
import com.olink.account.model.trade.order.CustomCurrencyRechargeOrder;
import com.olink.account.model.trade.order.CustomCurrencyRefundOrder;

/**
 * Created by zhangbin on 2016/10/5.
 * 自有币种持久化
 */
public interface ICurrencyDao extends IBaseTradeDao {

    /**
     * 申请入账
     *
     * @param customCurrencyRechargeOrder 入账订单
     * @return true|false
     */
    boolean doApplyCurrencyRecharge(CustomCurrencyRechargeOrder customCurrencyRechargeOrder) throws ServerException;

    /**
     * 确认入账
     *
     * @param customCurrencyRechargeOrder 入账订单
     * @return true|false
     */
    boolean doCurrencyRecharge(CustomCurrencyRechargeOrder customCurrencyRechargeOrder) throws ServerException;

    /**
     * 取消入账
     *
     * @param orderNo 入账订单号
     * @return true|false
     */
    boolean doCancleCurrencyRecharge(String orderNo) throws ServerException;

    /**
     * 申请消费
     *
     * @param customCurrencyPayOrder 消费订单
     * @return true|false
     */
    boolean doApplyCurrencyPayment(CustomCurrencyPayOrder customCurrencyPayOrder) throws ServerException;

    /**
     * 确认消费
     *
     * @param customCurrencyPayOrder 消费订单
     * @return true|false
     */
    boolean doCurrencyPayment(CustomCurrencyPayOrder customCurrencyPayOrder) throws ServerException;

    /**
     * 消费取消
     *
     * @param orderNo 消费订单号
     * @return true|false
     */
    boolean doCancleCurrencyPayment(String orderNo) throws ServerException;

    /**
     * 申请退款
     *
     * @param customCurrencyRefundOrder 退款订单
     * @return true|false
     */
    boolean doApplyCurrencyRefund(CustomCurrencyRefundOrder customCurrencyRefundOrder) throws ServerException;

    /**
     * 确认退款
     *
     * @param customCurrencyRefundOrder 退款订单
     * @return true|false
     */
    boolean doCurrencyRefund(CustomCurrencyRefundOrder customCurrencyRefundOrder) throws ServerException;

    /**
     * 退款取消
     *
     * @param orderNo 退款订单号
     * @return true|false
     */
    boolean doCancleCurrencyRefund(String orderNo) throws ServerException;

}

package com.olink.account.trade.dao;

import com.olink.account.base.IBaseTradeDao;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.order.*;

/**
 * C户信息
 * Created by Shepherd on 2016-08-24.
 */
public interface IMoneyDao extends IBaseTradeDao {

    /**
     * C户充值申请
     *
     * @param balanceRechargeOrder 充值订单
     * @return true|false
     */
    boolean doApplyRecharge(BalanceRechargeOrder balanceRechargeOrder) throws ServerException;

    /**
     * C户充值
     *
     * @param balanceRechargeOrder 充值订单
     * @return 充值后可用余额
     */
    boolean doRecharge(BalanceRechargeOrder balanceRechargeOrder) throws ServerException;

    /**
     * C户充值取消
     *
     * @param orderNo 充值订单号
     * @return true|false
     */
    boolean doCancleRecharge(String orderNo) throws ServerException;

    /**
     * C户余额支付到B户（申请）
     *
     * @param balancePayOrder 余额支付订单
     * @return true|false
     */
    boolean doApplyBalancePay(BalancePayOrder balancePayOrder) throws ServerException;

    /**
     * C户余额支付到B户（确认）
     *
     * @param balancePayOrder 余额支付订单
     * @return true|false
     */
    boolean doBalancePay(BalancePayOrder balancePayOrder) throws ServerException;

    /**
     * C户余额支付到B户取消
     *
     * @param orderNo 余额支付订单号
     * @return true|false
     */
    boolean doCancleBalancePay(String orderNo) throws ServerException;

    /**
     * C户非余额支付到B户申请
     *
     * @param nonBalancePayOrder 非余额支付订单
     * @return true|false
     */
    boolean doApplyNonBalancePay(NonBalancePayOrder nonBalancePayOrder) throws ServerException;

    /**
     * C户非余额支付到B户
     *
     * @param nonBalancePayOrder 非余额支付订单
     * @return true|false
     */
    boolean doNonBalancePay(NonBalancePayOrder nonBalancePayOrder) throws ServerException;

    /**
     * C户非余额支付取消
     *
     * @param orderNo 非余额支付订单号
     * @return true|false
     */
    boolean doCancleNonBalancePay(String orderNo) throws ServerException;

    /**
     * C户余额退款申请
     *
     * @param balanceRefundOrder 余额退款订单
     * @return true|false
     */
    boolean doApplyBalancePayRefund(BalanceRefundOrder balanceRefundOrder) throws ServerException;

    /**
     * C户余额退款确认
     *
     * @param balanceRefundOrder 余额退款订单
     * @return true|false
     */
    boolean doBalancePayRefund(BalanceRefundOrder balanceRefundOrder) throws ServerException;

    /**
     * C户余额退款取消
     *
     * @param orderNo 余额退款订单号
     * @return true|false
     */
    boolean doCancleBalancePayRefund(String orderNo) throws ServerException;

    /**
     * C户非余额退款申请
     *
     * @param nonBalanceRefundOrder 非余额退款订单
     * @return true|false
     */
    boolean doApplyNonBalancePayRefund(NonBalanceRefundOrder nonBalanceRefundOrder) throws ServerException;

    /**
     * C户非余额退款
     *
     * @param nonBalanceRefundOrder 非余额退款订单
     * @return true|false
     */
    boolean doNonBalancePayRefund(NonBalanceRefundOrder nonBalanceRefundOrder) throws ServerException;

    /**
     * C户非余额退款取消
     *
     * @param orderNo 非余额退款订单号
     * @return true|false
     */
    boolean doCancleNonBalancePayRefund(String orderNo) throws ServerException;

    /**
     * 余额转账申请
     *
     * @param balanceTransferOrder 余额转账订单
     * @return true|false
     */
    boolean doApplyBalanceTransfer(BalanceTransferOrder balanceTransferOrder) throws ServerException;

    /**
     * 非余额转账申请
     *
     * @param nonBalanceTransferOrder 非余额转账订单
     * @return true|false
     */
    boolean doApplyNonBalanceTransfer(NonBalanceTransferOrder nonBalanceTransferOrder) throws ServerException;

    /**
     * 余额转账确认
     *
     * @param balanceTransferOrder 余额转账订单
     * @return true|false
     */
    boolean doBalanceTransfer(BalanceTransferOrder balanceTransferOrder) throws ServerException;

    /**
     * 非余额转账确认
     *
     * @param nonBalanceTransferOrder 非余额转账订单
     * @return true|false
     */
    boolean doNonBalanceTransfer(NonBalanceTransferOrder nonBalanceTransferOrder) throws ServerException;

    /**
     * 余额转账取消
     *
     * @param orderNo 余额转账订单号
     * @return true|false
     */
    boolean doCancleBalanceTransfer(String orderNo) throws ServerException;

    /**
     * 非余额转账取消
     *
     * @param orderNo 非余额转账订单号
     * @return true|false
     */
    boolean doCancleNonBalanceTransfer(String orderNo) throws ServerException;

    /**
     * 余额提现申请
     *
     * @param balanceCashOrder 余额提现订单
     * @return true|false
     */
    boolean doApplyBalanceCash(BalanceCashOrder balanceCashOrder) throws ServerException;

    /**
     * 余额提现确认
     *
     * @param balanceCashOrder 余额提现订单
     * @return true|false
     */
    boolean doBalanceCash(BalanceCashOrder balanceCashOrder) throws ServerException;

    /**
     * 余额提现取消
     *
     * @param orderNo 余额提现订单号
     * @param userid  操作员id
     * @return true|false
     */
    boolean doCancleBalanceCash(String orderNo, String userid) throws ServerException;

    /**
     * B户余额结算申请
     *
     * @param balanceSettlementOrder B户余额结算订单
     * @return true|false
     */
    boolean doApplyBalanceSettlement(BalanceSettlementOrder balanceSettlementOrder) throws ServerException;

    /**
     * B户余额结算
     *
     * @param balanceSettlementOrder B户余额结算订单
     * @return true|false
     */
    boolean doBalanceSettlement(BalanceSettlementOrder balanceSettlementOrder) throws ServerException;

    /**
     * B户余额结算取消
     *
     * @param orderNo B户余额结算订单号
     * @param userid  操作员id
     * @return true|false
     */
    boolean doCancleBalanceSettlement(String orderNo, String userid) throws ServerException;

    /**
     * 二级商户结算申请
     *
     * @param shopSettlementOrder 二级商户结算订单
     * @return true|false
     */
    boolean doApplyShopSettlement(ShopSettlementOrder shopSettlementOrder) throws ServerException;

    /**
     * 二级商户结算确认
     *
     * @param shopSettlementOrder 二级商户结算订单
     * @return true|false
     */
    boolean doShopSettlement(ShopSettlementOrder shopSettlementOrder) throws ServerException;

    /**
     * 二级商户结算取消
     *
     * @param orderNo 二级商户结算订单号
     * @return true|false
     */
    boolean doCancleShopSettlement(String orderNo) throws ServerException;

    /**
     * B户通用记账申请
     *
     * @param normalTradeOrder B户通用记账订单
     * @return true|false
     */
    boolean doApplyNormalTrade(NormalTradeOrder normalTradeOrder) throws ServerException;

    /**
     * B户通用记账确认
     *
     * @param normalTradeOrder B户通用记账订单
     * @return true|false
     */
    boolean doNormalTrade(NormalTradeOrder normalTradeOrder) throws ServerException;

    /**
     * B户通用记账取消
     *
     * @param orderNo B户通用记账订单号
     * @return true|false
     */
    boolean doCancleNormalTrade(String orderNo) throws ServerException;

}

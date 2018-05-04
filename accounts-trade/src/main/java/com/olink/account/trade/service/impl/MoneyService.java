package com.olink.account.trade.service.impl;

import com.olink.account.base.impl.BaseTradeService;
import com.olink.account.dao.*;
import com.olink.account.enumration.*;
import com.olink.account.exception.ServerException;
import com.olink.account.model.kernel.user.SysUser;
import com.olink.account.model.trade.customer.*;
import com.olink.account.model.trade.order.*;
import com.olink.account.trade.dao.IMoneyDao;
import com.olink.account.trade.dao.impl.MoneyDao;
import com.olink.account.trade.service.IMoneyService;
import com.olink.account.dao.impl.*;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

/**
 * Created by Shepherd on 2016-08-26.
 * C户信息服务
 */
public class MoneyService extends BaseTradeService implements IMoneyService {

    private IMoneyDao moneyDao = new MoneyDao();

    private ICustomerDao customerDao = new CustomerDao(moneyDao.getDBTools());

    @Override
    public boolean doApplyRecharge(BalanceRechargeOrder balanceRechargeOrder) throws ServerException {
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(balanceRechargeOrder.getPaytype());
            balanceRechargeOrder.setPaytypename(payTypeEnum.getDescription());
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + balanceRechargeOrder.getPaytype());
        }
        CustAccount custAccount = customerDao.findCustAccountByCustId(balanceRechargeOrder.getCustid());
        if (custAccount == null) {
            throw new ServerException("客户信息不存在：" + balanceRechargeOrder.getCustid());
        }
        IBusinessDao businessDao = new BusinessDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        BusinessAccount YYbusinessAccount = businessDao.findYYBusinessAccount();
        if (YYbusinessAccount != null) {
            balanceRechargeOrder.setBusinessid(YYbusinessAccount.getBusinessid());
            balanceRechargeOrder.setBusinessname(YYbusinessAccount.getBusinessname());
            balanceRechargeOrder.setBusinesssubaccountcode(YYbusinessAccount.getSubAccountByType(AccountTypeEnum.Change).getCode());
            balanceRechargeOrder.setCustsubaccountcode(custAccount.getSubAccountByType(AccountTypeEnum.Change).getCode());
            balanceRechargeOrder.setDescription("余额充值交易");
            if (moneyDao.doApplyRecharge(balanceRechargeOrder)) {
                moneyDao.commitTranslist();
                return true;
            } else {
                moneyDao.rollBackTranslist();
                return false;
            }
        } else {
            moneyDao.rollBackTranslist();
            throw new ServerException("充值失败：找不到账户平台账户");
        }
    }

    @Override
    public double[] doRecharge(BalanceRechargeOrder balanceRechargeOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        BalanceRechargeOrder rechargeOrder = (BalanceRechargeOrder) orderDao.findOrderByOrderNoLock(balanceRechargeOrder.getOrderNo(), BalanceRechargeOrder.class);
        if (rechargeOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到充值订单");
        }
        if (!rechargeOrder.getPaytype().equals(balanceRechargeOrder.getPaytype())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("支付类型不匹配");
        }
        CustAccount custAccount = customerDao.findCustAccountByCustId(balanceRechargeOrder.getCustid());
        if (custAccount == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("客户信息不存在：" + balanceRechargeOrder.getCustid());
        }
        double[] balances = new double[2];
        if (moneyDao.doRecharge(rechargeOrder)) {
            custAccount = customerDao.findCustAccountByCustId(rechargeOrder.getCustid());
            CustSubAccount custSubAccount = custAccount.getSubAccountByType(AccountTypeEnum.All);
            balances[0] = custSubAccount.getBalance();
            balances[1] = rechargeOrder.getAfterbalance();
            moneyDao.commitTranslist();
        } else {
            moneyDao.rollBackTranslist();
            moneyDao.doChangeOrderSatus(rechargeOrder, OrderStatusEnum.failed);
            balances = new double[0];
        }
        return balances;
    }

    @Override
    public boolean doCancleRecharge(String orderNo) throws ServerException {
        moneyDao.beginTranslist();
        if (moneyDao.doCancleRecharge(orderNo)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyBalancePay(BalancePayOrder balancePayOrder) throws ServerException {
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(balancePayOrder.getPaytype());
            balancePayOrder.setPaytypename(payTypeEnum.getDescription());
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + balancePayOrder.getPaytype());
        }
        moneyDao.beginTranslist();
        CustAccount custAccount = customerDao.findCustAccountByCustId(balancePayOrder.getCustid());
        if (custAccount == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("客户信息不存在：" + balancePayOrder.getCustid());
        }
        balancePayOrder.setCustsubaccountcode(custAccount.getSubAccountByType(AccountTypeEnum.Change).getCode());
        custAccount.setPassword(balancePayOrder.getPassword());
        if (!customerDao.doValidatePayPassword(custAccount)) {
            moneyDao.rollBackTranslist();
            throw new ServerException("支付密码不正确");
        }
        if (!balancePayOrder.doValidateOrder(moneyDao.getDBTools().getDbcon())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("支付失败：账户余额不足！");
        }
        if (moneyDao.doApplyBalancePay(balancePayOrder)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doBalancePay(BalancePayOrder balancePayOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        BalancePayOrder payOrder = (BalancePayOrder) orderDao.findOrderByOrderNoLock(balancePayOrder.getOrderNo(), BalancePayOrder.class);
        if (payOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到支付订单");
        }
        if (!payOrder.getPaytype().equals(balancePayOrder.getPaytype())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("支付类型不匹配");
        }
        CustAccount custAccount = customerDao.findCustAccountByCustId(payOrder.getCustid());
        if (custAccount == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("客户信息不存在：" + payOrder.getCustid());
        }
        custAccount.setPassword(balancePayOrder.getPassword());
        if (!customerDao.doValidatePayPassword(custAccount)) {
            moneyDao.rollBackTranslist();
            throw new ServerException("支付密码不正确");
        }
        if (moneyDao.doBalancePay(payOrder)) {
            moneyDao.commitTranslist();
            balancePayOrder.setAmont(payOrder.getAmont());
            balancePayOrder.setCustid(payOrder.getCustid());
            balancePayOrder.setLastmodifydate(payOrder.getLastmodifydate());
            return true;
        } else {
            moneyDao.rollBackTranslist();
            moneyDao.doChangeOrderSatus(payOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleBalancePay(String orderNo) throws ServerException {
        moneyDao.beginTranslist();
        if (moneyDao.doCancleBalancePay(orderNo)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyNonBalancePay(NonBalancePayOrder nonBalancePayOrder) throws ServerException {
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(nonBalancePayOrder.getPaytype());
            nonBalancePayOrder.setPaytypename(payTypeEnum.getDescription());
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + nonBalancePayOrder.getPaytype());
        }
        CustAccount custAccount = customerDao.findCustAccountByCustId(nonBalancePayOrder.getCustid());
        if (custAccount == null) {
            throw new ServerException("客户信息不存在：" + nonBalancePayOrder.getCustid());
        }
        moneyDao.beginTranslist();
        if (moneyDao.doApplyNonBalancePay(nonBalancePayOrder)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doNonBalancePay(NonBalancePayOrder nonBalancePayOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        NonBalancePayOrder payOrder = (NonBalancePayOrder) orderDao.findOrderByOrderNoLock(nonBalancePayOrder.getOrderNo(), NonBalancePayOrder.class);
        if (payOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到支付订单");
        }
        if (!payOrder.getPaytype().equals(nonBalancePayOrder.getPaytype())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("支付类型不匹配");
        }
        payOrder.setTradeno(nonBalancePayOrder.getTradeno());
        payOrder.setTradestatus(nonBalancePayOrder.getTradestatus());
        payOrder.setBuyerid(nonBalancePayOrder.getBuyerid());
        payOrder.setBuyername(nonBalancePayOrder.getBuyername());
        payOrder.setBuyeraccount(nonBalancePayOrder.getBuyeraccount());
        if (moneyDao.doNonBalancePay(payOrder)) {
            moneyDao.commitTranslist();
            nonBalancePayOrder.setCustid(payOrder.getCustid());
            nonBalancePayOrder.setAmont(payOrder.getAmont());
            nonBalancePayOrder.setLastmodifydate(payOrder.getLastmodifydate());
            return true;
        } else {
            moneyDao.rollBackTranslist();
            moneyDao.doChangeOrderSatus(payOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleNonBalancePay(String orderNo) throws ServerException {
        moneyDao.beginTranslist();
        if (moneyDao.doCancleNonBalancePay(orderNo)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyBalancePayRefund(BalanceRefundOrder balanceRefundOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        IBusinessDao businessDao = new BusinessDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        BalancePayOrder payOrder = (BalancePayOrder) orderDao.findOrderByOrderNoLock(balanceRefundOrder.getOrigorderno(), BalancePayOrder.class);
        if (payOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到原支付订单！");
        }
        if (!balanceRefundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("退款请求非法，没有操作权限！");
        }
        if (!validateRefundPayTypeAndStatus(payOrder, balanceRefundOrder, 0)) {
            moneyDao.rollBackTranslist();
            throw new ServerException("原订单不允许进行退款操作！");
        }
        BusinessAccount businessAccount = businessDao.findBusinessAccountByBusinessId(payOrder.getBusinessid());
        CustAccount custAccount = customerDao.findCustAccountByCustId(payOrder.getCustid());
        if (custAccount == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("客户信息不存在：" + payOrder.getCustid());
        }
        if (!balanceRefundOrder.doValidateOrder(moneyDao.getDBTools().getDbcon())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("退款金额超过原订单剩余可退款额");
        }
        balanceRefundOrder.setBusinessid(businessAccount.getBusinessid());
        balanceRefundOrder.setBusinessname(businessAccount.getBusinessname());
        balanceRefundOrder.setBusinesssubaccountcode(businessAccount.getSubAccountByType(AccountTypeEnum.Change).getCode());
        balanceRefundOrder.setChannel(businessAccount.getChannel());
        balanceRefundOrder.setCustid(custAccount.getCustid());
        balanceRefundOrder.setCustsubaccountcode(custAccount.getSubAccountByType(AccountTypeEnum.Change).getCode());
        balanceRefundOrder.setDescription("余额订单：" + balanceRefundOrder.getOrigorderno() + " 退款");
        if (moneyDao.doApplyBalancePayRefund(balanceRefundOrder)) {
            moneyDao.commitTranslist();
            balanceRefundOrder.setSuramont(payOrder.getAmont() - (payOrder.getRefundamont() + balanceRefundOrder.getAmont()));
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doBalancePayRefund(BalanceRefundOrder balanceRefundOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        BalanceRefundOrder refundOrder = (BalanceRefundOrder) orderDao.findOrderByOrderNoLock(balanceRefundOrder.getOrderNo(), BalanceRefundOrder.class);
        if (refundOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到退款支付订单！");
        }
        BalancePayOrder payOrder = (BalancePayOrder) orderDao.findOrderByOrderNoLock(refundOrder.getOrigorderno(), BalancePayOrder.class);
        if (payOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到原支付订单！");
        }
        if (!refundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("退款请求非法，没有操作权限！");
        }
        if (!validateRefundPayTypeAndStatus(payOrder, refundOrder, 1)) {
            moneyDao.rollBackTranslist();
            throw new ServerException("原订单不允许进行退款操作！");
        }
        CustAccount custAccount = customerDao.findCustAccountByCustId(payOrder.getCustid());
        if (custAccount == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("客户信息不存在：" + payOrder.getCustid());
        }
        if (moneyDao.doBalancePayRefund(refundOrder)) {
            moneyDao.commitTranslist();
            balanceRefundOrder.setCustid(refundOrder.getCustid());
            balanceRefundOrder.setAmont(refundOrder.getAmont());
            balanceRefundOrder.setBeforebalance(refundOrder.getBeforebalance());
            balanceRefundOrder.setAfterbalance(refundOrder.getAfterbalance());
            balanceRefundOrder.setLastmodifydate(refundOrder.getLastmodifydate());
            balanceRefundOrder.setSuramont(payOrder.getAmont() - payOrder.getRefundamont());
            return true;
        } else {
            moneyDao.rollBackTranslist();
            moneyDao.doChangeOrderSatus(refundOrder, OrderStatusEnum.failed);
            throw new ServerException("退款失败");
        }
    }

    @Override
    public boolean doCancleBalancePayRefund(String orderNo) throws ServerException {
        moneyDao.beginTranslist();
        if (moneyDao.doCancleBalancePayRefund(orderNo)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyNonBalancePayRefund(NonBalanceRefundOrder nonBalanceRefundOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        IBusinessDao businessDao = new BusinessDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        NonBalancePayOrder payOrder = (NonBalancePayOrder) orderDao.findOrderByOrderNoLock(nonBalanceRefundOrder.getOrigorderno(), NonBalancePayOrder.class);
        if (payOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到原支付订单！");
        }
        if (!payOrder.getTradeno().equals(nonBalanceRefundOrder.getOrigtradeno())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("原订单第三方交易号不匹配！");
        }
        if (!nonBalanceRefundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("退款请求非法，没有操作权限！");
        }
        if (!validateRefundPayTypeAndStatus(payOrder, nonBalanceRefundOrder, 0)) {
            moneyDao.rollBackTranslist();
            throw new ServerException("原订单不允许进行退款操作！");
        }
        BusinessAccount businessAccount = businessDao.findBusinessAccountByBusinessId(payOrder.getBusinessid());
        CustAccount custAccount = customerDao.findCustAccountByCustId(payOrder.getCustid());
        if (custAccount == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("客户信息不存在：" + payOrder.getCustid());
        }
        if (!nonBalanceRefundOrder.doValidateOrder(moneyDao.getDBTools().getDbcon())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("退款金额超过原订单剩余可退款额");
        }
        nonBalanceRefundOrder.setBusinessid(businessAccount.getBusinessid());
        nonBalanceRefundOrder.setBusinessname(businessAccount.getBusinessname());
        nonBalanceRefundOrder.setBusinesssubaccountcode(businessAccount.getSubAccountByType(AccountTypeEnum.Change).getCode());
        nonBalanceRefundOrder.setChannel(businessAccount.getChannel());
        nonBalanceRefundOrder.setCustid(custAccount.getCustid());
        nonBalanceRefundOrder.setCustsubaccountcode(custAccount.getSubAccountByType(AccountTypeEnum.Change).getCode());
        nonBalanceRefundOrder.setDescription("非余额订单：" + nonBalanceRefundOrder.getOrigorderno() + " 退款");
        if (moneyDao.doApplyNonBalancePayRefund(nonBalanceRefundOrder)) {
            moneyDao.commitTranslist();
            nonBalanceRefundOrder.setSuramont(payOrder.getAmont() - (payOrder.getRefundamont() + nonBalanceRefundOrder.getAmont()));
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doNonBalancePayRefund(NonBalanceRefundOrder nonBalanceRefundOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        NonBalanceRefundOrder refundOrder = (NonBalanceRefundOrder) orderDao.findOrderByOrderNoLock(nonBalanceRefundOrder.getOrderNo(), NonBalanceRefundOrder.class);
        if (refundOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到退款支付订单！");
        }
        refundOrder.setTradeno(nonBalanceRefundOrder.getTradeno());
        refundOrder.setTradestatus(nonBalanceRefundOrder.getTradestatus());
        refundOrder.setBuyerid(nonBalanceRefundOrder.getBuyerid());
        refundOrder.setBuyername(nonBalanceRefundOrder.getBuyername());
        refundOrder.setBuyeraccount(nonBalanceRefundOrder.getBuyeraccount());
        NonBalancePayOrder payOrder = (NonBalancePayOrder) orderDao.findOrderByOrderNoLock(refundOrder.getOrigorderno(), NonBalancePayOrder.class);
        if (payOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到原支付订单！");
        }
        if (!refundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("退款请求非法，没有操作权限！");
        }
        if (!validateRefundPayTypeAndStatus(payOrder, refundOrder, 1)) {
            moneyDao.rollBackTranslist();
            throw new ServerException("原订单不允许进行退款操作！");
        }
        if (moneyDao.doNonBalancePayRefund(refundOrder)) {
            moneyDao.commitTranslist();
            nonBalanceRefundOrder.setCustid(refundOrder.getCustid());
            nonBalanceRefundOrder.setAmont(refundOrder.getAmont());
            nonBalanceRefundOrder.setLastmodifydate(refundOrder.getLastmodifydate());
            nonBalanceRefundOrder.setSuramont(payOrder.getAmont() - (payOrder.getRefundamont()));
            return true;
        } else {
            moneyDao.rollBackTranslist();
            moneyDao.doChangeOrderSatus(refundOrder, OrderStatusEnum.failed);
            throw new ServerException("退款失败");
        }
    }

    @Override
    public boolean doCancleNonBalancePayRefund(String orderNo) throws ServerException {
        moneyDao.beginTranslist();
        if (moneyDao.doCancleNonBalancePayRefund(orderNo)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyBalanceTransfer(BalanceTransferOrder balanceTransferOrder) throws ServerException {
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(balanceTransferOrder.getPaytype());
            balanceTransferOrder.setPaytypename(payTypeEnum.getDescription());
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + balanceTransferOrder.getPaytype());
        }
        moneyDao.beginTranslist();
        if (balanceTransferOrder.getCustid().equals(balanceTransferOrder.getRececustid())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("收款客户号与付款客户号相同：" + balanceTransferOrder.getCustid());
        }
        CustAccount custAccount = customerDao.findCustAccountByCustId(balanceTransferOrder.getCustid());
        if (custAccount == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("客户信息不存在：" + balanceTransferOrder.getCustid());
        }
        CustAccount receCustAccount = customerDao.findCustAccountByCustId(balanceTransferOrder.getRececustid());
        if (receCustAccount == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("收款客户信息不存在：" + balanceTransferOrder.getRececustid());
        }
        balanceTransferOrder.setCustsubaccountcode(custAccount.getSubAccountByType(AccountTypeEnum.Change).getCode());
        custAccount.setPassword(balanceTransferOrder.getPassword());
        if (!customerDao.doValidatePayPassword(custAccount)) {
            moneyDao.rollBackTranslist();
            throw new ServerException("支付密码不正确");
        }
        if (!balanceTransferOrder.doValidateOrder(moneyDao.getDBTools().getDbcon())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("支付失败：账户余额不足！");
        }
        if (moneyDao.doApplyBalanceTransfer(balanceTransferOrder)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyNonBalanceTransfer(NonBalanceTransferOrder nonBalanceTransferOrder) throws ServerException {
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(nonBalanceTransferOrder.getPaytype());
            nonBalanceTransferOrder.setPaytypename(payTypeEnum.getDescription());
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + nonBalanceTransferOrder.getPaytype());
        }
        CustAccount custAccount = customerDao.findCustAccountByCustId(nonBalanceTransferOrder.getCustid());
        if (custAccount == null) {
            throw new ServerException("客户信息不存在：" + nonBalanceTransferOrder.getCustid());
        }
        moneyDao.beginTranslist();
        if (moneyDao.doApplyNonBalanceTransfer(nonBalanceTransferOrder)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doBalanceTransfer(BalanceTransferOrder balanceTransferOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        BalanceTransferOrder transferOrder = (BalanceTransferOrder) orderDao.findOrderByOrderNoLock(balanceTransferOrder.getOrderNo(), BalanceTransferOrder.class);
        if (transferOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到转账订单");
        }
        if (!transferOrder.getPaytype().equals(balanceTransferOrder.getPaytype())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("支付类型不匹配");
        }
        CustAccount custAccount = customerDao.findCustAccountByCustId(transferOrder.getCustid());
        if (custAccount == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("客户信息不存在：" + transferOrder.getCustid());
        }
        CustAccount receCustAccount = customerDao.findCustAccountByCustId(transferOrder.getRececustid());
        if (receCustAccount == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("收款客户信息不存在：" + transferOrder.getRececustid());
        }
        if (moneyDao.doBalanceTransfer(transferOrder)) {
            moneyDao.commitTranslist();
            balanceTransferOrder.setCustid(transferOrder.getCustid());
            balanceTransferOrder.setRececustid(transferOrder.getRececustid());
            balanceTransferOrder.setTransferdate(transferOrder.getTransferdate());
            balanceTransferOrder.setAmont(transferOrder.getAmont());
            balanceTransferOrder.setLastmodifydate(transferOrder.getLastmodifydate());
            return true;
        } else {
            moneyDao.rollBackTranslist();
            moneyDao.doChangeOrderSatus(transferOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doNonBalanceTransfer(NonBalanceTransferOrder nonBalanceTransferOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        NonBalanceTransferOrder transferOrder = (NonBalanceTransferOrder) orderDao.findOrderByOrderNoLock(nonBalanceTransferOrder.getOrderNo(), NonBalanceTransferOrder.class);
        if (transferOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到支付订单");
        }
        if (!transferOrder.getPaytype().equals(nonBalanceTransferOrder.getPaytype())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("支付类型不匹配");
        }
        transferOrder.setTradeno(nonBalanceTransferOrder.getTradeno());
        transferOrder.setTradestatus(nonBalanceTransferOrder.getTradestatus());
        transferOrder.setBuyerid(nonBalanceTransferOrder.getBuyerid());
        transferOrder.setBuyername(nonBalanceTransferOrder.getBuyername());
        transferOrder.setBuyeraccount(nonBalanceTransferOrder.getBuyeraccount());
        if (moneyDao.doNonBalanceTransfer(transferOrder)) {
            moneyDao.commitTranslist();
            nonBalanceTransferOrder.setAmont(transferOrder.getAmont());
            nonBalanceTransferOrder.setCustid(transferOrder.getCustid());
            nonBalanceTransferOrder.setTransferdate(transferOrder.getTransferdate());
            nonBalanceTransferOrder.setLastmodifydate(transferOrder.getLastmodifydate());
            return true;
        } else {
            moneyDao.rollBackTranslist();
            moneyDao.doChangeOrderSatus(transferOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleBalanceTransfer(String orderNo) throws ServerException {
        moneyDao.beginTranslist();
        if (moneyDao.doCancleBalanceTransfer(orderNo)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doCancleNonBalanceTransfer(String orderNo) throws ServerException {
        moneyDao.beginTranslist();
        if (moneyDao.doCancleNonBalanceTransfer(orderNo)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyBalanceCash(BalanceCashOrder balanceCashOrder) throws ServerException {
        IBusinessDao businessDao = new BusinessDao(moneyDao.getDBTools());
        CustAccount custAccount = customerDao.findCustAccountByCustId(balanceCashOrder.getCustid());
        if (custAccount == null) {
            throw new ServerException("客户信息不存在：" + balanceCashOrder.getCustid());
        }
        CustBindInfo custBindInfo = customerDao.findCustBindInfoById(balanceCashOrder.getBindinfoid());
        if (custBindInfo == null) {
            throw new ServerException("找不到提现账户：" + balanceCashOrder.getBindinfoid());
        } else {
            if (!(custBindInfo.getIsdel() == DeleteEnum.notDelete.getValue() && custBindInfo.getStatus() == StatusEnum.activate.getValue())) {
                throw new ServerException("提现账户不可用：" + custBindInfo.getAccount());
            }
        }
        moneyDao.beginTranslist();
        custAccount.setPassword(balanceCashOrder.getPassword());
        if (!customerDao.doValidatePayPassword(custAccount)) {
            moneyDao.rollBackTranslist();
            throw new ServerException("支付密码不正确");
        }
        if (!balanceCashOrder.doValidateOrder(moneyDao.getDBTools().getDbcon())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("提现申请失败：账户余额不足！");
        }
        BusinessAccount YYbusinessAccount = businessDao.findYYBusinessAccount();
        if (YYbusinessAccount != null) {
            balanceCashOrder.setBusinessid(YYbusinessAccount.getBusinessid());
            balanceCashOrder.setBusinessname(YYbusinessAccount.getBusinessname());
            balanceCashOrder.setBusinesssubaccountcode(YYbusinessAccount.getSubAccountByType(AccountTypeEnum.Change).getCode());
            balanceCashOrder.setCustsubaccountcode(custAccount.getSubAccountByType(AccountTypeEnum.Change).getCode());
            balanceCashOrder.setPaytype(PayTypeEnum.otherspay.getName());
            balanceCashOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
            balanceCashOrder.setCbindtype(custBindInfo.getType());
            balanceCashOrder.setBuyerbankname(custBindInfo.getBank());
            balanceCashOrder.setBuyername(custBindInfo.getAccountname());
            balanceCashOrder.setBuyeraccount(custBindInfo.getAccount());
            balanceCashOrder.setDescription("余额提现交易");
            if (moneyDao.doApplyBalanceCash(balanceCashOrder)) {
                moneyDao.commitTranslist();
                return true;
            } else {
                moneyDao.rollBackTranslist();
                return false;
            }
        } else {
            moneyDao.rollBackTranslist();
            throw new ServerException("提现申请失败：找不到账户平台账户");
        }
    }

    @Override
    public boolean doBalanceCash(BalanceCashOrder balanceCashOrder) throws ServerException {
        ISystemDao sysUserDao = new SystemDao();
        SysUser sysUser = sysUserDao.findUserById(balanceCashOrder.getUserid());
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        BalanceCashOrder cashOrder = (BalanceCashOrder) orderDao.findOrderByOrderNoLock(balanceCashOrder.getOrderNo(), BalanceCashOrder.class);
        if (cashOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到提现订单");
        }
        cashOrder.setUserid(sysUser.getId());
        CustAccount custAccount = customerDao.findCustAccountByCustId(cashOrder.getCustid());
        if (custAccount == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("客户信息不存在：" + cashOrder.getCustid());
        }
        if (orderDao.doOrderReview(cashOrder.getOrderNo(), "审核通过", ReviewResultEnum.pass, sysUser.getId(), sysUser.getName()) && moneyDao.doBalanceCash(cashOrder)) {
            moneyDao.commitTranslist();
            balanceCashOrder.setLastmodifydate(cashOrder.getLastmodifydate());
            balanceCashOrder.setCustid(cashOrder.getCustid());
            balanceCashOrder.setAmont(cashOrder.getAmont());
            return true;
        } else {
            moneyDao.rollBackTranslist();
            moneyDao.doChangeOrderSatus(cashOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleBalanceCash(String orderNo, String content, String userid) throws ServerException {
        ISystemDao sysUserDao = new SystemDao();
        SysUser sysUser = sysUserDao.findUserById(userid);
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        if (orderDao.doOrderReview(orderNo, content, ReviewResultEnum.notpass, sysUser.getId(), sysUser.getName()) && moneyDao.doCancleBalanceCash(orderNo, userid)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyBalanceSettlement(BalanceSettlementOrder balanceSettlementOrder) throws ServerException {
        IBusinessDao businessDao = new BusinessDao(moneyDao.getDBTools());
        BusinessAccount businessAccount = businessDao.findBusinessAccountByBusinessId(balanceSettlementOrder.getBusinessid());
        if (businessAccount == null) {
            throw new ServerException("找不到待结算的B账户：" + balanceSettlementOrder.getBusinessid());
        }
        BusinessBindInfo businessBindInfo = businessDao.findBusinessBindInfoById(balanceSettlementOrder.getBindinfoid());
        if (businessBindInfo == null) {
            throw new ServerException("找不到结算账户：" + balanceSettlementOrder.getBindinfoid());
        } else {
            if (businessBindInfo.getStatus() != StatusEnum.activate.getValue()) {
                throw new ServerException("结算账户不可用：" + businessBindInfo.getId());
            }
        }
        moneyDao.beginTranslist();
        if (!balanceSettlementOrder.doValidateOrder(moneyDao.getDBTools().getDbcon())) {
            moneyDao.rollBackTranslist();
            throw new ServerException("结算申请失败：账户余额不足！");
        }
        BusinessAccount YYbusinessAccount = businessDao.findYYBusinessAccount();
        if (YYbusinessAccount != null) {
            balanceSettlementOrder.setBusinessname(businessAccount.getBusinessname());
            balanceSettlementOrder.setBusinesstradeno("");
            balanceSettlementOrder.setBusinesssubaccountcode(businessAccount.getSubAccountByType(AccountTypeEnum.Change).getCode());
            balanceSettlementOrder.setCustid("");
            balanceSettlementOrder.setPaytype(PayTypeEnum.otherspay.getName());
            balanceSettlementOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
            balanceSettlementOrder.setChannel(YYbusinessAccount.getChannel());
            balanceSettlementOrder.setBbindtype(businessBindInfo.getType());
            try {
                switch (BBindTypeEnum.getEnum(businessBindInfo.getType())) {
                    case AliPay:
                        balanceSettlementOrder.setBuyername(businessBindInfo.getPartner());
                        balanceSettlementOrder.setBuyeraccount(businessBindInfo.getSeller_email());
                        break;
                    case WeiXinPublic:
                    case WeiXinOpen:
                        balanceSettlementOrder.setBuyername(businessBindInfo.getPartner());
                        balanceSettlementOrder.setBuyeraccount(businessBindInfo.getPartner());
                        break;
                    case BankCard:
                        balanceSettlementOrder.setBuyerbankname(businessBindInfo.getBankname());
                        balanceSettlementOrder.setBuyername(businessBindInfo.getName());
                        balanceSettlementOrder.setBuyeraccount(businessBindInfo.getAccount());
                        break;
                    default:
                        throw new ServerException("结算申请失败：结算账户类型非法！");
                }
            } catch (EnumValueUndefinedException e) {
                throw new ServerException("结算申请失败：结算账户类型非法！");
            }
            if (moneyDao.doApplyBalanceSettlement(balanceSettlementOrder)) {
                moneyDao.commitTranslist();
                return true;
            } else {
                moneyDao.rollBackTranslist();
                return false;
            }
        } else {
            moneyDao.rollBackTranslist();
            throw new ServerException("结算申请失败：找不到账户平台账户");
        }
    }

    @Override
    public boolean doBalanceSettlement(BalanceSettlementOrder balanceSettlementOrder) throws ServerException {
        ISystemDao sysUserDao = new SystemDao();
        IBusinessDao businessDao = new BusinessDao(moneyDao.getDBTools());
        SysUser sysUser = sysUserDao.findUserById(balanceSettlementOrder.getUserid());
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        BalanceSettlementOrder settlementOrder = (BalanceSettlementOrder) orderDao.findOrderByOrderNoLock(balanceSettlementOrder.getOrderNo(), BalanceSettlementOrder.class);
        if (settlementOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到余额结算订单");
        }
        settlementOrder.setUserid(sysUser.getId());
        BusinessAccount businessAccount = businessDao.findBusinessAccountByBusinessId(settlementOrder.getBusinessid());
        if (businessAccount == null) {
            throw new ServerException("找不到待结算的B账户：" + settlementOrder.getBusinessid());
        }
        if (orderDao.doOrderReview(settlementOrder.getOrderNo(), "审核通过", ReviewResultEnum.pass, sysUser.getId(), sysUser.getName()) && moneyDao.doBalanceSettlement(settlementOrder)) {
            moneyDao.commitTranslist();
            balanceSettlementOrder.setLastmodifydate(settlementOrder.getLastmodifydate());
            balanceSettlementOrder.setBusinessid(settlementOrder.getBusinessid());
            balanceSettlementOrder.setAmont(settlementOrder.getAmont());
            return true;
        } else {
            moneyDao.rollBackTranslist();
            moneyDao.doChangeOrderSatus(settlementOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleBalanceSettlement(String orderNo, String content, String userid) throws ServerException {
        ISystemDao sysUserDao = new SystemDao();
        SysUser sysUser = sysUserDao.findUserById(userid);
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        if (orderDao.doOrderReview(orderNo, content, ReviewResultEnum.notpass, sysUser.getId(), sysUser.getName()) && moneyDao.doCancleBalanceSettlement(orderNo, userid)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyShopSettlement(ShopSettlementOrder shopSettlementOrder) throws ServerException {
        shopSettlementOrder.setPaytype(PayTypeEnum.otherspay.getName());
        shopSettlementOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        shopSettlementOrder.setCustid("");
        moneyDao.beginTranslist();
        if (moneyDao.doApplyShopSettlement(shopSettlementOrder)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doShopSettlement(ShopSettlementOrder shopSettlementOrder) throws ServerException {
        IBusinessDao businessDao = new BusinessDao(moneyDao.getDBTools());
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        ShopSettlementOrder settlementOrder = (ShopSettlementOrder) orderDao.findOrderByOrderNoLock(shopSettlementOrder.getOrderNo(), ShopSettlementOrder.class);
        if (settlementOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到二级商户结算订单");
        }
        BusinessAccount businessAccount = businessDao.findBusinessAccountByBusinessId(settlementOrder.getBusinessid());
        if (businessAccount == null) {
            throw new ServerException("找不到对应B账户：" + settlementOrder.getBusinessid());
        }
        if (moneyDao.doShopSettlement(settlementOrder)) {
            moneyDao.commitTranslist();
            shopSettlementOrder.setLastmodifydate(settlementOrder.getLastmodifydate());
            shopSettlementOrder.setShopno(settlementOrder.getShopno());
            shopSettlementOrder.setAmont(settlementOrder.getAmont());
            shopSettlementOrder.setRate(settlementOrder.getRate());
            return true;
        } else {
            moneyDao.rollBackTranslist();
            moneyDao.doChangeOrderSatus(settlementOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleShopSettlement(String orderNo) throws ServerException {
        moneyDao.beginTranslist();
        if (moneyDao.doCancleShopSettlement(orderNo)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyNormalTrade(NormalTradeOrder normalTradeOrder) throws ServerException {
        moneyDao.beginTranslist();
        if (moneyDao.doApplyNormalTrade(normalTradeOrder)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doNormalTrade(NormalTradeOrder normalTradeOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(moneyDao.getDBTools());
        moneyDao.beginTranslist();
        NormalTradeOrder tradeOrder = (NormalTradeOrder) orderDao.findOrderByOrderNoLock(normalTradeOrder.getOrderNo(), NormalTradeOrder.class);
        if (tradeOrder == null) {
            moneyDao.rollBackTranslist();
            throw new ServerException("找不到B户通用记账订单");
        }
        if (moneyDao.doNormalTrade(normalTradeOrder)) {
            moneyDao.commitTranslist();
            normalTradeOrder.setLastmodifydate(tradeOrder.getLastmodifydate());
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doCancleNormalTrade(String orderNo) throws ServerException {
        moneyDao.beginTranslist();
        if (moneyDao.doCancleNormalTrade(orderNo)) {
            moneyDao.commitTranslist();
            return true;
        } else {
            moneyDao.rollBackTranslist();
            return false;
        }
    }

}

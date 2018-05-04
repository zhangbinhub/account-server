package com.olink.account.trade.service.impl;

import com.olink.account.base.impl.BaseTradeService;
import com.olink.account.enumration.OrderStatusEnum;
import com.olink.account.enumration.PayTypeEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.customer.CustAccount;
import com.olink.account.model.trade.order.CustomCurrencyPayOrder;
import com.olink.account.model.trade.order.CustomCurrencyRechargeOrder;
import com.olink.account.model.trade.order.CustomCurrencyRefundOrder;
import com.olink.account.trade.service.ICurrencyService;
import com.olink.account.dao.IBusinessDao;
import com.olink.account.trade.dao.ICurrencyDao;
import com.olink.account.dao.ICustomerDao;
import com.olink.account.dao.IOrderDao;
import com.olink.account.dao.impl.BusinessDao;
import com.olink.account.trade.dao.impl.CurrencyDao;
import com.olink.account.dao.impl.CustomerDao;
import com.olink.account.dao.impl.OrderDao;

/**
 * Created by zhangbin on 2016/10/5.
 * 自有币种服务
 */
public class CurrencyService extends BaseTradeService implements ICurrencyService {

    private ICurrencyDao currencyDao = new CurrencyDao();

    @Override
    public boolean doApplyCurrencyRecharge(CustomCurrencyRechargeOrder customCurrencyRechargeOrder) throws ServerException {
        ICustomerDao customerDao = new CustomerDao(currencyDao.getDBTools());
        CustAccount custAccount = customerDao.findCustAccountByCustId(customCurrencyRechargeOrder.getCustid());
        if (custAccount == null) {
            throw new ServerException("客户信息不存在：" + customCurrencyRechargeOrder.getCustid());
        }
        customCurrencyRechargeOrder.setPaytype(PayTypeEnum.otherspay.getName());
        customCurrencyRechargeOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        currencyDao.beginTranslist();
        if (currencyDao.doApplyCurrencyRecharge(customCurrencyRechargeOrder)) {
            currencyDao.commitTranslist();
            return true;
        } else {
            currencyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doCurrencyRecharge(CustomCurrencyRechargeOrder customCurrencyRechargeOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(currencyDao.getDBTools());
        currencyDao.beginTranslist();
        CustomCurrencyRechargeOrder recharge = (CustomCurrencyRechargeOrder) orderDao.findOrderByOrderNoLock(customCurrencyRechargeOrder.getOrderNo(), CustomCurrencyRechargeOrder.class);
        if (recharge == null) {
            currencyDao.rollBackTranslist();
            throw new ServerException("找不到入账订单！");
        }
        if (currencyDao.doCurrencyRecharge(recharge)) {
            currencyDao.commitTranslist();
            customCurrencyRechargeOrder.setCurrencyname(recharge.getCurrencyname());
            customCurrencyRechargeOrder.setAmont(recharge.getAmont());
            customCurrencyRechargeOrder.setCustid(recharge.getCustid());
            customCurrencyRechargeOrder.setLastmodifydate(recharge.getLastmodifydate());
            return true;
        } else {
            currencyDao.rollBackTranslist();
            currencyDao.doChangeOrderSatus(recharge, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleCurrencyRecharge(String orderNo) throws ServerException {
        currencyDao.beginTranslist();
        if (currencyDao.doCancleCurrencyRecharge(orderNo)) {
            currencyDao.commitTranslist();
            return true;
        } else {
            currencyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyCurrencyPayment(CustomCurrencyPayOrder customCurrencyPayOrder) throws ServerException {
        ICustomerDao customerDao = new CustomerDao(currencyDao.getDBTools());
        CustAccount custAccount = customerDao.findCustAccountByCustId(customCurrencyPayOrder.getCustid());
        if (custAccount == null) {
            throw new ServerException("客户信息不存在：" + customCurrencyPayOrder.getCustid());
        }
        customCurrencyPayOrder.setPaytype(PayTypeEnum.otherspay.getName());
        customCurrencyPayOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        currencyDao.beginTranslist();
        if (currencyDao.doApplyCurrencyPayment(customCurrencyPayOrder)) {
            currencyDao.commitTranslist();
            return true;
        } else {
            currencyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doCurrencyPayment(CustomCurrencyPayOrder customCurrencyPayOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(currencyDao.getDBTools());
        currencyDao.beginTranslist();
        CustomCurrencyPayOrder pay = (CustomCurrencyPayOrder) orderDao.findOrderByOrderNoLock(customCurrencyPayOrder.getOrderNo(), CustomCurrencyPayOrder.class);
        if (pay == null) {
            currencyDao.rollBackTranslist();
            throw new ServerException("找不到入账订单！");
        }
        if (currencyDao.doCurrencyPayment(pay)) {
            currencyDao.commitTranslist();
            customCurrencyPayOrder.setAmont(pay.getAmont());
            customCurrencyPayOrder.setCustid(pay.getCustid());
            customCurrencyPayOrder.setCurrencyname(pay.getCurrencyname());
            customCurrencyPayOrder.setLastmodifydate(pay.getLastmodifydate());
            return true;
        } else {
            currencyDao.rollBackTranslist();
            currencyDao.doChangeOrderSatus(pay, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleCurrencyPayment(String orderNo) throws ServerException {
        currencyDao.beginTranslist();
        if (currencyDao.doCancleCurrencyPayment(orderNo)) {
            currencyDao.commitTranslist();
            return true;
        } else {
            currencyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyCurrencyRefund(CustomCurrencyRefundOrder customCurrencyRefundOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(currencyDao.getDBTools());
        IBusinessDao businessDao = new BusinessDao(currencyDao.getDBTools());
        ICustomerDao customerDao = new CustomerDao(currencyDao.getDBTools());
        currencyDao.beginTranslist();
        CustomCurrencyPayOrder payOrder = (CustomCurrencyPayOrder) orderDao.findOrderByOrderNoLock(customCurrencyRefundOrder.getOrigorderno(), CustomCurrencyPayOrder.class);
        if (payOrder == null) {
            currencyDao.rollBackTranslist();
            throw new ServerException("找不到原支付订单！");
        }
        if (!customCurrencyRefundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            currencyDao.rollBackTranslist();
            throw new ServerException("退款请求非法，没有操作权限！");
        }
        customCurrencyRefundOrder.setPaytype(PayTypeEnum.otherspay.getName());
        customCurrencyRefundOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        if (!validateRefundPayTypeAndStatus(payOrder, customCurrencyRefundOrder, 0)) {
            currencyDao.rollBackTranslist();
            throw new ServerException("原订单不允许进行退款操作！");
        }
        BusinessAccount businessAccount = businessDao.findBusinessAccountByBusinessId(payOrder.getBusinessid());
        CustAccount custAccount = customerDao.findCustAccountByCustId(payOrder.getCustid());
        if (custAccount == null) {
            currencyDao.rollBackTranslist();
            throw new ServerException("客户信息不存在：" + payOrder.getCustid());
        }
        if (!customCurrencyRefundOrder.doValidateOrder(currencyDao.getDBTools().getDbcon())) {
            currencyDao.rollBackTranslist();
            throw new ServerException("退款金额超过原订单剩余可退款额");
        }
        customCurrencyRefundOrder.setBusinessid(businessAccount.getBusinessid());
        customCurrencyRefundOrder.setBusinessname(businessAccount.getBusinessname());
        customCurrencyRefundOrder.setChannel(businessAccount.getChannel());
        customCurrencyRefundOrder.setCustid(custAccount.getCustid());
        customCurrencyRefundOrder.setDescription("自有币种支付订单：" + customCurrencyRefundOrder.getOrigorderno() + " 退款");
        if (currencyDao.doApplyCurrencyRefund(customCurrencyRefundOrder)) {
            currencyDao.commitTranslist();
            customCurrencyRefundOrder.setSuramont(payOrder.getAmont() - (payOrder.getRefundamont() + customCurrencyRefundOrder.getAmont()));
            return true;
        } else {
            currencyDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doCurrencyRefund(CustomCurrencyRefundOrder customCurrencyRefundOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(currencyDao.getDBTools());
        currencyDao.beginTranslist();
        CustomCurrencyRefundOrder refundOrder = (CustomCurrencyRefundOrder) orderDao.findOrderByOrderNoLock(customCurrencyRefundOrder.getOrderNo(), CustomCurrencyRefundOrder.class);
        if (refundOrder == null) {
            currencyDao.rollBackTranslist();
            throw new ServerException("找不到退款支付订单！");
        }
        CustomCurrencyPayOrder payOrder = (CustomCurrencyPayOrder) orderDao.findOrderByOrderNoLock(refundOrder.getOrigorderno(), CustomCurrencyPayOrder.class);
        if (payOrder == null) {
            currencyDao.rollBackTranslist();
            throw new ServerException("找不到原支付订单！");
        }
        if (!refundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            currencyDao.rollBackTranslist();
            throw new ServerException("退款请求非法，没有操作权限！");
        }
        if (!validateRefundPayTypeAndStatus(payOrder, refundOrder, 1)) {
            currencyDao.rollBackTranslist();
            throw new ServerException("原订单不允许进行退款操作！");
        }
        if (currencyDao.doCurrencyRefund(refundOrder)) {
            currencyDao.commitTranslist();
            customCurrencyRefundOrder.setCustid(refundOrder.getCustid());
            customCurrencyRefundOrder.setCurrencyname(refundOrder.getCurrencyname());
            customCurrencyRefundOrder.setAmont(refundOrder.getAmont());
            customCurrencyRefundOrder.setLastmodifydate(refundOrder.getLastmodifydate());
            customCurrencyRefundOrder.setSuramont(payOrder.getAmont() - (payOrder.getRefundamont()));
            return true;
        } else {
            currencyDao.rollBackTranslist();
            currencyDao.doChangeOrderSatus(refundOrder, OrderStatusEnum.failed);
            throw new ServerException("退款失败");
        }
    }

    @Override
    public boolean doCancleCurrencyRefund(String orderNo) throws ServerException {
        currencyDao.beginTranslist();
        if (currencyDao.doCancleCurrencyRefund(orderNo)) {
            currencyDao.commitTranslist();
            return true;
        } else {
            currencyDao.rollBackTranslist();
            return false;
        }
    }

}

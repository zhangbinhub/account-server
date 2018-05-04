package com.olink.account.trade.service.impl;

import com.olink.account.base.impl.BaseTradeService;
import com.olink.account.dao.*;
import com.olink.account.enumration.*;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.customer.BusinessBindInfo;
import com.olink.account.model.trade.customer.BusinessSubAccount;
import com.olink.account.model.trade.customer.CustAccount;
import com.olink.account.model.trade.order.IntegralPayOrder;
import com.olink.account.model.trade.order.IntegralRechargeOrder;
import com.olink.account.model.trade.order.IntegralRefundOrder;
import com.olink.account.model.trade.order.IntegralSettlementOrder;
import com.olink.account.model.kernel.user.SysUser;
import com.olink.account.trade.dao.IIntegralDao;
import com.olink.account.trade.dao.impl.IntegralDao;
import com.olink.account.trade.service.IIntegralService;
import com.olink.account.dao.impl.*;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/10/7.
 * 积分服务
 */
public class IntegralService extends BaseTradeService implements IIntegralService {

    private IIntegralDao integralDao = new IntegralDao();

    @Override
    public boolean doApplyRecharge(IntegralRechargeOrder integralRechargeOrder) throws ServerException {
        ICustomerDao customerDao = new CustomerDao(integralDao.getDBTools());
        CustAccount custAccount = customerDao.findCustAccountByCustId(integralRechargeOrder.getCustid());
        if (custAccount == null) {
            throw new ServerException("客户信息不存在：" + integralRechargeOrder.getCustid());
        }
        integralRechargeOrder.setPaytype(PayTypeEnum.otherspay.getName());
        integralRechargeOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        integralDao.beginTranslist();
        if (integralDao.doApplyIntegralRecharge(integralRechargeOrder)) {
            integralDao.commitTranslist();
            return true;
        } else {
            integralDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doRecharge(IntegralRechargeOrder integralRechargeOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(integralDao.getDBTools());
        integralDao.beginTranslist();
        IntegralRechargeOrder recharge = (IntegralRechargeOrder) orderDao.findOrderByOrderNoLock(integralRechargeOrder.getOrderNo(), IntegralRechargeOrder.class);
        if (recharge == null) {
            integralDao.rollBackTranslist();
            throw new ServerException("找不到入账订单！");
        }
        if (integralDao.doRecharge(recharge)) {
            integralDao.commitTranslist();
            integralRechargeOrder.setAmont(recharge.getAmont());
            integralRechargeOrder.setCustid(recharge.getCustid());
            integralRechargeOrder.setBeforebalance(recharge.getBeforebalance());
            integralRechargeOrder.setAfterbalance(recharge.getAfterbalance());
            integralRechargeOrder.setLastmodifydate(recharge.getLastmodifydate());
            return true;
        } else {
            integralDao.rollBackTranslist();
            integralDao.doChangeOrderSatus(recharge, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleRecharge(String orderNo) throws ServerException {
        integralDao.beginTranslist();
        if (integralDao.doCancleIntegralRecharge(orderNo)) {
            integralDao.commitTranslist();
            return true;
        } else {
            integralDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyPayment(IntegralPayOrder integralPayOrder) throws ServerException {
        ICustomerDao customerDao = new CustomerDao(integralDao.getDBTools());
        IDictionaryDao dictionaryDao = new DictionaryDao(integralDao.getDBTools());
        CustAccount custAccount = customerDao.findCustAccountByCustId(integralPayOrder.getCustid());
        if (custAccount == null) {
            throw new ServerException("客户信息不存在：" + integralPayOrder.getCustid());
        }
        integralPayOrder.setPaytype(PayTypeEnum.otherspay.getName());
        integralPayOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        integralPayOrder.setRatio(dictionaryDao.getIntegralRatio());
        integralDao.beginTranslist();
        if (!integralPayOrder.doValidateOrder(integralDao.getDBTools().getDbcon())) {
            integralDao.rollBackTranslist();
            throw new ServerException("消费失败：剩余积分不足！");
        }
        if (integralDao.doApplyPayment(integralPayOrder)) {
            integralDao.commitTranslist();
            return true;
        } else {
            integralDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doPayment(IntegralPayOrder integralPayOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(integralDao.getDBTools());
        integralDao.beginTranslist();
        IntegralPayOrder pay = (IntegralPayOrder) orderDao.findOrderByOrderNoLock(integralPayOrder.getOrderNo(), IntegralPayOrder.class);
        if (pay == null) {
            integralDao.rollBackTranslist();
            throw new ServerException("找不到入账订单！");
        }
        if (integralDao.doPayment(pay)) {
            integralDao.commitTranslist();
            integralPayOrder.setAmont(pay.getAmont());
            integralPayOrder.setBeforebalance(pay.getBeforebalance());
            integralPayOrder.setAfterbalance(pay.getAfterbalance());
            integralPayOrder.setRecebeforebalance(pay.getRecebeforebalance());
            integralPayOrder.setReceafterbalance(pay.getReceafterbalance());
            integralPayOrder.setCustid(pay.getCustid());
            integralPayOrder.setLastmodifydate(pay.getLastmodifydate());
            return true;
        } else {
            integralDao.rollBackTranslist();
            integralDao.doChangeOrderSatus(pay, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCanclePayment(String orderNo) throws ServerException {
        integralDao.beginTranslist();
        if (integralDao.doCanclePayment(orderNo)) {
            integralDao.commitTranslist();
            return true;
        } else {
            integralDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyRefund(IntegralRefundOrder integralRefundOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(integralDao.getDBTools());
        IBusinessDao businessDao = new BusinessDao(integralDao.getDBTools());
        ICustomerDao customerDao = new CustomerDao(integralDao.getDBTools());
        integralDao.beginTranslist();
        IntegralPayOrder payOrder = (IntegralPayOrder) orderDao.findOrderByOrderNoLock(integralRefundOrder.getOrigorderno(), IntegralPayOrder.class);
        if (payOrder == null) {
            integralDao.rollBackTranslist();
            throw new ServerException("找不到原支付订单！");
        }
        if (!integralRefundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            integralDao.rollBackTranslist();
            throw new ServerException("退款请求非法，没有操作权限！");
        }
        integralRefundOrder.setPaytype(PayTypeEnum.otherspay.getName());
        integralRefundOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        if (!validateRefundPayTypeAndStatus(payOrder, integralRefundOrder, 0)) {
            integralDao.rollBackTranslist();
            throw new ServerException("原订单不允许进行退款操作！");
        }
        BusinessAccount businessAccount = businessDao.findBusinessAccountByBusinessId(payOrder.getBusinessid());
        CustAccount custAccount = customerDao.findCustAccountByCustId(payOrder.getCustid());
        if (custAccount == null) {
            integralDao.rollBackTranslist();
            throw new ServerException("客户信息不存在：" + payOrder.getCustid());
        }
        if (!integralRefundOrder.doValidateOrder(integralDao.getDBTools().getDbcon())) {
            integralDao.rollBackTranslist();
            throw new ServerException("退款金额超过原订单剩余可退款额");
        }
        integralRefundOrder.setBusinessid(businessAccount.getBusinessid());
        integralRefundOrder.setBusinessname(businessAccount.getBusinessname());
        integralRefundOrder.setChannel(businessAccount.getChannel());
        integralRefundOrder.setCustid(custAccount.getCustid());
        integralRefundOrder.setDescription("积分消费订单：" + integralRefundOrder.getOrigorderno() + " 退款");
        integralRefundOrder.setRatio(payOrder.getRatio());
        if (integralDao.doApplyRefund(integralRefundOrder)) {
            integralDao.commitTranslist();
            integralRefundOrder.setSuramont(payOrder.getAmont() - (payOrder.getRefundamont() + integralRefundOrder.getAmont()));
            return true;
        } else {
            integralDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doRefund(IntegralRefundOrder integralRefundOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(integralDao.getDBTools());
        integralDao.beginTranslist();
        IntegralRefundOrder refundOrder = (IntegralRefundOrder) orderDao.findOrderByOrderNoLock(integralRefundOrder.getOrderNo(), IntegralRefundOrder.class);
        if (refundOrder == null) {
            integralDao.rollBackTranslist();
            throw new ServerException("找不到退款支付订单！");
        }
        IntegralPayOrder payOrder = (IntegralPayOrder) orderDao.findOrderByOrderNoLock(refundOrder.getOrigorderno(), IntegralPayOrder.class);
        if (payOrder == null) {
            integralDao.rollBackTranslist();
            throw new ServerException("找不到原支付订单！");
        }
        if (!refundOrder.getBusinessid().equals(payOrder.getBusinessid())) {
            integralDao.rollBackTranslist();
            throw new ServerException("退款请求非法，没有操作权限！");
        }
        if (!validateRefundPayTypeAndStatus(payOrder, refundOrder, 1)) {
            integralDao.rollBackTranslist();
            throw new ServerException("原订单不允许进行退款操作！");
        }
        if (integralDao.doRefund(refundOrder)) {
            integralDao.commitTranslist();
            integralRefundOrder.setCustid(refundOrder.getCustid());
            integralRefundOrder.setBeforebalance(refundOrder.getBeforebalance());
            integralRefundOrder.setAfterbalance(refundOrder.getAfterbalance());
            integralRefundOrder.setRecebeforebalance(refundOrder.getRecebeforebalance());
            integralRefundOrder.setReceafterbalance(refundOrder.getReceafterbalance());
            integralRefundOrder.setAmont(refundOrder.getAmont());
            integralRefundOrder.setLastmodifydate(refundOrder.getLastmodifydate());
            integralRefundOrder.setSuramont(payOrder.getAmont() - (payOrder.getRefundamont()));
            return true;
        } else {
            integralDao.rollBackTranslist();
            integralDao.doChangeOrderSatus(refundOrder, OrderStatusEnum.failed);
            throw new ServerException("退款失败");
        }
    }

    @Override
    public boolean doCancleRefund(String orderNo) throws ServerException {
        integralDao.beginTranslist();
        if (integralDao.doCancleRefund(orderNo)) {
            integralDao.commitTranslist();
            return true;
        } else {
            integralDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doApplyIntegralSettlement(IntegralSettlementOrder integralSettlementOrder) throws ServerException {
        IBusinessDao businessDao = new BusinessDao(integralDao.getDBTools());
        BusinessAccount businessAccount = businessDao.findBusinessAccountByBusinessId(integralSettlementOrder.getBusinessid());
        if (businessAccount == null) {
            throw new ServerException("找不到待结算的B账户：" + integralSettlementOrder.getBusinessid());
        }
        BusinessSubAccount businessSubAccount = businessAccount.getSubAccountByType(AccountTypeEnum.Integral);
        BusinessBindInfo businessBindInfo = businessDao.findBusinessBindInfoById(integralSettlementOrder.getBindinfoid());
        if (businessBindInfo == null) {
            throw new ServerException("找不到结算账户：" + integralSettlementOrder.getBindinfoid());
        } else {
            if (businessBindInfo.getStatus() != StatusEnum.activate.getValue()) {
                throw new ServerException("结算账户不可用：" + businessBindInfo.getId());
            }
        }
        integralDao.beginTranslist();
        integralSettlementOrder.setAmont(businessSubAccount.getBalance());
        integralSettlementOrder.setRatio(BigDecimal.valueOf(businessSubAccount.getMoney() / businessSubAccount.getBalance()).setScale(6, DecimalProcessModeEnum.Half_UP.getMode()).doubleValue());
        integralSettlementOrder.setMoney(businessSubAccount.getMoney());
        if (!integralSettlementOrder.doValidateOrder(integralDao.getDBTools().getDbcon())) {
            integralDao.rollBackTranslist();
            throw new ServerException("结算申请失败：账户余额不足！");
        }
        BusinessAccount YYbusinessAccount = businessDao.findYYBusinessAccount();
        if (YYbusinessAccount != null) {
            integralSettlementOrder.setBusinessname(businessAccount.getBusinessname());
            integralSettlementOrder.setBusinesstradeno("");
            integralSettlementOrder.setBusinesssubaccountcode(businessSubAccount.getCode());
            integralSettlementOrder.setCustid("");
            integralSettlementOrder.setPaytype(PayTypeEnum.otherspay.getName());
            integralSettlementOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
            integralSettlementOrder.setChannel(YYbusinessAccount.getChannel());
            integralSettlementOrder.setBbindtype(businessBindInfo.getType());
            try {
                switch (BBindTypeEnum.getEnum(businessBindInfo.getType())) {
                    case AliPay:
                        integralSettlementOrder.setBuyername(businessBindInfo.getPartner());
                        integralSettlementOrder.setBuyeraccount(businessBindInfo.getSeller_email());
                        break;
                    case WeiXinPublic:
                    case WeiXinOpen:
                        integralSettlementOrder.setBuyername(businessBindInfo.getPartner());
                        integralSettlementOrder.setBuyeraccount(businessBindInfo.getPartner());
                        break;
                    case BankCard:
                        integralSettlementOrder.setBuyername(businessBindInfo.getName());
                        integralSettlementOrder.setBuyeraccount(businessBindInfo.getAccount());
                        integralSettlementOrder.setBuyerbankname(businessBindInfo.getBankname());
                        break;
                    default:
                        throw new ServerException("结算申请失败：结算账户类型非法！");
                }
            } catch (EnumValueUndefinedException e) {
                throw new ServerException("结算申请失败：结算账户类型非法！");
            }
            if (integralDao.doApplyIntegralSettlement(integralSettlementOrder)) {
                integralDao.commitTranslist();
                return true;
            } else {
                integralDao.rollBackTranslist();
                return false;
            }
        } else {
            integralDao.rollBackTranslist();
            throw new ServerException("结算申请失败：找不到账户平台账户");
        }
    }

    @Override
    public boolean doIntegralSettlement(IntegralSettlementOrder integralSettlementOrder) throws ServerException {
        ISystemDao sysUserDao = new SystemDao();
        IBusinessDao businessDao = new BusinessDao(integralDao.getDBTools());
        SysUser sysUser = sysUserDao.findUserById(integralSettlementOrder.getUserid());
        IOrderDao orderDao = new OrderDao(integralDao.getDBTools());
        integralDao.beginTranslist();
        IntegralSettlementOrder settlementOrder = (IntegralSettlementOrder) orderDao.findOrderByOrderNoLock(integralSettlementOrder.getOrderNo(), IntegralSettlementOrder.class);
        if (settlementOrder == null) {
            integralDao.rollBackTranslist();
            throw new ServerException("找不到余额结算订单");
        }
        settlementOrder.setUserid(sysUser.getId());
        BusinessAccount businessAccount = businessDao.findBusinessAccountByBusinessId(settlementOrder.getBusinessid());
        if (businessAccount == null) {
            throw new ServerException("找不到待结算的B账户：" + settlementOrder.getBusinessid());
        }
        if (orderDao.doOrderReview(settlementOrder.getOrderNo(), "审核通过", ReviewResultEnum.pass, sysUser.getId(), sysUser.getName()) && integralDao.doIntegralSettlement(settlementOrder)) {
            integralDao.commitTranslist();
            integralSettlementOrder.setAmont(settlementOrder.getAmont());
            integralSettlementOrder.setLastmodifydate(settlementOrder.getLastmodifydate());
            integralSettlementOrder.setBusinessid(settlementOrder.getBusinessid());
            return true;
        } else {
            integralDao.rollBackTranslist();
            integralDao.doChangeOrderSatus(settlementOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleIntegralSettlement(String orderNo, String content, String userid) throws ServerException {
        ISystemDao sysUserDao = new SystemDao();
        SysUser sysUser = sysUserDao.findUserById(userid);
        IOrderDao orderDao = new OrderDao(integralDao.getDBTools());
        integralDao.beginTranslist();
        if (orderDao.doOrderReview(orderNo, content, ReviewResultEnum.notpass, sysUser.getId(), sysUser.getName()) && integralDao.doCancleIntegralSettlement(orderNo, userid)) {
            integralDao.commitTranslist();
            return true;
        } else {
            integralDao.rollBackTranslist();
            return false;
        }
    }

}

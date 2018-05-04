package com.olink.account.trade.service.impl;

import com.olink.account.base.impl.BaseTradeService;
import com.olink.account.enumration.OrderTypeEnum;
import com.olink.account.model.trade.order.OrderBase;
import com.olink.account.trade.dao.IAccountDao;
import com.olink.account.dao.IBusinessDao;
import com.olink.account.dao.IOrderDao;
import com.olink.account.dao.ISystemDao;
import com.olink.account.trade.dao.impl.AccountDao;
import com.olink.account.dao.impl.BusinessDao;
import com.olink.account.dao.impl.OrderDao;
import com.olink.account.dao.impl.SystemDao;
import com.olink.account.enumration.OrderStatusEnum;
import com.olink.account.enumration.PayTypeEnum;
import com.olink.account.enumration.ReviewResultEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.order.InternalTradeOrder;
import com.olink.account.model.kernel.user.SysUser;
import com.olink.account.trade.service.IAccountService;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

/**
 * Created by zhangbin on 2016/11/23.
 * 账务系统服务
 */
public class AccountService extends BaseTradeService implements IAccountService {

    private IAccountDao accountDao = new AccountDao();

    @Override
    public boolean doApplyInternalTrade(InternalTradeOrder internalTradeOrder) throws ServerException {
        IBusinessDao businessDao = new BusinessDao(accountDao.getDBTools());
        BusinessAccount businessAccount = businessDao.findBusinessAccountByBusinessId(internalTradeOrder.getBusinessid());
        if (businessAccount == null) {
            throw new ServerException("找不到关联的B账户：" + internalTradeOrder.getBusinessid());
        }
        internalTradeOrder.setBusinessname(businessAccount.getBusinessname());
        internalTradeOrder.setBusinesstradeno("");
        internalTradeOrder.setPaytype(PayTypeEnum.otherspay.getName());
        internalTradeOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        internalTradeOrder.setDescription("内部特殊交易订单");
        accountDao.beginTranslist();
        try {
            internalTradeOrder.setTradetypename(internalTradeOrder.getTradetype().getName());
            if (!CommonTools.isNullStr(internalTradeOrder.getOrigorderno())) {
                OrderTypeEnum orderTypeEnum = internalTradeOrder.getOrigordertype();
                if (orderTypeEnum == null) {
                    accountDao.rollBackTranslist();
                    throw new ServerException("原订单类型为空：" + internalTradeOrder.getOrigorderno());
                }
                IOrderDao orderDao = new OrderDao(accountDao.getDBTools());
                OrderBase origOrder = orderDao.findOrderByOrderNoLock(internalTradeOrder.getOrderNo(), orderTypeEnum.getOrderClass());
                internalTradeOrder.setOrigstatus(origOrder.getStatus());
            }
        } catch (EnumValueUndefinedException e) {
            accountDao.rollBackTranslist();
            throw new ServerException(e.getMessage());
        }
        if (!internalTradeOrder.doValidateOrder(businessDao.getDBTools().getDbcon())) {
            accountDao.rollBackTranslist();
            throw new ServerException("内部交易申请失败：账户余额不足！");
        }
        BusinessAccount YYbusinessAccount = businessDao.findYYBusinessAccount();
        if (YYbusinessAccount != null) {
            internalTradeOrder.setChannel(YYbusinessAccount.getChannel());
            if (accountDao.doApplyInternalTrade(internalTradeOrder)) {
                accountDao.commitTranslist();
                return true;
            } else {
                accountDao.rollBackTranslist();
                return false;
            }
        } else {
            accountDao.rollBackTranslist();
            throw new ServerException("内部交易申请失败：找不到账户平台账户");
        }
    }

    @Override
    public boolean doInternalTrade(InternalTradeOrder internalTradeOrder) throws ServerException {
        IOrderDao orderDao = new OrderDao(accountDao.getDBTools());
        accountDao.beginTranslist();
        InternalTradeOrder tradeOrder = (InternalTradeOrder) orderDao.findOrderByOrderNoLock(internalTradeOrder.getOrderNo(), InternalTradeOrder.class);
        if (tradeOrder == null) {
            accountDao.rollBackTranslist();
            throw new ServerException("找不到特殊交易订单");
        }
        tradeOrder.setUserid(internalTradeOrder.getUserid());
        if (doReviewInternalTrade(tradeOrder.getOrderNo(), tradeOrder.getUserid(), "审核通过", ReviewResultEnum.pass, ReviewResultEnum.pass.getValue()) && accountDao.doInternalTrade(tradeOrder)) {
            accountDao.commitTranslist();
            internalTradeOrder.setBusinessid(tradeOrder.getBusinessid());
            internalTradeOrder.setAmont(tradeOrder.getAmont());
            internalTradeOrder.setLastmodifydate(tradeOrder.getLastmodifydate());
            return true;
        } else {
            accountDao.rollBackTranslist();
            accountDao.doChangeOrderSatus(tradeOrder, OrderStatusEnum.failed);
            return false;
        }
    }

    @Override
    public boolean doCancleInternalTrade(String orderNo, String content, String userid) throws ServerException {
        accountDao.beginTranslist();
        if (doReviewInternalTrade(orderNo, userid, content, ReviewResultEnum.notpass, ReviewResultEnum.notpass.getValue()) && accountDao.doCancleInternalTrade(orderNo, userid)) {
            accountDao.commitTranslist();
            return true;
        } else {
            accountDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doReviewInternalTrade(String orderNo, String userid, String reviewContent, ReviewResultEnum reviewResultEnum, int checkstatus) throws ServerException {
        ISystemDao sysUserDao = new SystemDao();
        SysUser sysUser = sysUserDao.findUserById(userid);
        IOrderDao orderDao = new OrderDao(accountDao.getDBTools());
        InternalTradeOrder internalTradeOrder = (InternalTradeOrder) orderDao.findOrderByOrderNo(orderNo, InternalTradeOrder.class);
        internalTradeOrder.setUserid(userid);
        internalTradeOrder.setCheckstatus(checkstatus);
        return orderDao.doOrderReview(orderNo, reviewContent, reviewResultEnum, sysUser.getId(), sysUser.getName()) && accountDao.doUpdateInternalTrade(internalTradeOrder);
    }

}

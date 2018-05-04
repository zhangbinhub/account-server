package com.olink.account.base.impl;

import com.olink.account.base.IBaseTradeDao;
import com.olink.account.base.IBaseTradeService;
import com.olink.account.enumration.OrderStatusEnum;
import com.olink.account.enumration.OrderTypeEnum;
import com.olink.account.enumration.PayTypeEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.order.OrderBase;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

/**
 * Created by zhangbin on 2016/10/5.
 * 服务基类
 */
public abstract class BaseTradeService implements IBaseTradeService {

    /**
     * 校验退款订单支付类型及状态
     *
     * @param payOrder    原支付订单
     * @param refundOrder 退款订单
     * @param flag        0-申请，1-确认
     * @return true|false
     */
    protected boolean validateRefundPayTypeAndStatus(OrderBase payOrder, OrderBase refundOrder, int flag) throws ServerException {
        try {
            if (!OrderTypeEnum.BalancePay.equals(payOrder.getOrdertype()) && !OrderTypeEnum.NonBalancePay.equals(payOrder.getOrdertype())
                    && !OrderTypeEnum.IntegralPay.equals(payOrder.getOrdertype()) && !OrderTypeEnum.CustomCurrencyPay.equals(payOrder.getOrdertype())) {
                return false;
            }
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(refundOrder.getPaytype());
            refundOrder.setPaytypename(payTypeEnum.getDescription());
            if (!payOrder.getPaytype().equals(payTypeEnum.getName())) {
                return false;
            }
            if (flag == 0) {
                if (!payOrder.getStatus().equals(OrderStatusEnum.paySuccess.getValue()) && !payOrder.getStatus().equals(OrderStatusEnum.reviewing.getValue())
                        && !payOrder.getStatus().equals(OrderStatusEnum.success.getValue()) && !payOrder.getStatus().equals(OrderStatusEnum.refundSuccess.getValue())
                        && !payOrder.getStatus().equals(OrderStatusEnum.refundFail.getValue()) && !payOrder.getStatus().equals(OrderStatusEnum.refundCancle.getValue())
                        && !payOrder.getStatus().equals(OrderStatusEnum.adjustFail.getValue()) && !payOrder.getStatus().equals(OrderStatusEnum.adjustCancle.getValue())) {
                    return false;
                }
            } else {
                if (!payOrder.getStatus().equals(OrderStatusEnum.applyRefundSuccess.getValue()) && !payOrder.getStatus().equals(OrderStatusEnum.refunding.getValue())) {
                    return false;
                }
            }
            return true;
        } catch (EnumValueUndefinedException e) {
            return false;
        }
    }

    /**
     * 获取可用的系统参数值
     *
     * @param baseDao   dao对象
     * @param paramCode 参数编码
     * @return 参数值
     */
    protected String getSysParamAvailable(IBaseTradeDao baseDao, String paramCode) {
        return baseDao.getSysParamAvailable(paramCode);
    }

    /**
     * 判断系统参数是否可用
     *
     * @param baseDao   dao对象
     * @param paramCode 参数编码
     * @return true|false
     */
    protected boolean isSysParamAvailable(IBaseTradeDao baseDao, String paramCode) {
        return baseDao.isSysParamAvailable(paramCode);
    }

    /**
     * 获取积分兑换比例
     *
     * @param baseDao dao对象
     * @return 积分兑换比例
     */
    protected double getIntegralRatio(IBaseTradeDao baseDao) {
        return baseDao.getIntegralRatio();
    }

    /**
     * 获取系统会计日期
     *
     * @param baseDao dao对象
     * @return 会计日期
     */
    protected String getAccountDate(IBaseTradeDao baseDao) {
        return baseDao.getAccountDate();
    }

}

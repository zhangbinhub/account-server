package com.olink.account.model.trade.order;

import com.olink.account.enumration.*;
import com.olink.account.core.AccountResult;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/9/12.
 * 退款订单
 */
@ADBTable(tablename = "acc_order_balancerefund", isSeparate = true)
public class BalanceRefundOrder extends OrderBase {

    public BalanceRefundOrder() {
        super(OrderTypeEnum.BalanceRefund);
    }

    public String getOrigorderno() {
        return origorderno;
    }

    public void setOrigorderno(String origorderno) {
        this.origorderno = origorderno;
    }

    public OrderStatusEnum getOrigstatus() throws EnumValueUndefinedException {
        return OrderStatusEnum.getEnum(origstatus);
    }

    public void setOrigstatus(OrderStatusEnum origstatus) {
        this.origstatus = origstatus.getValue();
    }

    public String getRefunddate() {
        return refunddate;
    }

    public void setRefunddate(String refunddate) {
        this.refunddate = refunddate;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public double getBeforebalance() {
        return beforebalance.doubleValue();
    }

    public void setBeforebalance(double beforebalance) {
        this.beforebalance = BigDecimal.valueOf(beforebalance);
    }

    public double getAfterbalance() {
        return afterbalance.doubleValue();
    }

    public void setAfterbalance(double afterbalance) {
        this.afterbalance = BigDecimal.valueOf(afterbalance);
    }

    public double getRecebeforebalance() {
        return recebeforebalance.doubleValue();
    }

    public void setRecebeforebalance(double recebeforebalance) {
        this.recebeforebalance = BigDecimal.valueOf(recebeforebalance);
    }

    public double getReceafterbalance() {
        return receafterbalance.doubleValue();
    }

    public void setReceafterbalance(double receafterbalance) {
        this.receafterbalance = BigDecimal.valueOf(receafterbalance);
    }

    private final Logger log = Logger.getLogger(this.getClass());

    @ADBTableField(name = "origorderno", fieldType = DBTableFieldType.String, allowNull = false)
    private String origorderno;//原支付订单号

    @ADBTableField(name = "origstatus", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int origstatus;//原支付订单退款前状态

    @ADBTableField(name = "beforebalance", fieldType = DBTableFieldType.Decimal)
    private BigDecimal beforebalance;//退款前余额

    @ADBTableField(name = "afterbalance", fieldType = DBTableFieldType.Decimal)
    private BigDecimal afterbalance;//退款后余额

    @ADBTableField(name = "recebeforebalance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal recebeforebalance;//收款方退款前余额

    @ADBTableField(name = "receafterbalance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal receafterbalance;//收款方退款后余额

    @ADBTableField(name = "refunddate", fieldType = DBTableFieldType.String)
    private String refunddate;//退款时间

    @ADBTableField(name = "reason", fieldType = DBTableFieldType.String, allowNull = false)
    private String reason;//退款原因

    @Override
    public boolean beforeCreateOrder() {
        return beforeCreateOrder(new ConnectionFactory());
    }

    @Override
    public boolean beforeCreateOrder(ConnectionFactory connectionFactory) {
        try {
            if (!doValidateOrder(connectionFactory)) {
                return false;
            }
            this.addUpdateExcludes(new String[]{"beforebalance", "afterbalance"});
            BalancePayOrder payOrder = (BalancePayOrder) doViewByLock(getOrigorderno(), BalancePayOrder.class, null, connectionFactory);
            this.setOrigstatus(payOrder.getStatus());
            payOrder.setRefundamont(payOrder.getRefundamont() + getAmont());
            payOrder.setRefundactamont(payOrder.getRefundactamont() + getActamont());
            payOrder.addUpdateIncludes(new String[]{"refundamont"});
            payOrder.setStatus(generateOrigOrderStatus());
            this.setRecebeforebalance(getAccountResult().getResultBeforBalance(ChangeAccountTypeEnum.Business, AccountTypeEnum.Change, AmontTypeEnum.balance));
            if (payOrder.doUpdateOrder(connectionFactory)) {
                if (this.getOrigstatus().equals(OrderStatusEnum.paySuccess) || this.getOrigstatus().equals(OrderStatusEnum.reviewing)) {
                    //B账户总余额减少，C户总余额增加
                    this.setReceafterbalance(this.getRecebeforebalance());
                } else {
                    //B账户总余额、可用余额（待结算余额）减少，C户总余额增加
                    this.setReceafterbalance(getAccountResult().getResultAfterBalance(ChangeAccountTypeEnum.Business, AccountTypeEnum.Change, AmontTypeEnum.balance));
                }
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void afterCreateOrder() {

    }

    @Override
    public void afterCreateOrder(ConnectionFactory connectionFactory) {

    }

    @Override
    public boolean beforeUpdateOrder() {
        return beforeUpdateOrder(new ConnectionFactory());
    }

    @Override
    public boolean beforeUpdateOrder(ConnectionFactory connectionFactory) {
        updateNotifyDate();
        updateProcessDate(null);
        try {
            BalancePayOrder payOrder = (BalancePayOrder) doViewByLock(getOrigorderno(), BalancePayOrder.class, null, connectionFactory);
            this.addUpdateExcludes(new String[]{"origstatus"});
            payOrder.setStatus(generateOrigOrderStatus());
            if (getStatus().equals(OrderStatusEnum.success.getValue())) {
                //C户可用余额增加
                this.setRefunddate(CommonTools.getNowTimeString());
                this.setBeforebalance(getAccountResult().getResultBeforBalance(ChangeAccountTypeEnum.DisCust, AccountTypeEnum.Change, AmontTypeEnum.balance));
                this.setAfterbalance(getAccountResult().getResultAfterBalance(ChangeAccountTypeEnum.DisCust, AccountTypeEnum.Change, AmontTypeEnum.balance));
                this.addUpdateIncludes(new String[]{"notifydate", "processdate", "userid", "refunddate", "beforebalance", "afterbalance"});
                payOrder.addUpdateIncludes(new String[]{"status", "lastmodifydate"});
            } else if (getStatus().equals(OrderStatusEnum.failed.getValue()) || getStatus().equals(OrderStatusEnum.cancle.getValue())) {
                payOrder.setRefundamont(payOrder.getRefundamont() - getAmont());
                payOrder.setRefundactamont(payOrder.getRefundactamont() - getActamont());
                payOrder.addUpdateIncludes(new String[]{"refundamont"});
                if (this.getOrigstatus().equals(OrderStatusEnum.paySuccess) || this.getOrigstatus().equals(OrderStatusEnum.reviewing)) {
                    //B账户总余额增加，C户总余额减少
                    payOrder.setStatus(this.getOrigstatus());
                    this.addUpdateIncludes(new String[]{"notifydate", "processdate", "userid"});
                } else {
                    //B账户总余额、可用余额（待结算余额）增加，C户总余额减少
                    this.setReceafterbalance(this.getRecebeforebalance());
                    this.addUpdateIncludes(new String[]{"receafterbalance", "notifydate", "processdate", "userid"});
                }
            }
            return payOrder.doUpdateOrder(connectionFactory);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public void afterUpdateOrder() {

    }

    @Override
    public void afterUpdateOrder(ConnectionFactory connectionFactory) {

    }

    @Override
    public boolean beforeDeleteOrder() {
        return false;
    }

    @Override
    public boolean beforeDeleteOrder(ConnectionFactory connectionFactory) {
        return false;
    }

    @Override
    public void afterDeleteOrder() {

    }

    @Override
    public void afterDeleteOrder(ConnectionFactory connectionFactory) {

    }

    @Override
    public AccountResult validateOrder(ConnectionFactory connectionFactory) {
        BalancePayOrder payOrder = (BalancePayOrder) doViewByLock(getOrigorderno(), BalancePayOrder.class, null, connectionFactory);
        double suramont = payOrder.getAmont() - (payOrder.getRefundamont() + getAmont());
        int comresult = Double.compare(suramont, 0.00D);
        AccountResult result = new AccountResult();
        if (comresult >= 0) {
            result.setStatus(ResultStatusEnum.success);
        } else {
            result.setStatus(ResultStatusEnum.failed);
            result.setMessage("超过可退款金额");
        }
        return result;
    }
}

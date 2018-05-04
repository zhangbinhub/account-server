package com.olink.account.model.trade.order;

import com.olink.account.enumration.OrderStatusEnum;
import com.olink.account.enumration.OrderTypeEnum;
import com.olink.account.enumration.ResultStatusEnum;
import com.olink.account.core.AccountResult;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import org.apache.log4j.Logger;

/**
 * Created by zhangbin on 2016/10/4.
 * 自有币种退款订单
 */
@ADBTable(tablename = "acc_order_currencyrefund", isSeparate = true)
public class CustomCurrencyRefundOrder extends OrderBase {

    public CustomCurrencyRefundOrder() {
        super(OrderTypeEnum.CustomCurrencyRefund);
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

    public String getCurrencyname() {
        return currencyname;
    }

    public void setCurrencyname(String currencyname) {
        this.currencyname = currencyname;
    }

    @ADBTableField(name = "currencyname", fieldType = DBTableFieldType.String, allowNull = false)
    private String currencyname;//币种名称

    @ADBTableField(name = "origorderno", fieldType = DBTableFieldType.String, allowNull = false)
    private String origorderno;//原支付订单号

    @ADBTableField(name = "origstatus", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int origstatus;//原支付订单退款前状态

    @ADBTableField(name = "refunddate", fieldType = DBTableFieldType.String)
    private String refunddate;//退款时间

    @ADBTableField(name = "reason", fieldType = DBTableFieldType.String, allowNull = false)
    private String reason;//退款原因

    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    public AccountResult validateOrder(ConnectionFactory connectionFactory) {
        CustomCurrencyPayOrder payOrder = (CustomCurrencyPayOrder) doViewByLock(getOrigorderno(), CustomCurrencyPayOrder.class, null, connectionFactory);
        double suramont = payOrder.getAmont() - (payOrder.getRefundamont() + getAmont());
        int comresult = Double.compare(suramont, 0.00D);
        AccountResult result = new AccountResult();
        if (comresult >= 0) {
            result.setStatus(ResultStatusEnum.success);
        } else {
            result.setStatus(ResultStatusEnum.failed);
            result.setMessage("超过可退款额");
        }
        return result;
    }

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
            CustomCurrencyPayOrder payOrder = (CustomCurrencyPayOrder) doViewByLock(getOrigorderno(), CustomCurrencyPayOrder.class, null, connectionFactory);
            this.setOrigstatus(payOrder.getStatus());
            payOrder.setStatus(generateOrigOrderStatus());
            payOrder.setRefundamont(payOrder.getRefundamont() + getAmont());
            payOrder.addUpdateIncludes(new String[]{"refundamont"});
            return payOrder.doUpdateOrder(connectionFactory);
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
            CustomCurrencyPayOrder payOrder = (CustomCurrencyPayOrder) doViewByLock(getOrigorderno(), CustomCurrencyPayOrder.class, null, connectionFactory);
            payOrder.setStatus(generateOrigOrderStatus());
            if (getStatus().equals(OrderStatusEnum.success.getValue())) {
                this.setRefunddate(CommonTools.getNowTimeString());
                this.addUpdateIncludes(new String[]{"businesstradeno", "notifydate", "processdate", "userid", "refunddate"});
            } else if (getStatus().equals(OrderStatusEnum.failed.getValue()) || (getStatus().equals(OrderStatusEnum.cancle.getValue()))) {
                payOrder.setRefundamont(payOrder.getRefundamont() - getAmont());
                payOrder.addUpdateIncludes(new String[]{"refundamont"});
                if (this.getOrigstatus().equals(OrderStatusEnum.paySuccess) || this.getOrigstatus().equals(OrderStatusEnum.reviewing)) {
                    payOrder.setStatus(this.getOrigstatus());
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
}

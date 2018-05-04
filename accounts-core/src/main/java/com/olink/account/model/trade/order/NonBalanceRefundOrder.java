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
 * Created by zhangbin on 2016/9/25.
 * 非余额退款订单
 */
@ADBTable(tablename = "acc_order_nonbalancerefund", isSeparate = true)
public class NonBalanceRefundOrder extends OrderBase {

    public NonBalanceRefundOrder() {
        super(OrderTypeEnum.NonBalanceRefund);
    }

    public String getOrigorderno() {
        return origorderno;
    }

    public void setOrigorderno(String origorderno) {
        this.origorderno = origorderno;
    }

    public String getOrigtradeno() {
        return origtradeno;
    }

    public void setOrigtradeno(String origtradeno) {
        this.origtradeno = origtradeno;
    }

    public OrderStatusEnum getOrigstatus() throws EnumValueUndefinedException {
        return OrderStatusEnum.getEnum(origstatus);
    }

    public void setOrigstatus(OrderStatusEnum origstatus) {
        this.origstatus = origstatus.getValue();
    }

    public String getBuyerid() {
        return buyerid;
    }

    public void setBuyerid(String buyerid) {
        this.buyerid = buyerid;
    }

    public String getBuyername() {
        return buyername;
    }

    public void setBuyername(String buyername) {
        this.buyername = buyername;
    }

    public String getBuyeraccount() {
        return buyeraccount;
    }

    public void setBuyeraccount(String buyeraccount) {
        this.buyeraccount = buyeraccount;
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

    private final Logger log = Logger.getLogger(this.getClass());

    @ADBTableField(name = "origorderno", fieldType = DBTableFieldType.String, allowNull = false)
    private String origorderno;//原支付订单号

    @ADBTableField(name = "origstatus", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int origstatus;//原支付订单退款前状态

    @ADBTableField(name = "origtradeno", fieldType = DBTableFieldType.String, allowNull = false)
    private String origtradeno;//原第三方交易号

    @ADBTableField(name = "buyerid", fieldType = DBTableFieldType.String)
    private String buyerid;//购买人第三方id

    @ADBTableField(name = "buyername", fieldType = DBTableFieldType.String)
    private String buyername;//购买人银行卡开户名称

    @ADBTableField(name = "buyeraccount", fieldType = DBTableFieldType.String)
    private String buyeraccount;//购买人第三方账号名

    @ADBTableField(name = "refunddate", fieldType = DBTableFieldType.String)
    private String refunddate;//退款时间

    @ADBTableField(name = "reason", fieldType = DBTableFieldType.String, allowNull = false)
    private String reason;//退款原因

    @Override
    public AccountResult validateOrder(ConnectionFactory connectionFactory) {
        NonBalancePayOrder payOrder = (NonBalancePayOrder) doViewByLock(getOrigorderno(), NonBalancePayOrder.class, null, connectionFactory);
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
            NonBalancePayOrder payOrder = (NonBalancePayOrder) doViewByLock(getOrigorderno(), NonBalancePayOrder.class, null, connectionFactory);
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
            NonBalancePayOrder payOrder = (NonBalancePayOrder) doViewByLock(getOrigorderno(), NonBalancePayOrder.class, null, connectionFactory);
            payOrder.setStatus(generateOrigOrderStatus());
            if (getStatus().equals(OrderStatusEnum.success.getValue())) {
                this.setRefunddate(CommonTools.getNowTimeString());
                this.addUpdateIncludes(new String[]{"businesstradeno", "tradeno", "tradestatus", "notifydate", "processdate", "userid", "refunddate", "buyerid", "buyername", "buyeraccount"});
            } else if (getStatus().equals(OrderStatusEnum.failed.getValue()) || getStatus().equals(OrderStatusEnum.cancle.getValue())) {
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

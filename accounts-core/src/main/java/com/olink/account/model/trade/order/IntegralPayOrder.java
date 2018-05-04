package com.olink.account.model.trade.order;

import com.olink.account.enumration.*;
import com.olink.account.core.AccountResult;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import org.apache.log4j.Logger;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/10/7.
 * 积分消费订单
 */
@ADBTable(tablename = "acc_order_integralpay", isSeparate = true)
public class IntegralPayOrder extends OrderBase {

    public IntegralPayOrder() {
        super(OrderTypeEnum.IntegralPay);
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

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    @ADBTableField(name = "beforebalance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal beforebalance;//消费前余额

    @ADBTableField(name = "afterbalance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal afterbalance;//消费后余额

    @ADBTableField(name = "recebeforebalance", fieldType = DBTableFieldType.Decimal)
    private BigDecimal recebeforebalance;//收款方收款前余额

    @ADBTableField(name = "receafterbalance", fieldType = DBTableFieldType.Decimal)
    private BigDecimal receafterbalance;//收款方收款后余额

    @ADBTableField(name = "paydate", fieldType = DBTableFieldType.String, allowNull = false)
    private String paydate;//消费时间

    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    public AccountResult validateOrder(ConnectionFactory connectionFactory) {
        AccountResult result = new AccountResult();
        result.setStatus(ResultStatusEnum.success);
        return result;
    }

    @Override
    public boolean beforeCreateOrder() {
        return beforeCreateOrder(new ConnectionFactory());
    }

    @Override
    public boolean beforeCreateOrder(ConnectionFactory connectionFactory) {
        this.paydate = CommonTools.getNowTimeString();
        //C户积分减少
        this.setBeforebalance(getAccountResult().getResultBeforBalance(ChangeAccountTypeEnum.Cust, AccountTypeEnum.Integral, AmontTypeEnum.balance));
        this.setAfterbalance(getAccountResult().getResultAfterBalance(ChangeAccountTypeEnum.Cust, AccountTypeEnum.Integral, AmontTypeEnum.balance));
        this.addUpdateExcludes(new String[]{"recebeforebalance", "receafterbalance"});
        return true;
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
            if (getStatus().equals(OrderStatusEnum.success.getValue())) {
                //B账户积分增加
                this.setRecebeforebalance(getAccountResult().getResultBeforBalance(ChangeAccountTypeEnum.DisBusiness, AccountTypeEnum.Integral, AmontTypeEnum.balance));
                this.setReceafterbalance(getAccountResult().getResultAfterBalance(ChangeAccountTypeEnum.DisBusiness, AccountTypeEnum.Integral, AmontTypeEnum.balance));
                this.addUpdateIncludes(new String[]{"notifydate", "processdate", "userid", "recebeforebalance", "receafterbalance"});
            } else if (getStatus().equals(OrderStatusEnum.failed.getValue()) || getStatus().equals(OrderStatusEnum.cancle.getValue())) {
                //C户积分增加
                this.setAfterbalance(this.getBeforebalance());
                this.addUpdateIncludes(new String[]{"afterbalance", "notifydate", "processdate", "userid"});
            }
            return true;
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

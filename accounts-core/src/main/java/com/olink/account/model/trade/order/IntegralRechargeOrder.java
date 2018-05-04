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
 * 积分获取订单
 */
@ADBTable(tablename = "acc_order_integralrecharge", isSeparate = true)
public class IntegralRechargeOrder extends OrderBase {

    public IntegralRechargeOrder() {
        super(OrderTypeEnum.IntegralRecharge);
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

    public String getRechargedate() {
        return rechargedate;
    }

    public void setRechargedate(String rechargedate) {
        this.rechargedate = rechargedate;
    }

    @ADBTableField(name = "beforebalance", fieldType = DBTableFieldType.Decimal)
    private BigDecimal beforebalance;//获取前余额

    @ADBTableField(name = "afterbalance", fieldType = DBTableFieldType.Decimal)
    private BigDecimal afterbalance;//获取后余额

    @ADBTableField(name = "rechargedate", fieldType = DBTableFieldType.String, allowNull = false)
    private String rechargedate;//获取时间

    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    public boolean doValidateOrder(ConnectionFactory connectionFactory) {
        return false;
    }

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
        this.rechargedate = CommonTools.getNowTimeString();
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
                //C户积分增加
                this.setBeforebalance(getAccountResult().getResultBeforBalance(ChangeAccountTypeEnum.DisCust, AccountTypeEnum.Integral, AmontTypeEnum.balance));
                this.setAfterbalance(getAccountResult().getResultAfterBalance(ChangeAccountTypeEnum.DisCust, AccountTypeEnum.Integral, AmontTypeEnum.balance));
                this.addUpdateIncludes(new String[]{"businesstradeno", "notifydate", "processdate", "userid", "beforebalance", "afterbalance"});
            } else if (getStatus().equals(OrderStatusEnum.failed.getValue()) || getStatus().equals(OrderStatusEnum.cancle.getValue())) {
                this.addUpdateIncludes(new String[]{"notifydate", "processdate", "userid"});
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

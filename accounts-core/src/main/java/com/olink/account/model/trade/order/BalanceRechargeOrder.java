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
 * Created by zhangbin on 2016/9/12.
 * 充值订单
 */
@ADBTable(tablename = "acc_order_balancerecharge", isSeparate = true)
public class BalanceRechargeOrder extends OrderBase {

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

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    @ADBTableField(name = "buyerid", fieldType = DBTableFieldType.String)
    private String buyerid;//购买人第三方id

    @ADBTableField(name = "buyername", fieldType = DBTableFieldType.String)
    private String buyername;//购买人银行卡开户名称

    @ADBTableField(name = "buyeraccount", fieldType = DBTableFieldType.String)
    private String buyeraccount;//购买人第三方账号名

    @ADBTableField(name = "beforebalance", fieldType = DBTableFieldType.Decimal)
    private BigDecimal beforebalance;//充值前余额

    @ADBTableField(name = "afterbalance", fieldType = DBTableFieldType.Decimal)
    private BigDecimal afterbalance;//充值后余额

    @ADBTableField(name = "paydate", fieldType = DBTableFieldType.String, allowNull = false)
    private String paydate;//支付时间

    private final Logger log = Logger.getLogger(this.getClass());

    public BalanceRechargeOrder() {
        super(OrderTypeEnum.BalanceRecharge);
    }

    @Override
    public boolean beforeCreateOrder() {
        return beforeCreateOrder(new ConnectionFactory());
    }

    @Override
    public boolean beforeCreateOrder(ConnectionFactory connectionFactory) {
        //运营账户总余额、可用余额（待结算余额）增加，C户总余额增加
        this.paydate = CommonTools.getNowTimeString();
        this.addUpdateExcludes(new String[]{"beforebalance", "afterbalance"});
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
                //C户可用余额增加
                this.setBeforebalance(getAccountResult().getResultBeforBalance(ChangeAccountTypeEnum.DisCust, AccountTypeEnum.Change, AmontTypeEnum.balance));
                this.setAfterbalance(getAccountResult().getResultAfterBalance(ChangeAccountTypeEnum.DisCust, AccountTypeEnum.Change, AmontTypeEnum.balance));
                this.addUpdateIncludes(new String[]{"businesstradeno", "tradeno", "tradestatus", "notifydate", "processdate", "userid", "buyerid", "buyername", "buyeraccount", "beforebalance", "afterbalance"});
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

    @Override
    public AccountResult validateOrder(ConnectionFactory connectionFactory) {
        AccountResult result = new AccountResult();
        result.setStatus(ResultStatusEnum.success);
        return result;
    }
}

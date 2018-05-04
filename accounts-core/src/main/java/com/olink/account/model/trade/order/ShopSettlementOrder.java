package com.olink.account.model.trade.order;

import com.olink.account.enumration.OrderTypeEnum;
import com.olink.account.enumration.ResultStatusEnum;
import com.olink.account.core.AccountResult;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/12/4.
 * 二级商户结算订单
 */
@ADBTable(tablename = "acc_order_shopsettlement", isSeparate = true)
public class ShopSettlementOrder extends OrderBase {

    public String getShopno() {
        return shopno;
    }

    public void setShopno(String shopno) {
        this.shopno = shopno;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public double getRate() {
        return rate.doubleValue();
    }

    public void setRate(double rate) {
        this.rate = BigDecimal.valueOf(rate);
    }

    public String getPlansettlementdate() {
        return plansettlementdate;
    }

    public void setPlansettlementdate(String plansettlementdate) {
        this.plansettlementdate = plansettlementdate;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSettlementdate() {
        return settlementdate;
    }

    public void setSettlementdate(String settlementdate) {
        this.settlementdate = settlementdate;
    }

    @ADBTableField(name = "shopno", fieldType = DBTableFieldType.String, allowNull = false)
    private String shopno;//开户行名称

    @ADBTableField(name = "accounttype", fieldType = DBTableFieldType.String, allowNull = false)
    private String accounttype;//结算账户类型

    @ADBTableField(name = "bankname", fieldType = DBTableFieldType.String)
    private String bankname;//开户行名称

    @ADBTableField(name = "accountname", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountname;//账户名称

    @ADBTableField(name = "account", fieldType = DBTableFieldType.String, allowNull = false)
    private String account;//第三方账号名

    @ADBTableField(name = "rate", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal rate;//费率

    @ADBTableField(name = "plansettlementdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String plansettlementdate;//商户计划结算时间

    @ADBTableField(name = "settlementdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String settlementdate;//结算时间

    @ADBTableField(name = "remark", fieldType = DBTableFieldType.String, allowNull = false)
    private String remark;//附加说明

    public ShopSettlementOrder() {
        super(OrderTypeEnum.ShopSettlement);
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
        this.settlementdate = CommonTools.getNowTimeString();
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
        updateProcessDate(getUserid());
        this.addUpdateIncludes(new String[]{"notifydate", "processdate", "userid"});
        return true;
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

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
import org.apache.log4j.Logger;

/**
 * Created by zhangbin on 2016/10/4.
 * 自有币种消费订单
 */
@ADBTable(tablename = "acc_order_currencypay", isSeparate = true)
public class CustomCurrencyPayOrder extends OrderBase {

    public CustomCurrencyPayOrder() {
        super(OrderTypeEnum.CustomCurrencyPay);
    }

    public String getPaydate() {
        return paydate;
    }

    public void setPaydate(String paydate) {
        this.paydate = paydate;
    }

    public String getCurrencyname() {
        return currencyname;
    }

    public void setCurrencyname(String currencyname) {
        this.currencyname = currencyname;
    }

    @ADBTableField(name = "currencyname", fieldType = DBTableFieldType.String, allowNull = false)
    private String currencyname;//币种名称

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
        this.paydate = CommonTools.getNowTimeString();
        return true;
    }

    @Override
    public boolean beforeCreateOrder(ConnectionFactory connectionFactory) {
        return beforeCreateOrder();
    }

    @Override
    public void afterCreateOrder() {

    }

    @Override
    public void afterCreateOrder(ConnectionFactory connectionFactory) {

    }

    @Override
    public boolean beforeUpdateOrder() {
        return false;
    }

    @Override
    public boolean beforeUpdateOrder(ConnectionFactory connectionFactory) {
        updateNotifyDate();
        updateProcessDate(null);
        try {
            if (getStatus().equals(OrderStatusEnum.success.getValue())) {
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

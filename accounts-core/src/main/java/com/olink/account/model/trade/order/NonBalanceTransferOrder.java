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
 * Created by zhangbin on 2016/10/7.
 * 非余额转账订单
 */
@ADBTable(tablename = "acc_order_nonbalancetransfer", isSeparate = true)
public class NonBalanceTransferOrder extends OrderBase {

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

    public String getRecetype() {
        return recetype;
    }

    public void setRecetype(String recetype) {
        this.recetype = recetype;
    }

    public String getRecebankname() {
        return recebankname;
    }

    public void setRecebankname(String recebankname) {
        this.recebankname = recebankname;
    }

    public String getRecename() {
        return recename;
    }

    public void setRecename(String recename) {
        this.recename = recename;
    }

    public String getReceaccount() {
        return receaccount;
    }

    public void setReceaccount(String receaccount) {
        this.receaccount = receaccount;
    }

    public String getTransferdate() {
        return transferdate;
    }

    public void setTransferdate(String transferdate) {
        this.transferdate = transferdate;
    }

    @ADBTableField(name = "buyerid", fieldType = DBTableFieldType.String)
    private String buyerid;//购买人第三方id

    @ADBTableField(name = "buyername", fieldType = DBTableFieldType.String)
    private String buyername;//购买人银行卡开户名称

    @ADBTableField(name = "buyeraccount", fieldType = DBTableFieldType.String)
    private String buyeraccount;//购买人第三方账号名

    @ADBTableField(name = "recetype", fieldType = DBTableFieldType.String, allowNull = false)
    private String recetype;//收款人账户类型

    @ADBTableField(name = "recebankname", fieldType = DBTableFieldType.String)
    private String recebankname;//收款人开户行名称

    @ADBTableField(name = "recename", fieldType = DBTableFieldType.String, allowNull = false)
    private String recename;//收款人银行卡开户名称

    @ADBTableField(name = "receaccount", fieldType = DBTableFieldType.String, allowNull = false)
    private String receaccount;//收款人第三方账号名

    @ADBTableField(name = "transferdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String transferdate;//转账时间

    private final Logger log = Logger.getLogger(this.getClass());

    public NonBalanceTransferOrder() {
        super(OrderTypeEnum.NonBalanceTransfer);
    }

    @Override
    public AccountResult validateOrder(ConnectionFactory connectionFactory) {
        AccountResult result = new AccountResult();
        result.setStatus(ResultStatusEnum.success);
        return result;
    }

    @Override
    public boolean beforeCreateOrder() {
        this.transferdate = CommonTools.getNowTimeString();
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
        return beforeUpdateOrder(new ConnectionFactory());
    }

    @Override
    public boolean beforeUpdateOrder(ConnectionFactory connectionFactory) {
        updateNotifyDate();
        updateProcessDate(null);
        try {
            if (getStatus().equals(OrderStatusEnum.success.getValue())) {
                this.addUpdateIncludes(new String[]{"businesstradeno", "tradeno", "tradestatus", "buyerid", "buyername", "buyeraccount", "notifydate", "processdate", "userid"});
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

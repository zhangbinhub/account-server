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
 * 余额转账订单
 */
@ADBTable(tablename = "acc_order_balancetransfer", isSeparate = true)
public class BalanceTransferOrder extends OrderBase {

    public BalanceTransferOrder() {
        super(OrderTypeEnum.BalanceTransfer);
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

    public String getTransferdate() {
        return transferdate;
    }

    public void setTransferdate(String transferdate) {
        this.transferdate = transferdate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @ADBTableField(name = "beforebalance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal beforebalance;//支付前余额

    @ADBTableField(name = "afterbalance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal afterbalance;//支付后余额

    @ADBTableField(name = "recebeforebalance", fieldType = DBTableFieldType.Decimal)
    private BigDecimal recebeforebalance;//收款方收款前余额

    @ADBTableField(name = "receafterbalance", fieldType = DBTableFieldType.Decimal)
    private BigDecimal receafterbalance;//收款方收款后余额

    @ADBTableField(name = "transferdate", fieldType = DBTableFieldType.String)
    private String transferdate;//转账时间

    private String password;

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
        this.transferdate = CommonTools.getNowTimeString();
        //目标C户总余额增加，C户总余额、可用余额减少
        this.setBeforebalance(getAccountResult().getResultBeforBalance(ChangeAccountTypeEnum.Cust, AccountTypeEnum.Change, AmontTypeEnum.balance));
        this.setAfterbalance(getAccountResult().getResultAfterBalance(ChangeAccountTypeEnum.Cust, AccountTypeEnum.Change, AmontTypeEnum.balance));
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
                //目标C户可用余额增加
                this.setRecebeforebalance(getAccountResult().getResultBeforBalance(ChangeAccountTypeEnum.DisCust, AccountTypeEnum.Change, AmontTypeEnum.balance));
                this.setReceafterbalance(getAccountResult().getResultAfterBalance(ChangeAccountTypeEnum.DisCust, AccountTypeEnum.Change, AmontTypeEnum.balance));
                this.addUpdateIncludes(new String[]{"notifydate", "processdate", "userid", "recebeforebalance", "receafterbalance"});
            } else if (getStatus().equals(OrderStatusEnum.failed.getValue()) || getStatus().equals(OrderStatusEnum.cancle.getValue())) {
                //目标C户总余额减少，C户总余额、可用余额增加
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

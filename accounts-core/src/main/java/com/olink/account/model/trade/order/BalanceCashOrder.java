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
 * Created by zhangbin on 2016/9/29.
 * 余额提现订单
 */
@ADBTable(tablename = "acc_order_balancecash", isSeparate = true)
public class BalanceCashOrder extends OrderBase {

    public String getBuyerbankname() {
        return buyerbankname;
    }

    public void setBuyerbankname(String buyerbankname) {
        this.buyerbankname = buyerbankname;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCashdate() {
        return cashdate;
    }

    public void setCashdate(String cashdate) {
        this.cashdate = cashdate;
    }

    public String getCbindtype() {
        return cbindtype;
    }

    public void setCbindtype(String cbindtype) {
        this.cbindtype = cbindtype;
    }

    public String getBindinfoid() {
        return bindinfoid;
    }

    public void setBindinfoid(String bindinfoid) {
        this.bindinfoid = bindinfoid;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    @ADBTableField(name = "cbindtype", fieldType = DBTableFieldType.String, allowNull = false)
    private String cbindtype;//客户第三方类型

    @ADBTableField(name = "buyerbankname", fieldType = DBTableFieldType.String)
    private String buyerbankname;//开户行名称

    @ADBTableField(name = "buyername", fieldType = DBTableFieldType.String, allowNull = false)
    private String buyername;//客户银行卡开户名称

    @ADBTableField(name = "buyeraccount", fieldType = DBTableFieldType.String, allowNull = false)
    private String buyeraccount;//客户第三方账号名

    @ADBTableField(name = "beforebalance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal beforebalance;//提现前余额

    @ADBTableField(name = "afterbalance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal afterbalance;//提现后余额

    @ADBTableField(name = "remark", fieldType = DBTableFieldType.String)
    private String remark;//附加说明

    @ADBTableField(name = "cashdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String cashdate;//提取时间

    private String bindinfoid;//绑定信息id

    private String password;//支付密码

    private String opinion;//审核意见

    private final Logger log = Logger.getLogger(this.getClass());

    public BalanceCashOrder() {
        super(OrderTypeEnum.BalanceCash);
    }

    @Override
    public boolean beforeCreateOrder() {
        return beforeCreateOrder(new ConnectionFactory());
    }

    @Override
    public boolean beforeCreateOrder(ConnectionFactory connectionFactory) {
        this.cashdate = CommonTools.getNowTimeString();
        //账务平台可用余额减少，C户可用余额减少
        this.setBeforebalance(getAccountResult().getResultBeforBalance(ChangeAccountTypeEnum.Cust, AccountTypeEnum.Change, AmontTypeEnum.balance));
        this.setAfterbalance(getAccountResult().getResultAfterBalance(ChangeAccountTypeEnum.Cust, AccountTypeEnum.Change, AmontTypeEnum.balance));
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
        try {
            if (getStatus().equals(OrderStatusEnum.success.getValue())) {
                this.addUpdateIncludes(new String[]{"notifydate", "processdate", "userid"});
            } else if (getStatus().equals(OrderStatusEnum.failed.getValue()) || getStatus().equals(OrderStatusEnum.cancle.getValue())) {
                this.addUpdateIncludes(new String[]{"afterbalance", "notifydate", "processdate", "userid"});
                this.setAfterbalance(this.getBeforebalance());
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

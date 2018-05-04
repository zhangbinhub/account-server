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
 * Created by zhangbin on 2016/10/17.
 * 积分结算订单
 */
@ADBTable(tablename = "acc_order_integralsettlement", isSeparate = true)
public class IntegralSettlementOrder extends OrderBase {

    public double getMoney() {
        return money.doubleValue();
    }

    public void setMoney(double money) {
        this.money = BigDecimal.valueOf(money);
    }

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

    public String getSettlementdate() {
        return settlementdate;
    }

    public void setSettlementdate(String settlementdate) {
        this.settlementdate = settlementdate;
    }

    public String getBbindtype() {
        return bbindtype;
    }

    public void setBbindtype(String bbindtype) {
        this.bbindtype = bbindtype;
    }

    public String getBindinfoid() {
        return bindinfoid;
    }

    public void setBindinfoid(String bindinfoid) {
        this.bindinfoid = bindinfoid;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    @ADBTableField(name = "money", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal money;//结算金额

    @ADBTableField(name = "bbindtype", fieldType = DBTableFieldType.String, allowNull = false)
    private String bbindtype;//客户第三方类型

    @ADBTableField(name = "buyerbankname", fieldType = DBTableFieldType.String)
    private String buyerbankname;//开户行名称

    @ADBTableField(name = "buyername", fieldType = DBTableFieldType.String, allowNull = false)
    private String buyername;//客户银行卡开户名称

    @ADBTableField(name = "buyeraccount", fieldType = DBTableFieldType.String, allowNull = false)
    private String buyeraccount;//客户第三方账号名

    @ADBTableField(name = "beforebalance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal beforebalance;//结算前积分

    @ADBTableField(name = "afterbalance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal afterbalance;//结算后积分

    @ADBTableField(name = "remark", fieldType = DBTableFieldType.String)
    private String remark;//附加说明

    @ADBTableField(name = "settlementdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String settlementdate;//提取时间

    private String bindinfoid;//绑定信息id

    private String opinion;//审核意见

    private final Logger log = Logger.getLogger(this.getClass());

    public IntegralSettlementOrder() {
        super(OrderTypeEnum.IntegralSettlement);
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
        //账务平台积分减少，B户积分减少
        this.setBeforebalance(getAccountResult().getResultBeforBalance(ChangeAccountTypeEnum.Business, AccountTypeEnum.Integral, AmontTypeEnum.balance));
        this.setAfterbalance(getAccountResult().getResultAfterBalance(ChangeAccountTypeEnum.Business, AccountTypeEnum.Integral, AmontTypeEnum.balance));
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
                this.addUpdateIncludes(new String[]{"afterbalance"});
                this.addUpdateIncludes(new String[]{"notifydate", "processdate", "userid"});
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
}

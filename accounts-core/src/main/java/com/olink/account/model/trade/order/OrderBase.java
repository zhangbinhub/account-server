package com.olink.account.model.trade.order;

import com.olink.account.enumration.*;
import com.olink.account.core.AccountEntrance;
import com.olink.account.core.AccountResult;
import com.olink.account.model.trade.accounting.EntryItemParam;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.dictionary.D_DateParam;
import com.olink.account.model.trade.dictionary.D_OrderType;
import com.olink.account.utils.CodeFactory;
import com.olink.account.utils.DBAccess;
import pers.acp.gateway.tradeorder.base.BusinessOrder;
import pers.acp.tools.common.CalendarTools;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.common.DBConTools;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.security.key.KeyManagement;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by zhangbin on 2016/9/12.
 * 订单基类
 */
@ADBTable(tablename = "acc_order_base")
public abstract class OrderBase extends BusinessOrder {

    public OrderBase() {
        super();
        DBConTools dbTools = DBAccess.getTradeInstance().getDBTool();
        Map<String, Object> param = new HashMap<>();
        param.put(BusinessAccount.class.getCanonicalName() + ".isdefault", DefaultEnum.isdefault.getValue());
        param.put(BusinessAccount.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
        List<DBTable> businessAccounts = dbTools.getDataObjListBySql(param, BusinessAccount.class, null, null);
        if (businessAccounts.size() > 0) {
            BusinessAccount businessAccount = (BusinessAccount) businessAccounts.get(0);
            yyBusinessId = businessAccount.getBusinessid();
        } else {
            log.error("找不到运营B户");
        }
    }

    public OrderBase(OrderTypeEnum orderTypeEnum) {
        this();
        this.ordertype = orderTypeEnum.getValue();
    }

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public String getRecebusinessid() {
        return recebusinessid;
    }

    public void setRecebusinessid(String recebusinessid) {
        this.recebusinessid = recebusinessid;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getBusinesssubaccountcode() {
        return businesssubaccountcode;
    }

    public void setBusinesssubaccountcode(String businesssubaccountcode) {
        this.businesssubaccountcode = businesssubaccountcode;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getRececustid() {
        return rececustid;
    }

    public void setRececustid(String rececustid) {
        this.rececustid = rececustid;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
    }

    public double getAmont() {
        return amont.doubleValue();
    }

    public void setAmont(double amont) {
        this.amont = BigDecimal.valueOf(amont);
    }

    public double getRatio() {
        return ratio.doubleValue();
    }

    public void setRatio(double ratio) {
        this.ratio = BigDecimal.valueOf(ratio);
    }

    public double getActamont() {
        return actamont.doubleValue();
    }

    public double getRefundamont() {
        return refundamont.doubleValue();
    }

    public void setRefundamont(double refundamont) {
        this.refundamont = BigDecimal.valueOf(refundamont);
    }

    public double getRefundactamont() {
        return refundactamont.doubleValue();
    }

    public void setRefundactamont(double refundactamont) {
        this.refundactamont = BigDecimal.valueOf(refundactamont);
    }

    public double getExtamont1() {
        return extamont1.doubleValue();
    }

    public void setExtamont1(double extamont1) {
        this.extamont1 = BigDecimal.valueOf(extamont1);
    }

    public double getExtamont2() {
        return extamont2.doubleValue();
    }

    public void setExtamont2(double extamont2) {
        this.extamont2 = BigDecimal.valueOf(extamont2);
    }

    public double getExtamont3() {
        return extamont3.byteValueExact();
    }

    public void setExtamont3(double extamont3) {
        this.extamont3 = BigDecimal.valueOf(extamont3);
    }

    public double getExtamont4() {
        return extamont4.doubleValue();
    }

    public void setExtamont4(double extamont4) {
        this.extamont4 = BigDecimal.valueOf(extamont4);
    }

    public double getExtamont5() {
        return extamont5.doubleValue();
    }

    public void setExtamont5(double extamont5) {
        this.extamont5 = BigDecimal.valueOf(extamont5);
    }

    public double getExtamont6() {
        return extamont6.doubleValue();
    }

    public void setExtamont6(double extamont6) {
        this.extamont6 = BigDecimal.valueOf(extamont6);
    }

    public double getExtamont7() {
        return extamont7.doubleValue();
    }

    public void setExtamont7(double extamont7) {
        this.extamont7 = BigDecimal.valueOf(extamont7);
    }

    public double getExtamont8() {
        return extamont8.doubleValue();
    }

    public void setExtamont8(double extamont8) {
        this.extamont8 = BigDecimal.valueOf(extamont8);
    }

    public double getExtamont9() {
        return extamont9.doubleValue();
    }

    public void setExtamont9(double extamont9) {
        this.extamont9 = BigDecimal.valueOf(extamont9);
    }

    public double getExtamont10() {
        return extamont10.doubleValue();
    }

    public void setExtamont10(double extamont10) {
        this.extamont10 = BigDecimal.valueOf(extamont10);
    }

    public String getAccountdate() {
        return accountdate;
    }

    public String getStatementdate() {
        return statementdate;
    }

    public String getBindaccountdate() {
        return bindaccountdate;
    }

    public String getCreatedate() {
        return createdate;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public OrderStatusEnum getStatus() throws EnumValueUndefinedException {
        return OrderStatusEnum.getEnum(status);
    }

    public void setStatus(OrderStatusEnum status) {
        this.status = status.getValue();
    }

    public int getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getTradeno() {
        return tradeno;
    }

    public void setTradeno(String tradeno) {
        this.tradeno = tradeno;
    }

    public String getTradestatus() {
        return tradestatus;
    }

    public void setTradestatus(String tradestatus) {
        this.tradestatus = tradestatus;
    }

    public String getNotifydate() {
        return notifydate;
    }

    public void setNotifydate(String notifydate) {
        this.notifydate = notifydate;
    }

    public String getProcessdate() {
        return processdate;
    }

    public void setProcessdate(String processdate) {
        this.processdate = processdate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLastmodifydate() {
        return lastmodifydate;
    }

    public void setLastmodifydate(String lastmodifydate) {
        this.lastmodifydate = lastmodifydate;
    }

    public void setOrdertype(OrderTypeEnum orderTypeEnum) {
        this.ordertype = orderTypeEnum.getValue();
    }

    public int getOrdertype() {
        return ordertype;
    }

    public String getCustsubaccountcode() {
        return custsubaccountcode;
    }

    public void setCustsubaccountcode(String custsubaccountcode) {
        this.custsubaccountcode = custsubaccountcode;
    }

    public String getBusinesstradeno() {
        return businesstradeno;
    }

    public void setBusinesstradeno(String businesstradeno) {
        this.businesstradeno = businesstradeno;
    }

    public String getPaytypename() {
        return paytypename;
    }

    public void setPaytypename(String paytypename) {
        this.paytypename = paytypename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getSuramont() {
        return suramont.doubleValue();
    }

    public void setSuramont(double suramont) {
        this.suramont = BigDecimal.valueOf(suramont);
    }

    public String getRemark1() {
        return remark1;
    }

    public void setRemark1(String remark1) {
        this.remark1 = remark1;
    }

    public String getRemark2() {
        return remark2;
    }

    public void setRemark2(String remark2) {
        this.remark2 = remark2;
    }

    public String getRemark3() {
        return remark3;
    }

    public void setRemark3(String remark3) {
        this.remark3 = remark3;
    }

    public String getRemark4() {
        return remark4;
    }

    public void setRemark4(String remark4) {
        this.remark4 = remark4;
    }

    public String getRemark5() {
        return remark5;
    }

    public void setRemark5(String remark5) {
        this.remark5 = remark5;
    }

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public String getField6() {
        return field6;
    }

    public void setField6(String field6) {
        this.field6 = field6;
    }

    public String getField7() {
        return field7;
    }

    public void setField7(String field7) {
        this.field7 = field7;
    }

    public String getField8() {
        return field8;
    }

    public void setField8(String field8) {
        this.field8 = field8;
    }

    public String getField9() {
        return field9;
    }

    public void setField9(String field9) {
        this.field9 = field9;
    }

    public String getField10() {
        return field10;
    }

    public void setField10(String field10) {
        this.field10 = field10;
    }

    public String getYyBusinessId() {
        return yyBusinessId;
    }

    public AccountResult getAccountResult() {
        return accountResult;
    }

    public void setEntryItemParams(List<EntryItemParam> entryItemParams) {
        this.entryItemParams = entryItemParams;
    }

    public List<EntryItemParam> getEntryItemParams() {
        return entryItemParams;
    }

    @ADBTableField(name = "ordertype", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int ordertype;//订单类型 enum OrderTypeEnum.value

    @ADBTableField(name = "businessid", fieldType = DBTableFieldType.String, allowNull = false)
    private String businessid;//B户客户号

    @ADBTableField(name = "recebusinessid", fieldType = DBTableFieldType.String)
    private String recebusinessid;//收款方B户客户号

    @ADBTableField(name = "businessname", fieldType = DBTableFieldType.String, allowNull = false)
    private String businessname;//B户名称

    @ADBTableField(name = "businesstradeno", fieldType = DBTableFieldType.String, allowNull = false)
    private String businesstradeno;//商户交易号

    @ADBTableField(name = "businesssubaccountcode", fieldType = DBTableFieldType.String)
    private String businesssubaccountcode;//B户虚拟账户编号

    @ADBTableField(name = "custid", fieldType = DBTableFieldType.String, allowNull = false)
    private String custid;//C户客户号

    @ADBTableField(name = "rececustid", fieldType = DBTableFieldType.String)
    private String rececustid;//收款方C户客户号

    @ADBTableField(name = "custsubaccountcode", fieldType = DBTableFieldType.String)
    private String custsubaccountcode;//C户虚拟账户编号

    @ADBTableField(name = "paytype", fieldType = DBTableFieldType.String)
    private String paytype;//支付方式

    @ADBTableField(name = "paytypename", fieldType = DBTableFieldType.String)
    private String paytypename;//支付方式名称

    @ADBTableField(name = "amont", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal amont;//交易额

    @ADBTableField(name = "ratio", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal ratio = BigDecimal.valueOf(1.00D);//实际交易额比例

    @ADBTableField(name = "actamont", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal actamont;//交易金额

    @ADBTableField(name = "accountdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountdate;//订单会计日期

    @ADBTableField(name = "statementdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String statementdate;//会计出账日期

    @ADBTableField(name = "bindaccountdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String bindaccountdate;//会计扎账日期

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;//订单创建时间

    @ADBTableField(name = "channel", fieldType = DBTableFieldType.String, allowNull = false)
    private String channel;//交易渠道号

    @ADBTableField(name = "description", fieldType = DBTableFieldType.String, allowNull = false)
    private String description;//订单描述

    @ADBTableField(name = "status", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int status;

    @ADBTableField(name = "checkstatus ", fieldType = DBTableFieldType.Integer)
    private Integer checkstatus;

    @ADBTableField(name = "tradeno", fieldType = DBTableFieldType.String)
    private String tradeno;//第三方交易号

    @ADBTableField(name = "tradestatus", fieldType = DBTableFieldType.String)
    private String tradestatus;//第三方交易状态

    @ADBTableField(name = "notifydate", fieldType = DBTableFieldType.String)
    private String notifydate;//通知时间

    @ADBTableField(name = "processdate", fieldType = DBTableFieldType.String)
    private String processdate;//订单处理时间

    @ADBTableField(name = "userid", fieldType = DBTableFieldType.String)
    private String userid;//订单处理人id

    @ADBTableField(name = "lastmodifydate", fieldType = DBTableFieldType.String, allowNull = false)
    private String lastmodifydate;//订单最后修改时间

    @ADBTableField(name = "refundamont", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal refundamont = BigDecimal.valueOf(0.00D);//已退额

    @ADBTableField(name = "refundactamont", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal refundactamont = BigDecimal.valueOf(0.00D);//已退金额

    @ADBTableField(name = "extamont1", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal extamont1 = BigDecimal.valueOf(0.00D);//扩展额1

    @ADBTableField(name = "extamont2", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal extamont2 = BigDecimal.valueOf(0.00D);//扩展额2

    @ADBTableField(name = "extamont3", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal extamont3 = BigDecimal.valueOf(0.00D);//扩展额3

    @ADBTableField(name = "extamont4", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal extamont4 = BigDecimal.valueOf(0.00D);//扩展额4

    @ADBTableField(name = "extamont5", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal extamont5 = BigDecimal.valueOf(0.00D);//扩展额5

    @ADBTableField(name = "extamont6", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal extamont6 = BigDecimal.valueOf(0.00D);//扩展额6

    @ADBTableField(name = "extamont7", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal extamont7 = BigDecimal.valueOf(0.00D);//扩展额7

    @ADBTableField(name = "extamont8", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal extamont8 = BigDecimal.valueOf(0.00D);//扩展额8

    @ADBTableField(name = "extamont9", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal extamont9 = BigDecimal.valueOf(0.00D);//扩展额9

    @ADBTableField(name = "extamont10", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal extamont10 = BigDecimal.valueOf(0.00D);//扩展额10

    @ADBTableField(name = "remark1", fieldType = DBTableFieldType.String)
    private String remark1;//备注1

    @ADBTableField(name = "remark2", fieldType = DBTableFieldType.String)
    private String remark2;//备注2

    @ADBTableField(name = "remark3", fieldType = DBTableFieldType.String)
    private String remark3;//备注3

    @ADBTableField(name = "remark4", fieldType = DBTableFieldType.String)
    private String remark4;//备注4

    @ADBTableField(name = "remark5", fieldType = DBTableFieldType.String)
    private String remark5;//备注5

    @ADBTableField(name = "field1", fieldType = DBTableFieldType.String)
    private String field1;//	扩展字段1

    @ADBTableField(name = "field2", fieldType = DBTableFieldType.String)
    private String field2;//	扩展字段2

    @ADBTableField(name = "field3", fieldType = DBTableFieldType.String)
    private String field3;//	扩展字段3

    @ADBTableField(name = "field4", fieldType = DBTableFieldType.String)
    private String field4;//	扩展字段4

    @ADBTableField(name = "field5", fieldType = DBTableFieldType.String)
    private String field5;//	扩展字段5

    @ADBTableField(name = "field6", fieldType = DBTableFieldType.String)
    private String field6;//	扩展字段6

    @ADBTableField(name = "field7", fieldType = DBTableFieldType.String)
    private String field7;//	扩展字段7

    @ADBTableField(name = "field8", fieldType = DBTableFieldType.String)
    private String field8;//	扩展字段8

    @ADBTableField(name = "field9", fieldType = DBTableFieldType.String)
    private String field9;//	扩展字段9

    @ADBTableField(name = "field10", fieldType = DBTableFieldType.String)
    private String field10;//	扩展字段10

    private String yyBusinessId;//账务平台B户客户号

    private BigDecimal suramont;//可退额

    private AccountResult accountResult;//核心记账结果

    private List<EntryItemParam> entryItemParams;//自定义记账参数

    private final Logger log = Logger.getLogger(this.getClass());

    @Override
    public String generateOrderNo() {
        return CodeFactory.generateOrderNo(getOrdertype());
    }

    @Override
    public String generateOrderNo(ConnectionFactory connectionFactory) {
        return generateOrderNo();
    }

    /**
     * 是否允许更新订单信息
     *
     * @param connectionFactory 数据库连接
     * @return true|false
     */
    private boolean isEnableUpdateOrder(ConnectionFactory connectionFactory) throws Exception {
        OrderStatusEnum currOrderStatusEnum = getCurrOrderStatus(connectionFactory);
        OrderStatusEnum newOrderStatusEnum = getStatus();
        if (currOrderStatusEnum.equals(newOrderStatusEnum.getValue())) {//订单状态相等时
            return !(currOrderStatusEnum.equals(OrderStatusEnum.success.getValue()) || currOrderStatusEnum.equals(OrderStatusEnum.failed.getValue()) || currOrderStatusEnum.equals(OrderStatusEnum.cancle.getValue())
                    || currOrderStatusEnum.equals(OrderStatusEnum.refundSuccess.getValue()) || currOrderStatusEnum.equals(OrderStatusEnum.refundFail.getValue()) || currOrderStatusEnum.equals(OrderStatusEnum.refundCancle.getValue())
                    || currOrderStatusEnum.equals(OrderStatusEnum.adjustSuccess.getValue()) || currOrderStatusEnum.equals(OrderStatusEnum.adjustFail.getValue()) || currOrderStatusEnum.equals(OrderStatusEnum.adjustCancle.getValue()));
        } else {//订单状态不相等时
            if (currOrderStatusEnum.equals(OrderStatusEnum.create.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.paySuccess.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.reviewing.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.paySuccess.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.reviewing.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.success.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.failed.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.cancle.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.reviewing.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.success.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.failed.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.cancle.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.success.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefundSuccess.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.refunding.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.applyRefundSuccess.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.refunding.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.refundSuccess.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.refundFail.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.refundCancle.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.refunding.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.paySuccess.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.reviewing.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.refundSuccess.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.refundFail.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.refundCancle.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.refundSuccess.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.refundFail.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.refundCancle.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyAdjustSuccess.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.adjusting.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.applyAdjustSuccess.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.adjusting.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.adjustSuccess.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.adjustFail.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.adjustCancle.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.adjusting.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.adjustSuccess.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.adjustFail.getValue())
                        || newOrderStatusEnum.equals(OrderStatusEnum.adjustCancle.getValue());
            }
            if (currOrderStatusEnum.equals(OrderStatusEnum.adjustFail.getValue())) {
                return newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue());
            }
            return currOrderStatusEnum.equals(OrderStatusEnum.adjustCancle.getValue()) && (
                    newOrderStatusEnum.equals(OrderStatusEnum.applyRefund.getValue()) || newOrderStatusEnum.equals(OrderStatusEnum.applyAdjust.getValue()));
        }
    }

    @Override
    public boolean doCreateOrder() {
        return this.doCreateOrder(new ConnectionFactory());
    }

    @Override
    public boolean doCreateOrder(ConnectionFactory connectionFactory) {
        this.getUpdateExcludes().clear();
        this.getUpdateIncludes().clear();
        if (!doValidateOrderType(connectionFactory)) {
            return false;
        }
        try {
            updateAccountDate(connectionFactory);
        } catch (Exception e) {
            log.error("更新会计相关日期失败：" + e.getMessage(), e);
            return false;
        }
        updateCreateDate();
        setStatus(OrderStatusEnum.create);
        this.actamont = BigDecimal.valueOf(getAmont() * getRatio()).setScale(2, DecimalProcessModeEnum.Half_UP.getMode());
        AccountEntrance accountEntrance = AccountEntrance.getInstance(this, connectionFactory);
        if (accountEntrance != null) {
            accountResult = accountEntrance.doProcess();
            return accountResult.getStatus().equals(ResultStatusEnum.success.getCode()) && super.doCreateOrder(connectionFactory);
        } else {
            return false;
        }
    }

    @Override
    public boolean doUpdateOrder() {
        return this.doUpdateOrder(new ConnectionFactory());
    }

    @Override
    public boolean doUpdateOrder(ConnectionFactory dbcon) {
        if (!doValidateOrderType(dbcon)) {
            return false;
        }
        try {
            updateAccountDate(dbcon);
        } catch (Exception e) {
            log.error("更新会计相关日期失败：" + e.getMessage(), e);
            return false;
        }
        try {
            if (isEnableUpdateOrder(dbcon)) {
                AccountEntrance accountEntrance = AccountEntrance.getInstance(this, dbcon);
                if (accountEntrance != null) {
                    accountResult = accountEntrance.doProcess();
                    this.addUpdateIncludes(new String[]{"accountdate", "statementdate", "bindaccountdate", "status", "lastmodifydate"});
                    return accountResult.getStatus().equals(ResultStatusEnum.success.getCode()) && super.doUpdateOrder(dbcon);
                } else {
                    return false;
                }
            } else {
                log.error("订单信息不允许修改");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return false;
    }

    /**
     * 校验订单类型
     *
     * @return true|false
     */
    private boolean doValidateOrderType(ConnectionFactory connectionFactory) {
        if (this.ordertype < 100) {
            log.error("订单类型非法：" + this.ordertype);
            return false;
        } else {
            Map<String, Object> params = new HashMap<>();
            params.put(D_OrderType.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
            OrderTypeEnum orderTypeEnum;
            try {
                orderTypeEnum = OrderTypeEnum.getEnum(this.ordertype);
            } catch (EnumValueUndefinedException e) {
                orderTypeEnum = null;
            }
            if (orderTypeEnum != null) {//普通交易
                if (!orderTypeEnum.getOrderClass().equals(this.getClass())) {
                    log.error("订单类型非法：" + orderTypeEnum.getValue() + " => " + this.getClass().getCanonicalName());
                    return false;
                }
                this.accountdate = null;
                if (!this.getClass().equals(NormalTradeOrder.class)) {
                    entryItemParams = null;
                }
                params.put(D_OrderType.class.getCanonicalName() + ".code", orderTypeEnum.getValue() + "");
                params.put(D_OrderType.class.getCanonicalName() + ".field6", InternalTrade.No.getValue() + "");
                if (this.ordertype < 300 || this.ordertype >= 400) {
                    setRatio(1);
                }
            } else {//内部特殊交易
                if (!this.getClass().equals(InternalTradeOrder.class)) {
                    log.error("不是内部特殊交易订单类型：" + this.ordertype + " => " + this.getClass().getCanonicalName());
                    return false;
                }
                entryItemParams = null;
                params.put(D_OrderType.class.getCanonicalName() + ".code", this.ordertype + "");
                params.put(D_OrderType.class.getCanonicalName() + ".field6", InternalTrade.Yes.getValue() + "");
            }
            D_OrderType orderTypeObj = (D_OrderType) D_OrderType.getInstance(params, D_OrderType.class, null, connectionFactory);
            if (orderTypeObj == null) {
                log.error("订单类型非法：" + this.ordertype);
                return false;
            }
            return true;
        }
    }

    /**
     * 获取当前订单状态
     *
     * @param connectionFactory 数据库连接
     * @return 订单状态
     */
    public OrderStatusEnum getCurrOrderStatus(ConnectionFactory connectionFactory) throws EnumValueUndefinedException {
        OrderBase orderBase = (OrderBase) doViewByLock(getOrderNo(), this.getClass(), null, connectionFactory);
        if (orderBase == null) {
            return OrderStatusEnum.other;
        } else {
            return orderBase.getStatus();
        }
    }

    /**
     * 设置会计相关日期
     *
     * @param connectionFactory 数据库连接
     */
    private void updateAccountDate(ConnectionFactory connectionFactory) throws Exception {
        if (CommonTools.isNullStr(this.accountdate)) {
            this.accountdate = D_DateParam.getAccountDate(connectionFactory);
        }
        int statementdateno = Integer.valueOf(D_DateParam.getStatementDateNo(connectionFactory));
        int bindaccountdateno = Integer.valueOf(D_DateParam.getBindAccountDateNo(connectionFactory));
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Calendar accountdate_ca = Calendar.getInstance();
        accountdate_ca.setTime(simpleDateFormat.parse(this.accountdate));
        int dayno = CalendarTools.getDayNo(accountdate_ca);
        int offermonth_stat = 0;
        int offermonth_bind;
        if (dayno >= statementdateno) {
            offermonth_stat = 1;
        }
        if (statementdateno > bindaccountdateno) {
            offermonth_bind = offermonth_stat + 1;
        } else {
            offermonth_bind = offermonth_stat;
        }
        Calendar statment = Calendar.getInstance();
        statment.add(Calendar.MONTH, offermonth_stat);
        statment.set(Calendar.DATE, statementdateno);
        this.statementdate = simpleDateFormat.format(statment.getTime());
        Calendar binddate = Calendar.getInstance();
        binddate.add(Calendar.MONTH, offermonth_bind);
        binddate.set(Calendar.DATE, bindaccountdateno);
        this.bindaccountdate = simpleDateFormat.format(binddate.getTime());
    }

    /**
     * 自动设置创建时间
     */
    private void updateCreateDate() {
        String now = CommonTools.getNowTimeString();
        this.createdate = now;
        this.lastmodifydate = now;
    }

    /**
     * 自动更新通知时间
     */
    void updateNotifyDate() {
        this.notifydate = CommonTools.getNowTimeString();
    }

    /**
     * 自动更新处理人和处理时间
     *
     * @param userid 处理人id
     */
    void updateProcessDate(String userid) {
        String now = CommonTools.getNowTimeString();
        this.userid = userid;
        this.processdate = now;
        this.lastmodifydate = now;
    }

    /**
     * 根据现有订单状态生成原订单状态
     *
     * @return 原订单状态
     */
    OrderStatusEnum generateOrigOrderStatus() throws EnumValueUndefinedException {
        OrderStatusEnum result;
        OrderStatusEnum refundStatus = getStatus();
        switch (refundStatus) {
            case success:
                result = OrderStatusEnum.refundSuccess;
                break;
            case failed:
                result = OrderStatusEnum.refundFail;
                break;
            case cancle:
                result = OrderStatusEnum.refundCancle;
                break;
            case paySuccess:
                result = OrderStatusEnum.applyRefundSuccess;
                break;
            case reviewing:
                result = OrderStatusEnum.refunding;
                break;
            case create:
                result = OrderStatusEnum.applyRefund;
                break;
            default:
                result = OrderStatusEnum.other;
        }
        return result;
    }

    /**
     * 订单记账校验
     *
     * @param connectionFactory 数据库连接
     * @return true|false
     */
    public boolean doValidateOrder(ConnectionFactory connectionFactory) {
        accountResult = validateOrder(connectionFactory);
        if (accountResult.getStatus().equals(ResultStatusEnum.success.getCode())) {
            AccountEntrance accountEntrance = AccountEntrance.getInstance(this, connectionFactory);
            if (accountEntrance != null) {
                accountResult = accountEntrance.doValidate();
                if (accountResult.getStatus().equals(ResultStatusEnum.success.getCode())) {
                    return true;
                } else {
                    log.error(accountResult.getMessage());
                    return false;
                }
            } else {
                accountResult.setStatus(ResultStatusEnum.failed);
                accountResult.setMessage("核心记账规则出错");
                log.error("核心记账规则出错");
                return false;
            }
        } else {
            log.error(accountResult.getMessage());
            return false;
        }
    }

    /**
     * 订单扩展校验
     *
     * @param connectionFactory 数据库连接对象
     * @return 记账结果对象
     */
    public abstract AccountResult validateOrder(ConnectionFactory connectionFactory);

}

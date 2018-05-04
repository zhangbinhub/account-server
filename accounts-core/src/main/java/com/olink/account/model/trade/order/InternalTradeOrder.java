package com.olink.account.model.trade.order;

import com.olink.account.enumration.*;
import com.olink.account.core.AccountResult;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import org.apache.log4j.Logger;

/**
 * Created by zhangbin on 2016/11/23.
 * 账务系统特殊交易订单
 */
@ADBTable(tablename = "acc_order_internaltrade", isSeparate = true)
public class InternalTradeOrder extends OrderBase {

    public TradeTypeEnum getTradetype() throws EnumValueUndefinedException {
        return TradeTypeEnum.getEnum(tradetype);
    }

    public void setTradetype(TradeTypeEnum tradetype) {
        this.tradetype = tradetype.getValue();
    }

    public String getTradetypename() {
        return tradetypename;
    }

    public void setTradetypename(String tradetypename) {
        this.tradetypename = tradetypename;
    }

    public String getTradedescription() {
        return tradedescription;
    }

    public void setTradedescription(String tradedescription) {
        this.tradedescription = tradedescription;
    }

    public String getOrigorderno() {
        return origorderno;
    }

    public void setOrigorderno(String origorderno) {
        this.origorderno = origorderno;
    }

    public OrderTypeEnum getOrigordertype() throws EnumValueUndefinedException {
        if (origordertype == null) {
            return null;
        } else {
            return OrderTypeEnum.getEnum(origordertype);
        }
    }

    public void setOrigordertype(OrderTypeEnum origordertype) {
        this.origordertype = origordertype.getValue();
    }

    public OrderStatusEnum getOrigstatus() throws EnumValueUndefinedException {
        if (origstatus == null) {
            return null;
        } else {
            return OrderStatusEnum.getEnum(origstatus);
        }
    }

    public void setOrigstatus(OrderStatusEnum origstatus) {
        this.origstatus = origstatus.getValue();
    }

    public String getReserve1() {
        return reserve1;
    }

    public void setReserve1(String reserve1) {
        this.reserve1 = reserve1;
    }

    public String getReserve2() {
        return reserve2;
    }

    public void setReserve2(String reserve2) {
        this.reserve2 = reserve2;
    }

    public String getReserve3() {
        return reserve3;
    }

    public void setReserve3(String reserve3) {
        this.reserve3 = reserve3;
    }

    public String getReserve4() {
        return reserve4;
    }

    public void setReserve4(String reserve4) {
        this.reserve4 = reserve4;
    }

    public String getReserve5() {
        return reserve5;
    }

    public void setReserve5(String reserve5) {
        this.reserve5 = reserve5;
    }

    public String getReserve6() {
        return reserve6;
    }

    public void setReserve6(String reserve6) {
        this.reserve6 = reserve6;
    }

    public String getReserve7() {
        return reserve7;
    }

    public void setReserve7(String reserve7) {
        this.reserve7 = reserve7;
    }

    public String getReserve8() {
        return reserve8;
    }

    public void setReserve8(String reserve8) {
        this.reserve8 = reserve8;
    }

    public String getReserve9() {
        return reserve9;
    }

    public void setReserve9(String reserve9) {
        this.reserve9 = reserve9;
    }

    public String getReserve10() {
        return reserve10;
    }

    public void setReserve10(String reserve10) {
        this.reserve10 = reserve10;
    }

    public String getOpinion() {
        return opinion;
    }

    public void setOpinion(String opinion) {
        this.opinion = opinion;
    }

    public ReviewResultEnum getReviewresult() throws EnumValueUndefinedException {
        return ReviewResultEnum.getEnum(reviewresult);
    }

    public void setReviewresult(ReviewResultEnum reviewresult) {
        this.reviewresult = reviewresult.getValue();
    }

    @ADBTableField(name = "tradetype", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int tradetype;// 交易类型

    @ADBTableField(name = "tradetypename", fieldType = DBTableFieldType.String, allowNull = false)
    private String tradetypename;// 交易类型名称

    @ADBTableField(name = "tradedescription", fieldType = DBTableFieldType.String, allowNull = false)
    private String tradedescription;// 交易描述

    @ADBTableField(name = "origorderno", fieldType = DBTableFieldType.String)
    private String origorderno;// 原订单号

    @ADBTableField(name = "origordertype", fieldType = DBTableFieldType.String)
    private Integer origordertype;// 原订单类型

    @ADBTableField(name = "origstatus", fieldType = DBTableFieldType.String)
    private Integer origstatus;// 原订单状态

    @ADBTableField(name = "reserve1", fieldType = DBTableFieldType.String)
    private String reserve1;//	扩展字段1

    @ADBTableField(name = "reserve2", fieldType = DBTableFieldType.String)
    private String reserve2;//	扩展字段2

    @ADBTableField(name = "reserve3", fieldType = DBTableFieldType.String)
    private String reserve3;//	扩展字段3

    @ADBTableField(name = "reserve4", fieldType = DBTableFieldType.String)
    private String reserve4;//	扩展字段4

    @ADBTableField(name = "reserve5", fieldType = DBTableFieldType.String)
    private String reserve5;//	扩展字段5

    @ADBTableField(name = "reserve6", fieldType = DBTableFieldType.String)
    private String reserve6;//	扩展字段6

    @ADBTableField(name = "reserve7", fieldType = DBTableFieldType.String)
    private String reserve7;//	扩展字段7

    @ADBTableField(name = "reserve8", fieldType = DBTableFieldType.String)
    private String reserve8;//	扩展字段8

    @ADBTableField(name = "reserve9", fieldType = DBTableFieldType.String)
    private String reserve9;//	扩展字段9

    @ADBTableField(name = "reserve10", fieldType = DBTableFieldType.String)
    private String reserve10;//	扩展字段10

    private String opinion;//审核意见

    private int reviewresult;//审核结果

    private final Logger log = Logger.getLogger(this.getClass());

    public InternalTradeOrder() {
        super();
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
            if (getStatus().equals(OrderStatusEnum.success.getValue()) || getStatus().equals(OrderStatusEnum.failed.getValue()) || getStatus().equals(OrderStatusEnum.cancle.getValue())) {
                this.addUpdateIncludes(new String[]{"checkstatus", "notifydate", "processdate", "userid"});
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

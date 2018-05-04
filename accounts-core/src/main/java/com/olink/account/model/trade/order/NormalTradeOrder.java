package com.olink.account.model.trade.order;

import com.olink.account.enumration.OrderTypeEnum;
import com.olink.account.enumration.ResultStatusEnum;
import com.olink.account.core.AccountResult;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;

/**
 * Created by zhangbin on 2016/12/11.
 * B户通用记账交易
 */
@ADBTable(tablename = "acc_order_normaltrade", isSeparate = true)
public class NormalTradeOrder extends OrderBase {

    public String getTradedescription() {
        return tradedescription;
    }

    public void setTradedescription(String tradedescription) {
        this.tradedescription = tradedescription;
    }

    public String getShopno() {
        return shopno;
    }

    public void setShopno(String shopno) {
        this.shopno = shopno;
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

    public String getReserve11() {
        return reserve11;
    }

    public void setReserve11(String reserve11) {
        this.reserve11 = reserve11;
    }

    public String getReserve12() {
        return reserve12;
    }

    public void setReserve12(String reserve12) {
        this.reserve12 = reserve12;
    }

    public String getReserve13() {
        return reserve13;
    }

    public void setReserve13(String reserve13) {
        this.reserve13 = reserve13;
    }

    public String getReserve14() {
        return reserve14;
    }

    public void setReserve14(String reserve14) {
        this.reserve14 = reserve14;
    }

    public String getReserve15() {
        return reserve15;
    }

    public void setReserve15(String reserve15) {
        this.reserve15 = reserve15;
    }

    public String getReserve16() {
        return reserve16;
    }

    public void setReserve16(String reserve16) {
        this.reserve16 = reserve16;
    }

    public String getReserve17() {
        return reserve17;
    }

    public void setReserve17(String reserve17) {
        this.reserve17 = reserve17;
    }

    public String getReserve18() {
        return reserve18;
    }

    public void setReserve18(String reserve18) {
        this.reserve18 = reserve18;
    }

    public String getReserve19() {
        return reserve19;
    }

    public void setReserve19(String reserve19) {
        this.reserve19 = reserve19;
    }

    public String getReserve20() {
        return reserve20;
    }

    public void setReserve20(String reserve20) {
        this.reserve20 = reserve20;
    }

    @ADBTableField(name = "tradedescription", fieldType = DBTableFieldType.String, allowNull = false)
    private String tradedescription;// 交易描述

    @ADBTableField(name = "shopno", fieldType = DBTableFieldType.String, allowNull = false)
    private String shopno;//开户行名称

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

    @ADBTableField(name = "reserve11", fieldType = DBTableFieldType.String)
    private String reserve11;//	扩展字段11

    @ADBTableField(name = "reserve12", fieldType = DBTableFieldType.String)
    private String reserve12;//	扩展字段12

    @ADBTableField(name = "reserve13", fieldType = DBTableFieldType.String)
    private String reserve13;//	扩展字段13

    @ADBTableField(name = "reserve14", fieldType = DBTableFieldType.String)
    private String reserve14;//	扩展字段14

    @ADBTableField(name = "reserve15", fieldType = DBTableFieldType.String)
    private String reserve15;//	扩展字段15

    @ADBTableField(name = "reserve16", fieldType = DBTableFieldType.String)
    private String reserve16;//	扩展字段16

    @ADBTableField(name = "reserve17", fieldType = DBTableFieldType.String)
    private String reserve17;//	扩展字段17

    @ADBTableField(name = "reserve18", fieldType = DBTableFieldType.String)
    private String reserve18;//	扩展字段18

    @ADBTableField(name = "reserve19", fieldType = DBTableFieldType.String)
    private String reserve19;//	扩展字段19

    @ADBTableField(name = "reserve20", fieldType = DBTableFieldType.String)
    private String reserve20;//	扩展字段20

    public NormalTradeOrder() {
        super(OrderTypeEnum.NormalTrade);
    }

    @Override
    public AccountResult validateOrder(ConnectionFactory connectionFactory) {
        AccountResult result = new AccountResult();
        result.setStatus(ResultStatusEnum.success);
        return result;
    }

    @Override
    public boolean beforeCreateOrder() {
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

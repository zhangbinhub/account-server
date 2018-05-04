package com.olink.account.model.trade.customer;

import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;

/**
 * Created by zhangbin on 2016/9/7.
 * B户虚拟账户表
 */
@ADBTable(tablename = "acc_business_subaccount")
public class BusinessSubAccount extends SubAccount {

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    @ADBTableField(name = "businessid", fieldType = DBTableFieldType.String, allowNull = false)
    private String businessid;

    @ADBTableField(name = "isdefault", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int isdefault;

}

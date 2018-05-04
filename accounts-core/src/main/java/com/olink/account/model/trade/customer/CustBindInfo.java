package com.olink.account.model.trade.customer;

import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;

/**
 * C户绑定信息表
 * Created by Shepherd on 2016-08-26.
 */
@ADBTable(tablename = "acc_cust_bindinfo")
public class CustBindInfo extends DBTable {

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "custid", fieldType = DBTableFieldType.String, allowNull = false)
    private String custid;

    @ADBTableField(name = "type", fieldType = DBTableFieldType.String, allowNull = false)
    private String type;

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;//	创建时间

    @ADBTableField(name = "bank", fieldType = DBTableFieldType.String)
    private String bank;//	开户行

    @ADBTableField(name = "accountname", fieldType = DBTableFieldType.String)
    private String accountname;//	开户名

    @ADBTableField(name = "account", fieldType = DBTableFieldType.String, allowNull = false)
    private String account;//	卡号|微信号|支付宝号

    @ADBTableField(name = "isdefault", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int isdefault;//	是否默认

    @ADBTableField(name = "status", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int status;//	激活|禁用等

    @ADBTableField(name = "isdel", fieldType = DBTableFieldType.Integer)
    private int isdel;//是否删除

    @ADBTableField(name = "channel", fieldType = DBTableFieldType.String, allowNull = false)
    private String channel;//渠道

    @ADBTableField(name = "sort", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int sort;//序号

    public CustBindInfo() {
    }

    public CustBindInfo(String id, String custid, String type, String createdate, String bank, String accountname,
                        String account, int isdefault, int status, int isdel, String channel, int sort) {
        this.id = id;
        this.custid = custid;
        this.type = type;
        this.createdate = createdate;
        this.bank = bank;
        this.accountname = accountname;
        this.account = account;
        this.isdefault = isdefault;
        this.status = status;
        this.isdel = isdel;
        this.channel = channel;
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
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

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsdel() {
        return isdel;
    }

    public void setIsdel(int isdel) {
        this.isdel = isdel;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}

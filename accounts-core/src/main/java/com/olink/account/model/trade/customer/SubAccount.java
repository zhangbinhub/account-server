package com.olink.account.model.trade.customer;

import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/9/20.
 * 子账户基类
 */
@ADBTable(isVirtual = true)
public class SubAccount extends DBTable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public double getBalance() {
        return balance.doubleValue();
    }

    public void setBalance(double balance) {
        this.balance = BigDecimal.valueOf(balance);
    }

    public double getMoney() {
        return money.doubleValue();
    }

    public void setMoney(double money) {
        this.money = BigDecimal.valueOf(money);
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getEdituserid() {
        return edituserid;
    }

    public void setEdituserid(String edituserid) {
        this.edituserid = edituserid;
    }

    public String getAuthuserid() {
        return authuserid;
    }

    public void setAuthuserid(String authuserid) {
        this.authuserid = authuserid;
    }

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "type", fieldType = DBTableFieldType.String, allowNull = false)
    private String type;

    @ADBTableField(name = "code", fieldType = DBTableFieldType.String, allowNull = false)
    private String code;

    @ADBTableField(name = "balance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal balance;

    @ADBTableField(name = "money", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal money;

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;

    @ADBTableField(name = "status", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int status;

    @ADBTableField(name = "edituserid", fieldType = DBTableFieldType.String)
    private String edituserid;//编辑人id

    @ADBTableField(name = "authuserid", fieldType = DBTableFieldType.String)
    private String authuserid;//授权人id

}

package com.olink.account.model.batch.accounting;

import com.olink.account.enumration.AccountItemStyleEnum;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/11/30.
 * 内部账汇总表
 */
@ADBTable(tablename = "acc_inner_summary")
public class InnerSummary extends DBTable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public String getAccountitemname() {
        return accountitemname;
    }

    public void setAccountitemname(String accountitemname) {
        this.accountitemname = accountitemname;
    }

    public String getAccountitemcode() {
        return accountitemcode;
    }

    public void setAccountitemcode(String accountitemcode) {
        this.accountitemcode = accountitemcode;
    }

    public String getParentname() {
        return parentname;
    }

    public void setParentname(String parentname) {
        this.parentname = parentname;
    }

    public String getParentcode() {
        return parentcode;
    }

    public void setParentcode(String parentcode) {
        this.parentcode = parentcode;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public double getPrevbalance() {
        return prevbalance.doubleValue();
    }

    public void setPrevbalance(double prevbalance) {
        this.prevbalance = BigDecimal.valueOf(prevbalance);
    }

    public double getBalance() {
        return balance.doubleValue();
    }

    public void setBalance(double balance) {
        this.balance = BigDecimal.valueOf(balance);
    }

    public AccountItemStyleEnum getType() throws EnumValueUndefinedException {
        return AccountItemStyleEnum.getEnum(type);
    }

    public void setType(AccountItemStyleEnum type) {
        this.type = type.getValue();
    }

    public String getAccountdate() {
        return accountdate;
    }

    public void setAccountdate(String accountdate) {
        this.accountdate = accountdate;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "businessid", fieldType = DBTableFieldType.String, allowNull = false)
    private String businessid;

    @ADBTableField(name = "accountitemname", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountitemname;

    @ADBTableField(name = "accountitemcode", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountitemcode;

    @ADBTableField(name = "parentname", fieldType = DBTableFieldType.String, allowNull = false)
    private String parentname;

    @ADBTableField(name = "parentcode", fieldType = DBTableFieldType.String, allowNull = false)
    private String parentcode;

    @ADBTableField(name = "level", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int level;

    @ADBTableField(name = "prevbalance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal prevbalance;

    @ADBTableField(name = "balance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal balance;

    @ADBTableField(name = "type", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int type;

    @ADBTableField(name = "accountdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountdate;

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;

}

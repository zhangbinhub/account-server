package com.olink.account.model.trade.customer;

import com.olink.account.enumration.AccountItemTypeEnum;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2017/2/21.
 * B户内部账户
 */
@ADBTable(tablename = "acc_business_inneraccount")
public class BusinessInnerAccount extends DBTable {

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

    public String getInnerid() {
        return innerid;
    }

    public void setInnerid(String innerid) {
        this.innerid = innerid;
    }

    public String getAccountitemfirstname() {
        return accountitemfirstname;
    }

    public void setAccountitemfirstname(String accountitemfirstname) {
        this.accountitemfirstname = accountitemfirstname;
    }

    public String getAccountitemfirstcode() {
        return accountitemfirstcode;
    }

    public void setAccountitemfirstcode(String accountitemfirstcode) {
        this.accountitemfirstcode = accountitemfirstcode;
    }

    public String getAccountitemsecdname() {
        return accountitemsecdname;
    }

    public void setAccountitemsecdname(String accountitemsecdname) {
        this.accountitemsecdname = accountitemsecdname;
    }

    public String getAccountitemsecdcode() {
        return accountitemsecdcode;
    }

    public void setAccountitemsecdcode(String accountitemsecdcode) {
        this.accountitemsecdcode = accountitemsecdcode;
    }

    public String getAccountitemthirdname() {
        return accountitemthirdname;
    }

    public void setAccountitemthirdname(String accountitemthirdname) {
        this.accountitemthirdname = accountitemthirdname;
    }

    public String getAccountitemthirdcode() {
        return accountitemthirdcode;
    }

    public void setAccountitemthirdcode(String accountitemthirdcode) {
        this.accountitemthirdcode = accountitemthirdcode;
    }

    public String getAccountsubitemname() {
        return accountsubitemname;
    }

    public void setAccountsubitemname(String accountsubitemname) {
        this.accountsubitemname = accountsubitemname;
    }

    public String getAccountsubitemcode() {
        return accountsubitemcode;
    }

    public void setAccountsubitemcode(String accountsubitemcode) {
        this.accountsubitemcode = accountsubitemcode;
    }

    public AccountItemTypeEnum getType() throws EnumValueUndefinedException {
        return AccountItemTypeEnum.getEnum(type);
    }

    public void setType(AccountItemTypeEnum type) {
        this.type = type.getValue();
    }

    public double getBalance() {
        return balance.doubleValue();
    }

    public void setBalance(double balance) {
        this.balance = BigDecimal.valueOf(balance);
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

    public String getUpdatedate() {
        return updatedate;
    }

    public void setUpdatedate(String updatedate) {
        this.updatedate = updatedate;
    }

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "businessid", fieldType = DBTableFieldType.String, allowNull = false)
    private String businessid;

    @ADBTableField(name = "innerid", fieldType = DBTableFieldType.String, allowNull = false)
    private String innerid;

    @ADBTableField(name = "accountitemfirstname", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountitemfirstname;

    @ADBTableField(name = "accountitemfirstcode", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountitemfirstcode;

    @ADBTableField(name = "accountitemsecdname", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountitemsecdname;

    @ADBTableField(name = "accountitemsecdcode", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountitemsecdcode;

    @ADBTableField(name = "accountitemthirdname", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountitemthirdname;

    @ADBTableField(name = "accountitemthirdcode", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountitemthirdcode;

    @ADBTableField(name = "accountsubitemname", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountsubitemname;

    @ADBTableField(name = "accountsubitemcode", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountsubitemcode;

    @ADBTableField(name = "type", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int type;

    @ADBTableField(name = "balance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal balance;

    @ADBTableField(name = "accountdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountdate;

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;

    @ADBTableField(name = "updatedate", fieldType = DBTableFieldType.String, allowNull = false)
    private String updatedate;

}

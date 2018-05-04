package com.olink.account.model.batch.accounting;

import com.olink.account.enumration.AccountTypeEnum;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2017/2/21.
 * 客户账月统计
 */
@ADBTable(tablename = "acc_cust_monthstatistics")
public class CustomerMonthStatistics extends DBTable {

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

    public String getCustsubaccountcode() {
        return custsubaccountcode;
    }

    public void setCustsubaccountcode(String custsubaccountcode) {
        this.custsubaccountcode = custsubaccountcode;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrevbalance() {
        return prevbalance.doubleValue();
    }

    public void setPrevbalance(double prevbalance) {
        this.prevbalance = BigDecimal.valueOf(prevbalance);
    }

    public double getAmontexpend() {
        return amontexpend.doubleValue();
    }

    public void setAmontexpend(double amontexpend) {
        this.amontexpend = BigDecimal.valueOf(amontexpend);
    }

    public double getAmontrevenue() {
        return amontrevenue.doubleValue();
    }

    public void setAmontrevenue(double amontrevenue) {
        this.amontrevenue = BigDecimal.valueOf(amontrevenue);
    }

    public double getBalance() {
        return balance.doubleValue();
    }

    public void setBalance(double balance) {
        this.balance = BigDecimal.valueOf(balance);
    }

    public String getYearmonth() {
        return yearmonth;
    }

    public void setYearmonth(String yearmonth) {
        this.yearmonth = yearmonth;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "custid", fieldType = DBTableFieldType.String, allowNull = false)
    private String custid;

    @ADBTableField(name = "custsubaccountcode", fieldType = DBTableFieldType.String, allowNull = false)
    private String custsubaccountcode;

    @ADBTableField(name = "type", fieldType = DBTableFieldType.String, allowNull = false)
    private String type;

    @ADBTableField(name = "prevbalance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal prevbalance;

    @ADBTableField(name = "amontexpend", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal amontexpend;

    @ADBTableField(name = "amontrevenue", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal amontrevenue;

    @ADBTableField(name = "balance", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal balance;

    @ADBTableField(name = "yearmonth", fieldType = DBTableFieldType.String, allowNull = false)
    private String yearmonth;

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;

}

package com.olink.account.model.trade.customer;

import com.olink.account.enumration.ChangeBalanceDirectEnum;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2017/5/5.
 * 余额变动记录
 */
@ADBTable(tablename = "acc_cust_changelog")
public class CustChangeLog extends DBTable {

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

    public String getChangedate() {
        return changedate;
    }

    public void setChangedate(String changedate) {
        this.changedate = changedate;
    }

    public ChangeBalanceDirectEnum getChangedirect() throws EnumValueUndefinedException {
        return ChangeBalanceDirectEnum.getEnum(changedirect);
    }

    public void setChangedirect(ChangeBalanceDirectEnum changedirect) {
        this.changedirect = changedirect.getValue();
    }

    public double getAmont() {
        return amont.doubleValue();
    }

    public void setAmont(double amont) {
        this.amont = BigDecimal.valueOf(amont);
    }

    public double getMoney() {
        return money.doubleValue();
    }

    public void setMoney(double money) {
        this.money = BigDecimal.valueOf(money);
    }

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "custid", fieldType = DBTableFieldType.String, allowNull = false)
    private String custid;

    @ADBTableField(name = "type", fieldType = DBTableFieldType.String, allowNull = false)
    private String type;

    @ADBTableField(name = "changedate", fieldType = DBTableFieldType.String, allowNull = false)
    private String changedate;

    @ADBTableField(name = "changedirect", fieldType = DBTableFieldType.Integer, allowNull = false)
    private Integer changedirect;

    @ADBTableField(name = "amont", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal amont;

    @ADBTableField(name = "money", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal money;

}

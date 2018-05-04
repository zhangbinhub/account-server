package com.olink.account.model.trade.accounting;

import com.olink.account.enumration.AccountItemTypeEnum;
import com.olink.account.enumration.BalanceDirectEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.utils.CodeFactory;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/11/3.
 * 会计分录流水
 */
@ADBTable(tablename = "acc_entry_itemflow")
public class EntryItemFlow extends DBTable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderno() {
        return orderno;
    }

    public void setOrderno(String orderno) {
        this.orderno = orderno;
    }

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
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

    public BalanceDirectEnum getBalancedirect() throws EnumValueUndefinedException {
        return BalanceDirectEnum.getEnum(balancedirect);
    }

    public void setBalancedirect(BalanceDirectEnum balancedirect) {
        this.balancedirect = balancedirect.getValue();
    }

    public double getAmont() {
        return amont.doubleValue();
    }

    public void setAmont(double amont) {
        this.amont = BigDecimal.valueOf(amont);
    }

    public String getAccountdate() {
        return accountdate;
    }

    public void setAccountdate(String accountdate) {
        this.accountdate = accountdate;
    }

    public String getStatementdate() {
        return statementdate;
    }

    public void setStatementdate(String statementdate) {
        this.statementdate = statementdate;
    }

    public String getBindaccountdate() {
        return bindaccountdate;
    }

    public void setBindaccountdate(String bindaccountdate) {
        this.bindaccountdate = bindaccountdate;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
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

    public String getInnerId() throws ServerException {
        return CodeFactory.generateBusinessInnerId(businessid, accountitemfirstcode, accountitemsecdcode, accountitemthirdcode, accountsubitemcode);
    }

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "orderno", fieldType = DBTableFieldType.String, allowNull = false)
    private String orderno;

    @ADBTableField(name = "businessid", fieldType = DBTableFieldType.String, allowNull = false)
    private String businessid;

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

    @ADBTableField(name = "balancedirect", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int balancedirect;

    @ADBTableField(name = "amont", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal amont;

    @ADBTableField(name = "accountdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountdate;

    @ADBTableField(name = "statementdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String statementdate;

    @ADBTableField(name = "bindaccountdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String bindaccountdate;

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;

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

}

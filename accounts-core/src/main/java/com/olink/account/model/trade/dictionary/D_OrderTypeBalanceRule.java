package com.olink.account.model.trade.dictionary;

import com.olink.account.enumration.ChangeAccountTypeEnum;
import com.olink.account.enumration.ChangeBalanceDirectEnum;
import com.olink.account.enumration.OrderStatusEnum;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

/**
 * Created by zhangbin on 2016/10/21.
 * 虚拟账户余额变化规则
 */
@ADBTable(tablename = "acc_dic_ordertype_balancerule")
public class D_OrderTypeBalanceRule extends DBTable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrdertypeid() {
        return ordertypeid;
    }

    public void setOrdertypeid(String ordertypeid) {
        this.ordertypeid = ordertypeid;
    }

    public OrderStatusEnum getOldstatus() throws EnumValueUndefinedException {
        return OrderStatusEnum.getEnum(oldstatus);
    }

    public void setOldstatus(OrderStatusEnum oldstatus) {
        this.oldstatus = oldstatus.getValue();
    }

    public OrderStatusEnum getNewstatus() throws EnumValueUndefinedException {
        return OrderStatusEnum.getEnum(newstatus);
    }

    public void setNewstatus(OrderStatusEnum newstatus) {
        this.newstatus = newstatus.getValue();
    }

    public ChangeAccountTypeEnum getType() throws EnumValueUndefinedException {
        return ChangeAccountTypeEnum.getEnum(type);
    }

    public void setType(ChangeAccountTypeEnum type) {
        this.type = type.getValue();
    }

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public String getAmontruleid() {
        return amontruleid;
    }

    public void setAmontruleid(String amontruleid) {
        this.amontruleid = amontruleid;
    }

    public String getAccounttype() {
        return accounttype;
    }

    public void setAccounttype(String accounttype) {
        this.accounttype = accounttype;
    }

    public ChangeBalanceDirectEnum getBalancedirect() throws EnumValueUndefinedException {
        return ChangeBalanceDirectEnum.getEnum(balancedirect);
    }

    public void setBalancedirect(ChangeBalanceDirectEnum balancedirect) {
        this.balancedirect = balancedirect.getValue();
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
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

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "ordertypeid", fieldType = DBTableFieldType.String, allowNull = false)
    private String ordertypeid;//订单类型id

    @ADBTableField(name = "oldstatus", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int oldstatus;//订单原状态

    @ADBTableField(name = "newstatus", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int newstatus;//订单目标状态

    @ADBTableField(name = "type", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int type;//变更账户类型

    @ADBTableField(name = "businessid", fieldType = DBTableFieldType.String)
    private String businessid;//B户客户号

    @ADBTableField(name = "amontruleid", fieldType = DBTableFieldType.String, allowNull = false)
    private String amontruleid;//发生额分解规则id

    @ADBTableField(name = "accounttype", fieldType = DBTableFieldType.String, allowNull = false)
    private String accounttype;//虚拟账户类型编码

    @ADBTableField(name = "balancedirect", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int balancedirect;//余额变动方向

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;//	创建时间

    @ADBTableField(name = "modifydate", fieldType = DBTableFieldType.String, allowNull = false)
    private String modifydate;//	修改时间

    @ADBTableField(name = "userid", fieldType = DBTableFieldType.String, allowNull = false)
    private String userid;//	创建人

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

}

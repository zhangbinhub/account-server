package com.olink.account.model.trade.customer;

import com.olink.account.enumration.CustomerType;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

/**
 * Created by zhangbin on 2017/5/22.
 * 客户号顺序号码表
 */
@ADBTable(tablename = "acc_customerid_serial")
public class CustomerIdSerial extends DBTable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRegioncode() {
        return regioncode;
    }

    public void setRegioncode(String regioncode) {
        this.regioncode = regioncode;
    }

    public CustomerType getCusttype() throws EnumValueUndefinedException {
        return CustomerType.getEnum(custtype);
    }

    public void setCusttype(CustomerType custtype) {
        this.custtype = custtype.getValue();
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
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

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "regioncode", fieldType = DBTableFieldType.String, allowNull = false)
    private String regioncode;

    @ADBTableField(name = "custtype", fieldType = DBTableFieldType.Integer, allowNull = false)
    private Integer custtype;

    @ADBTableField(name = "timestamp", fieldType = DBTableFieldType.String, allowNull = false)
    private String timestamp;

    @ADBTableField(name = "number", fieldType = DBTableFieldType.Integer, allowNull = false)
    private Integer number;

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;

    @ADBTableField(name = "modifydate", fieldType = DBTableFieldType.String, allowNull = false)
    private String modifydate;

    @ADBTableField(name = "userid", fieldType = DBTableFieldType.String)
    private String userid;

}

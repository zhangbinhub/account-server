package com.olink.account.model.trade.packages;

import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.dbconnection.entity.DBTablePrimaryKeyType;

/**
 * Created by zhangbin on 2016/9/17.
 * 系统请求报文记录表
 */
@ADBTable(tablename = "acc_packageInfo_record")
public class PackageInfoRecord extends DBTable {

    public String getValidatecode() {
        return validatecode;
    }

    public void setValidatecode(String validatecode) {
        this.validatecode = validatecode;
    }

    public long getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(long receivetime) {
        this.receivetime = receivetime;
    }

    public String getClientip() {
        return clientip;
    }

    public void setClientip(String clientip) {
        this.clientip = clientip;
    }

    @ADBTablePrimaryKey(name = "validatecode", pKeyType = DBTablePrimaryKeyType.String)
    private String validatecode;

    @ADBTableField(name = "receivetime", fieldType = DBTableFieldType.Integer, allowNull = false)
    private long receivetime;

    @ADBTableField(name = "clientip", fieldType = DBTableFieldType.String, allowNull = false)
    private String clientip;
}

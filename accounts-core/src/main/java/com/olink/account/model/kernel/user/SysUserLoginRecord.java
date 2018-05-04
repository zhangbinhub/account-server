package com.olink.account.model.kernel.user;

import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.dbconnection.entity.DBTablePrimaryKeyType;

/**
 * Created by zhangbin on 2017/5/11.
 * 用户WEB登录记录
 */
@ADBTable(tablename = "T_User_LoginRecord")
public class SysUserLoginRecord extends DBTable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getLoginip() {
        return loginip;
    }

    public void setLoginip(String loginip) {
        this.loginip = loginip;
    }

    public String getLogintime() {
        return logintime;
    }

    public void setLogintime(String logintime) {
        this.logintime = logintime;
    }

    public String getLogindate() {
        return logindate;
    }

    public void setLogindate(String logindate) {
        this.logindate = logindate;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    @ADBTablePrimaryKey(name = "id", pKeyType = DBTablePrimaryKeyType.String)
    private String id;

    @ADBTableField(name = "userid", fieldType = DBTableFieldType.String, allowNull = false)
    private String userid;

    @ADBTableField(name = "login_ip", fieldType = DBTableFieldType.String)
    private String loginip;

    @ADBTableField(name = "login_time", fieldType = DBTableFieldType.String)
    private String logintime;

    @ADBTableField(name = "login_date", fieldType = DBTableFieldType.String)
    private String logindate;

    @ADBTableField(name = "appid", fieldType = DBTableFieldType.String)
    private String appid;

}

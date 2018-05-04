package com.olink.account.model.kernel.user;

import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;

/**
 * 用户角色关联信息
 * Created by Shepherd on 2016-08-25.
 */
@ADBTable(tablename = "T_User_Role_Set")
public class SysUserRoleSet extends DBTable {

    @ADBTablePrimaryKey(name = "id")
    private  String id;

    @ADBTableField(name = "userid", fieldType = DBTableFieldType.String, allowNull = false)
    private  String userid;

    @ADBTableField(name = "roleid", fieldType = DBTableFieldType.String, allowNull = false)
    private  String roleid;

    public SysUserRoleSet() {
        super();
    }

    public SysUserRoleSet(String userid, String roleid) {
        this.userid = userid;
        this.roleid = roleid;
    }

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

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }
}

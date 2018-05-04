package com.olink.account.model.kernel.user;

import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.dbconnection.entity.DBTablePrimaryKeyType;

/**
 * 用户信息表
 * Created by Shepherd on 2016-08-25.
 */
@ADBTable(tablename = "t_user")
public class SysUser extends DBTable {

    @ADBTablePrimaryKey(name = "id", pKeyType = DBTablePrimaryKeyType.String)
    private String id;

    @ADBTableField(name = "name", fieldType = DBTableFieldType.String, allowNull = false)
    private String name;//昵称

    @ADBTableField(name = "loginno", fieldType = DBTableFieldType.String, allowNull = false)
    private String loginno;//登录号

    @ADBTableField(name = "password", fieldType = DBTableFieldType.String, allowNull = false)
    private String password;//密码

    @ADBTableField(name = "levels", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int levels;//级别

    @ADBTableField(name = "status", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int status;//状态

    @ADBTableField(name = "sort", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int sort;//排序序号

    public SysUser() {
        super();
    }

    public SysUser(String id, String name, String loginno, String password, int levels, int status, int sort) {
        this.id = id;
        this.name = name;
        this.loginno = loginno;
        this.password = password;
        this.levels = levels;
        this.status = status;
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginno() {
        return loginno;
    }

    public void setLoginno(String loginno) {
        this.loginno = loginno;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}

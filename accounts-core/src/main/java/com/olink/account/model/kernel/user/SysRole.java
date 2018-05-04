package com.olink.account.model.kernel.user;

import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;

/**
 * 角色信息表
 * Created by Shepherd on 2016-08-25.
 */
@ADBTable(tablename = "T_Role")
public class SysRole extends DBTable {

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "appid", fieldType = DBTableFieldType.String, allowNull = false)
    private String appid;//应用id

    @ADBTableField(name = "name", fieldType = DBTableFieldType.String, allowNull = false)
    private String name;//角色名称

    @ADBTableField(name = "levels", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int levels;//层级

    @ADBTableField(name = "sort", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int sort;//排序

    public SysRole() {
        super();
    }

    public SysRole(String appid, String name, int levels, int sort) {
        this.appid = appid;
        this.name = name;
        this.levels = levels;
        this.sort = sort;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevels() {
        return levels;
    }

    public void setLevels(int levels) {
        this.levels = levels;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}

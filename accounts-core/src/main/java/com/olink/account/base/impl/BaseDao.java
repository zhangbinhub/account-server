package com.olink.account.base.impl;

import com.olink.account.base.IBaseDao;
import pers.acp.tools.common.DBConTools;

/**
 * Created by zhangbin on 2016/12/30.
 * 持久化对象
 */
public abstract class BaseDao implements IBaseDao {

    protected DBConTools dbTools;

    public BaseDao(DBConTools dbTools) {
        this.dbTools = dbTools;
    }

    @Override
    public String getCreateTableSQL(String tableName) {
        return dbTools.getDataListBySql("show create table " + tableName).get(0).get("create table".toUpperCase()).toString();
    }

    @Override
    public void beginTranslist() {
        dbTools.beginTranslist();
    }

    @Override
    public void commitTranslist() {
        dbTools.commitTranslist();
    }

    @Override
    public void rollBackTranslist() {
        dbTools.rollBackTranslist();
    }

    @Override
    public DBConTools getDBTools() {
        return dbTools;
    }

}

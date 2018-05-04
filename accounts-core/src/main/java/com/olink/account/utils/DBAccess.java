package com.olink.account.utils;

import pers.acp.tools.common.DBConTools;

/**
 * Created by Shepherd on 2016-08-08.
 * 数据操作类
 */
public class DBAccess {

    /**
     * 数据库操作工具对象
     */
    private DBConTools dbTools;

    /**
     * 取系统默认数据源
     */
    private DBAccess() {
        dbTools = new DBConTools();
    }

    /**
     * 取指定编号的数据源
     *
     * @param dbno 数据源编号
     */
    private DBAccess(int dbno) {
        dbTools = new DBConTools(dbno);
    }

    /**
     * 取系统数据源
     *
     * @return 实例对象
     */
    public static DBAccess getSysInstance() {
        return new DBAccess();
    }

    /**
     * 取交易库数据源
     *
     * @return 实例对象
     */
    public static DBAccess getTradeInstance() {
        return new DBAccess(1);
    }

    /**
     * 取管理库数据源
     *
     * @return 实例对象
     */
    public static DBAccess getManageInstance() {
        return new DBAccess(2);
    }

    /**
     * 获取当前实例的数据库操作工具对象
     *
     * @return 数据库操作工具对象
     */
    public DBConTools getDBTool() {
        return dbTools;
    }

}

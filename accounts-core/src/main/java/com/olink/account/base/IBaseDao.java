package com.olink.account.base;

import pers.acp.tools.common.DBConTools;

/**
 * Created by zhangbin on 2016/12/30.
 * 持久化接口
 */
public interface IBaseDao {

    /**
     * 获取建表语句
     *
     * @param tableName 表名
     * @return 建表语句
     */
    String getCreateTableSQL(String tableName);

    /**
     * 启动事务
     */
    void beginTranslist();

    /**
     * 提交事务
     */
    void commitTranslist();

    /**
     * 事务回滚
     */
    void rollBackTranslist();

    /**
     * 获取数据库操作工具
     *
     * @return 数据库工具
     */
    DBConTools getDBTools();

}

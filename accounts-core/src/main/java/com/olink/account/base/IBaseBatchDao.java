package com.olink.account.base;

import pers.acp.tools.dbconnection.entity.DBTable;

/**
 * Created by zhangbin on 2016/12/14.
 * 管理库持久化基类接口
 */
public interface IBaseBatchDao extends IBaseDao {

    /**
     * 创建表
     *
     * @param createSQL 建表语句
     * @return true|false
     */
    boolean doCreateTable(String createSQL);

    /**
     * 创建历史数据表对应记录
     *
     * @param hisDbName    历史数据库名
     * @param hisTableName 历史数据表名
     * @param hisDbNo      历史数据源编号
     * @param dbName       原数据库名
     * @param tableName    原数据表名
     * @param dbNo         原数据源编号
     */
    void doCreateHisTableLog(String hisDbName, String hisTableName, String hisDbNo, String dbName, String tableName, String dbNo);

    /**
     * 创建历史表
     *
     * @param tablename    原始表名
     * @param histablename 历史表名
     * @return true|false
     */
    boolean doCreateHisTable(String tablename, String histablename);

    /**
     * 删除原始表
     *
     * @param tablename 原始表名
     * @return true|false
     */
    boolean doDropTable(String tablename);

    /**
     * 创建记录
     *
     * @param dbTable  记录实例
     * @param prevyear 历史表后缀
     * @return true|false
     */
    boolean doCreateRecords(DBTable dbTable, String prevyear);

}

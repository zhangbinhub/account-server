package com.olink.account.base.impl;

import com.olink.account.base.IBaseBatchDao;
import com.olink.account.enumration.StatusEnum;
import com.olink.account.model.batch.info.BatchHisTables;
import com.olink.account.utils.DBAccess;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.common.DBConTools;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/12/14.
 * 批量系统持久化基类
 */
public abstract class BaseBatchDao extends BaseDao implements IBaseBatchDao {

    public BaseBatchDao() {
        super(DBAccess.getManageInstance().getDBTool());
    }

    public BaseBatchDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public boolean doCreateTable(String createSQL) {
        return dbTools.doUpdate(createSQL);
    }

    @Override
    public void doCreateHisTableLog(String hisDbName, String hisTableName, String hisDbNo, String dbName, String tableName, String dbNo) {
        Map<String, Object> param = new HashMap<>();
        param.put(BatchHisTables.class.getCanonicalName() + ".name", hisDbName.toUpperCase());
        param.put(BatchHisTables.class.getCanonicalName() + ".code", hisTableName.toUpperCase());
        param.put(BatchHisTables.class.getCanonicalName() + ".remark", hisDbNo);
        param.put(BatchHisTables.class.getCanonicalName() + ".parent", dbName.toUpperCase());
        param.put(BatchHisTables.class.getCanonicalName() + ".parentid", tableName.toUpperCase());
        param.put(BatchHisTables.class.getCanonicalName() + ".type", dbNo);
        BatchHisTables batchHisTables = (BatchHisTables) BatchHisTables.getInstance(param, BatchHisTables.class, null, dbTools.getDbcon());
        if (batchHisTables == null) {
            batchHisTables = new BatchHisTables();
            batchHisTables.setName(hisDbName.toUpperCase());
            batchHisTables.setCode(hisTableName.toUpperCase());
            batchHisTables.setRemark(hisDbNo);
            batchHisTables.setParent(dbName.toUpperCase());
            batchHisTables.setParentid(tableName.toUpperCase());
            batchHisTables.setType(dbNo);
            batchHisTables.setCreatedate(CommonTools.getNowTimeString());
            batchHisTables.setModifydate(CommonTools.getNowTimeString());
            batchHisTables.setStatus(StatusEnum.activate);
            batchHisTables.doCreate(dbTools.getDbcon());
        }
    }

    @Override
    public boolean doCreateHisTable(String tablename, String histablename) {
        String create_sql = "CREATE TABLE if Not exists " + histablename + " like " + tablename;
        return dbTools.doUpdate(create_sql);
    }

    @Override
    public boolean doDropTable(String tablename) {
        String drop_sql = "DROP TABLE IF EXISTS " + tablename;
        return dbTools.doUpdate(drop_sql);
    }

    @Override
    public boolean doCreateRecords(DBTable dbTable, String prevyear) {
        dbTable.setSuffix("_" + prevyear);
        return dbTable.doCreate(dbTools.getDbcon());
    }

}

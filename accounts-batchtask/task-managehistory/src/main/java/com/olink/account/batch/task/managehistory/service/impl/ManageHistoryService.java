package com.olink.account.batch.task.managehistory.service.impl;

import com.olink.account.base.impl.BaseBatchService;
import com.olink.account.batch.dao.ISituationDao;
import com.olink.account.batch.dao.impl.SituationDao;
import com.olink.account.batch.exception.BatchException;
import com.olink.account.batch.task.managehistory.dao.IManageHistoryDao;
import com.olink.account.batch.task.managehistory.dao.impl.ManageHistoryDao;
import com.olink.account.batch.task.managehistory.service.IManageHistoryService;
import com.olink.account.dao.ISystemDao;
import com.olink.account.dao.impl.SystemDao;
import com.olink.account.model.batch.info.BatchSituation;
import com.olink.account.model.kernel.user.SysUserLoginRecord;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.TimerException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhangbin on 2017/5/11.
 * 历史系统数据服务
 */
public class ManageHistoryService extends BaseBatchService implements IManageHistoryService {

    private IManageHistoryDao manageHistoryDao = new ManageHistoryDao();

    private ISystemDao systemDao = new SystemDao();

    /**
     * 原始表名
     */
    private List<String> tablenames = new ArrayList<>();

    @Override
    public boolean doHistory(String yearmonth) throws BatchException {
        try {
            String prevyear = yearmonth.substring(0, 4);
            doCreateTable(prevyear);
            manageHistoryDao.beginTranslist();
            systemDao.beginTranslist();
            if (!doHisSystem(prevyear, yearmonth)) {
                manageHistoryDao.rollBackTranslist();
                systemDao.rollBackTranslist();
                return false;
            }
            if (!doHisBatchSituation(prevyear, yearmonth)) {
                manageHistoryDao.rollBackTranslist();
                systemDao.rollBackTranslist();
                return false;
            }
            manageHistoryDao.commitTranslist();
            systemDao.commitTranslist();
            return true;
        } catch (Exception e) {
            manageHistoryDao.rollBackTranslist();
            systemDao.rollBackTranslist();
            throw new BatchException(e.getMessage());
        } finally {
            dropTable();
        }
    }

    @Override
    public void dropTable() {
        for (String tablename : tablenames) {
            manageHistoryDao.doDropTable(tablename);
        }
    }

    /**
     * 创建原始表
     *
     * @param prevyear 历史表后缀
     */
    private void doCreateTable(String prevyear) throws BatchException, IllegalAccessException, InstantiationException {
        tablenames.clear();
        SysUserLoginRecord sysUserLoginRecord = new SysUserLoginRecord();
        if (!manageHistoryDao.doCreateTable(systemDao.getCreateTableSQL(sysUserLoginRecord.getCurrTableName()))
                || !manageHistoryDao.doCreateHisTable(sysUserLoginRecord.getCurrTableName(), sysUserLoginRecord.getCurrTableName() + "_" + prevyear)) {
            throw new BatchException("创建用户WEB登录记录表失败");
        }
        manageHistoryDao.doCreateHisTableLog(manageHistoryDao.getDBTools().getDbName(), sysUserLoginRecord.getCurrTableName() + "_" + prevyear, manageHistoryDao.getDBTools().getDbNo() + "",
                systemDao.getDBTools().getDbName(), sysUserLoginRecord.getCurrTableName(), systemDao.getDBTools().getDbNo() + "");
        tablenames.add(sysUserLoginRecord.getCurrTableName());

        BatchSituation batchSituation = new BatchSituation();
        if (!manageHistoryDao.doCreateHisTable(batchSituation.getCurrTableName(), batchSituation.getCurrTableName() + "_" + prevyear)) {
            throw new BatchException("创建日终任务处理记录表失败");
        }
        manageHistoryDao.doCreateHisTableLog(manageHistoryDao.getDBTools().getDbName(), batchSituation.getCurrTableName() + "_" + prevyear, manageHistoryDao.getDBTools().getDbNo() + "",
                manageHistoryDao.getDBTools().getDbName(), batchSituation.getCurrTableName(), manageHistoryDao.getDBTools().getDbNo() + "");
    }

    /**
     * 执行系统数据迁移
     *
     * @param prevyear  历史表后缀
     * @param yearmonth 数据年月
     * @return true|false
     */
    private boolean doHisSystem(String prevyear, String yearmonth) {
        //迁移内部账统计信息
        List<DBTable> loginRecordList = systemDao.findLoginRecordByYearMonth(yearmonth);
        for (DBTable dbTable : loginRecordList) {
            SysUserLoginRecord sysUserLoginRecord = (SysUserLoginRecord) dbTable;
            if (!systemDao.doDeleteUserLoginRecord(sysUserLoginRecord)) {
                return false;
            }
            if (!manageHistoryDao.doCreateRecords(sysUserLoginRecord, prevyear)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 执行批量任务处理记录表
     *
     * @param prevyear  历史表后缀
     * @param yearmonth 数据年月
     * @return true|false
     */
    private boolean doHisBatchSituation(String prevyear, String yearmonth) throws TimerException {
        ISituationDao situationDao = new SituationDao(manageHistoryDao.getDBTools());
        List<DBTable> situationList = situationDao.findBatchSituationForPassByYearMonth(yearmonth);
        for (DBTable dbTable : situationList) {
            BatchSituation batchSituation = (BatchSituation) dbTable;
            if (!situationDao.doDeleteSituation(batchSituation)) {
                return false;
            }
            if (!manageHistoryDao.doCreateRecords(batchSituation, prevyear)) {
                return false;
            }
        }
        return true;
    }

}

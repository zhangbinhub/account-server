package com.olink.account.batch.service.impl;

import com.olink.account.base.impl.BaseBatchService;
import com.olink.account.batch.dao.ISituationDao;
import com.olink.account.batch.dao.impl.SituationDao;
import com.olink.account.batch.service.ISituationService;
import com.olink.account.enumration.BatchTaskStatus;
import com.olink.account.model.batch.info.BatchSituation;
import pers.acp.tools.common.CalendarTools;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.exceptions.TimerException;

import java.util.Calendar;
import java.util.List;

/**
 * Created by zhangbin on 2017/1/3.
 * 处理情况服务类
 */
public class SituationService extends BaseBatchService implements ISituationService {

    private ISituationDao situationDao = new SituationDao();

    @Override
    public boolean doCreateSituation(BatchSituation batchSituation) {
        batchSituation.setDescription("任务开始");
        return situationDao.doCreateSituation(batchSituation);
    }

    @Override
    public List<DBTable> findBatchSituationForNoPass() {
        return situationDao.findBatchSituationForNoPass();
    }

    @Override
    public BatchSituation findBatchSituation(String batchSituationId) {
        return situationDao.findBatchSituation(batchSituationId);
    }

    @Override
    public List<DBTable> findBatchSituationByClassName(String batchTaskClassName) {
        return situationDao.findBatchSituationByClassName(batchTaskClassName);
    }

    @Override
    public BatchSituation findBatchSituationCurrDay(String batchTaskClassName, String accountDate) {
        return situationDao.findBatchSituationPrevDay(batchTaskClassName, accountDate);
    }

    @Override
    public BatchSituation findBatchSituationPrevDay(String batchTaskClassName, String accountDate) throws TimerException {
        Calendar calendar = CalendarTools.getCalendar(accountDate);
        calendar = CalendarTools.getPrevDay(calendar);
        return situationDao.findBatchSituationPrevDay(batchTaskClassName, CommonTools.getDateTimeString(calendar.getTime(), "yyyy-MM-dd"));
    }

    @Override
    public boolean doUpdateSituation(BatchSituation batchSituation) throws EnumValueUndefinedException {
        BatchTaskStatus batchTaskStatus = batchSituation.getStatus();
        BatchSituation batchSituationOld = findBatchSituation(batchSituation.getId());
        switch (batchTaskStatus) {
            case begin:
                batchSituationOld.setDescription("任务开始");
                break;
            case processing:
                batchSituationOld.setDescription("正在执行中");
                break;
            case igone:
                batchSituationOld.setDescription("智能忽略");
                break;
            case success:
                batchSituationOld.setDescription("执行完成");
                break;
            case faild:
                batchSituationOld.setDescription(batchSituation.getDescription());
                break;
        }
        batchSituationOld.setStatus(batchTaskStatus);
        batchSituationOld.setPackagelastmodifydate(batchSituation.getPackagelastmodifydate());
        batchSituationOld.setEdituserid(batchSituation.getEdituserid());
        batchSituationOld.setAuthuserid(batchSituation.getAuthuserid());
        return situationDao.doUpdateSituation(batchSituationOld);
    }

    @Override
    public void dropTable() {

    }
}

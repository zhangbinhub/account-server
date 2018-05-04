package com.olink.account.batch.dao.impl;

import com.olink.account.base.impl.BaseBatchDao;
import com.olink.account.batch.dao.ISituationDao;
import com.olink.account.enumration.BatchTaskStatus;
import com.olink.account.model.batch.info.BatchSituation;
import pers.acp.tools.common.CalendarTools;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.common.DBConTools;
import pers.acp.tools.dbconnection.entity.DBTable;
import org.apache.log4j.Logger;
import pers.acp.tools.exceptions.TimerException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2017/1/3.
 * 处理结果持久化
 */
public class SituationDao extends BaseBatchDao implements ISituationDao {

    public SituationDao() {
        super();
    }

    public SituationDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public boolean doCreateSituation(BatchSituation batchSituation) {
        batchSituation.setLastmodifydate(CommonTools.getNowTimeString());
        batchSituation.setCreatedate(CommonTools.getNowTimeString());
        batchSituation.setStatus(BatchTaskStatus.begin);
        return batchSituation.doCreate(dbTools.getDbcon());
    }

    @Override
    public List<DBTable> findBatchSituationForNoPass() {
        return dbTools.getDataObjListBySql(null, BatchSituation.class, null, "and ${" + BatchSituation.class.getCanonicalName() + ".status} not in (" + BatchTaskStatus.igone.getValue() + "," + BatchTaskStatus.success.getValue() + ")");
    }

    @Override
    public List<DBTable> findBatchSituationForPassByYearMonth(String yearmonth) throws TimerException {
        Calendar c = CalendarTools.getCalendar(yearmonth + "-01");
        c.add(Calendar.MONTH, 1);
        String accountdate = CommonTools.getDateTimeString(c.getTime(), "yyyy-MM-dd");
        Map<String, Object> params = new HashMap<>();
        params.put(BatchSituation.class.getCanonicalName() + ".status", BatchTaskStatus.success.getValue());
        return dbTools.getDataObjListBySql(params, BatchSituation.class, null, "and ${" + BatchSituation.class.getCanonicalName() + ".accountdate}<'" + accountdate + "'");
    }

    @Override
    public BatchSituation findBatchSituation(String batchSituationId) {
        return (BatchSituation) BatchSituation.getInstance(batchSituationId, BatchSituation.class, null, dbTools.getDbcon());
    }

    @Override
    public List<DBTable> findBatchSituationByClassName(String batchTaskClassName) {
        Map<String, Object> params = new HashMap<>();
        params.put(BatchSituation.class.getCanonicalName() + ".taskclassname", batchTaskClassName);
        return dbTools.getDataObjListBySql(params, BatchSituation.class, null, "order by ${" + BatchSituation.class.getCanonicalName() + ".lastmodifydate} desc");
    }

    @Override
    public BatchSituation findBatchSituationPrevDay(String batchTaskClassName, String accountDate) {
        Map<String, Object> params = new HashMap<>();
        params.put(BatchSituation.class.getCanonicalName() + ".taskclassname", batchTaskClassName);
        params.put(BatchSituation.class.getCanonicalName() + ".accountdate", accountDate);
        return (BatchSituation) BatchSituation.getInstance(params, BatchSituation.class, null, dbTools.getDbcon(), "order by ${" + BatchSituation.class.getCanonicalName() + ".lastmodifydate} desc");
    }

    @Override
    public boolean doUpdateSituation(BatchSituation batchSituation) {
        batchSituation.setLastmodifydate(CommonTools.getNowTimeString());
        batchSituation.addUpdateIncludes(new String[]{"status", "lastmodifydate", "description", "packagelastmodifydate", "edituserid", "authuserid"});
        return batchSituation.doUpdate(dbTools.getDbcon());
    }

    @Override
    public boolean doDeleteSituation(BatchSituation batchSituation) {
        return batchSituation.doDelete(dbTools.getDbcon());
    }

}

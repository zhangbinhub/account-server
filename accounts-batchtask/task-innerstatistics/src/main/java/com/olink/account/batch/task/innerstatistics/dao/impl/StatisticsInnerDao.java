package com.olink.account.batch.task.innerstatistics.dao.impl;

import com.olink.account.base.impl.BaseBatchDao;
import com.olink.account.batch.task.innerstatistics.dao.IStatisticsInnerDao;
import com.olink.account.model.batch.accounting.InnerStatistics;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.common.DBConTools;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2017/5/8.
 * 内部账统计持久化
 */
public class StatisticsInnerDao extends BaseBatchDao implements IStatisticsInnerDao {

    public StatisticsInnerDao() {
        super();
    }

    public StatisticsInnerDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public InnerStatistics findInnerStatisticsByInneridAndAccountDate(String innerid, String accountDate) {
        Map<String, Object> param = new HashMap<>();
        param.put(InnerStatistics.class.getCanonicalName() + ".innerid", innerid);
        param.put(InnerStatistics.class.getCanonicalName() + ".accountdate", accountDate);
        return (InnerStatistics) InnerStatistics.getInstance(param, InnerStatistics.class, null, dbTools.getDbcon());
    }

    @Override
    public boolean doDeleteInnerStatistics(String accountDate) {
        InnerStatistics innerStatistics = new InnerStatistics();
        return dbTools.doUpdate("delete from " + innerStatistics.getCurrTableName() + " where accountdate='" + accountDate + "'");
    }

    @Override
    public boolean doCreateInnerStatistics(InnerStatistics innerStatistics) {
        innerStatistics.setCreatedate(CommonTools.getNowTimeString());
        return innerStatistics.doCreate(dbTools.getDbcon());
    }
}

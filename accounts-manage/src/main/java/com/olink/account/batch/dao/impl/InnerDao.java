package com.olink.account.batch.dao.impl;

import com.olink.account.base.impl.BaseBatchDao;
import com.olink.account.batch.dao.IInnerDao;
import com.olink.account.model.batch.accounting.InnerStatistics;
import com.olink.account.model.batch.accounting.InnerSummary;
import pers.acp.tools.common.DBConTools;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.List;

/**
 * Created by zhangbin on 2017/5/10.
 * 内部账信息持久化
 */
public class InnerDao extends BaseBatchDao implements IInnerDao {

    public InnerDao() {
        super();
    }

    public InnerDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public List<DBTable> findInnerStatisticsForBind(String bindaccountdate) {
        return dbTools.getDataObjListBySql(null, InnerStatistics.class, null, " and ${" + InnerStatistics.class.getCanonicalName() + ".accountdate}<'" + bindaccountdate + "'");
    }

    @Override
    public List<DBTable> findInnerSummaryForBind(String bindaccountdate) {
        return dbTools.getDataObjListBySql(null, InnerSummary.class, null, " and ${" + InnerSummary.class.getCanonicalName() + ".accountdate}<'" + bindaccountdate + "'");
    }

    @Override
    public boolean doDeleteInnerStatistics(InnerStatistics innerStatistics) {
        return innerStatistics.doDelete(dbTools.getDbcon());
    }

    @Override
    public boolean doDeleteInnerSummary(InnerSummary innerSummary) {
        return innerSummary.doDelete(dbTools.getDbcon());
    }

}

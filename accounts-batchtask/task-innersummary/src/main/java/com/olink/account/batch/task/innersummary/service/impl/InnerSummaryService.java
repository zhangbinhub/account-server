package com.olink.account.batch.task.innersummary.service.impl;

import com.olink.account.base.impl.BaseBatchService;
import com.olink.account.batch.exception.BatchException;
import com.olink.account.batch.task.innersummary.dao.IInnerSummaryDao;
import com.olink.account.batch.task.innersummary.dao.impl.InnerSummaryDao;
import com.olink.account.batch.task.innersummary.service.IInnerSummaryService;
import com.olink.account.model.trade.dictionary.D_AccountItem;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.exceptions.TimerException;

import java.util.Map;

/**
 * Created by zhangbin on 2017/5/9.
 * 内部账科目汇总服务
 */
public class InnerSummaryService extends BaseBatchService implements IInnerSummaryService {

    private IInnerSummaryDao innerSummaryDao = new InnerSummaryDao();

    @Override
    public boolean doSummary(String accountDate, Map<String, D_AccountItem> accountItemMap) throws BatchException, TimerException, EnumValueUndefinedException {
        innerSummaryDao.beginTranslist();
        if (!innerSummaryDao.doDeleteInnerSummary(accountDate)) {
            innerSummaryDao.rollBackTranslist();
            throw new BatchException("删除内部账科目汇总信息失败！");
        }
        if (innerSummaryDao.doSummary(accountDate, accountItemMap)) {
            innerSummaryDao.commitTranslist();
            return true;
        } else {
            innerSummaryDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public void dropTable() {

    }
}

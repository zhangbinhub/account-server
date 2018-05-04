package com.olink.account.batch.task.custstatistics.service.impl;

import com.olink.account.base.impl.BaseBatchService;
import com.olink.account.batch.task.custstatistics.dao.ICustomerStatisticsMonthDao;
import com.olink.account.batch.task.custstatistics.dao.impl.CustomerStatisticsMonthDao;
import com.olink.account.batch.task.custstatistics.service.ICustomerStatisticsMonthService;

/**
 * Created by zhangbin on 2017/5/5.
 * 客户账月统计服务
 */
public class CustomerStatisticsMonthService extends BaseBatchService implements ICustomerStatisticsMonthService {

    private ICustomerStatisticsMonthDao customerStatisticsMonthDao = new CustomerStatisticsMonthDao();

    @Override
    public boolean doMonthStatistics(String yearmonth) {
        customerStatisticsMonthDao.beginTranslist();
        if (customerStatisticsMonthDao.doMonthStatistics(yearmonth)) {
            customerStatisticsMonthDao.commitTranslist();
            return true;
        } else {
            customerStatisticsMonthDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public void doHistory(int month) {
        customerStatisticsMonthDao.beginTranslist();
        if (customerStatisticsMonthDao.doHistory(month)) {
            customerStatisticsMonthDao.commitTranslist();
        } else {
            customerStatisticsMonthDao.rollBackTranslist();
        }
    }

    @Override
    public void dropTable() {

    }
}

package com.olink.account.batch.task.custstatistics.service.impl;

import com.olink.account.base.impl.BaseBatchService;
import com.olink.account.batch.task.custstatistics.dao.ICustomerStatisticsYearDao;
import com.olink.account.batch.task.custstatistics.dao.impl.CustomerStatisticsYearDao;
import com.olink.account.batch.task.custstatistics.service.ICustomerStatisticsYearService;

/**
 * Created by zhangbin on 2017/5/5.
 * 客户账月统计服务
 */
public class CustomerStatisticsYearService extends BaseBatchService implements ICustomerStatisticsYearService {

    private ICustomerStatisticsYearDao customerStatisticsYearDao = new CustomerStatisticsYearDao();

    @Override
    public boolean doYearStatistics(String year) {
        customerStatisticsYearDao.beginTranslist();
        if (customerStatisticsYearDao.doYearStatistics(year)) {
            customerStatisticsYearDao.commitTranslist();
            return true;
        } else {
            customerStatisticsYearDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public void dropTable() {

    }
}

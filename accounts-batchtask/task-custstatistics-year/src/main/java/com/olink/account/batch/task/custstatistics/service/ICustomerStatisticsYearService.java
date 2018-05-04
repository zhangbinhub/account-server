package com.olink.account.batch.task.custstatistics.service;

import com.olink.account.base.IBaseBatchService;

/**
 * Created by zhangbin on 2017/5/5.
 * 客户账月统计接口
 */
public interface ICustomerStatisticsYearService extends IBaseBatchService {

    /**
     * 执行客户账年统计
     *
     * @param year 统计年
     * @return true|false
     */
    boolean doYearStatistics(String year);

}

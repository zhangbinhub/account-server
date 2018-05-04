package com.olink.account.batch.task.custstatistics.service;

import com.olink.account.base.IBaseBatchService;

/**
 * Created by zhangbin on 2017/5/5.
 * 客户账月统计接口
 */
public interface ICustomerStatisticsMonthService extends IBaseBatchService {

    /**
     * 执行客户账月统计
     *
     * @param yearmonth 统计年月
     * @return true|false
     */
    boolean doMonthStatistics(String yearmonth);

    /**
     * 指定月数以前的数据迁移至历史表
     *
     * @param month 月数
     */
    void doHistory(int month);

}

package com.olink.account.batch.task.custstatistics.dao;

import com.olink.account.base.IBaseBatchDao;
import com.olink.account.model.batch.accounting.CustomerYearStatistics;

/**
 * Created by zhangbin on 2017/5/5.
 * 客户账月统计持久化
 */
public interface ICustomerStatisticsYearDao extends IBaseBatchDao {

    /**
     * 执行客户账年统计
     *
     * @param year 统计年
     * @return true|false
     */
    boolean doYearStatistics(String year);

    /**
     * 获取上年的统计信息
     *
     * @param custid 客户号
     * @param type   虚拟账户类型
     * @param year   年
     * @return 客户账年统计信息
     */
    CustomerYearStatistics findYearStatisticsByYear(String custid, String type, String year) throws Exception;

}

package com.olink.account.batch.task.custstatistics.dao;

import com.olink.account.base.IBaseBatchDao;
import com.olink.account.model.batch.accounting.CustomerMonthStatistics;
import pers.acp.tools.exceptions.TimerException;

/**
 * Created by zhangbin on 2017/5/5.
 * 客户账月统计持久化
 */
public interface ICustomerStatisticsMonthDao extends IBaseBatchDao {

    /**
     * 执行客户账月统计
     *
     * @param yearmonth 统计年月
     * @return true|false
     */
    boolean doMonthStatistics(String yearmonth);

    /**
     * 获取上月的统计信息
     *
     * @param custid    客户号
     * @param type      虚拟账户类型
     * @param yearmonth 年月
     * @return 客户账月统计信息
     */
    CustomerMonthStatistics findMonthStatisticsByYearMonth(String custid, String type, String yearmonth) throws Exception;

    /**
     * 指定月数以前的数据迁移至历史表
     *
     * @param month 月数
     * @return true|false
     */
    boolean doHistory(int month);

}

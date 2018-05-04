package com.olink.account.batch.task.innerstatistics.dao;

import com.olink.account.base.IBaseBatchDao;
import com.olink.account.model.batch.accounting.InnerStatistics;

/**
 * Created by zhangbin on 2017/5/8.
 * 内部账统计持久化接口
 */
public interface IStatisticsInnerDao extends IBaseBatchDao {

    /**
     * 根据内部账号和会计日期查询统计结果
     *
     * @param innerid     内部账号
     * @param accountDate 会计日期
     * @return 统计结果
     */
    InnerStatistics findInnerStatisticsByInneridAndAccountDate(String innerid, String accountDate);

    /**
     * 删除统计信息
     *
     * @param accountDate 会计日期
     * @return true|false
     */
    boolean doDeleteInnerStatistics(String accountDate);

    /**
     * 创建统计信息
     *
     * @param innerStatistics 统计实例
     * @return true|false
     */
    boolean doCreateInnerStatistics(InnerStatistics innerStatistics);

}

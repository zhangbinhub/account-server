package com.olink.account.batch.dao;

import com.olink.account.base.IBaseBatchDao;
import com.olink.account.model.batch.accounting.InnerStatistics;
import com.olink.account.model.batch.accounting.InnerSummary;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.List;

/**
 * Created by zhangbin on 2017/5/10.
 * 内部账信息持久化
 */
public interface IInnerDao extends IBaseBatchDao {

    /**
     * 获取待扎账的内部账统计信息
     *
     * @param bindaccountdate 会计日期（扎账日）
     * @return 内部账统计信息
     */
    List<DBTable> findInnerStatisticsForBind(String bindaccountdate);

    /**
     * 获取待扎账的内部账汇总信息
     *
     * @param bindaccountdate 会计日期（扎账日）
     * @return 内部账统计信息
     */
    List<DBTable> findInnerSummaryForBind(String bindaccountdate);

    /**
     * 删除内部账统计信息
     *
     * @param innerStatistics 内部账统计信息实例
     * @return true|false
     */
    boolean doDeleteInnerStatistics(InnerStatistics innerStatistics);

    /**
     * 删除内部账汇总信息
     *
     * @param innerSummary 内部账汇总信息实例
     * @return true|false
     */
    boolean doDeleteInnerSummary(InnerSummary innerSummary);

}

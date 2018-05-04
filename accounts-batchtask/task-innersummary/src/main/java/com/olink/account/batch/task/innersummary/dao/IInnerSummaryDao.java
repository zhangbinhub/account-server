package com.olink.account.batch.task.innersummary.dao;

import com.olink.account.base.IBaseBatchDao;
import com.olink.account.batch.exception.BatchException;
import com.olink.account.model.trade.dictionary.D_AccountItem;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.exceptions.TimerException;

import java.util.Map;

/**
 * Created by zhangbin on 2017/5/9.
 * 内部账科目汇总持久化接口
 */
public interface IInnerSummaryDao extends IBaseBatchDao {

    /**
     * 执行内部账科目汇总统计
     *
     * @param accountDate    会计日期
     * @param accountItemMap 会计科目字典
     * @return true|false
     */
    boolean doSummary(String accountDate, Map<String, D_AccountItem> accountItemMap) throws BatchException, TimerException, EnumValueUndefinedException;

    /**
     * 删除内部账科目汇总信息
     *
     * @param accountDate 会计日期
     * @return true|false
     */
    boolean doDeleteInnerSummary(String accountDate);

}

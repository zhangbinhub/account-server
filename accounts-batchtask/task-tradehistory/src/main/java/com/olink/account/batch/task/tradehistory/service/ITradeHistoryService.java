package com.olink.account.batch.task.tradehistory.service;

import com.olink.account.base.IBaseBatchService;
import com.olink.account.batch.exception.BatchException;

/**
 * Created by zhangbin on 2017/5/10.
 * 历史交易数据服务接口
 */
public interface ITradeHistoryService extends IBaseBatchService {

    /**
     * 执行历史数据处理
     *
     * @param accountDate 会计日期
     * @return true|false
     */
    boolean doHistory(String accountDate) throws BatchException;

}

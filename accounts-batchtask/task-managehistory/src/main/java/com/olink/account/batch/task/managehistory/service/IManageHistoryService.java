package com.olink.account.batch.task.managehistory.service;

import com.olink.account.base.IBaseBatchService;
import com.olink.account.batch.exception.BatchException;

/**
 * Created by zhangbin on 2017/5/11.
 * 历史系统数据服务接口
 */
public interface IManageHistoryService extends IBaseBatchService {

    /**
     * 执行历史数据迁移
     *
     * @param yearmonth 历史数据年月
     * @return true|false
     */
    boolean doHistory(String yearmonth) throws BatchException;

}

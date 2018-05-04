package com.olink.account.batch.task.innerstatistics.service;

import com.olink.account.base.IBaseBatchService;
import com.olink.account.batch.exception.BatchException;

/**
 * Created by zhangbin on 2017/5/8.
 * 内部账统计服务接口
 */
public interface IStatisticsInnerService extends IBaseBatchService {

    /**
     * 执行内部账统计
     *
     * @param accountDate 会计日期
     * @return true|false
     */
    boolean doStatisticsInnerAccount(String accountDate) throws BatchException;

}

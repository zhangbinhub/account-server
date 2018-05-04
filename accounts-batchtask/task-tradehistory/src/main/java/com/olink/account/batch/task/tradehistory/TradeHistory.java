package com.olink.account.batch.task.tradehistory;

import com.olink.account.batch.task.BaseBatchTask;
import com.olink.account.batch.task.BatchTaskResult;
import com.olink.account.batch.task.tradehistory.service.ITradeHistoryService;
import com.olink.account.batch.task.tradehistory.service.impl.TradeHistoryService;
import com.olink.account.model.trade.dictionary.D_Batch;

/**
 * Created by zhangbin on 2017/5/10.
 * 历史交易数据迁移
 */
public class TradeHistory extends BaseBatchTask {

    private ITradeHistoryService tradeHistoryService = new TradeHistoryService();

    /**
     * 任务构造函数
     *
     * @param accountDate           会计日期
     * @param packagelastmodifydate 组件包最后修改时间
     * @param taskName              任务名称
     * @param batchConfig           任务配置类
     * @param prevTaskNo            前置任务序号
     * @param needExecuteImmediate  是否需要立即执行
     */
    public TradeHistory(String accountDate, String packagelastmodifydate, String taskName, D_Batch batchConfig, Integer prevTaskNo, boolean needExecuteImmediate) {
        super(accountDate, packagelastmodifydate, taskName, batchConfig, prevTaskNo, needExecuteImmediate);
    }

    @Override
    public boolean beforeExcute() throws Exception {
        return true;
    }

    @Override
    public BatchTaskResult doExcuteTask() throws Exception {
        BatchTaskResult result;
        if (tradeHistoryService.doHistory(getAccountDate())) {
            result = BatchTaskResult.getSuccessResult();
        } else {
            result = BatchTaskResult.getFaildResult("历史交易数据迁移失败");
        }
        return result;
    }

    @Override
    public void afterExcute(BatchTaskResult batchTaskResult) throws Exception {

    }
}

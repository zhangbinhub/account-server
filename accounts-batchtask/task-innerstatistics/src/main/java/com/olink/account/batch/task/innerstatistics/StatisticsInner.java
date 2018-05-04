package com.olink.account.batch.task.innerstatistics;

import com.olink.account.batch.service.IDateService;
import com.olink.account.batch.service.impl.DateService;
import com.olink.account.batch.task.BaseBatchTask;
import com.olink.account.batch.task.BatchTaskResult;
import com.olink.account.batch.task.innerstatistics.service.IStatisticsInnerService;
import com.olink.account.batch.task.innerstatistics.service.impl.StatisticsInnerService;
import com.olink.account.model.trade.dictionary.D_Batch;

/**
 * Created by zhangbin on 2017/5/5.
 * 内部账统计
 */
public class StatisticsInner extends BaseBatchTask {

    private IStatisticsInnerService statisticsInnerService = new StatisticsInnerService();

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
    public StatisticsInner(String accountDate, String packagelastmodifydate, String taskName, D_Batch batchConfig, Integer prevTaskNo, boolean needExecuteImmediate) {
        super(accountDate, packagelastmodifydate, taskName, batchConfig, prevTaskNo, needExecuteImmediate);
    }

    @Override
    public boolean beforeExcute() throws Exception {
        return true;
    }

    @Override
    public BatchTaskResult doExcuteTask() throws Exception {
        BatchTaskResult result;
        IDateService dateService = new DateService();
        if (statisticsInnerService.doStatisticsInnerAccount(dateService.getPrevAccountDate(getAccountDate()))) {
            result = BatchTaskResult.getSuccessResult();
        } else {
            result = BatchTaskResult.getFaildResult("内部账户统计失败");
        }
        return result;
    }

    @Override
    public void afterExcute(BatchTaskResult batchTaskResult) throws Exception {

    }
}

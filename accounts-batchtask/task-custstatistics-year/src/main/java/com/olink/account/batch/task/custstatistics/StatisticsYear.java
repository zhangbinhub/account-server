package com.olink.account.batch.task.custstatistics;

import com.olink.account.batch.task.BaseBatchTask;
import com.olink.account.batch.task.BatchTaskResult;
import com.olink.account.batch.task.custstatistics.service.ICustomerStatisticsYearService;
import com.olink.account.batch.task.custstatistics.service.impl.CustomerStatisticsYearService;
import com.olink.account.model.trade.dictionary.D_Batch;
import pers.acp.tools.common.CommonTools;

import java.util.Calendar;

/**
 * Created by zhangbin on 2017/5/5.
 * 客户账统计（年）
 */
public class StatisticsYear extends BaseBatchTask {

    private ICustomerStatisticsYearService customerStatisticsYearService = new CustomerStatisticsYearService();

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
    public StatisticsYear(String accountDate, String packagelastmodifydate, String taskName, D_Batch batchConfig, Integer prevTaskNo, boolean needExecuteImmediate) {
        super(accountDate, packagelastmodifydate, taskName, batchConfig, prevTaskNo, needExecuteImmediate);
    }

    @Override
    public boolean beforeExcute() throws Exception {
        return true;
    }

    @Override
    public BatchTaskResult doExcuteTask() throws Exception {
        BatchTaskResult result;
        Calendar c = Calendar.getInstance();
        c.add(Calendar.YEAR, -1);
        String year = CommonTools.getDateTimeString(c.getTime(), "yyyy");
        if (customerStatisticsYearService.doYearStatistics(year)) {
            result = BatchTaskResult.getSuccessResult();
        } else {
            result = BatchTaskResult.getFaildResult("客户账年统计失败：" + year);
        }
        return result;
    }

    @Override
    public void afterExcute(BatchTaskResult batchTaskResult) throws Exception {

    }
}

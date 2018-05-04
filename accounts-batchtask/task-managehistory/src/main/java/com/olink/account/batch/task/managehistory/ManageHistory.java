package com.olink.account.batch.task.managehistory;

import com.olink.account.batch.task.BaseBatchTask;
import com.olink.account.batch.task.BatchTaskResult;
import com.olink.account.batch.task.managehistory.service.IManageHistoryService;
import com.olink.account.batch.task.managehistory.service.impl.ManageHistoryService;
import com.olink.account.model.trade.dictionary.D_Batch;
import pers.acp.tools.common.CalendarTools;
import pers.acp.tools.common.CommonTools;

import java.util.Calendar;

/**
 * Created by zhangbin on 2017/5/11.
 * 历史系统数据迁移
 */
public class ManageHistory extends BaseBatchTask {

    private IManageHistoryService manageHistoryService = new ManageHistoryService();

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
    public ManageHistory(String accountDate, String packagelastmodifydate, String taskName, D_Batch batchConfig, Integer prevTaskNo, boolean needExecuteImmediate) {
        super(accountDate, packagelastmodifydate, taskName, batchConfig, prevTaskNo, needExecuteImmediate);
    }

    @Override
    public boolean beforeExcute() throws Exception {
        return true;
    }

    @Override
    public BatchTaskResult doExcuteTask() throws Exception {
        BatchTaskResult result;
        Calendar c = CalendarTools.getCalendar(getAccountDate());
        c.add(Calendar.MONTH, -1);
        String yearmonth = CommonTools.getDateTimeString(c.getTime(), "yyyy-MM");
        if (manageHistoryService.doHistory(yearmonth)) {
            result = BatchTaskResult.getSuccessResult();
        } else {
            result = BatchTaskResult.getFaildResult("历史系统数据迁移失败");
        }
        return result;
    }

    @Override
    public void afterExcute(BatchTaskResult batchTaskResult) throws Exception {

    }
}

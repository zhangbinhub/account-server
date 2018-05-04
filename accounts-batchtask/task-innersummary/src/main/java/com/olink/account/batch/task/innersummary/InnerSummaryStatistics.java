package com.olink.account.batch.task.innersummary;

import com.olink.account.batch.service.IDateService;
import com.olink.account.batch.service.impl.DateService;
import com.olink.account.batch.task.BaseBatchTask;
import com.olink.account.batch.task.BatchTaskResult;
import com.olink.account.batch.task.innersummary.service.IInnerSummaryService;
import com.olink.account.batch.task.innersummary.service.impl.InnerSummaryService;
import com.olink.account.enumration.DictionaryTableEnum;
import com.olink.account.model.trade.dictionary.D_AccountItem;
import com.olink.account.model.trade.dictionary.D_Batch;
import com.olink.account.service.IDictionaryService;
import com.olink.account.service.impl.DictionaryService;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2017/5/9.
 * 内部账科目汇总
 */
public class InnerSummaryStatistics extends BaseBatchTask {

    private IInnerSummaryService innerSummaryService = new InnerSummaryService();

    private Map<String, D_AccountItem> accountItemMap = null;

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
    public InnerSummaryStatistics(String accountDate, String packagelastmodifydate, String taskName, D_Batch batchConfig, Integer prevTaskNo, boolean needExecuteImmediate) {
        super(accountDate, packagelastmodifydate, taskName, batchConfig, prevTaskNo, needExecuteImmediate);
    }

    /**
     * 获取科目列表
     *
     * @return 科目列表
     */
    private Map<String, D_AccountItem> findAccountItem() {
        Map<String, D_AccountItem> accountItemMap = new HashMap<>();
        IDictionaryService dictionaryService = new DictionaryService();
        List<DBTable> accountItemList = dictionaryService.findDictionaryList(DictionaryTableEnum.accountItem, null, "");
        for (DBTable dbTable : accountItemList) {
            D_AccountItem accountItem = (D_AccountItem) dbTable;
            String parentcode = "";
            for (DBTable dbTable1 : accountItemList) {
                D_AccountItem accountItem1 = (D_AccountItem) dbTable1;
                if (accountItem1.getId().equals(accountItem.getParentid())) {
                    parentcode = accountItem1.getCode();
                    break;
                }
            }
            accountItem.setParentcode(parentcode);
            accountItemMap.put(accountItem.getParentcode() + accountItem.getCode(), accountItem);
        }
        return accountItemMap;
    }

    @Override
    public boolean beforeExcute() throws Exception {
        accountItemMap = findAccountItem();
        return accountItemMap != null;
    }

    @Override
    public BatchTaskResult doExcuteTask() throws Exception {
        BatchTaskResult result;
        IDateService dateService = new DateService();
        if (innerSummaryService.doSummary(dateService.getPrevAccountDate(getAccountDate()), accountItemMap)) {
            result = BatchTaskResult.getSuccessResult();
        } else {
            result = BatchTaskResult.getFaildResult("内部账科目汇总失败");
        }
        return result;
    }

    @Override
    public void afterExcute(BatchTaskResult batchTaskResult) throws Exception {

    }

}

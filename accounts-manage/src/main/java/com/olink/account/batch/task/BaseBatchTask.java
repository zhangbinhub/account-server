package com.olink.account.batch.task;

import com.olink.account.batch.service.ISituationService;
import com.olink.account.batch.service.impl.SituationService;
import com.olink.account.enumration.BatchTaskStatus;
import com.olink.account.enumration.BatchTaskType;
import com.olink.account.model.batch.info.BatchSituation;
import com.olink.account.model.trade.dictionary.D_Batch;
import pers.acp.tools.common.CalendarTools;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.exceptions.TimerException;
import pers.acp.tools.task.timer.basetask.BaseTimerTask;
import pers.acp.tools.task.timer.ruletype.CircleType;
import pers.acp.tools.task.timer.ruletype.ExcuteType;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;

/**
 * Created by zhangbin on 2016/12/14.
 * 批量任务基类
 */
public abstract class BaseBatchTask extends BaseTimerTask {

    /**
     * 日志对象
     */
    private final Logger log = Logger.getLogger(this.getClass());

    public BatchTaskResult getBatchTaskResult() {
        return (BatchTaskResult) getTaskResult();
    }

    protected String getAccountDate() {
        return accountDate;
    }

    /**
     * 会计日期
     */
    private String accountDate;

    /**
     * 组件包最后修改时间
     */
    private String packagelastmodifydate;

    /**
     * 批量任务配置类
     */
    private D_Batch batchConfig;

    /**
     * 任务处理信息id
     */
    private String batchSituationId;

    /**
     * 前置任务序号
     */
    private Integer prevTaskNo;

    /**
     * 任务是否可执行
     */
    private boolean isExcutable = false;

    /**
     * 是否需要立即执行
     */
    private boolean needExecuteImmediate = false;

    /**
     * 手动触发人id
     */
    private String edituserid;

    /**
     * 授权人id
     */
    private String authuserid;

    /**
     * 处理情况服务对象
     */
    private ISituationService situationService = new SituationService();

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
    public BaseBatchTask(String accountDate, String packagelastmodifydate, String taskName, D_Batch batchConfig, Integer prevTaskNo, boolean needExecuteImmediate) {
        super(taskName);
        this.accountDate = accountDate;
        this.packagelastmodifydate = packagelastmodifydate;
        this.batchConfig = batchConfig;
        this.prevTaskNo = prevTaskNo;
        this.needExecuteImmediate = needExecuteImmediate;
    }

    /**
     * 任务开始
     *
     * @return true|false
     */
    public boolean beginTask() {
        try {
            if (batchConfig != null) {
                BatchSituation currBatchSituation = situationService.findBatchSituationCurrDay(this.getClass().getCanonicalName(), this.accountDate);
                if (currBatchSituation != null) {
                    if (needExecuteImmediate || currBatchSituation.getStatus().equals(BatchTaskStatus.faild)) {
                        isExcutable = true;
                        log.error("任务重新启动：" + this.getTaskName());
                        this.batchSituationId = currBatchSituation.getId();
                        doChangeSituationStatus(BatchTaskStatus.begin, null);
                        return isExcutable;
                    } else {
                        isExcutable = false;
                        TaskManager.releaseLoad(this.getClass().getCanonicalName());
                        return false;
                    }
                }
                currBatchSituation = new BatchSituation();
                currBatchSituation.setTaskname(this.getTaskName().trim());
                currBatchSituation.setTaskclassname(this.getClass().getCanonicalName());
                currBatchSituation.setTaskpackagename(batchConfig.getField1().trim());
                currBatchSituation.setTaskno(Integer.valueOf(batchConfig.getType().trim()));
                BatchTaskType batchTaskType = BatchTaskType.getEnum(Integer.valueOf(batchConfig.getField5()));
                currBatchSituation.setTasktype(batchTaskType);
                currBatchSituation.setPackagelastmodifydate(packagelastmodifydate);
                currBatchSituation.setEdituserid(edituserid);
                currBatchSituation.setAuthuserid(authuserid);
                if (batchTaskType.equals(BatchTaskType.sequentially.getValue())) {
                    if (prevTaskNo != null) {
                        D_Batch prevBatch = TaskManager.getBatchConfig(prevTaskNo);
                        if (prevBatch != null) {
                            currBatchSituation.setPrevtaskclassname(prevBatch.getField1().trim());
                            currBatchSituation.setPrevtaskno(prevTaskNo);
                        }
                    }
                }
                currBatchSituation.setAccountdate(this.accountDate);
                if (situationService.doCreateSituation(currBatchSituation)) {
                    batchSituationId = currBatchSituation.getId();
                    isExcutable = doValidateTaskExcutable();
                    if (!isExcutable) {
                        if (batchTaskType.equals(BatchTaskType.Independent.getValue())) {
                            log.error("任务智能忽略：" + this.getTaskName());
                            doChangeSituationStatus(BatchTaskStatus.igone, null);
                        }
                        TaskManager.releaseLoad(this.getClass().getCanonicalName());
                    }
                    return isExcutable;
                } else {
                    String message = "创建任务处理信息失败！";
                    BatchTaskResult batchTaskResult = BatchTaskResult.getFaildResult(message);
                    setTaskResult(batchTaskResult);
                    log.error(message);
                    TaskManager.releaseLoad(this.getClass().getCanonicalName());
                    return false;
                }
            } else {
                String message = "任务配置信息为空！";
                BatchTaskResult batchTaskResult = BatchTaskResult.getFaildResult(message);
                setTaskResult(batchTaskResult);
                log.error("任务配置信息为空！");
                TaskManager.releaseLoad(this.getClass().getCanonicalName());
                return false;
            }
        } catch (Exception e) {
            doChangeSituationStatus(BatchTaskStatus.faild, "任务开始执行出错：" + e.getMessage());
            String message = "任务开始执行出错：" + e.getMessage();
            BatchTaskResult batchTaskResult = BatchTaskResult.getFaildResult(message);
            setTaskResult(batchTaskResult);
            log.error("任务开始执行出错：" + e.getMessage(), e);
            TaskManager.releaseLoad(this.getClass().getCanonicalName());
            return false;
        }
    }

    /**
     * 校验任务是否可执行
     *
     * @return true|false
     */
    private boolean doValidateTaskExcutable() throws EnumValueUndefinedException, TimerException {
        if (needExecuteImmediate) {
            return true;
        }
        BatchTaskType batchTaskType = BatchTaskType.getEnum(Integer.valueOf(batchConfig.getField5()));
        if (batchTaskType.equals(BatchTaskType.Independent.getValue())) {
            CircleType circleType = CircleType.getEnum(batchConfig.getField2());
            ExcuteType excuteType = ExcuteType.getEnum(batchConfig.getField3());
            String rules = batchConfig.getField4();
            Date now = new Date();
            Date prev = CalendarTools.getPrevDay(CalendarTools.getCalendar()).getTime();
            boolean circleflag = false;
            boolean excuteflag = true;
            switch (circleType) {
                case Day:
                    if (excuteType.equals(ExcuteType.WeekDay.getValue())) {
                        excuteflag = CalendarTools.isWeekDay(now);
                    } else {
                        excuteflag = !excuteType.equals(ExcuteType.Weekend.getValue()) || CalendarTools.isWeekend(now);
                    }
                    circleflag = CalendarTools.validateDay(now, prev, rules);
                    break;
                case Week:
                    circleflag = CalendarTools.validateWeek(now, prev, rules);
                    break;
                case Month:
                    circleflag = CalendarTools.validateMonth(now, prev, rules);
                    break;
                case Quarter:
                    circleflag = CalendarTools.validateQuarter(now, prev, rules);
                    break;
                case Year:
                    circleflag = CalendarTools.validateYear(now, prev, rules);
                    break;
            }
            return circleflag && excuteflag;
        } else {
            if (prevTaskNo != null) {
                D_Batch prevBatch = TaskManager.getBatchConfig(prevTaskNo);
                if (prevBatch != null) {
                    String prevTaskClassName = prevBatch.getCode().trim();
                    List<DBTable> batchSituationList = situationService.findBatchSituationByClassName(prevTaskClassName);
                    if (!batchSituationList.isEmpty()) {
                        BatchSituation prevBatchSituation = situationService.findBatchSituationPrevDay(prevTaskClassName, this.accountDate);
                        if (prevBatchSituation == null) {
                            return false;
                        }
                        BatchTaskStatus batchTaskStatus = prevBatchSituation.getStatus();
                        if (!batchTaskStatus.equals(BatchTaskStatus.success.getValue())) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }

    /**
     * 任务执行前方法
     *
     * @return true|false
     */
    public abstract boolean beforeExcute() throws Exception;

    /**
     * 任务执行方法
     *
     * @return 任务执行结果
     */
    public abstract BatchTaskResult doExcuteTask() throws Exception;

    /**
     * 任务执行后方法
     *
     * @param batchTaskResult 任务执行结果
     */
    public abstract void afterExcute(BatchTaskResult batchTaskResult) throws Exception;

    @Override
    public boolean beforeExcuteFun() {
        try {
            if (isExcutable) {
                doChangeSituationStatus(BatchTaskStatus.processing, null);
                return getTaskResult() == null && beforeExcute();
            } else {
                String message = "任务执行出错：不符合执行规则";
                doChangeSituationStatus(BatchTaskStatus.faild, message);
                BatchTaskResult batchTaskResult = BatchTaskResult.getFaildResult(message);
                setTaskResult(batchTaskResult);
                log.error(message);
                TaskManager.releaseLoad(this.getClass().getCanonicalName());
                return false;
            }
        } catch (Exception e) {
            String message = "任务执行出错：" + e.getMessage();
            doChangeSituationStatus(BatchTaskStatus.faild, message);
            BatchTaskResult batchTaskResult = BatchTaskResult.getFaildResult(message);
            setTaskResult(batchTaskResult);
            log.error(message, e);
            TaskManager.releaseLoad(this.getClass().getCanonicalName());
            return false;
        }
    }

    @Override
    public Object excuteFun() {
        try {
            return doExcuteTask();
        } catch (Exception e) {
            String message = "任务执行出错：" + e.getMessage();
            doChangeSituationStatus(BatchTaskStatus.faild, message);
            BatchTaskResult batchTaskResult = BatchTaskResult.getFaildResult(message);
            log.error(message, e);
            return batchTaskResult;
        }
    }

    @Override
    public void afterExcuteFun(Object o) {
        try {
            BatchTaskResult taskResult = (BatchTaskResult) o;
            if (taskResult != null && taskResult.isPass()) {
                doChangeSituationStatus(BatchTaskStatus.success, null);
            } else {
                if (taskResult == null) {
                    String message = "任务执行失败";
                    doChangeSituationStatus(BatchTaskStatus.faild, message);
                    BatchTaskResult batchTaskResult = BatchTaskResult.getFaildResult(message);
                    setTaskResult(batchTaskResult);
                } else {
                    doChangeSituationStatus(BatchTaskStatus.faild, taskResult.getMessage());
                }
            }
            afterExcute(taskResult);
            TaskManager.releaseLoad(this.getClass().getCanonicalName());
        } catch (Exception e) {
            String message = "任务执行出错：" + e.getMessage();
            doChangeSituationStatus(BatchTaskStatus.faild, message);
            BatchTaskResult batchTaskResult = BatchTaskResult.getFaildResult(message);
            setTaskResult(batchTaskResult);
            log.error(message, e);
            TaskManager.releaseLoad(this.getClass().getCanonicalName());
        }
    }

    /**
     * 更新处理情况状态
     *
     * @param batchTaskStatus 状态
     * @param message         描述
     */
    private void doChangeSituationStatus(BatchTaskStatus batchTaskStatus, String message) {
        if (batchSituationId != null) {
            BatchSituation batchSituation = new BatchSituation();
            batchSituation.setId(batchSituationId);
            batchSituation.setPackagelastmodifydate(packagelastmodifydate);
            batchSituation.setStatus(batchTaskStatus);
            batchSituation.setDescription(message);
            batchSituation.setEdituserid(edituserid);
            batchSituation.setAuthuserid(authuserid);
            try {
                situationService.doUpdateSituation(batchSituation);
            } catch (Exception e) {
                String errmessage = "修改任务状态出错：" + e.getMessage();
                doChangeSituationStatus(BatchTaskStatus.faild, errmessage);
                BatchTaskResult batchTaskResult = BatchTaskResult.getFaildResult(errmessage);
                setTaskResult(batchTaskResult);
                log.error(errmessage, e);
            }
        }
    }

}

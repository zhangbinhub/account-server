package com.olink.account.batch.service;

import com.olink.account.base.IBaseBatchService;
import com.olink.account.model.batch.info.BatchSituation;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.exceptions.TimerException;

import java.util.List;

/**
 * Created by zhangbin on 2017/1/3.
 * 处理情况服务接口
 */
public interface ISituationService extends IBaseBatchService {

    /**
     * 创建记录
     *
     * @param batchSituation 批量任务执行情况对象
     * @return true|false
     */
    boolean doCreateSituation(BatchSituation batchSituation);

    /**
     * 获取处理信息对象
     *
     * @return 处理信息对象
     */
    List<DBTable> findBatchSituationForNoPass();

    /**
     * 获取处理信息对象
     *
     * @param batchSituationId 处理信息id
     * @return 处理信息对象
     */
    BatchSituation findBatchSituation(String batchSituationId);

    /**
     * 获取处理信息对象
     *
     * @param batchTaskClassName 任务类名
     * @return 处理信息对象
     */
    List<DBTable> findBatchSituationByClassName(String batchTaskClassName);

    /**
     * 获取当天处理信息
     *
     * @param batchTaskClassName 任务类名
     * @param accountDate        会计日期
     * @return 处理信息对象
     */
    BatchSituation findBatchSituationCurrDay(String batchTaskClassName, String accountDate);

    /**
     * 获取上一天处理信息
     *
     * @param batchTaskClassName 任务类名
     * @param accountDate        会计日期
     * @return 处理信息对象
     */
    BatchSituation findBatchSituationPrevDay(String batchTaskClassName, String accountDate) throws TimerException;

    /**
     * 更新记录
     *
     * @param batchSituation 批量任务执行情况对象
     * @return true|false
     */
    boolean doUpdateSituation(BatchSituation batchSituation) throws EnumValueUndefinedException;

}

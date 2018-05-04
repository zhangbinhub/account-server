package com.olink.account.batch.init;

import com.olink.account.batch.config.MainServerConfig;
import com.olink.account.batch.exception.BatchException;
import com.olink.account.batch.service.IDateService;
import com.olink.account.batch.service.impl.DateService;
import com.olink.account.batch.task.BatchTaskResult;
import com.olink.account.batch.task.BatchTimerDriver;
import com.olink.account.batch.task.TaskManager;
import pers.acp.communications.server.ctrl.DaemonServiceManager;
import pers.acp.tools.task.timer.basetask.BaseTimerTask;
import org.apache.log4j.Logger;

/**
 * Created by zhangbin on 2016/12/14.
 * 系统启动类
 */
class StartServer {

    /**
     * 日志对象
     */
    private static final Logger log = Logger.getLogger(StartServer.class);

    /**
     * 启动
     */
    static void doStart() {
        try {
            MainServerConfig mainServerConfig = MainServerConfig.getInstance();
            if (TaskManager.initTaskList()) {
                BatchTimerDriver server = new BatchTimerDriver("批量任务定时器", mainServerConfig.getCircleType(), mainServerConfig.getRule(), mainServerConfig.getExcuteType());
                server.startTimer(new BaseTimerTask("批量任务") {

                    @Override
                    public boolean beforeExcuteFun() {
                        return true;
                    }

                    @Override
                    public Object excuteFun() {
                        doExcuteBatchTasks();
                        return null;
                    }

                    @Override
                    public void afterExcuteFun(Object o) {

                    }

                });
                DaemonServiceManager.addService(server);
            } else {
                throw new BatchException("初始化任务列表失败！");
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

    /**
     * 执行批处理任务
     */
    private static void doExcuteBatchTasks() {
        if (TaskManager.initTaskList()) {
            IDateService dateService = new DateService();
            if (dateService.doChangeAccountDate()) {
                BatchTaskResult batchTaskResult = TaskManager.doExcuteTask();
                if (batchTaskResult != null) {
                    if (batchTaskResult.isPass()) {
                        log.info(batchTaskResult.getMessage());
                    } else {
                        log.error("批量任务执行失败：" + batchTaskResult.getMessage());
                    }
                } else {
                    log.error("批量任务执行出错！");
                }
            } else {
                log.error("切换会计日期失败！");
            }
        } else {
            log.error("初始化任务列表失败！");
        }
    }

}

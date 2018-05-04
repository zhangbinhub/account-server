package com.olink.account.batch.task;

import pers.acp.communications.server.base.interfaces.IDaemonService;
import pers.acp.tools.task.timer.TimerDriver;
import pers.acp.tools.task.timer.ruletype.CircleType;
import pers.acp.tools.task.timer.ruletype.ExcuteType;

/**
 * Created by zhangbin on 2017/1/4.
 * 批量任务定时器
 */
public class BatchTimerDriver extends TimerDriver implements IDaemonService {

    private String timerName;

    public BatchTimerDriver(String timerName, CircleType circleType, String rules, ExcuteType excuteType) {
        super(circleType, rules, excuteType);
        this.timerName = timerName;
    }

    @Override
    public String getServiceName() {
        return timerName;
    }

    @Override
    public void stopService() {
        this.stopTimer();
    }

}

package com.olink.account.batch.config;

import pers.acp.tools.config.base.BaseConfig;
import pers.acp.tools.exceptions.ConfigException;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.task.timer.ruletype.CircleType;
import pers.acp.tools.task.timer.ruletype.ExcuteType;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

/**
 * Created by zhangbin on 2016/12/14.
 * 系统启动规则
 */
@XStreamAlias("main-server")
public class MainServerConfig extends BaseConfig {

    public static MainServerConfig getInstance() throws ConfigException {
        return Load(MainServerConfig.class);
    }

    public CircleType getCircleType() throws EnumValueUndefinedException {
        return CircleType.getEnum(circleType);
    }

    public ExcuteType getExcuteType() throws EnumValueUndefinedException {
        return ExcuteType.getEnum(excuteType);
    }

    public String getRule() {
        return rule;
    }

    public String getTaskJarPath() {
        return taskJarPath;
    }

    @XStreamAlias("circleType")
    private String circleType;

    @XStreamAlias("excuteType")
    private String excuteType;

    @XStreamAlias("rule")
    private String rule;

    @XStreamAlias("taskJarPath")
    private String taskJarPath;

}

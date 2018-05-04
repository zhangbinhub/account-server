package com.olink.account.batch.init;

import com.olink.account.batch.config.MainServerConfig;
import com.olink.account.batch.config.SystemProperties;
import com.olink.account.config.ServerConfig;
import pers.acp.tools.exceptions.ConfigException;

/**
 * Created by zhang on 2016/7/22.
 * 系统初始化类
 */
public class InitServer {

    /**
     * 执行初始化
     */
    public void doInit() throws ConfigException {
        ServerConfig.getInstance();
        MainServerConfig.getInstance();
        SystemProperties.getInstance();
        StartServer.doStart();
    }

}

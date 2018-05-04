package com.olink.account.trade.init;

import com.olink.account.config.ServerConfig;
import pers.acp.tools.exceptions.ConfigException;

/**
 * Created by zhang on 2016/7/22.
 * 系统初始化类
 */
public class InitServer {

    public void doInit() throws ConfigException {
        ServerConfig.getInstance();
    }

}

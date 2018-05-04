package com.olink.account.batch.config;

import org.apache.log4j.Logger;

import java.util.Properties;

/**
 * Created by zhangbin on 2017/5/3.
 * 批量系统配置信息
 */
public class SystemProperties extends Properties {

    private static final Logger log = Logger.getLogger(SystemProperties.class);

    private static SystemProperties instance = null;

    /**
     * 获取配置实例
     *
     * @return 配置实例
     */
    public static SystemProperties getInstance() {
        try {
            if (instance == null) {
                instance = new SystemProperties();
                instance.load(SystemProperties.class.getClassLoader().getResourceAsStream("system.properties"));
            }
            return instance;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 获取批量操作员id
     *
     * @return 批量操作员id
     */
    public String getOperatorId() {
        return instance.getProperty("operatorId");
    }

    /**
     * 获取交易系统请求地址
     *
     * @return 交易系统请求地址
     */
    public String getTradeSysHost() {
        return instance.getProperty("tradesys.host");
    }

    /**
     * 获取账务系统应用id
     *
     * @return 账务系统应用id
     */
    public String getAppId() {
        return instance.getProperty("appid");
    }

}

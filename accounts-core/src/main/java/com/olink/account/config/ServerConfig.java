package com.olink.account.config;

import pers.acp.tools.config.base.BaseConfig;
import pers.acp.tools.exceptions.ConfigException;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

import java.util.List;

/**
 * Created by zhang on 2016/7/22.
 * 接口配置类
 */
@XStreamAlias("servers")
public class ServerConfig extends BaseConfig {

    public static ServerConfig getInstance() throws ConfigException {
        return Load(ServerConfig.class);
    }

    public boolean isAllowGet() {
        return allowGet;
    }

    public String getJsonKey() {
        return jsonKey;
    }

    public List<Server> getServer() {
        return server;
    }

    public boolean isPackageLog() {
        return packageLog;
    }

    @XStreamAsAttribute
    @XStreamAlias("packageLog")
    private boolean packageLog;

    @XStreamAsAttribute
    @XStreamAlias("allowGet")
    private boolean allowGet;

    @XStreamAsAttribute
    @XStreamAlias("jsonKey")
    private String jsonKey;

    @XStreamImplicit(itemFieldName = "server")
    private List<Server> server;

    public class Server {

        public String getServerno() {
            return serverno;
        }

        public String getServerClass() {
            return serverClass;
        }

        public boolean isBack() {
            return isBack;
        }

        public boolean isValidate() {
            return isValidate;
        }

        public boolean isCheckRepeat() {
            return isCheckRepeat;
        }

        public long getMinSpacingTime() {
            return minSpacingTime;
        }

        public boolean isValidateAccountDate() {
            return isValidateAccountDate;
        }

        @XStreamAsAttribute
        @XStreamAlias("serverno")
        private String serverno;

        @XStreamAsAttribute
        @XStreamAlias("serverClass")
        private String serverClass;

        @XStreamAsAttribute
        @XStreamAlias("isBack")
        private boolean isBack;

        @XStreamAsAttribute
        @XStreamAlias("isValidate")
        private boolean isValidate;

        @XStreamAsAttribute
        @XStreamAlias("isCheckRepeat")
        private boolean isCheckRepeat;

        @XStreamAsAttribute
        @XStreamAlias("minSpacingTime")
        private long minSpacingTime;

        @XStreamAsAttribute
        @XStreamAlias("isValidateAccountDate")
        private boolean isValidateAccountDate;

    }

}

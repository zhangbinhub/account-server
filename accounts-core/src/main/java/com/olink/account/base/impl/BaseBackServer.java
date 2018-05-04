package com.olink.account.base.impl;

import com.olink.account.base.IBaseServer;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.packages.Service;
import com.olink.account.service.ISysUserService;
import com.olink.account.service.impl.SysUserService;
import com.olink.account.utils.Utility;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;
import pers.acp.communications.tools.CommunicationTools;
import pers.acp.tools.common.CommonTools;

/**
 * Created by zhangbin on 2016/10/9.
 * 内部接口基类
 */
public abstract class BaseBackServer implements IBaseServer {

    protected HttpServletRequestAcp request;

    protected String serverno;

    protected String customerid;

    protected String validatecode;

    protected Service service;

    protected String appid;

    protected String userid;

    public BaseBackServer(HttpServletRequestAcp request) {
        this.request = request;
    }

    protected boolean validateAmont(String amont) {
        return Utility.isAvailableAmont(amont);
    }

    @Override
    public BaseServerResult doServer() throws ServerException {
        doValidate();
        return doBackServer();
    }

    /**
     * 校验
     */
    private void doValidate() throws ServerException {
        if (CommonTools.isNullStr(appid)) {
            throw new ServerException("没有操作权限");
        }
        if (CommonTools.isNullStr(userid)) {
            throw new ServerException("没有操作权限");
        }
        if (validatePermissions(appid, userid, service.getAction())) {
            ISysUserService sysUserService = new SysUserService();
            if (!sysUserService.validatePower(appid, userid)) {
                throw new ServerException("没有操作权限");
            }
        }
    }

    /**
     * 校验用户是否有权限
     *
     * @param appid           应用id
     * @param userid          用户id
     * @param permissionsCode 权限编码
     */
    protected boolean validatePermissions(String appid, String userid, String permissionsCode) throws ServerException {
        if (!CommunicationTools.validatePermissions(appid, userid, permissionsCode)) {
            throw new ServerException("操作员没有权限：" + permissionsCode);
        } else {
            return true;
        }
    }

    /**
     * 后台接口处理方法
     *
     * @return 处理结果
     */
    public abstract BaseServerResult doBackServer() throws ServerException;

}

package com.olink.account.service.impl;

import com.olink.account.base.impl.BaseTradeService;
import com.olink.account.dao.ISystemDao;
import com.olink.account.dao.impl.SystemDao;
import com.olink.account.exception.ServerException;
import com.olink.account.service.ISysUserService;

/**
 * Created by Shepherd on 2016-08-25.
 * 系统用户服务
 */
public class SysUserService extends BaseTradeService implements ISysUserService {

    private ISystemDao sysUserDao = new SystemDao();

    @Override
    public String validatePassword(String loginno, String password) throws ServerException {
        return sysUserDao.validatePassword(loginno, password);
    }

    @Override
    public boolean validatePower(String appid, String userid) {
        sysUserDao.beginTranslist();
        if (sysUserDao.validatePower(appid, userid)) {
            sysUserDao.commitTranslist();
            return true;
        } else {
            sysUserDao.rollBackTranslist();
            return false;
        }
    }

}

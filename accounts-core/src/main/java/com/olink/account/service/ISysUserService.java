package com.olink.account.service;

import com.olink.account.base.IBaseTradeService;
import com.olink.account.exception.ServerException;

/**
 * Created by Shepherd on 2016-08-25.
 * 系统用户服务
 */
public interface ISysUserService extends IBaseTradeService {

    /**
     * 校验用户密码
     *
     * @param loginno  登录名
     * @param password 密码
     * @return 用户id
     */
    String validatePassword(String loginno, String password) throws ServerException;

    /**
     * 校验操作权限
     *
     * @param appid  应用id
     * @param userid 用户id
     * @return true|false
     */
    boolean validatePower(String appid, String userid);

}

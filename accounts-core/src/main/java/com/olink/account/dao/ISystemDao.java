package com.olink.account.dao;

import com.olink.account.base.IBaseKernelDao;
import com.olink.account.exception.ServerException;
import com.olink.account.model.kernel.user.SysUser;
import com.olink.account.model.kernel.user.SysUserLoginRecord;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.List;

/**
 * Created by Shepherd on 2016-08-25.
 * 系统用户持久化
 */
public interface ISystemDao extends IBaseKernelDao {

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

    /**
     * 通过角色名称获取角色列表
     *
     * @param rolenames 角色名称，分离
     * @return 角色列表
     */
    List<DBTable> findRoleListByName(String rolenames);

    /**
     * 获取用户对象
     *
     * @param userid 用户id
     * @return true|false
     */
    SysUser findUserById(String userid);

    /**
     * 删除用户角色关联信息
     *
     * @param userid 用户id
     * @return true|false
     */
    boolean doDelUserRoleSet(String userid);

    /**
     * 创建用户
     *
     * @param sysUser   用户对象
     * @param rolenames 角色名称，分离
     * @return true|false
     */
    boolean createUser(SysUser sysUser, String rolenames);

    /**
     * C户修改登录名（手机号）
     *
     * @param sysUser 用户对象
     * @return true|false
     */
    boolean doModifyLoginNo(SysUser sysUser);

    /**
     * 获取指定年月用户WEB登录记录
     *
     * @param yearmonth 年月
     * @return 用户WEB登录记录
     */
    List<DBTable> findLoginRecordByYearMonth(String yearmonth);

    /**
     * 删除用户WEB登录记录
     *
     * @param sysUserLoginRecord 用户WEB登录记录
     * @return true|false
     */
    boolean doDeleteUserLoginRecord(SysUserLoginRecord sysUserLoginRecord);

}

package com.olink.account.dao;

import com.olink.account.base.IBaseTradeDao;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.customer.BusinessBindInfo;
import com.olink.account.model.trade.customer.BusinessInnerAccount;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.List;

/**
 * Created by zhangbin on 2016/9/7.
 * B户持久化接口
 */
public interface IBusinessDao extends IBaseTradeDao {

    /**
     * 获取B户列表
     *
     * @return B户列表
     */
    List<BusinessAccount> findBusinessAccount();

    /**
     * 通过id获取B户信息
     *
     * @param id B户id
     * @return B户信息
     */
    BusinessAccount findBusinessAccountById(String id);

    /**
     * 通过客户号获取B户信息
     *
     * @param businessId B户客户号
     * @return B户账户信息
     */
    BusinessAccount findBusinessAccountByBusinessId(String businessId);

    /**
     * 获取运营B户信息
     *
     * @return B户信息
     */
    BusinessAccount findYYBusinessAccount();

    /**
     * 通过B户客户号获取子账户列表
     *
     * @param businessid B客户号
     * @return 结果集
     */
    List<DBTable> findBusinessSubAccountByBusinessid(String businessid);

    /**
     * 通过id获取绑定信息
     *
     * @param id 绑定信息id
     * @return 结果集
     */
    BusinessBindInfo findBusinessBindInfoById(String id);

    /**
     * 通过B户客户号获取绑定信息
     *
     * @param businessid B客户号
     * @return 结果集
     */
    List<DBTable> findBusinessBindInfoByBusinessid(String businessid);

    /**
     * 获取B户内部账户
     *
     * @param businessid B客户号
     * @return 结果集
     */
    List<DBTable> findBusinessInnerAccountByBusinessid(String businessid);

    /**
     * 获取B户内部账户
     *
     * @param innerid 内部账号
     * @return 内部账户
     */
    BusinessInnerAccount findBusinessInnerAccountByInnerid(String innerid);

    /**
     * 创建B户内部账户
     *
     * @param businessInnerAccount 内部账户
     * @return true|false
     */
    boolean doCreateInnerAccount(BusinessInnerAccount businessInnerAccount) throws EnumValueUndefinedException, ServerException;

    /**
     * 更新B户内部账户
     *
     * @param businessInnerAccount 内部账户
     * @return true|false
     */
    boolean doUpdateInnerAccount(BusinessInnerAccount businessInnerAccount);

}

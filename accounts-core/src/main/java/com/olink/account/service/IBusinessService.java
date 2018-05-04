package com.olink.account.service;

import com.olink.account.base.IBaseTradeService;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.customer.BusinessBindInfo;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.List;

/**
 * Created by zhangbin on 2016/9/7.
 * B户服务接口
 */
public interface IBusinessService extends IBaseTradeService {

    /**
     * 通过id获取B户校验key
     *
     * @param id B户id
     * @return B户校验key
     */
    String getBusinessKeyById(String id);

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
     * @return B户账户信息
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
     * 获取B户默认结算绑定信息
     *
     * @param businessid 账户类型编码
     * @return 结果集
     */
    BusinessBindInfo findBusinessDefaultBindInfoByBusinessId(String businessid);

    /**
     * 获取运营B户信息
     *
     * @return B户信息
     */
    BusinessAccount findYYBusinessAccount();

    /**
     * 获取运营B户绑定类型
     *
     * @return 结果集
     */
    List<DBTable> findYYBusinessBindInfoType();

    /**
     * 获取运营B户绑定信息
     *
     * @param typecode 账户类型编码
     * @return 结果集
     */
    BusinessBindInfo findYYBusinessBindInfo(String typecode);

}
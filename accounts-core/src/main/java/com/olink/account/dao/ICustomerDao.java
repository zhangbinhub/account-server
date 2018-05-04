package com.olink.account.dao;

import com.olink.account.base.IBaseTradeDao;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.CustAccount;
import com.olink.account.model.trade.customer.CustBindInfo;
import com.olink.account.model.trade.customer.CustSubAccount;
import com.olink.account.model.trade.dictionary.Dictionary;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.List;

/**
 * Created by zhangbin on 2016/12/2.
 * C户持久化接口
 */
public interface ICustomerDao extends IBaseTradeDao {

    /**
     * 获取C户列表
     *
     * @return C户列表
     */
    List<CustAccount> findCustAccount() throws ServerException;

    /**
     * 根据id获取C户信息
     *
     * @param id C户id
     * @return C户对象
     */
    CustAccount findCustAccountById(String id) throws ServerException;

    /**
     * 根据custid获取C户信息
     *
     * @param custid 客户号
     * @return C户对象
     */
    CustAccount findCustAccountByCustId(String custid) throws ServerException;

    /**
     * 根据电话号码查询C户
     *
     * @param telephone 手机号
     * @return C户对象
     */
    CustAccount findCustAccountByTelephone(String telephone) throws ServerException;

    /**
     * 根据C户客户号获取绑定信息
     *
     * @param custid 客户号
     * @return 绑定信息
     */
    List<DBTable> findCustBindInfoByCustId(String custid);

    /**
     * 根据id获取C户绑定信息
     *
     * @param id 绑定信息id
     * @return 绑定信息
     */
    CustBindInfo findCustBindInfoById(String id);

    /**
     * 根据客户id获取子账户列表
     *
     * @param custid 客户号
     * @return 结果集
     */
    List<DBTable> findCustSubAccountByCustId(String custid) throws ServerException;

    /**
     * 获取指定年月的虚拟账户余额变动记录
     *
     * @param custid    C户客户号
     * @param type      虚拟账户类型
     * @param yearmonth 年月
     * @return 余额变动记录
     */
    List<DBTable> findCustChangeLog(String custid, String type, String yearmonth) throws ServerException;

    /**
     * 创建C户
     *
     * @param custAccount_p C户对象
     * @return C户对象
     */
    CustAccount doCreateCustAccount(CustAccount custAccount_p) throws ServerException;

    /**
     * 创建C户虚拟账户
     *
     * @param custid      C户客户号
     * @param accountType 虚拟账户类型
     * @return 虚拟账户信息
     */
    CustSubAccount doCreateCustSubAccount(String custid, Dictionary accountType);

    /**
     * C户修改手机号
     *
     * @param custAccount C户对象
     * @return true|false
     */
    boolean doModifyTelephone(CustAccount custAccount) throws ServerException;

    /**
     * C户添加绑定信息
     *
     * @param custBindInfo C户绑定信息对象
     * @return 处理结果 true or false
     */
    boolean doAddCustBindInfo(CustBindInfo custBindInfo);

    /**
     * C户删除绑定信息
     *
     * @param id C户绑定信息id
     * @return 处理结果 true or false
     */
    boolean doDelCustBinfInfo(String id);

    /**
     * 绑定信息设置为默认
     *
     * @param custBindInfo_p C户绑定信息对象
     * @return 处理结果 true or false
     */
    boolean setDefaultCustBindInfo(CustBindInfo custBindInfo_p);

    /**
     * 设置C户支付密码
     *
     * @param custAccount C户对象
     * @return 处理结果 true or false
     */
    boolean doSetCustPayPassword(CustAccount custAccount);

    /**
     * 验证C户支付密码
     *
     * @param custAccount custAccount C户对象
     * @return 处理结果 true or false
     */
    boolean doValidatePayPassword(CustAccount custAccount);

}

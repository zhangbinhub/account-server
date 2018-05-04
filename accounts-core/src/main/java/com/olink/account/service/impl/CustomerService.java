package com.olink.account.service.impl;

import com.olink.account.base.impl.BaseTradeService;
import com.olink.account.dao.ICustomerDao;
import com.olink.account.dao.IDictionaryDao;
import com.olink.account.dao.ISystemDao;
import com.olink.account.dao.impl.CustomerDao;
import com.olink.account.dao.impl.DictionaryDao;
import com.olink.account.dao.impl.SystemDao;
import com.olink.account.enumration.DictionaryTableEnum;
import com.olink.account.enumration.StatusEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.CustAccount;
import com.olink.account.model.trade.customer.CustBindInfo;
import com.olink.account.model.trade.customer.CustSubAccount;
import com.olink.account.model.trade.dictionary.Dictionary;
import com.olink.account.model.kernel.user.SysUser;
import com.olink.account.service.ICustomerService;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.entity.DBTable;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by zhangbin on 2016/12/2.
 * C户服务
 */
public class CustomerService extends BaseTradeService implements ICustomerService {

    private final Logger log = Logger.getLogger(this.getClass());

    private ICustomerDao customerDao = new CustomerDao();

    /**
     * C户用户级别
     */
    private static final int cust_levels = 100;

    /**
     * C户角色名称
     */
    private static final String cust_rolename = "C类客户";

    @Override
    public List<CustAccount> findCustAccount() throws ServerException {
        return customerDao.findCustAccount();
    }

    @Override
    public CustAccount findCustAccountByCustId(String custid) throws ServerException {
        return customerDao.findCustAccountByCustId(custid);
    }

    @Override
    public CustAccount findCustAccountByTelephone(String telephone) throws ServerException {
        return customerDao.findCustAccountByTelephone(telephone);
    }

    @Override
    public List<DBTable> findCustBindInfoByCustId(String custid) {
        return customerDao.findCustBindInfoByCustId(custid);
    }

    @Override
    public CustBindInfo findCustBindInfoById(String id) {
        return customerDao.findCustBindInfoById(id);
    }

    @Override
    public List<DBTable> findCustSubAccountByCustId(String custid) throws ServerException {
        return customerDao.findCustSubAccountByCustId(custid);
    }

    @Override
    public List<DBTable> findCustChangeLog(String custid, String type, String yearmonth) throws ServerException {
        return customerDao.findCustChangeLog(custid, type, yearmonth);
    }

    @Override
    public CustAccount doCreateCustAccount(CustAccount custAccount_p) throws ServerException {
        ISystemDao userDao = new SystemDao();
        IDictionaryDao dictionaryDao = new DictionaryDao(customerDao.getDBTools());
        customerDao.beginTranslist();
        userDao.beginTranslist();
        CustAccount custAccount_new = customerDao.doCreateCustAccount(custAccount_p);
        if (custAccount_new == null) {
            customerDao.rollBackTranslist();
            userDao.rollBackTranslist();
        } else {
            List<DBTable> custSubAccounts = customerDao.findCustSubAccountByCustId(custAccount_new.getCustid());
            if (custSubAccounts.isEmpty()) {
                log.info("正在创建子账户信息...");
                List<DBTable> accountTypes = dictionaryDao.findDictionaryList(DictionaryTableEnum.accountType, null, "");
                for (DBTable dbTable : accountTypes) {
                    Dictionary accountType = (Dictionary) dbTable;
                    CustSubAccount custSubAccount = customerDao.doCreateCustSubAccount(custAccount_new.getCustid(), accountType);
                    if (custSubAccount == null) {
                        customerDao.rollBackTranslist();
                        userDao.rollBackTranslist();
                        return null;
                    }
                    custSubAccounts.add(custSubAccount);
                }
            }
            custAccount_new.setSubAccount(custSubAccounts);
            SysUser sysUser = new SysUser(custAccount_new.getUserid(), custAccount_new.getNickname(), custAccount_new.getTelephone(), custAccount_new.getLoginpwd(), cust_levels, StatusEnum.activate.getValue(), cust_levels);
            if (userDao.createUser(sysUser, cust_rolename)) {
                customerDao.commitTranslist();
                userDao.commitTranslist();
            } else {
                customerDao.rollBackTranslist();
                userDao.rollBackTranslist();
                custAccount_new = null;
            }
        }
        return custAccount_new;
    }

    @Override
    public boolean doModifyTelephone(CustAccount custAccount) throws ServerException {
        ISystemDao sysUserDao = new SystemDao();
        customerDao.beginTranslist();
        sysUserDao.beginTranslist();
        CustAccount custAccount_o = customerDao.findCustAccountByCustId(custAccount.getCustid());
        if (custAccount_o != null) {
            if (!custAccount_o.getTelephone().equals(custAccount.getTelephone())) {
                customerDao.rollBackTranslist();
                sysUserDao.rollBackTranslist();
                throw new ServerException("原手机号不正确");
            }
            custAccount.setUserid(custAccount_o.getUserid());
        } else {
            customerDao.rollBackTranslist();
            sysUserDao.rollBackTranslist();
            throw new ServerException("客户信息不存在：" + custAccount.getCustid());
        }
        SysUser sysUser = sysUserDao.findUserById(custAccount.getUserid());
        if (sysUser.getPassword().equals(custAccount.getLoginpwd())) {
            sysUser.setLoginno(custAccount.getTelephone_new());
            sysUser.setPassword(custAccount.getLoginpwd_new());
            custAccount.setTelephone(custAccount.getTelephone_new());
            if (customerDao.doModifyTelephone(custAccount) && sysUserDao.doModifyLoginNo(sysUser)) {
                customerDao.commitTranslist();
                sysUserDao.commitTranslist();
                return true;
            } else {
                customerDao.rollBackTranslist();
                sysUserDao.rollBackTranslist();
                return false;
            }
        } else {
            customerDao.rollBackTranslist();
            sysUserDao.rollBackTranslist();
            throw new ServerException("原登录密码不正确");
        }
    }

    @Override
    public boolean doAddCustBindInfo(CustBindInfo custBindInfo) throws ServerException {
        CustAccount custAccount = customerDao.findCustAccountByCustId(custBindInfo.getCustid());
        if (custAccount == null) {
            throw new ServerException("客户信息不存在：" + custBindInfo.getCustid());
        }
        return customerDao.doAddCustBindInfo(custBindInfo);
    }

    @Override
    public boolean doDelCustBinfInfo(String ids) {
        String[] iDs = ids.split(",");
        customerDao.beginTranslist();
        for (String id : iDs) {
            if (!customerDao.doDelCustBinfInfo(id)) {
                customerDao.rollBackTranslist();
                return false;
            }
        }
        customerDao.commitTranslist();
        return true;
    }

    @Override
    public boolean setDefaultCustBindInfo(CustBindInfo custBindInfo_p) throws ServerException {
        CustAccount custAccount = customerDao.findCustAccountByCustId(custBindInfo_p.getCustid());
        if (custAccount == null) {
            throw new ServerException("客户信息不存在：" + custBindInfo_p.getCustid());
        }
        customerDao.beginTranslist();
        if (!customerDao.setDefaultCustBindInfo(custBindInfo_p)) {
            customerDao.rollBackTranslist();
            return false;
        }
        customerDao.commitTranslist();
        return true;
    }

    @Override
    public boolean doSetCustPayPassword(CustAccount custAccount) throws ServerException {
        CustAccount custAccount_o = customerDao.findCustAccountByCustId(custAccount.getCustid());
        if (custAccount_o == null) {
            throw new ServerException("客户信息不存在：" + custAccount.getCustid());
        }
        if (CommonTools.isNullStr(custAccount_o.getPassword())) {
            return customerDao.doSetCustPayPassword(custAccount);
        } else {
            throw new ServerException("已设置支付密码，请使用支付密码修改!");
        }
    }

    @Override
    public boolean doModCustPayPassword(CustAccount custAccount) throws ServerException {
        customerDao.beginTranslist();
        CustAccount custAccount_o = customerDao.findCustAccountByCustId(custAccount.getCustid());
        if (custAccount_o == null) {
            customerDao.rollBackTranslist();
            throw new ServerException("客户信息不存在：" + custAccount.getCustid());
        }
        if (customerDao.doValidatePayPassword(custAccount)) {
            custAccount.setPassword(custAccount.getPassword_new());
            if (customerDao.doSetCustPayPassword(custAccount)) {
                customerDao.commitTranslist();
                return true;
            } else {
                customerDao.rollBackTranslist();
                return false;
            }
        } else {
            customerDao.rollBackTranslist();
            throw new ServerException("原支付密码不正确");
        }
    }

    @Override
    public boolean doValidatePayPassword(CustAccount custAccount) throws ServerException {
        CustAccount custAccount_o = customerDao.findCustAccountByCustId(custAccount.getCustid());
        if (custAccount_o == null) {
            throw new ServerException("客户信息不存在：" + custAccount.getCustid());
        }
        return custAccount.getPassword().equals(custAccount_o.getPassword());
    }

}

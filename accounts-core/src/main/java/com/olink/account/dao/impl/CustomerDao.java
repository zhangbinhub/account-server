package com.olink.account.dao.impl;

import com.olink.account.base.impl.BaseTradeDao;
import com.olink.account.dao.ICustomerDao;
import com.olink.account.enumration.*;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.CustAccount;
import com.olink.account.model.trade.customer.CustBindInfo;
import com.olink.account.model.trade.customer.CustChangeLog;
import com.olink.account.model.trade.customer.CustSubAccount;
import com.olink.account.model.trade.dictionary.Dictionary;
import com.olink.account.utils.CodeFactory;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.common.DBConTools;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by zhangbin on 2016/12/2.
 * C户持久化
 */
public class CustomerDao extends BaseTradeDao implements ICustomerDao {

    private final Logger log = Logger.getLogger(this.getClass());

    public CustomerDao() {
        super();
    }

    public CustomerDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public List<CustAccount> findCustAccount() throws ServerException {
        List<CustAccount> custAccountList = new ArrayList<>();
        List<DBTable> dbTables = dbTools.getDataObjListBySql(null, CustAccount.class, null, null);
        for (DBTable dbTable : dbTables) {
            CustAccount custAccount = (CustAccount) dbTable;
            custAccount.setSubAccount(findCustSubAccountByCustId(custAccount.getCustid()));
            custAccountList.add(custAccount);
        }
        return custAccountList;
    }

    @Override
    public CustAccount findCustAccountById(String id) throws ServerException {
        CustAccount custAccount = (CustAccount) CustAccount.getInstance(id, CustAccount.class, null, dbTools.getDbcon());
        if (custAccount != null) {
            custAccount.setSubAccount(findCustSubAccountByCustId(custAccount.getCustid()));
        }
        return custAccount;
    }

    @Override
    public CustAccount findCustAccountByCustId(String custid) throws ServerException {
        Map<String, Object> param = new HashMap<>();
        param.put(CustAccount.class.getCanonicalName() + ".custid", custid);
        param.put(CustAccount.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
        CustAccount custAccount = (CustAccount) CustAccount.getInstance(param, CustAccount.class, null, dbTools.getDbcon());
        if (custAccount != null) {
            custAccount.setSubAccount(findCustSubAccountByCustId(custAccount.getCustid()));
        }
        return custAccount;
    }

    @Override
    public CustAccount findCustAccountByTelephone(String telephone) throws ServerException {
        Map<String, Object> param = new HashMap<>();
        param.put(CustAccount.class.getCanonicalName() + ".telephone", telephone);
        param.put(CustAccount.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
        CustAccount custAccount = (CustAccount) CustAccount.getInstance(param, CustAccount.class, null, dbTools.getDbcon());
        if (custAccount != null) {
            custAccount.setSubAccount(findCustSubAccountByCustId(custAccount.getCustid()));
        }
        return custAccount;
    }

    @Override
    public List<DBTable> findCustBindInfoByCustId(String custid) {
        Map<String, Object> param = new HashMap<>();
        param.put(CustBindInfo.class.getCanonicalName() + ".custid", custid);
        param.put(CustBindInfo.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
        return dbTools.getDataObjListBySql(param, CustBindInfo.class, null, " order by ${" + CustBindInfo.class.getCanonicalName() + ".sort} asc");
    }

    @Override
    public CustBindInfo findCustBindInfoById(String id) {
        return (CustBindInfo) CustBindInfo.getInstance(id, CustBindInfo.class, null, dbTools.getDbcon());
    }

    @Override
    public List<DBTable> findCustSubAccountByCustId(String custid) throws ServerException {
        Map<String, Object> param = new HashMap<>();
        param.put(CustSubAccount.class.getCanonicalName() + ".custid", custid);
        param.put(CustSubAccount.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
        List<DBTable> subAccounts = dbTools.getDataObjListBySql(param, CustSubAccount.class, null, null);
        for (DBTable dbTable : subAccounts) {
            CustSubAccount subAccount = (CustSubAccount) dbTable;
            AccountTypeEnum accountType;
            try {
                accountType = AccountTypeEnum.getEnum(subAccount.getType());
            } catch (EnumValueUndefinedException e) {
                throw new ServerException("子账户类型不匹配");
            }
            switch (accountType) {
                case Integral:
                    double ratio = getIntegralRatio();
                    subAccount.setRatio(ratio);
                    subAccount.setMoney(BigDecimal.valueOf(ratio * subAccount.getBalance()).setScale(2, DecimalProcessModeEnum.Down.getMode()).doubleValue());
                    break;
                default:
                    subAccount.setRatio(1);
            }
        }
        return subAccounts;
    }

    @Override
    public List<DBTable> findCustChangeLog(String custid, String type, String yearmonth) throws ServerException {
        Map<String, Object> param = new HashMap<>();
        param.put(CustChangeLog.class.getCanonicalName() + ".custid", custid);
        param.put(CustChangeLog.class.getCanonicalName() + ".type", type);
        return dbTools.getDataObjListBySql(param, CustChangeLog.class, null, " and ${" + CustChangeLog.class.getCanonicalName() + ".changedate} like '" + yearmonth + "%'");
    }

    @Override
    public CustAccount doCreateCustAccount(CustAccount custAccount_p) throws ServerException {
        //进行用户重复校验
        CustAccount custAccount_o = findCustAccountByTelephone(custAccount_p.getTelephone());
        if (custAccount_o == null) {//C户不存在需要创建
            String channel = custAccount_p.getChannel();
            String regioncode = channel.substring(0, 6);
            String custid = CodeFactory.generateCustId(regioncode, dbTools);
            if (CommonTools.isNullStr(custid)) {
                return null;
            }
            custAccount_p.setCustid(custid);
            custAccount_p.setCreatedate(CommonTools.getNowTimeString());
            custAccount_p.setUserid(CommonTools.getUuid());
            custAccount_p.setStatus(StatusEnum.activate.getValue());
            custAccount_p.addUpdateExcludes(new String[]{"password"});
            if (custAccount_p.doCreate(dbTools.getDbcon())) {
                log.info("C户创建成功！");
                return custAccount_p;
            } else {
                return null;
            }
        } else {
            return custAccount_o;
        }
    }

    @Override
    public CustSubAccount doCreateCustSubAccount(String custid, Dictionary accountType) {
        String code = CodeFactory.generateCustSubCode(custid, accountType);
        CustSubAccount subAccount = new CustSubAccount(CommonTools.getUuid(), custid, code, StatusEnum.activate.getValue(), accountType.getType(), CommonTools.getNowTimeString(), 0.00D, 0.00D);
        if (subAccount.doCreate(dbTools.getDbcon())) {
            return subAccount;
        } else {
            log.info("创建子账户：" + accountType.getName() + "(" + accountType.getType() + ") >> 失败!");
            return null;
        }
    }

    @Override
    public boolean doModifyTelephone(CustAccount custAccount) throws ServerException {
        Map<String, Object> param = new HashMap<>();
        param.put(CustAccount.class.getCanonicalName() + ".telephone", custAccount.getTelephone());
        CustAccount custAccount_o = (CustAccount) CustAccount.getInstance(param, CustAccount.class, null, dbTools.getDbcon());
        if (custAccount_o == null) {
            custAccount.addUpdateIncludes(new String[]{"telephone"});
            return custAccount.doUpdate(dbTools.getDbcon());
        } else {
            throw new ServerException("手机号已被注册！");
        }
    }

    @Override
    public boolean doAddCustBindInfo(CustBindInfo custBindInfo) {
        custBindInfo.setCreatedate(CommonTools.getNowTimeString());
        custBindInfo.setStatus(StatusEnum.activate.getValue());
        return custBindInfo.doCreate(dbTools.getDbcon());
    }

    @Override
    public boolean doDelCustBinfInfo(String id) {
        CustBindInfo custBindInfo = (CustBindInfo) CustBindInfo.getInstance(id, CustBindInfo.class, null, dbTools.getDbcon());
        if (custBindInfo != null) {
            custBindInfo.setIsdel(DeleteEnum.isDelete.getValue());
            custBindInfo.setIsdefault(DefaultEnum.notdefault.getValue());
            custBindInfo.addUpdateIncludes(new String[]{"isdel", "isdefault"});
            if (!custBindInfo.doUpdate(dbTools.getDbcon())) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean setDefaultCustBindInfo(CustBindInfo custBindInfo_p) {
        Map<String, Object> param = new HashMap<>();
        param.put(CustBindInfo.class.getCanonicalName() + ".custid", custBindInfo_p.getCustid());
        param.put(CustBindInfo.class.getCanonicalName() + ".type", custBindInfo_p.getType());
        param.put(CustBindInfo.class.getCanonicalName() + ".isdefault", 1);
        List<DBTable> bindlist = dbTools.getDataObjListBySql(param, CustBindInfo.class, null, "for update");
        if (bindlist.size() > 0) {
            log.info("已经存在默认绑定信息！正在解绑......");
            for (DBTable dbTable : bindlist) {
                CustBindInfo bindInfo = (CustBindInfo) dbTable;
                bindInfo.setIsdefault(DefaultEnum.notdefault.getValue());
                bindInfo.addUpdateIncludes(new String[]{"isdefault"});
                if (!bindInfo.doUpdate(dbTools.getDbcon())) {
                    log.error("解除默认失败！");
                    return false;
                }
            }
        }
        CustBindInfo custBindInfo = (CustBindInfo) CustBindInfo.getInstance(custBindInfo_p.getId(), CustBindInfo.class, null, dbTools.getDbcon());
        if (custBindInfo == null) {
            return false;
        }
        custBindInfo.setIsdefault(DefaultEnum.isdefault.getValue());
        custBindInfo.addUpdateIncludes(new String[]{"isdefault"});
        if (!custBindInfo.doUpdate(dbTools.getDbcon())) {
            log.error("设置默认失败！");
            return false;
        }
        return true;
    }

    @Override
    public boolean doSetCustPayPassword(CustAccount custAccount) {
        custAccount.addUpdateIncludes(new String[]{"password"});
        return custAccount.doUpdate(dbTools.getDbcon());
    }

    @Override
    public boolean doValidatePayPassword(CustAccount custAccount) {
        CustAccount custAccount_o = (CustAccount) CustAccount.getInstanceByLock(custAccount.getCustid(), CustAccount.class, null, dbTools.getDbcon());
        return custAccount.getPassword().equals(custAccount_o.getPassword());
    }

}

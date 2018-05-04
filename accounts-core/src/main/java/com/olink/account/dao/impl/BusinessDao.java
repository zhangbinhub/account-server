package com.olink.account.dao.impl;

import com.olink.account.base.impl.BaseTradeDao;
import com.olink.account.dao.IBusinessDao;
import com.olink.account.enumration.DefaultEnum;
import com.olink.account.enumration.StatusEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.customer.BusinessBindInfo;
import com.olink.account.model.trade.customer.BusinessInnerAccount;
import com.olink.account.model.trade.customer.BusinessSubAccount;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.common.DBConTools;
import pers.acp.tools.dbconnection.entity.DBTable;
import org.apache.log4j.Logger;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/9/7.
 * B户持久化
 */
public class BusinessDao extends BaseTradeDao implements IBusinessDao {

    private final Logger log = Logger.getLogger(this.getClass());

    public BusinessDao() {
        super();
    }

    public BusinessDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public List<BusinessAccount> findBusinessAccount() {
        List<BusinessAccount> businessAccountList = new ArrayList<>();
        List<DBTable> dbTables = dbTools.getDataObjListBySql(null, BusinessAccount.class, null, null);
        for (DBTable dbTable : dbTables) {
            BusinessAccount businessAccount = (BusinessAccount) dbTable;
            businessAccount.setSubAccounts(findBusinessSubAccountByBusinessid(businessAccount.getBusinessid()));
            businessAccount.setBindInfos(findBusinessBindInfoByBusinessid(businessAccount.getBusinessid()));
            businessAccountList.add(businessAccount);
        }
        return businessAccountList;
    }

    @Override
    public BusinessAccount findBusinessAccountById(String id) {
        BusinessAccount businessAccount = (BusinessAccount) BusinessAccount.getInstance(id, BusinessAccount.class, null, dbTools.getDbcon());
        if (businessAccount != null) {
            businessAccount.setSubAccounts(findBusinessSubAccountByBusinessid(businessAccount.getBusinessid()));
            businessAccount.setBindInfos(findBusinessBindInfoByBusinessid(businessAccount.getBusinessid()));
        }
        return businessAccount;
    }

    @Override
    public BusinessAccount findBusinessAccountByBusinessId(String businessId) {
        Map<String, Object> param = new HashMap<>();
        param.put(BusinessAccount.class.getCanonicalName() + ".businessid", businessId);
        BusinessAccount businessAccount = (BusinessAccount) BusinessAccount.getInstance(param, BusinessAccount.class, null, dbTools.getDbcon());
        if (businessAccount != null) {
            businessAccount.setSubAccounts(findBusinessSubAccountByBusinessid(businessAccount.getBusinessid()));
            businessAccount.setBindInfos(findBusinessBindInfoByBusinessid(businessAccount.getBusinessid()));
        }
        return businessAccount;
    }

    @Override
    public BusinessAccount findYYBusinessAccount() {
        Map<String, Object> param = new HashMap<>();
        param.put(BusinessAccount.class.getCanonicalName() + ".isdefault", DefaultEnum.isdefault.getValue());
        param.put(BusinessAccount.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
        List<DBTable> businessAccounts = dbTools.getDataObjListBySql(param, BusinessAccount.class, null, null);
        if (businessAccounts.size() > 0) {
            BusinessAccount businessAccount = (BusinessAccount) businessAccounts.get(0);
            businessAccount.setSubAccounts(findBusinessSubAccountByBusinessid(businessAccount.getBusinessid()));
            businessAccount.setBindInfos(findBusinessBindInfoByBusinessid(businessAccount.getBusinessid()));
            return businessAccount;
        } else {
            log.error("找不到运营B户");
            return null;
        }
    }

    @Override
    public List<DBTable> findBusinessSubAccountByBusinessid(String businessid) {
        Map<String, Object> param = new HashMap<>();
        param.put(BusinessSubAccount.class.getCanonicalName() + ".businessid", businessid);
        return dbTools.getDataObjListBySql(param, BusinessSubAccount.class, null, null);
    }

    @Override
    public BusinessBindInfo findBusinessBindInfoById(String id) {
        return (BusinessBindInfo) BusinessBindInfo.getInstance(id, BusinessBindInfo.class, null, dbTools.getDbcon());
    }

    @Override
    public List<DBTable> findBusinessBindInfoByBusinessid(String businessid) {
        Map<String, Object> param = new HashMap<>();
        param.put(BusinessBindInfo.class.getCanonicalName() + ".businessid", businessid);
        return dbTools.getDataObjListBySql(param, BusinessBindInfo.class, null, "order by ${" + BusinessBindInfo.class.getCanonicalName() + ".type},${" + BusinessBindInfo.class.getCanonicalName() + ".isdefault} desc");
    }

    @Override
    public List<DBTable> findBusinessInnerAccountByBusinessid(String businessid) {
        Map<String, Object> param = new HashMap<>();
        param.put(BusinessInnerAccount.class.getCanonicalName() + ".businessid", businessid);
        return dbTools.getDataObjListBySql(param, BusinessInnerAccount.class, null, "order by ${" + BusinessInnerAccount.class.getCanonicalName() + ".innerid}");
    }

    @Override
    public BusinessInnerAccount findBusinessInnerAccountByInnerid(String innerid) {
        Map<String, Object> param = new HashMap<>();
        param.put(BusinessInnerAccount.class.getCanonicalName() + ".innerid", innerid);
        return (BusinessInnerAccount) BusinessInnerAccount.getInstance(param, BusinessInnerAccount.class, null, dbTools.getDbcon());
    }

    @Override
    public boolean doCreateInnerAccount(BusinessInnerAccount businessInnerAccount) throws EnumValueUndefinedException, ServerException {
        businessInnerAccount.setCreatedate(CommonTools.getNowTimeString());
        return businessInnerAccount.doCreate(dbTools.getDbcon());
    }

    @Override
    public boolean doUpdateInnerAccount(BusinessInnerAccount businessInnerAccount) {
        businessInnerAccount.addUpdateIncludes(new String[]{"balance", "accountdate", "updatedate"});
        businessInnerAccount.setUpdatedate(CommonTools.getNowTimeString());
        return businessInnerAccount.doUpdate(dbTools.getDbcon());
    }

}

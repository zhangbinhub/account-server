package com.olink.account.service.impl;

import com.olink.account.base.impl.BaseTradeService;
import com.olink.account.dao.IBusinessDao;
import com.olink.account.dao.IDictionaryDao;
import com.olink.account.dao.impl.BusinessDao;
import com.olink.account.dao.impl.DictionaryDao;
import com.olink.account.enumration.*;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.customer.BusinessBindInfo;
import com.olink.account.service.IBusinessService;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/9/7.
 * B户服务
 */
public class BusinessService extends BaseTradeService implements IBusinessService {

    private IBusinessDao businessDao = new BusinessDao();

    @Override
    public String getBusinessKeyById(String id) {
        BusinessAccount businessAccount = businessDao.findBusinessAccountById(id);
        if (businessAccount != null) {
            if (businessAccount.getStatus() == StatusEnum.activate.getValue()) {
                return businessAccount.getBusinesskey();
            } else {
                return "";
            }
        } else {
            return "";
        }
    }

    @Override
    public List<BusinessAccount> findBusinessAccount() {
        return businessDao.findBusinessAccount();
    }

    @Override
    public BusinessAccount findBusinessAccountById(String id) {
        return businessDao.findBusinessAccountById(id);
    }

    @Override
    public BusinessAccount findBusinessAccountByBusinessId(String businessId) {
        return businessDao.findBusinessAccountByBusinessId(businessId);
    }

    @Override
    public BusinessBindInfo findBusinessDefaultBindInfoByBusinessId(String businessid) {
        BusinessAccount businessAccount = businessDao.findBusinessAccountByBusinessId(businessid);
        List<DBTable> businessBindInfos = businessAccount.getBindInfos();
        for (DBTable dbTable : businessBindInfos) {
            BusinessBindInfo businessBindInfo = (BusinessBindInfo) dbTable;
            if (businessBindInfo.getType().equals(businessAccount.getSettlementtype())) {
                if (businessBindInfo.getStatus() == StatusEnum.activate.getValue()) {
                    return businessBindInfo;
                }
            }
        }
        return null;
    }

    @Override
    public BusinessAccount findYYBusinessAccount() {
        return businessDao.findYYBusinessAccount();
    }

    @Override
    public List<DBTable> findYYBusinessBindInfoType() {
        BusinessAccount businessAccount = businessDao.findYYBusinessAccount();
        if (businessAccount != null) {
            List<DBTable> businessBindInfos = businessAccount.getBindInfos();
            StringBuilder codesB = new StringBuilder();
            businessBindInfos.stream().filter(businessBindInfo -> ((BusinessBindInfo) businessBindInfo).getStatus() == StatusEnum.activate.getValue()).forEach(businessBindInfo -> codesB.append("'").append(((BusinessBindInfo) businessBindInfo).getType()).append("',"));
            String codes = codesB.substring(0, codesB.length() - 1);
            IDictionaryDao dictionaryDao = new DictionaryDao();
            Map<String, Object> param = new HashMap<>();
            param.put(DictionaryTableEnum.businessBindType.getDictionaryClass().getCanonicalName() + ".status", StatusEnum.activate.getValue());
            return dictionaryDao.findDictionaryList(DictionaryTableEnum.businessBindType, param, " and ${" + DictionaryTableEnum.businessBindType.getDictionaryClass().getCanonicalName() + ".code} in (" + codes + ")");
        } else {
            return null;
        }
    }

    @Override
    public BusinessBindInfo findYYBusinessBindInfo(String typecode) {
        BusinessAccount businessAccount = businessDao.findYYBusinessAccount();
        if (businessAccount != null) {
            List<DBTable> businessBindInfos = businessAccount.getBindInfos();
            for (DBTable dbTable : businessBindInfos) {
                BusinessBindInfo businessBindInfo = (BusinessBindInfo) dbTable;
                if (businessBindInfo.getStatus() == StatusEnum.activate.getValue() && businessBindInfo.getType().equals(typecode)) {
                    return businessBindInfo;
                }
            }
            return null;
        } else {
            return null;
        }
    }

}

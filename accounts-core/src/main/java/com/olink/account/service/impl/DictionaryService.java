package com.olink.account.service.impl;

import com.olink.account.base.impl.BaseTradeService;
import com.olink.account.dao.IDictionaryDao;
import com.olink.account.dao.impl.DictionaryDao;
import com.olink.account.enumration.DictionaryTableEnum;
import com.olink.account.enumration.StatusEnum;
import com.olink.account.service.IDictionaryService;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/9/8.
 * 字典服务
 */
public class DictionaryService extends BaseTradeService implements IDictionaryService {

    private IDictionaryDao dao = new DictionaryDao();

    @Override
    public List<DBTable> findDictionaryList(DictionaryTableEnum dictionaryTableEnum, Map<String, Object> whereValues, String attachStr) {
        return dao.findDictionaryList(dictionaryTableEnum, whereValues, attachStr);
    }

    @Override
    public Object[] findDictionaryList(DictionaryTableEnum dictionaryTableEnum, Map<String, Object> whereValues, String attachStr, int currentPage, int pageSize) {
        return dao.findDictionaryList(dictionaryTableEnum, whereValues, attachStr, currentPage, pageSize);
    }

    @Override
    public List<DBTable> findDictionaryAvailableList(DictionaryTableEnum dictionaryTableEnum, Map<String, Object> whereValues, String attachStr) {
        if (whereValues == null) {
            whereValues = new HashMap<>();
        }
        whereValues.put(dictionaryTableEnum.getDictionaryClass().getCanonicalName() + ".status", StatusEnum.activate.getValue());
        return dao.findDictionaryList(dictionaryTableEnum, whereValues, attachStr);
    }

    @Override
    public Object[] findDictionaryAvailableList(DictionaryTableEnum dictionaryTableEnum, Map<String, Object> whereValues, String attachStr, int currentPage, int pageSize) {
        if (whereValues == null) {
            whereValues = new HashMap<>();
        }
        whereValues.put(dictionaryTableEnum.getDictionaryClass().getCanonicalName() + ".status", StatusEnum.activate.getValue());
        return dao.findDictionaryList(dictionaryTableEnum, whereValues, attachStr, currentPage, pageSize);
    }

    @Override
    public boolean isOuterTradeEnabled() {
        return dao.isOuterTradeEnabled();
    }

    @Override
    public boolean validateAccountDate() {
        String accountdate = dao.getAccountDate();
        return CommonTools.getNowString().equals(accountdate);
    }

}

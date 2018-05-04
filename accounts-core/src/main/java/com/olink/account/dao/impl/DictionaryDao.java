package com.olink.account.dao.impl;

import com.olink.account.base.impl.BaseTradeDao;
import com.olink.account.dao.IDictionaryDao;
import com.olink.account.enumration.DictionaryTableEnum;
import com.olink.account.model.trade.dictionary.Dictionary;
import pers.acp.tools.common.DBConTools;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.List;
import java.util.Map;

/**
 * Created by Shepherd on 2016-08-08.
 * 字典持久化
 */
public class DictionaryDao extends BaseTradeDao implements IDictionaryDao {

    public DictionaryDao() {
        super();
    }

    public DictionaryDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public List<DBTable> findDictionaryList(DictionaryTableEnum dictionaryTableEnum, Map<String, Object> whereValues, String attachStr) {
        Class<? extends Dictionary> cls = dictionaryTableEnum.getDictionaryClass();
        return dbTools.getDataObjListBySql(whereValues, cls, null, attachStr);
    }

    @Override
    public Object[] findDictionaryList(DictionaryTableEnum dictionaryTableEnum, Map<String, Object> whereValues, String attachStr, int currentPage, int pageSize) {
        Class<? extends Dictionary> cls = dictionaryTableEnum.getDictionaryClass();
        return dbTools.getDataObjListBySql(currentPage, pageSize, whereValues, cls, null, attachStr);
    }

}

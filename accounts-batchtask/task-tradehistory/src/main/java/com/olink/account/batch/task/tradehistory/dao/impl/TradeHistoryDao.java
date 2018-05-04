package com.olink.account.batch.task.tradehistory.dao.impl;

import com.olink.account.base.impl.BaseBatchDao;
import com.olink.account.batch.task.tradehistory.dao.ITradeHistoryDao;
import com.olink.account.utils.DBAccess;
import pers.acp.tools.common.DBConTools;

/**
 * Created by zhangbin on 2017/5/10.
 * 历史交易数据持久化
 */
public class TradeHistoryDao extends BaseBatchDao implements ITradeHistoryDao {

    public TradeHistoryDao() {
        super();
    }

    public TradeHistoryDao(DBConTools dbTools) {
        super(dbTools);
    }

}

package com.olink.account.batch.task.managehistory.dao.impl;

import com.olink.account.base.impl.BaseBatchDao;
import com.olink.account.batch.task.managehistory.dao.IManageHistoryDao;
import pers.acp.tools.common.DBConTools;

/**
 * Created by zhangbin on 2017/5/11.
 * 历史系统数据持久化
 */
public class ManageHistoryDao extends BaseBatchDao implements IManageHistoryDao {

    public ManageHistoryDao() {
        super();
    }

    public ManageHistoryDao(DBConTools dbTools) {
        super(dbTools);
    }

}

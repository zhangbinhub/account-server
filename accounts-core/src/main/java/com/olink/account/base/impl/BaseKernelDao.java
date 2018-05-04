package com.olink.account.base.impl;

import com.olink.account.base.IBaseKernelDao;
import com.olink.account.utils.DBAccess;
import pers.acp.tools.common.DBConTools;
import org.apache.log4j.Logger;

/**
 * Created by zhangbin on 2016/12/30.
 * 核心库持久化
 */
public abstract class BaseKernelDao extends BaseDao implements IBaseKernelDao {

    private final Logger log = Logger.getLogger(this.getClass());

    public BaseKernelDao() {
        super(DBAccess.getSysInstance().getDBTool());
    }

    public BaseKernelDao(DBConTools dbTools) {
        super(dbTools);
    }

}

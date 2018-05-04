package com.olink.account.batch.dao;

import com.olink.account.base.IBaseTradeDao;

/**
 * Created by zhangbin on 2016/12/30.
 * 日期持久化接口
 */
public interface IDateDao extends IBaseTradeDao {

    /**
     * 切换系统会计日期
     *
     * @return true|false
     */
    boolean doChangeAccountDate();

}

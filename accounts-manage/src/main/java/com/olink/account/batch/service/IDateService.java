package com.olink.account.batch.service;

import com.olink.account.base.IBaseBatchService;
import pers.acp.tools.exceptions.TimerException;

/**
 * Created by zhangbin on 2016/12/30.
 * 日期服务接口
 */
public interface IDateService extends IBaseBatchService {

    /**
     * 切换系统会计日期
     *
     * @return true|false
     */
    boolean doChangeAccountDate();

    /**
     * 获取系统会计日期
     *
     * @return 会计日期
     */
    String getAccountDate();

    /**
     * 获取上一个会计日期
     *
     * @return 上一个会计日期
     */
    String getPrevAccountDate() throws TimerException;

    /**
     * 获取上一个会计日期
     *
     * @return 上一个会计日期
     */
    String getPrevAccountDate(String accountDate) throws TimerException;

}

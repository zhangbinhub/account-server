package com.olink.account.batch.service.impl;

import com.olink.account.base.impl.BaseTradeService;
import com.olink.account.batch.dao.IDateDao;
import com.olink.account.batch.dao.impl.DateDao;
import com.olink.account.batch.service.IDateService;
import pers.acp.tools.common.CalendarTools;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.exceptions.TimerException;

import java.util.Calendar;

/**
 * Created by zhangbin on 2016/12/30.
 * 日期服务
 */
public class DateService extends BaseTradeService implements IDateService {

    private IDateDao dateDao = new DateDao();

    @Override
    public boolean doChangeAccountDate() {
        dateDao.beginTranslist();
        if (dateDao.doChangeAccountDate()) {
            dateDao.commitTranslist();
            return true;
        } else {
            dateDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public String getAccountDate() {
        return dateDao.getAccountDate();
    }

    @Override
    public String getPrevAccountDate() throws TimerException {
        String accountDate = dateDao.getAccountDate();
        return getPrevAccountDate(accountDate);
    }

    @Override
    public String getPrevAccountDate(String accountDate) throws TimerException {
        Calendar calendar = CalendarTools.getCalendar(accountDate);
        calendar = CalendarTools.getPrevDay(calendar);
        return CommonTools.getDateTimeString(calendar.getTime(), "yyyy-MM-dd");
    }

    @Override
    public void dropTable() {

    }
}

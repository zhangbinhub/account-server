package com.olink.account.batch.dao.impl;

import com.olink.account.base.impl.BaseTradeDao;
import com.olink.account.batch.dao.IDateDao;
import com.olink.account.model.trade.dictionary.D_DateParam;
import pers.acp.tools.common.CalendarTools;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.common.DBConTools;
import org.apache.log4j.Logger;

import java.util.Calendar;

/**
 * Created by zhangbin on 2016/12/30.
 * 日期持久化
 */
public class DateDao extends BaseTradeDao implements IDateDao {

    /**
     * 日志对象
     */
    private final Logger log = Logger.getLogger(this.getClass());

    public DateDao() {
        super();
    }

    public DateDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public boolean doChangeAccountDate() {
        D_DateParam dateParam = D_DateParam.getAccountDateObj(dbTools.getDbcon());
        if (dateParam != null) {
            String accountDate = dateParam.getCode();
            try {
                Calendar calendar = CalendarTools.getCalendar(accountDate);
                calendar = CalendarTools.getNextDay(calendar);
                dateParam.setCode(CommonTools.getDateTimeString(calendar.getTime(), "yyyy-MM-dd"));
                dateParam.setModifydate(CommonTools.getNowTimeString());
                if (!dateParam.doUpdate(dbTools.getDbcon())) {
                    log.error("切换会计日期失败：数据库操作异常");
                    return false;
                }
                return true;
            } catch (Exception e) {
                log.error("切换会计日期失败：" + e.getMessage(), e);
                return false;
            }
        } else {
            log.error("获取会计日期对象失败：没有可用的会计日期参数配置！");
            return false;
        }
    }

}

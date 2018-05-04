package com.olink.account.batch.task.custstatistics.dao.impl;

import com.olink.account.base.impl.BaseBatchDao;
import com.olink.account.batch.task.custstatistics.dao.ICustomerStatisticsMonthDao;
import com.olink.account.dao.ICustomerDao;
import com.olink.account.dao.impl.CustomerDao;
import com.olink.account.enumration.ChangeBalanceDirectEnum;
import com.olink.account.model.batch.accounting.CustomerMonthStatistics;
import com.olink.account.model.trade.customer.CustAccount;
import com.olink.account.model.trade.customer.CustChangeLog;
import com.olink.account.model.trade.customer.CustSubAccount;
import org.apache.log4j.Logger;
import pers.acp.tools.common.CalendarTools;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.common.DBConTools;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2017/5/5.
 * 客户账月统计持久化
 */
public class CustomerStatisticsMonthDao extends BaseBatchDao implements ICustomerStatisticsMonthDao {

    private final Logger log = Logger.getLogger(this.getClass());

    public CustomerStatisticsMonthDao() {
        super();
    }

    public CustomerStatisticsMonthDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public boolean doMonthStatistics(String yearmonth) {
        try {
            CustomerMonthStatistics before = new CustomerMonthStatistics();
            String delete_sql = "delete from " + before.getCurrTableName() + " where yearmonth='" + yearmonth + "'";
            if (!dbTools.doUpdate(delete_sql)) {
                log.error("删除客户账月统计信息失败：" + delete_sql);
                return false;
            }
            ICustomerDao customerDao = new CustomerDao();
            List<CustAccount> custAccountList = customerDao.findCustAccount();
            for (CustAccount custAccount : custAccountList) {
                List<DBTable> custSubAccountList = custAccount.getSubAccount();
                for (DBTable dbTable : custSubAccountList) {
                    CustSubAccount custSubAccount = (CustSubAccount) dbTable;
                    //上月余额
                    double prevbalance = 0.00D;
                    before = findMonthStatisticsByYearMonth(custAccount.getCustid(), custSubAccount.getType(), yearmonth);
                    if (before != null) {
                        prevbalance = before.getBalance();
                    }
                    //支出额
                    double amontexpend = 0.00D;
                    //收入额
                    double amontrevenue = 0.00D;
                    List<DBTable> custChangeLogList = customerDao.findCustChangeLog(custAccount.getCustid(), custSubAccount.getType(), yearmonth);
                    for (DBTable dbTable1 : custChangeLogList) {
                        CustChangeLog custChangeLog = (CustChangeLog) dbTable1;
                        if (custChangeLog.getChangedirect().equals(ChangeBalanceDirectEnum.Plus.getValue())) {
                            amontrevenue += custChangeLog.getAmont();
                        } else if (custChangeLog.getChangedirect().equals(ChangeBalanceDirectEnum.Minus.getValue())) {
                            amontexpend += custChangeLog.getAmont();
                        }
                    }
                    CustomerMonthStatistics customerMonthStatistics = new CustomerMonthStatistics();
                    customerMonthStatistics.setCustid(custAccount.getCustid());
                    customerMonthStatistics.setCustsubaccountcode(custSubAccount.getCode());
                    customerMonthStatistics.setType(custSubAccount.getType());
                    customerMonthStatistics.setPrevbalance(prevbalance);
                    customerMonthStatistics.setAmontexpend(amontexpend);
                    customerMonthStatistics.setAmontrevenue(amontrevenue);
                    customerMonthStatistics.setBalance(amontrevenue - amontexpend + prevbalance);
                    customerMonthStatistics.setYearmonth(yearmonth);
                    customerMonthStatistics.setCreatedate(CommonTools.getNowTimeString());
                    if (!customerMonthStatistics.doCreate(dbTools.getDbcon())) {
                        return false;
                    }
                }
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    @Override
    public CustomerMonthStatistics findMonthStatisticsByYearMonth(String custid, String type, String yearmonth) throws Exception {
        Calendar c = CalendarTools.getCalendar(yearmonth + "-01");
        c.add(Calendar.MONTH, -1);
        String beforemonth = CommonTools.getDateTimeString(c.getTime(), "yyyy-MM");
        Map<String, Object> params = new HashMap<>();
        params.put(CustomerMonthStatistics.class.getCanonicalName() + ".custid", custid);
        params.put(CustomerMonthStatistics.class.getCanonicalName() + ".type", type);
        params.put(CustomerMonthStatistics.class.getCanonicalName() + ".yearmonth", beforemonth);
        return (CustomerMonthStatistics) CustomerMonthStatistics.getInstance(params, CustomerMonthStatistics.class, null, dbTools.getDbcon());
    }

    @Override
    public boolean doHistory(int month) {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MONTH, -month);
        String yearmonth = CommonTools.getDateTimeString(c.getTime(), "yyyy-MM");
        List<DBTable> customerMonthStatisticsList = dbTools.getDataObjListBySql(null, CustomerMonthStatistics.class, null, " and ${" + CustomerMonthStatistics.class.getCanonicalName() + ".yearmonth} < '" + yearmonth + "' order by ${" + CustomerMonthStatistics.class.getCanonicalName() + ".yearmonth} asc");
        for (DBTable dbTable : customerMonthStatisticsList) {
            CustomerMonthStatistics customerMonthStatistics = (CustomerMonthStatistics) dbTable;
            String tablename = customerMonthStatistics.getCurrTableName();
            String year = customerMonthStatistics.getYearmonth().substring(0, 4);
            String histablename = customerMonthStatistics.getCurrTableName() + "_" + year;
            if (!doCreateHisTable(tablename, histablename)) {
                log.error("创建客户账月统计历史表失败");
                return false;
            }
            doCreateHisTableLog(getDBTools().getDbName(), histablename, getDBTools().getDbNo() + "", getDBTools().getDbName(), tablename, getDBTools().getDbNo() + "");
        }
        for (DBTable dbTable : customerMonthStatisticsList) {
            CustomerMonthStatistics customerMonthStatistics = (CustomerMonthStatistics) dbTable;
            if (!customerMonthStatistics.doDelete(dbTools.getDbcon())) {
                return false;
            }
            String year = customerMonthStatistics.getYearmonth().substring(0, 4);
            customerMonthStatistics.setSuffix("_" + year);
            if (!customerMonthStatistics.doCreate(dbTools.getDbcon())) {
                return false;
            }
        }
        return true;
    }

}

package com.olink.account.batch.task.custstatistics.dao.impl;

import com.olink.account.base.impl.BaseBatchDao;
import com.olink.account.batch.task.custstatistics.dao.ICustomerStatisticsYearDao;
import com.olink.account.dao.ICustomerDao;
import com.olink.account.dao.impl.CustomerDao;
import com.olink.account.enumration.ChangeBalanceDirectEnum;
import com.olink.account.model.batch.accounting.CustomerMonthStatistics;
import com.olink.account.model.batch.accounting.CustomerYearStatistics;
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
public class CustomerStatisticsYearDao extends BaseBatchDao implements ICustomerStatisticsYearDao {

    private final Logger log = Logger.getLogger(this.getClass());

    public CustomerStatisticsYearDao() {
        super();
    }

    public CustomerStatisticsYearDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public boolean doYearStatistics(String year) {
        try {
            CustomerYearStatistics tmpyear = new CustomerYearStatistics();
            String tablename = tmpyear.getCurrTableName();
            String histablename = tmpyear.getCurrTableName() + "_" + year;
            String create_sql = "CREATE TABLE if Not exists " + histablename + " like " + tablename;
            if (!dbTools.doUpdate(create_sql)) {
                log.error("创建客户账年统计表失败：" + create_sql);
                return false;
            }
            String delete_sql = "delete from " + histablename;
            if (!dbTools.doUpdate(delete_sql)) {
                log.error("删除客户账年统计信息失败：" + delete_sql);
                return false;
            }
            List<DBTable> monthList = dbTools.getDataObjListBySql(null, CustomerMonthStatistics.class, null, " and ${" + CustomerMonthStatistics.class.getCanonicalName() + ".yearmonth} like '" + year + "%'");
            CustomerMonthStatistics tmpmonth = new CustomerMonthStatistics();
            tmpmonth.setSuffix("_" + year);
            monthList.addAll(dbTools.getDataObjListBySql(null, CustomerMonthStatistics.class, tmpmonth, null));
            ICustomerDao customerDao = new CustomerDao();
            List<CustAccount> custAccountList = customerDao.findCustAccount();
            for (CustAccount custAccount : custAccountList) {
                List<DBTable> custSubAccountList = custAccount.getSubAccount();
                for (DBTable dbTable : custSubAccountList) {
                    CustSubAccount custSubAccount = (CustSubAccount) dbTable;
                    //上月余额
                    double prevbalance = 0.00D;
                    CustomerYearStatistics before = findYearStatisticsByYear(custSubAccount.getCustid(), custSubAccount.getType(), year);
                    if (before != null) {
                        prevbalance = before.getBalance();
                    }
                    //支出额
                    double amontexpend = 0.00D;
                    //收入额
                    double amontrevenue = 0.00D;
                    for (DBTable dbTable1 : monthList) {
                        CustomerMonthStatistics customerMonthStatistics = (CustomerMonthStatistics) dbTable1;
                        if (customerMonthStatistics.getCustid().equals(custSubAccount.getCustid()) && customerMonthStatistics.getCustsubaccountcode().equals(custSubAccount.getCode())) {
                            amontexpend += customerMonthStatistics.getAmontexpend();
                            amontrevenue += customerMonthStatistics.getAmontrevenue();
                        }
                    }
                    CustomerYearStatistics customerYearStatistics = new CustomerYearStatistics();
                    customerYearStatistics.setSuffix("_" + year);
                    customerYearStatistics.setCustid(custSubAccount.getCustid());
                    customerYearStatistics.setCustsubaccountcode(custSubAccount.getCode());
                    customerYearStatistics.setType(custSubAccount.getType());
                    customerYearStatistics.setPrevbalance(prevbalance);
                    customerYearStatistics.setAmontexpend(amontexpend);
                    customerYearStatistics.setAmontrevenue(amontrevenue);
                    customerYearStatistics.setBalance(amontrevenue - amontexpend + prevbalance);
                    customerYearStatistics.setYear(year);
                    customerYearStatistics.setCreatedate(CommonTools.getNowTimeString());
                    if (!customerYearStatistics.doCreate(dbTools.getDbcon())) {
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
    public CustomerYearStatistics findYearStatisticsByYear(String custid, String type, String year) throws Exception {
        Calendar c = CalendarTools.getCalendar(year + "-01-01");
        c.add(Calendar.YEAR, -1);
        String beforeyear = CommonTools.getDateTimeString(c.getTime(), "yyyy");
        Map<String, Object> params = new HashMap<>();
        params.put(CustomerYearStatistics.class.getCanonicalName() + ".custid", custid);
        params.put(CustomerYearStatistics.class.getCanonicalName() + ".type", type);
        CustomerYearStatistics customerYearStatistics = new CustomerYearStatistics();
        customerYearStatistics.setSuffix("_" + beforeyear);
        return (CustomerYearStatistics) CustomerYearStatistics.getInstance(params, CustomerYearStatistics.class, customerYearStatistics, dbTools.getDbcon());
    }

}

package com.olink.account.batch.task.innersummary.dao.impl;

import com.olink.account.base.impl.BaseBatchDao;
import com.olink.account.batch.exception.BatchException;
import com.olink.account.batch.task.innersummary.dao.IInnerSummaryDao;
import com.olink.account.enumration.AccountItemStyleEnum;
import com.olink.account.model.batch.accounting.InnerStatistics;
import com.olink.account.model.batch.accounting.InnerSummary;
import com.olink.account.model.trade.dictionary.D_AccountItem;
import pers.acp.tools.common.CalendarTools;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.exceptions.TimerException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2017/5/9.
 * 内部账科目汇总持久化
 */
public class InnerSummaryDao extends BaseBatchDao implements IInnerSummaryDao {

    private Map<String, D_AccountItem> accountItemMap = new HashMap<>();

    @Override
    public boolean doSummary(String accountDate, Map<String, D_AccountItem> accountItemMap) throws BatchException, TimerException, EnumValueUndefinedException {
        this.accountItemMap = accountItemMap;
        Map<String, InnerSummary> innerSummaryMap = new HashMap<>();
        Calendar calendar = CalendarTools.getCalendar(accountDate);
        calendar = CalendarTools.getPrevDay(calendar);
        String prevAccountDate = CommonTools.getDateTimeString(calendar.getTime(), "yyyy-MM-dd");
        List<DBTable> dbTableList = findInnerStatisticsByAccountDate(accountDate);
        for (DBTable dbTable : dbTableList) {
            InnerStatistics innerStatistics = (InnerStatistics) dbTable;
            String key1 = innerStatistics.getBusinessid() + innerStatistics.getAccountitemfirstcode();
            String key2 = innerStatistics.getBusinessid() + innerStatistics.getAccountitemfirstcode() + innerStatistics.getAccountitemsecdcode();
            String key3 = innerStatistics.getBusinessid() + innerStatistics.getAccountitemfirstcode() + innerStatistics.getAccountitemsecdcode() + innerStatistics.getAccountitemthirdcode();
            generateInnerSummary(1, key1, innerSummaryMap, innerStatistics, prevAccountDate);
            generateInnerSummary(2, key2, innerSummaryMap, innerStatistics, prevAccountDate);
            generateInnerSummary(3, key3, innerSummaryMap, innerStatistics, prevAccountDate);
        }
        return doCreateInnerSummary(innerSummaryMap);
    }

    @Override
    public boolean doDeleteInnerSummary(String accountDate) {
        InnerSummary innerSummary = new InnerSummary();
        return dbTools.doUpdate("delete from " + innerSummary.getCurrTableName() + " where accountdate='" + accountDate + "'");
    }

    /**
     * 生成内部账科目汇总信息
     *
     * @param level           科目级别
     * @param key             唯一键
     * @param innerSummaryMap map
     * @param innerStatistics 内部账统计信息
     * @param prevAccountDate 上个会计日期
     */
    private void generateInnerSummary(int level, String key, Map<String, InnerSummary> innerSummaryMap, InnerStatistics innerStatistics, String prevAccountDate) throws EnumValueUndefinedException, BatchException {
        InnerSummary innerSummary;
        if (!innerSummaryMap.containsKey(key)) {
            innerSummary = new InnerSummary();
            innerSummary.setBusinessid(innerStatistics.getBusinessid());
            InnerSummary prevInnerSummary;
            String accountcode;
            String parentcode;
            switch (level) {
                case 1:
                    accountcode = innerStatistics.getAccountitemfirstcode();
                    parentcode = "";
                    prevInnerSummary = findInnerSummaryByAccountDate(innerStatistics.getBusinessid(), innerStatistics.getAccountitemfirstcode(), "", prevAccountDate);
                    innerSummary.setAccountitemname(innerStatistics.getAccountitemfirstname());
                    innerSummary.setParentname("");
                    break;
                case 2:
                    accountcode = innerStatistics.getAccountitemsecdcode();
                    parentcode = innerStatistics.getAccountitemfirstcode();
                    prevInnerSummary = findInnerSummaryByAccountDate(innerStatistics.getBusinessid(), innerStatistics.getAccountitemsecdcode(), innerStatistics.getAccountitemfirstcode(), prevAccountDate);
                    innerSummary.setAccountitemname(innerStatistics.getAccountitemsecdname());
                    innerSummary.setParentname(innerStatistics.getAccountitemfirstname());
                    break;
                case 3:
                    accountcode = innerStatistics.getAccountitemthirdcode();
                    parentcode = innerStatistics.getAccountitemsecdcode();
                    prevInnerSummary = findInnerSummaryByAccountDate(innerStatistics.getBusinessid(), innerStatistics.getAccountitemthirdcode(), innerStatistics.getAccountitemsecdcode(), prevAccountDate);
                    innerSummary.setAccountitemname(innerStatistics.getAccountitemthirdname());
                    innerSummary.setParentname(innerStatistics.getAccountitemsecdname());
                    break;
                default:
                    throw new BatchException("生成内部科目汇总信息失败！");
            }
            innerSummary.setAccountitemcode(accountcode);
            innerSummary.setParentcode(parentcode);
            String field2 = "";
            if (accountItemMap.containsKey(parentcode + accountcode)) {
                field2 = accountItemMap.get(parentcode + accountcode).getField2();
            }
            innerSummary.setType(AccountItemStyleEnum.getEnum(Integer.valueOf(field2)));
            innerSummary.setLevel(level);
            innerSummary.setPrevbalance(prevInnerSummary.getBalance());
            innerSummary.setBalance(innerStatistics.getBalance());
            innerSummaryMap.put(key, innerSummary);
        } else {
            innerSummary = innerSummaryMap.get(key);
            innerSummary.setBalance(innerSummary.getBalance() + innerStatistics.getBalance());
        }
    }

    private List<DBTable> findInnerStatisticsByAccountDate(String accountDate) {
        Map<String, Object> param = new HashMap<>();
        param.put(InnerStatistics.class.getCanonicalName() + ".accountdate", accountDate);
        return dbTools.getDataObjListBySql(param, InnerStatistics.class, null, " order by ${" + InnerStatistics.class.getCanonicalName() + ".businessid},${" + InnerStatistics.class.getCanonicalName() + ".innerid}");
    }

    /**
     * 获取内部账科目汇总信息
     *
     * @param businessid  B客户号
     * @param code        科目编码
     * @param parentcode  上级科目编码
     * @param accountdate 会计日期
     * @return 内部账科目汇总信息
     */
    private InnerSummary findInnerSummaryByAccountDate(String businessid, String code, String parentcode, String accountdate) {
        Map<String, Object> param = new HashMap<>();
        param.put(InnerSummary.class.getCanonicalName() + ".businessid", businessid);
        param.put(InnerSummary.class.getCanonicalName() + ".accountdate", accountdate);
        param.put(InnerSummary.class.getCanonicalName() + ".accountitemcode", code);
        param.put(InnerSummary.class.getCanonicalName() + ".parentcode", parentcode);
        return (InnerSummary) InnerSummary.getInstance(param, InnerSummary.class, null, dbTools.getDbcon());
    }

    /**
     * 创建内部账科目统计信息
     *
     * @param innerSummaryMap 内部账科目统计信息
     * @return true|false
     */
    private boolean doCreateInnerSummary(Map<String, InnerSummary> innerSummaryMap) {
        for (Map.Entry<String, InnerSummary> entry : innerSummaryMap.entrySet()) {
            InnerSummary innerSummary = entry.getValue();
            innerSummary.setCreatedate(CommonTools.getNowTimeString());
            if (!innerSummary.doCreate(dbTools.getDbcon())) {
                return false;
            }
        }
        return true;
    }

}

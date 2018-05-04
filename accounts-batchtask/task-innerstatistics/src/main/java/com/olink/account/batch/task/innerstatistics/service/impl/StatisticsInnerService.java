package com.olink.account.batch.task.innerstatistics.service.impl;

import com.olink.account.base.impl.BaseBatchService;
import com.olink.account.batch.exception.BatchException;
import com.olink.account.batch.task.innerstatistics.dao.IStatisticsInnerDao;
import com.olink.account.batch.task.innerstatistics.dao.impl.StatisticsInnerDao;
import com.olink.account.batch.task.innerstatistics.service.IStatisticsInnerService;
import com.olink.account.dao.IBusinessDao;
import com.olink.account.dao.IOrderDao;
import com.olink.account.dao.impl.BusinessDao;
import com.olink.account.dao.impl.OrderDao;
import com.olink.account.enumration.AccountItemTypeEnum;
import com.olink.account.enumration.BalanceDirectEnum;
import com.olink.account.enumration.OrderStatusEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.batch.accounting.InnerStatistics;
import com.olink.account.model.trade.accounting.EntryItemFlow;
import com.olink.account.model.trade.customer.BusinessInnerAccount;
import com.olink.account.model.trade.order.OrderBase;
import pers.acp.tools.common.CalendarTools;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import pers.acp.tools.exceptions.TimerException;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2017/5/8.
 * 内部账服务
 */
public class StatisticsInnerService extends BaseBatchService implements IStatisticsInnerService {

    private IStatisticsInnerDao statisticsInnerDao = new StatisticsInnerDao();

    private IBusinessDao businessDao = new BusinessDao();

    private IOrderDao orderDao = new OrderDao(businessDao.getDBTools());

    private Map<String, OrderBase> adjustOrderMap;

    @Override
    public boolean doStatisticsInnerAccount(String accountDate) throws BatchException {
        adjustOrderMap = new HashMap<>();
        statisticsInnerDao.beginTranslist();
        businessDao.beginTranslist();
        try {
            List<EntryItemFlow> entryItemFlowList = orderDao.findEntryItemFlowByAccountDate(accountDate);
            for (EntryItemFlow entryItemFlow : entryItemFlowList) {
                if (needOrderAdjust(entryItemFlow.getOrderno())) {
                    OrderBase orderBase = orderDao.doChangeOrderSatus(entryItemFlow.getOrderno(), OrderStatusEnum.applyAdjust);
                    if (orderBase != null && orderDao.doChangeOrderSatus(orderBase, OrderStatusEnum.applyAdjustSuccess)) {
                        adjustOrderMap.put(entryItemFlow.getOrderno(), orderBase);
                    } else {
                        orderDao.doChangeOrderSatus(orderBase, OrderStatusEnum.adjustFail);
                        throw new ServerException("订单【" + entryItemFlow.getOrderno() + "】申请核算失败！");
                    }
                }
                doCreateOrUpdateInnerAccount(entryItemFlow);
            }
            doStatistics(accountDate, entryItemFlowList);
            for (Map.Entry<String, OrderBase> entry : adjustOrderMap.entrySet()) {
                OrderBase orderBase = entry.getValue();
                orderDao.doChangeOrderSatus(orderBase, OrderStatusEnum.adjustSuccess);
            }
            statisticsInnerDao.commitTranslist();
            businessDao.commitTranslist();
            return true;
        } catch (Exception e) {
            statisticsInnerDao.rollBackTranslist();
            businessDao.rollBackTranslist();
            throw new BatchException(e.getMessage());
        }
    }

    /**
     * 判断订单是否可以进行核算
     *
     * @param orderno 订单号
     * @return true|false
     */
    private boolean needOrderAdjust(String orderno) throws EnumValueUndefinedException {
        return orderDao.needOrderAdjust(orderno);
    }

    /**
     * 更新内部账户余额
     *
     * @param entryItemFlow 科目流水实例
     */
    private void doCreateOrUpdateInnerAccount(EntryItemFlow entryItemFlow) throws ServerException, EnumValueUndefinedException, BatchException {
        String businessid = entryItemFlow.getBusinessid();
        BusinessInnerAccount businessInnerAccount = businessDao.findBusinessInnerAccountByInnerid(entryItemFlow.getInnerId());
        if (businessInnerAccount == null) {
            businessInnerAccount = new BusinessInnerAccount();
            businessInnerAccount.setBusinessid(businessid);
            businessInnerAccount.setInnerid(entryItemFlow.getInnerId());
            businessInnerAccount.setAccountitemfirstname(entryItemFlow.getAccountitemfirstname());
            businessInnerAccount.setAccountitemfirstcode(entryItemFlow.getAccountitemfirstcode());
            businessInnerAccount.setAccountitemsecdname(entryItemFlow.getAccountitemsecdname());
            businessInnerAccount.setAccountitemsecdcode(entryItemFlow.getAccountitemsecdcode());
            businessInnerAccount.setAccountitemthirdname(entryItemFlow.getAccountitemthirdname());
            businessInnerAccount.setAccountitemthirdcode(entryItemFlow.getAccountitemthirdcode());
            businessInnerAccount.setAccountsubitemname(entryItemFlow.getAccountsubitemname());
            businessInnerAccount.setAccountsubitemcode(entryItemFlow.getAccountsubitemcode());
            businessInnerAccount.setAccountdate(entryItemFlow.getAccountdate());
            AccountItemTypeEnum accountItemType = entryItemFlow.getType();
            if (accountItemType.equals(AccountItemTypeEnum.common)) {
                orderDao.doChangeOrderSatus(adjustOrderMap.get(entryItemFlow.getOrderno()), OrderStatusEnum.adjustFail);
                throw new ServerException("内部账户类型不能为“资产负债共同类”");
            }
            businessInnerAccount.setType(accountItemType);
            BalanceDirectEnum balanceDirect = entryItemFlow.getBalancedirect();
            if ((balanceDirect.equals(BalanceDirectEnum.debits) && accountItemType.equals(AccountItemTypeEnum.debits))
                    || (balanceDirect.equals(BalanceDirectEnum.credits) && accountItemType.equals(AccountItemTypeEnum.credits))) {
                businessInnerAccount.setBalance(0 + entryItemFlow.getAmont());
            } else {
                businessInnerAccount.setBalance(0 - entryItemFlow.getAmont());
            }
            if (!businessDao.doCreateInnerAccount(businessInnerAccount)) {
                orderDao.doChangeOrderSatus(adjustOrderMap.get(entryItemFlow.getOrderno()), OrderStatusEnum.adjustFail);
                throw new BatchException("创建内部账户失败：" + entryItemFlow.getInnerId());
            }
        } else {
            businessInnerAccount.setAccountdate(entryItemFlow.getAccountdate());
            AccountItemTypeEnum accountItemType = businessInnerAccount.getType();
            BalanceDirectEnum balanceDirect = entryItemFlow.getBalancedirect();
            if ((balanceDirect.equals(BalanceDirectEnum.debits) && accountItemType.equals(AccountItemTypeEnum.debits))
                    || (balanceDirect.equals(BalanceDirectEnum.credits) && accountItemType.equals(AccountItemTypeEnum.credits))) {
                businessInnerAccount.setBalance(businessInnerAccount.getBalance() + entryItemFlow.getAmont());
            } else {
                businessInnerAccount.setBalance(businessInnerAccount.getBalance() - entryItemFlow.getAmont());
            }
        }
        orderDao.doChangeOrderSatus(adjustOrderMap.get(entryItemFlow.getOrderno()), OrderStatusEnum.adjusting);
    }

    /**
     * 执行内部账统计
     *
     * @param accountDate       会计日期
     * @param entryItemFlowList 科目流水列表
     */
    private void doStatistics(String accountDate, List<EntryItemFlow> entryItemFlowList) throws TimerException, ServerException, BatchException, EnumValueUndefinedException {
        if (!statisticsInnerDao.doDeleteInnerStatistics(accountDate)) {
            throw new BatchException("删除内部账统计信息失败！");
        }
        Map<String, InnerStatistics> innerStatisticsMap = new HashMap<>();
        Calendar calendar = CalendarTools.getCalendar(accountDate);
        calendar = CalendarTools.getPrevDay(calendar);
        String prevAccountDate = CommonTools.getDateTimeString(calendar.getTime(), "yyyy-MM-dd");
        double amontdebit = 0.00D;
        double amontcredit = 0.00D;
        for (EntryItemFlow entryItemFlow : entryItemFlowList) {
            InnerStatistics innerStatistics;
            if (!innerStatisticsMap.containsKey(entryItemFlow.getInnerId())) {
                InnerStatistics prevStatistics = statisticsInnerDao.findInnerStatisticsByInneridAndAccountDate(entryItemFlow.getInnerId(), prevAccountDate);
                double prevbalance = 0.00D;
                if (prevStatistics != null) {
                    prevbalance = prevStatistics.getBalance();
                }
                innerStatistics = new InnerStatistics();
                innerStatistics.setBusinessid(entryItemFlow.getBusinessid());
                innerStatistics.setInnerid(entryItemFlow.getInnerId());
                innerStatistics.setAccountitemfirstname(entryItemFlow.getAccountitemfirstname());
                innerStatistics.setAccountitemfirstcode(entryItemFlow.getAccountitemfirstcode());
                innerStatistics.setAccountitemsecdname(entryItemFlow.getAccountitemsecdname());
                innerStatistics.setAccountitemsecdcode(entryItemFlow.getAccountitemsecdcode());
                innerStatistics.setAccountitemthirdname(entryItemFlow.getAccountitemthirdname());
                innerStatistics.setAccountitemthirdcode(entryItemFlow.getAccountitemthirdcode());
                innerStatistics.setAccountsubitemname(entryItemFlow.getAccountsubitemname());
                innerStatistics.setAccountsubitemcode(entryItemFlow.getAccountsubitemcode());
                innerStatistics.setType(entryItemFlow.getType());
                innerStatistics.setPrevbalance(prevbalance);
                innerStatistics.setAmontcredit(0);
                innerStatistics.setAmontdebit(0);
                innerStatistics.setBalance(0);
                innerStatistics.setAccountdate(accountDate);
                innerStatisticsMap.put(innerStatistics.getInnerid(), innerStatistics);
            }
            innerStatistics = innerStatisticsMap.get(entryItemFlow.getInnerId());
            AccountItemTypeEnum accountItemType = entryItemFlow.getType();
            BalanceDirectEnum balanceDirect = entryItemFlow.getBalancedirect();
            if ((balanceDirect.equals(BalanceDirectEnum.debits) && accountItemType.equals(AccountItemTypeEnum.debits))
                    || (balanceDirect.equals(BalanceDirectEnum.credits) && accountItemType.equals(AccountItemTypeEnum.credits))) {
                innerStatistics.setBalance(innerStatistics.getBalance() + entryItemFlow.getAmont());
            } else {
                innerStatistics.setBalance(innerStatistics.getBalance() - entryItemFlow.getAmont());
            }
            if (balanceDirect.equals(BalanceDirectEnum.debits)) {
                innerStatistics.setAmontdebit(innerStatistics.getAmontdebit() + entryItemFlow.getAmont());
                amontdebit += entryItemFlow.getAmont();
            }
            if (balanceDirect.equals(BalanceDirectEnum.credits)) {
                innerStatistics.setAmontcredit(innerStatistics.getAmontcredit() + entryItemFlow.getAmont());
                amontcredit += entryItemFlow.getAmont();
            }
        }
        for (Map.Entry<String, InnerStatistics> entry : innerStatisticsMap.entrySet()) {
            if (!statisticsInnerDao.doCreateInnerStatistics(entry.getValue())) {
                throw new BatchException("保存内部账统计信息失败！");
            }
        }
        if (Double.compare(amontcredit, amontdebit) != 0) {
            throw new BatchException("会计日期【" + accountDate + "】会计分录借贷双方发生额不相等！");
        }
    }

    @Override
    public void dropTable() {

    }
}

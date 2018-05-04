package com.olink.account.batch.task.tradehistory.service.impl;

import com.olink.account.base.impl.BaseBatchService;
import com.olink.account.batch.dao.IInnerDao;
import com.olink.account.batch.dao.impl.InnerDao;
import com.olink.account.batch.exception.BatchException;
import com.olink.account.batch.task.tradehistory.dao.ITradeHistoryDao;
import com.olink.account.batch.task.tradehistory.dao.impl.TradeHistoryDao;
import com.olink.account.batch.task.tradehistory.service.ITradeHistoryService;
import com.olink.account.dao.IOrderDao;
import com.olink.account.dao.impl.OrderDao;
import com.olink.account.enumration.OrderTypeEnum;
import com.olink.account.model.batch.accounting.InnerStatistics;
import com.olink.account.model.batch.accounting.InnerSummary;
import com.olink.account.model.trade.accounting.EntryItemFlow;
import com.olink.account.model.trade.info.OrderReviewRecord;
import com.olink.account.model.trade.order.OrderBase;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2017/5/10.
 * 历史交易数据服务
 */
public class TradeHistoryService extends BaseBatchService implements ITradeHistoryService {

    private ITradeHistoryDao tradeHistoryDao = new TradeHistoryDao();

    private IOrderDao orderDao = new OrderDao();

    private IInnerDao innerDao = new InnerDao(tradeHistoryDao.getDBTools());

    /**
     * 原始表名
     */
    private List<String> tablenames = new ArrayList<>();

    @Override
    public boolean doHistory(String accountDate) throws BatchException {
        try {
            String prevyear = accountDate.substring(0, 4);
            doCreateTable(prevyear);
            tradeHistoryDao.beginTranslist();
            orderDao.beginTranslist();
            if (!doHisOrder(prevyear, accountDate)) {
                tradeHistoryDao.rollBackTranslist();
                orderDao.rollBackTranslist();
                return false;
            }
            if (!doHisInner(prevyear, accountDate)) {
                tradeHistoryDao.rollBackTranslist();
                orderDao.rollBackTranslist();
                return false;
            }
            tradeHistoryDao.commitTranslist();
            orderDao.commitTranslist();
            return true;
        } catch (Exception e) {
            tradeHistoryDao.rollBackTranslist();
            orderDao.rollBackTranslist();
            throw new BatchException(e.getMessage());
        } finally {
            dropTable();
        }
    }

    /**
     * 创建原始表
     *
     * @param prevyear 历史表后缀
     */
    private void doCreateTable(String prevyear) throws BatchException, IllegalAccessException, InstantiationException {
        tablenames.clear();
        if (!tradeHistoryDao.doCreateTable(orderDao.getCreateTableSQL("acc_order_base"))
                || !tradeHistoryDao.doCreateHisTable("acc_order_base", "acc_order_base_" + prevyear)) {
            throw new BatchException("创建订单基础表失败");
        }
        tradeHistoryDao.doCreateHisTableLog(tradeHistoryDao.getDBTools().getDbName(), "acc_order_base" + "_" + prevyear, tradeHistoryDao.getDBTools().getDbNo() + "",
                orderDao.getDBTools().getDbName(), "acc_order_base", orderDao.getDBTools().getDbNo() + "");
        tablenames.add("acc_order_base");

        for (OrderTypeEnum orderTypeEnum : OrderTypeEnum.values()) {
            OrderBase order = orderTypeEnum.getOrderClass().newInstance();
            String createsql = orderDao.getCreateTableSQL(order.getCurrTableName());
            if (!tradeHistoryDao.doCreateTable(createsql)
                    || !tradeHistoryDao.doCreateHisTable(order.getCurrTableName(), order.getCurrTableName() + "_" + prevyear)) {
                throw new BatchException("创建订单表失败");
            }
            tradeHistoryDao.doCreateHisTableLog(tradeHistoryDao.getDBTools().getDbName(), order.getCurrTableName() + "_" + prevyear, tradeHistoryDao.getDBTools().getDbNo() + "",
                    orderDao.getDBTools().getDbName(), order.getCurrTableName(), orderDao.getDBTools().getDbNo() + "");
            tablenames.add(order.getCurrTableName());
        }

        if (!tradeHistoryDao.doCreateTable(orderDao.getCreateTableSQL("acc_order_internaltrade"))
                || !tradeHistoryDao.doCreateHisTable("acc_order_internaltrade", "acc_order_internaltrade" + prevyear)) {
            throw new BatchException("创建内部特殊交易订单表失败");
        }
        tradeHistoryDao.doCreateHisTableLog(tradeHistoryDao.getDBTools().getDbName(), "acc_order_internaltrade" + "_" + prevyear, tradeHistoryDao.getDBTools().getDbNo() + "",
                orderDao.getDBTools().getDbName(), "acc_order_internaltrade", orderDao.getDBTools().getDbNo() + "");
        tablenames.add("acc_order_internaltrade");

        EntryItemFlow entryItemFlow = new EntryItemFlow();
        if (!tradeHistoryDao.doCreateTable(orderDao.getCreateTableSQL(entryItemFlow.getCurrTableName()))
                || !tradeHistoryDao.doCreateHisTable(entryItemFlow.getCurrTableName(), entryItemFlow.getCurrTableName() + "_" + prevyear)) {
            throw new BatchException("创建科目流水表失败");
        }
        tradeHistoryDao.doCreateHisTableLog(tradeHistoryDao.getDBTools().getDbName(), entryItemFlow.getCurrTableName() + "_" + prevyear, tradeHistoryDao.getDBTools().getDbNo() + "",
                orderDao.getDBTools().getDbName(), entryItemFlow.getCurrTableName(), orderDao.getDBTools().getDbNo() + "");
        tablenames.add(entryItemFlow.getCurrTableName());

        OrderReviewRecord orderReviewRecord = new OrderReviewRecord();
        if (!tradeHistoryDao.doCreateTable(orderDao.getCreateTableSQL(orderReviewRecord.getCurrTableName()))
                || !tradeHistoryDao.doCreateHisTable(orderReviewRecord.getCurrTableName(), orderReviewRecord.getCurrTableName() + "_" + prevyear)) {
            throw new BatchException("创建订单审核记录表失败");
        }
        tradeHistoryDao.doCreateHisTableLog(tradeHistoryDao.getDBTools().getDbName(), orderReviewRecord.getCurrTableName() + "_" + prevyear, tradeHistoryDao.getDBTools().getDbNo() + "",
                orderDao.getDBTools().getDbName(), orderReviewRecord.getCurrTableName(), orderDao.getDBTools().getDbNo() + "");
        tablenames.add(orderReviewRecord.getCurrTableName());

        InnerStatistics innerStatistics = new InnerStatistics();
        if (!tradeHistoryDao.doCreateHisTable(innerStatistics.getCurrTableName(), innerStatistics.getCurrTableName() + "_" + prevyear)) {
            throw new BatchException("创建内部账统计表失败");
        }
        tradeHistoryDao.doCreateHisTableLog(tradeHistoryDao.getDBTools().getDbName(), innerStatistics.getCurrTableName() + "_" + prevyear, tradeHistoryDao.getDBTools().getDbNo() + "",
                tradeHistoryDao.getDBTools().getDbName(), innerStatistics.getCurrTableName(), tradeHistoryDao.getDBTools().getDbNo() + "");

        InnerSummary innerSummary = new InnerSummary();
        if (!tradeHistoryDao.doCreateHisTable(innerSummary.getCurrTableName(), innerSummary.getCurrTableName() + "_" + prevyear)) {
            throw new BatchException("创建内部账科目汇总表失败");
        }
        tradeHistoryDao.doCreateHisTableLog(tradeHistoryDao.getDBTools().getDbName(), innerSummary.getCurrTableName() + "_" + prevyear, tradeHistoryDao.getDBTools().getDbNo() + "",
                tradeHistoryDao.getDBTools().getDbName(), innerSummary.getCurrTableName(), tradeHistoryDao.getDBTools().getDbNo() + "");
    }

    /**
     * 订单数据迁移至历史表
     *
     * @param prevyear    历史表后缀
     * @param accountDate 会计日期
     * @return true|false
     */
    private boolean doHisOrder(String prevyear, String accountDate) {
        List<Map<String, Integer>> orderinfoList = orderDao.findAllAdjustedOrder(accountDate);
        for (Map<String, Integer> orderinfo : orderinfoList) {
            for (Map.Entry<String, Integer> entry : orderinfo.entrySet()) {
                String orderno = entry.getKey();
                int ordertype = entry.getValue();
                //迁移订单信息
                OrderBase order = orderDao.findOrderByOrderNo(orderno, ordertype);
                if (!orderDao.doDeleteOrder(order)) {
                    return false;
                }
                if (!tradeHistoryDao.doCreateRecords(order, prevyear)) {
                    return false;
                }
                //迁移科目流水
                List<EntryItemFlow> entryItemFlowList = orderDao.findEntryItemFlowByOrderNo(orderno);
                for (EntryItemFlow entryItemFlow : entryItemFlowList) {
                    if (!orderDao.doDeleteEntryItemFlow(entryItemFlow)) {
                        return false;
                    }
                    if (!tradeHistoryDao.doCreateRecords(entryItemFlow, prevyear)) {
                        return false;
                    }
                }
                //迁移订单审核记录表
                List<DBTable> reviewRecordList = orderDao.findReviewRecords(orderno);
                for (DBTable dbTable : reviewRecordList) {
                    OrderReviewRecord orderReviewRecord = (OrderReviewRecord) dbTable;
                    if (!orderDao.doDeleteOrderReview(orderReviewRecord)) {
                        return false;
                    }
                    if (!tradeHistoryDao.doCreateRecords(orderReviewRecord, prevyear)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    /**
     * 迁移内部账数据
     *
     * @param prevyear    历史表后缀
     * @param accountDate 会计日期
     * @return true|false
     */
    private boolean doHisInner(String prevyear, String accountDate) {
        //迁移内部账统计信息
        List<DBTable> innerStatisticsList = innerDao.findInnerStatisticsForBind(accountDate);
        for (DBTable dbTable : innerStatisticsList) {
            InnerStatistics innerStatistics = (InnerStatistics) dbTable;
            if (!innerDao.doDeleteInnerStatistics(innerStatistics)) {
                return false;
            }
            if (!tradeHistoryDao.doCreateRecords(innerStatistics, prevyear)) {
                return false;
            }
        }
        //迁移内部账汇总信息
        List<DBTable> innerSummaryList = innerDao.findInnerSummaryForBind(accountDate);
        for (DBTable dbTable : innerSummaryList) {
            InnerSummary innerSummary = (InnerSummary) dbTable;
            if (!innerDao.doDeleteInnerSummary(innerSummary)) {
                return false;
            }
            if (!tradeHistoryDao.doCreateRecords(innerSummary, prevyear)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void dropTable() {
        for (String tablename : tablenames) {
            tradeHistoryDao.doDropTable(tablename);
        }
    }
}

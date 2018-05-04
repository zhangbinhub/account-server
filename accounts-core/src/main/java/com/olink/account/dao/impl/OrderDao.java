package com.olink.account.dao.impl;

import com.olink.account.base.impl.BaseTradeDao;
import com.olink.account.dao.IOrderDao;
import com.olink.account.enumration.OrderStatusEnum;
import com.olink.account.enumration.OrderTypeEnum;
import com.olink.account.enumration.ReviewResultEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.accounting.EntryItemFlow;
import com.olink.account.model.trade.dictionary.D_DateParam;
import com.olink.account.model.trade.info.OrderReviewRecord;
import com.olink.account.model.trade.order.InternalTradeOrder;
import com.olink.account.model.trade.order.OrderBase;
import net.sf.json.JSONArray;
import org.apache.log4j.Logger;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.common.DBConTools;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/9/28.
 * 订单持久化
 */
public class OrderDao extends BaseTradeDao implements IOrderDao {

    private final Logger log = Logger.getLogger(this.getClass());

    public OrderDao() {
        super();
    }

    public OrderDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public List<Map<String, Integer>> findAllAdjustedOrder(String bindaccountdate) {
        List<Map<String, Integer>> orderlist = new ArrayList<>();
        String searchSQL = "select orderNo,ordertype from acc_order_base where status=" + OrderStatusEnum.adjustSuccess.getValue() + " and bindaccountdate='" + bindaccountdate + "'";
        List<Map<String, Object>> result = dbTools.getDataListBySql(searchSQL);
        for (Map<String, Object> order : result) {
            Map<String, Integer> orderinfo = new HashMap<>();
            orderinfo.put(order.get("orderNo".toUpperCase()).toString(), Integer.valueOf(order.get("ordertype".toUpperCase()).toString()));
            orderlist.add(orderinfo);
        }
        return orderlist;
    }

    @Override
    public OrderBase findOrderByOrderNo(String orderNo, int ordertype) {
        OrderTypeEnum orderStatus = null;
        try {
            orderStatus = OrderTypeEnum.getEnum(ordertype);
        } catch (EnumValueUndefinedException e) {
            log.error(e.getMessage(), e);
        }
        OrderBase orderBase;
        if (orderStatus == null) {
            orderBase = findOrderByOrderNo(orderNo, InternalTradeOrder.class);
        } else {
            orderBase = findOrderByOrderNo(orderNo, orderStatus.getOrderClass());
        }
        return orderBase;
    }

    @Override
    public OrderBase findOrderByOrderNoLock(String orderNo, int ordertype) {
        OrderTypeEnum orderStatus = null;
        try {
            orderStatus = OrderTypeEnum.getEnum(ordertype);
        } catch (EnumValueUndefinedException e) {
            log.error(e.getMessage(), e);
        }
        OrderBase orderBase;
        if (orderStatus == null) {
            orderBase = findOrderByOrderNoLock(orderNo, InternalTradeOrder.class);
        } else {
            orderBase = findOrderByOrderNoLock(orderNo, orderStatus.getOrderClass());
        }
        return orderBase;
    }

    @Override
    public OrderBase findOrderByOrderNo(String orderNo, Class<? extends OrderBase> orderClass) {
        return (OrderBase) OrderBase.doView(orderNo, orderClass, null, dbTools.getDbcon());
    }

    @Override
    public OrderBase findOrderByOrderNoLock(String orderNo, Class<? extends OrderBase> orderClass) {
        return (OrderBase) OrderBase.doViewByLock(orderNo, orderClass, null, dbTools.getDbcon());
    }

    @Override
    public List<EntryItemFlow> findEntryItemFlowByAccountDate(String accountDate) {
        Map<String, Object> params = new HashMap<>();
        params.put(EntryItemFlow.class.getCanonicalName() + ".accountdate", accountDate);
        List<DBTable> dbTables = dbTools.getDataObjListBySql(params, EntryItemFlow.class, null, null);
        List<EntryItemFlow> entryItemFlows = new ArrayList<>();
        for (DBTable dbTable : dbTables) {
            entryItemFlows.add((EntryItemFlow) dbTable);
        }
        return entryItemFlows;
    }

    @Override
    public List<EntryItemFlow> findEntryItemFlowByOrderNo(String orderNo) {
        Map<String, Object> params = new HashMap<>();
        params.put(EntryItemFlow.class.getCanonicalName() + ".orderno", orderNo);
        List<DBTable> dbTables = dbTools.getDataObjListBySql(params, EntryItemFlow.class, null, null);
        List<EntryItemFlow> entryItemFlows = new ArrayList<>();
        for (DBTable dbTable : dbTables) {
            entryItemFlows.add((EntryItemFlow) dbTable);
        }
        return entryItemFlows;
    }

    @Override
    public OrderBase doChangeOrderSatus(String orderno, OrderStatusEnum orderStatusEnum) throws ServerException {
        String searchSQL = "select ordertype from acc_order_base where orderno='" + orderno + "'";
        List<Map<String, Object>> result = dbTools.getDataListBySql(searchSQL);
        if (result.size() > 0) {
            Map<String, Object> item = result.get(0);
            OrderBase orderBase = findOrderByOrderNoLock(orderno, Integer.valueOf(item.get("ordertype".toUpperCase()).toString()));
            if (doChangeOrderSatus(orderBase, orderStatusEnum)) {
                return orderBase;
            }
        }
        return null;
    }

    @Override
    public boolean needOrderAdjust(String orderno) throws EnumValueUndefinedException {
        String searchSQL = "select status from acc_order_base where orderno='" + orderno + "'";
        List<Map<String, Object>> result = dbTools.getDataListBySql(searchSQL);
        if (result.size() > 0) {
            Map<String, Object> item = result.get(0);
            OrderStatusEnum orderStatus = OrderStatusEnum.getEnum(Integer.valueOf(item.get("status".toUpperCase()).toString()));
            if (orderStatus.equals(OrderStatusEnum.success) || orderStatus.equals(OrderStatusEnum.refundSuccess)
                    || orderStatus.equals(OrderStatusEnum.refundFail) || orderStatus.equals(OrderStatusEnum.refundCancle)
                    || orderStatus.equals(OrderStatusEnum.adjustFail) || orderStatus.equals(OrderStatusEnum.adjustCancle)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<DBTable> findReviewRecords(String orderNo) {
        Map<String, Object> param = new HashMap<>();
        param.put(OrderReviewRecord.class.getCanonicalName() + ".orderNo", orderNo);
        return dbTools.getDataObjListBySql(param, OrderReviewRecord.class, null, " order by ${" + OrderReviewRecord.class.getCanonicalName() + ".reviewdate} desc");
    }

    @Override
    public List<DBTable> findReviewRecordsByLock(String orderNo) {
        Map<String, Object> param = new HashMap<>();
        param.put(OrderReviewRecord.class.getCanonicalName() + ".orderNo", orderNo);
        return dbTools.getDataObjListBySql(param, OrderReviewRecord.class, null, " order by ${" + OrderReviewRecord.class.getCanonicalName() + ".reviewdate} desc for update");
    }

    @Override
    public OrderReviewRecord findReviewRecord(String reviewid) {
        return (OrderReviewRecord) OrderReviewRecord.getInstance(reviewid, OrderReviewRecord.class, null, dbTools.getDbcon());
    }

    @Override
    public OrderReviewRecord findReviewRecordByLock(String reviewid) {
        return (OrderReviewRecord) OrderReviewRecord.getInstanceByLock(reviewid, OrderReviewRecord.class, null, dbTools.getDbcon());
    }

    @Override
    public boolean doDeleteOrder(OrderBase orderBase) {
        return orderBase.doDelete(dbTools.getDbcon());
    }

    @Override
    public boolean doDeleteEntryItemFlow(EntryItemFlow entryItemFlow) {
        return entryItemFlow.doDelete(dbTools.getDbcon());
    }

    @Override
    public boolean doOrderReview(String orderNo, String content, ReviewResultEnum reviewResultEnum, String userid, String username) {
        OrderReviewRecord orderReviewRecord = new OrderReviewRecord();
        orderReviewRecord.setOrderNo(orderNo);
        orderReviewRecord.setContent(content);
        orderReviewRecord.setResult(reviewResultEnum);
        orderReviewRecord.setUserid(userid);
        orderReviewRecord.setUsername(username);
        orderReviewRecord.setAccountdate(D_DateParam.getAccountDate(dbTools.getDbcon()));
        orderReviewRecord.setReviewdate(CommonTools.getNowTimeString());
        return orderReviewRecord.doCreate(dbTools.getDbcon());
    }

    @Override
    public boolean doDeleteOrderReview(OrderReviewRecord orderReviewRecord) {
        return orderReviewRecord.doDelete(dbTools.getDbcon());
    }

    @Override
    public boolean doDeleteOrderReview(String reviewid) {
        OrderReviewRecord orderReviewRecord = findReviewRecordByLock(reviewid);
        return doDeleteOrderReview(orderReviewRecord);
    }

    @Override
    public boolean doDeleteOrderReviews(String orderNo) {
        OrderReviewRecord orderReviewRecord = new OrderReviewRecord();
        return dbTools.doUpdate("delete from " + orderReviewRecord.getCurrTableName() + " where orderNo='" + orderNo + "'");
    }
}

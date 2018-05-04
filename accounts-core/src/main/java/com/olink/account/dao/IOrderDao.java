package com.olink.account.dao;

import com.olink.account.base.IBaseTradeDao;
import com.olink.account.enumration.OrderStatusEnum;
import com.olink.account.enumration.ReviewResultEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.accounting.EntryItemFlow;
import com.olink.account.model.trade.info.OrderReviewRecord;
import com.olink.account.model.trade.order.OrderBase;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/9/28.
 * 订单持久化
 */
public interface IOrderDao extends IBaseTradeDao {

    /**
     * 获取所有已核算订单
     *
     * @param bindaccountdate 会计扎账日期
     * @return 订单列表map(key=orderno, value=ordertype)
     */
    List<Map<String, Integer>> findAllAdjustedOrder(String bindaccountdate);

    /**
     * 获取订单实例
     *
     * @param orderNo   订单号
     * @param ordertype 订单类型
     * @return 订单实例
     */
    OrderBase findOrderByOrderNo(String orderNo, int ordertype);

    /**
     * 获取订单实例
     *
     * @param orderNo   订单号
     * @param ordertype 订单类型
     * @return 订单实例
     */
    OrderBase findOrderByOrderNoLock(String orderNo, int ordertype);

    /**
     * 获取订单实例
     *
     * @param orderNo    订单号
     * @param orderClass 订单类
     * @return 订单实例
     */
    OrderBase findOrderByOrderNo(String orderNo, Class<? extends OrderBase> orderClass);

    /**
     * 获取订单实例
     *
     * @param orderNo    订单号
     * @param orderClass 订单类
     * @return 订单实例
     */
    OrderBase findOrderByOrderNoLock(String orderNo, Class<? extends OrderBase> orderClass);

    /**
     * 获取会计科目流水
     *
     * @param accountDate 会计日期
     * @return 会计科目流水
     */
    List<EntryItemFlow> findEntryItemFlowByAccountDate(String accountDate);

    /**
     * 获取会计科目流水
     *
     * @param orderNo 订单编号
     * @return 会计科目流水
     */
    List<EntryItemFlow> findEntryItemFlowByOrderNo(String orderNo);

    /**
     * 修改订单状态
     *
     * @param orderno         订单号
     * @param orderStatusEnum 订单状态
     * @return 订单实例
     */
    OrderBase doChangeOrderSatus(String orderno, OrderStatusEnum orderStatusEnum) throws ServerException;

    /**
     * 判断订单是否可以进行核算
     *
     * @param orderno 订单号
     * @return true|false
     */
    boolean needOrderAdjust(String orderno) throws EnumValueUndefinedException;

    /**
     * 获取订单审核记录
     *
     * @param orderNo 订单号
     * @return 订单审核记录
     */
    List<DBTable> findReviewRecords(String orderNo);

    /**
     * 获取订单审核记录
     *
     * @param orderNo 订单号
     * @return 订单审核记录
     */
    List<DBTable> findReviewRecordsByLock(String orderNo);

    /**
     * 获取订单审核记录
     *
     * @param reviewid 审核记录id
     * @return 审核记录实例
     */
    OrderReviewRecord findReviewRecord(String reviewid);

    /**
     * 获取订单审核记录
     *
     * @param reviewid 审核记录id
     * @return 审核记录实例
     */
    OrderReviewRecord findReviewRecordByLock(String reviewid);

    /**
     * 删除订单记录
     *
     * @param orderBase 订单实例
     * @return true|false
     */
    boolean doDeleteOrder(OrderBase orderBase);

    /**
     * 删除科目流水
     *
     * @param entryItemFlow 科目流水实例
     * @return true|false
     */
    boolean doDeleteEntryItemFlow(EntryItemFlow entryItemFlow);

    /**
     * 审核订单
     *
     * @param orderNo          订单号
     * @param content          审核内容
     * @param reviewResultEnum 审核结果
     * @param userid           审核人
     * @param username         审核人名称
     * @return true|false
     */
    boolean doOrderReview(String orderNo, String content, ReviewResultEnum reviewResultEnum, String userid, String username);

    /**
     * 删除审核记录
     *
     * @param orderReviewRecord 订单审核记录实例
     * @return true|false
     */
    boolean doDeleteOrderReview(OrderReviewRecord orderReviewRecord);

    /**
     * 删除审核记录
     *
     * @param reviewid 审核记录id
     * @return true|false
     */
    boolean doDeleteOrderReview(String reviewid);

    /**
     * 删除审核记录
     *
     * @param orderNo 订单号
     * @return true|false
     */
    boolean doDeleteOrderReviews(String orderNo);

}

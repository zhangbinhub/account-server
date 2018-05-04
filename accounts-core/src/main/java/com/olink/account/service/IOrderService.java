package com.olink.account.service;

import com.olink.account.base.IBaseTradeService;
import com.olink.account.enumration.ReviewResultEnum;
import com.olink.account.model.trade.info.OrderReviewRecord;
import com.olink.account.model.trade.order.OrderBase;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.List;

/**
 * Created by zhangbin on 2016/9/29.
 * 订单服务
 */
public interface IOrderService extends IBaseTradeService {

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
     * 获取订单审核记录
     *
     * @param orderNo 订单号
     * @return 订单审核记录
     */
    List<DBTable> findReviewRecords(String orderNo);

    /**
     * 获取订单审核记录
     *
     * @param reviewid 审核记录id
     * @return 审核记录实例
     */
    OrderReviewRecord findReviewRecord(String reviewid);

    /**
     * 审核订单
     *
     * @param orderNo      订单号
     * @param content      审核内容
     * @param reviewResultEnum 审核结果
     * @param userid       审核人
     * @param username     审核人名称
     * @return true|false
     */
    boolean doOrderReview(String orderNo, String content, ReviewResultEnum reviewResultEnum, String userid, String username);

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

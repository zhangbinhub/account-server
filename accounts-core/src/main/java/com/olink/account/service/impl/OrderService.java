package com.olink.account.service.impl;

import com.olink.account.base.impl.BaseTradeService;
import com.olink.account.dao.IOrderDao;
import com.olink.account.dao.impl.OrderDao;
import com.olink.account.enumration.ReviewResultEnum;
import com.olink.account.model.trade.info.OrderReviewRecord;
import com.olink.account.model.trade.order.OrderBase;
import com.olink.account.service.IOrderService;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.List;

/**
 * Created by zhangbin on 2016/9/29.
 * 订单服务
 */
public class OrderService extends BaseTradeService implements IOrderService {

    private IOrderDao orderDao = new OrderDao();

    @Override
    public OrderBase findOrderByOrderNo(String orderNo, Class<? extends OrderBase> orderClass) {
        return orderDao.findOrderByOrderNo(orderNo, orderClass);
    }

    @Override
    public OrderBase findOrderByOrderNoLock(String orderNo, Class<? extends OrderBase> orderClass) {
        return orderDao.findOrderByOrderNoLock(orderNo, orderClass);
    }

    @Override
    public List<DBTable> findReviewRecords(String orderNo) {
        return orderDao.findReviewRecords(orderNo);
    }

    @Override
    public OrderReviewRecord findReviewRecord(String reviewid) {
        return orderDao.findReviewRecord(reviewid);
    }

    @Override
    public boolean doOrderReview(String orderNo, String content, ReviewResultEnum reviewResultEnum, String userid, String username) {
        orderDao.beginTranslist();
        if (orderDao.doOrderReview(orderNo, content, reviewResultEnum, userid, username)) {
            orderDao.commitTranslist();
            return true;
        } else {
            orderDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doDeleteOrderReview(String reviewid) {
        orderDao.beginTranslist();
        if (orderDao.doDeleteOrderReview(reviewid)) {
            orderDao.commitTranslist();
            return true;
        } else {
            orderDao.rollBackTranslist();
            return false;
        }
    }

    @Override
    public boolean doDeleteOrderReviews(String orderNo) {
        orderDao.beginTranslist();
        if (orderDao.doDeleteOrderReviews(orderNo)) {
            orderDao.commitTranslist();
            return true;
        } else {
            orderDao.rollBackTranslist();
            return false;
        }
    }
}

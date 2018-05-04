package com.olink.account.trade.service;

import com.olink.account.base.IBaseTradeService;
import com.olink.account.enumration.ReviewResultEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.order.InternalTradeOrder;

/**
 * Created by zhangbin on 2016/11/23.
 * 账务系统服务接口
 */
public interface IAccountService extends IBaseTradeService {

    /**
     * 内部交易申请
     *
     * @param internalTradeOrder 内部交易订单
     * @return true|false
     */
    boolean doApplyInternalTrade(InternalTradeOrder internalTradeOrder) throws ServerException;

    /**
     * 内部交易
     *
     * @param internalTradeOrder 内部交易订单
     * @return true|false
     */
    boolean doInternalTrade(InternalTradeOrder internalTradeOrder) throws ServerException;

    /**
     * 内部交易取消
     *
     * @param orderNo 内部交易订单号
     * @param content 取消原因
     * @param userid  审核员id
     * @return true|false
     */
    boolean doCancleInternalTrade(String orderNo, String content, String userid) throws ServerException;

    /**
     * 内部交易审核
     *
     * @param orderNo          内部交易订单号
     * @param userid           审核员id
     * @param reviewContent    审核内容
     * @param reviewResultEnum 审核结果
     * @param checkstatus      复核状态
     * @return true|false
     */
    boolean doReviewInternalTrade(String orderNo, String userid, String reviewContent, ReviewResultEnum reviewResultEnum, int checkstatus) throws ServerException;

}

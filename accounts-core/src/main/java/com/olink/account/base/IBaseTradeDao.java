package com.olink.account.base;

import com.olink.account.enumration.OrderStatusEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.order.OrderBase;

/**
 * Created by zhangbin on 2016/9/8.
 * 交易库持久化层接口
 */
public interface IBaseTradeDao extends IBaseDao {

    /**
     * 修改订单状态
     *
     * @param orderBase       订单对象
     * @param orderStatusEnum 订单状态
     */
    boolean doChangeOrderSatus(OrderBase orderBase, OrderStatusEnum orderStatusEnum) throws ServerException;

    /**
     * 获取可用的系统参数值
     *
     * @param paramCode 参数编码
     * @return 参数值
     */
    String getSysParamAvailable(String paramCode);

    /**
     * 判断系统参数是否可用
     *
     * @param paramCode 参数编码
     * @return true|false
     */
    boolean isSysParamAvailable(String paramCode);

    /**
     * 获取积分兑换比例
     *
     * @return 积分兑换比例
     */
    double getIntegralRatio();

    /**
     * 获取系统会计日期
     *
     * @return 会计日期
     */
    String getAccountDate();

    /**
     * 外部交易是否可用
     *
     * @return true|false
     */
    boolean isOuterTradeEnabled();

}

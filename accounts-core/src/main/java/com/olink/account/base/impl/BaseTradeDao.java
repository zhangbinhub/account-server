package com.olink.account.base.impl;

import com.olink.account.base.IBaseTradeDao;
import com.olink.account.enumration.OrderStatusEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.dictionary.D_DateParam;
import com.olink.account.model.trade.dictionary.D_IntergralParam;
import com.olink.account.model.trade.dictionary.D_SysParam;
import com.olink.account.model.trade.order.OrderBase;
import com.olink.account.utils.DBAccess;
import pers.acp.tools.common.DBConTools;
import org.apache.log4j.Logger;

/**
 * Created by zhangbin on 2016/9/8.
 * 持久化层基类
 */
public abstract class BaseTradeDao extends BaseDao implements IBaseTradeDao {

    private final Logger log = Logger.getLogger(this.getClass());

    public BaseTradeDao() {
        super(DBAccess.getTradeInstance().getDBTool());
    }

    public BaseTradeDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public boolean doChangeOrderSatus(OrderBase orderBase, OrderStatusEnum orderStatusEnum) throws ServerException {
        try {
            if (orderBase != null) {
                orderBase.setStatus(orderStatusEnum);
                orderBase.getUpdateIncludes().clear();
                orderBase.addUpdateIncludes(new String[]{"accountdate", "statementdate", "bindaccountdate", "status", "lastmodifydate"});
                if (orderBase.doUpdateOrder(dbTools.getDbcon())) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new ServerException("订单状态修改失败！");
        }
    }

    @Override
    public String getSysParamAvailable(String paramCode) {
        return D_SysParam.getSysParamAvailable(paramCode, dbTools.getDbcon());
    }

    @Override
    public boolean isSysParamAvailable(String paramCode) {
        return D_SysParam.isSysParamAvailable(paramCode, dbTools.getDbcon());
    }

    @Override
    public double getIntegralRatio() {
        return D_IntergralParam.getIntegralRatio(dbTools.getDbcon());
    }

    @Override
    public String getAccountDate() {
        return D_DateParam.getAccountDate(dbTools.getDbcon());
    }

    @Override
    public boolean isOuterTradeEnabled() {
        return D_SysParam.getOuterTradeEnabled(dbTools.getDbcon());
    }

}

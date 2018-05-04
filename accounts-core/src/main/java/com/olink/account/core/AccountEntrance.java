package com.olink.account.core;

import com.olink.account.enumration.ResultStatusEnum;
import com.olink.account.enumration.StatusEnum;
import com.olink.account.core.accounting.AccountingEntry;
import com.olink.account.core.amont.AmontDecompose;
import com.olink.account.core.customer.SubAccountEntry;
import com.olink.account.model.trade.dictionary.D_OrderType;
import com.olink.account.model.trade.order.OrderBase;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/26.
 * 核心记账入口
 */
public class AccountEntrance {

    /**
     * 获取实例
     *
     * @param order             订单
     * @param connectionFactory 数据库连接对象
     * @return 实例对象
     */
    public static AccountEntrance getInstance(OrderBase order, ConnectionFactory connectionFactory) {
        try {
            /* 获取订单类型对象 */
            Map<String, Object> param = new HashMap<>();
            param.put(D_OrderType.class.getCanonicalName() + ".code", order.getOrdertype());
            param.put(D_OrderType.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
            D_OrderType orderTypeObj = (D_OrderType) D_OrderType.getInstance(param, D_OrderType.class, null, connectionFactory);
            if (orderTypeObj == null) {
                throw new AccountException("订单类型非法！");
            }
            return new AccountEntrance(order, orderTypeObj, connectionFactory);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return null;
        }
    }

    /**
     * 构造函数
     *
     * @param order             订单
     * @param orderTypeObj      订单类型对象
     * @param connectionFactory 数据库连接对象
     */
    private AccountEntrance(OrderBase order, D_OrderType orderTypeObj, ConnectionFactory connectionFactory) throws EnumValueUndefinedException, AccountException {
        /* 初始化各实例 */
        doInit(order, orderTypeObj, connectionFactory);
    }

    /**
     * 初始化各实例
     */
    private void doInit(OrderBase order, D_OrderType orderTypeObj, ConnectionFactory connectionFactory) throws EnumValueUndefinedException, AccountException {
        AmontDecompose amontDecompose = AmontDecompose.getInstance(order, orderTypeObj, connectionFactory);
        subAccountEntry = SubAccountEntry.getInstance(order, amontDecompose, orderTypeObj, connectionFactory);
        accountingEntry = AccountingEntry.getInstance(order, amontDecompose, orderTypeObj, connectionFactory);
    }

    /**
     * 记账前校验
     *
     * @return 记账结果对象
     */
    public AccountResult doValidate() {
        return subAccountEntry.doValidate();
    }

    /**
     * 执行记账处理
     *
     * @return 记账结果对象
     */
    public AccountResult doProcess() {
        AccountResult result = doValidate();
        if (result.getStatus().equals(ResultStatusEnum.success.getCode())) {
            result = subAccountEntry.doBalanceChange();
            if (result.getStatus().equals(ResultStatusEnum.success.getCode())) {
                AccountResult accountresult = accountingEntry.doAccounting();
                if (!accountresult.getStatus().equals(ResultStatusEnum.success.getCode())) {
                    log.error("会计记账失败：" + accountresult.getMessage());
                    result.setStatus(accountresult.getStatus());
                    result.setMessage(accountresult.getMessage());
                }
            } else {
                log.error("余额变更失败：" + result.getMessage());
            }
        } else {
            log.error("记账校验失败：" + result.getMessage());
        }
        return result;
    }

    private static final Logger log = Logger.getLogger(AccountEntrance.class);

    /**
     * 虚拟账户余额操作对象
     */
    private SubAccountEntry subAccountEntry;

    /**
     * 会计分录对象
     */
    private AccountingEntry accountingEntry;

}

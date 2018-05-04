package com.olink.account.core.customer;

import com.olink.account.enumration.*;
import com.olink.account.core.AccountException;
import com.olink.account.core.AccountResult;
import com.olink.account.core.amont.AmontDecompose;
import com.olink.account.model.trade.dictionary.D_OrderType;
import com.olink.account.model.trade.dictionary.D_OrderTypeBalanceRule;
import com.olink.account.model.trade.order.OrderBase;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/31.
 * 虚拟账户（子账户）
 */
public class SubAccountEntry {

    /**
     * 获取虚拟账户余额操作实例
     *
     * @param order             订单对象
     * @param amontDecompose    发生额分解类
     * @param orderTypeObj      订单类型对象
     * @param connectionFactory 数据库连接对象
     * @return 分解实例
     */
    public static SubAccountEntry getInstance(OrderBase order, AmontDecompose amontDecompose, D_OrderType orderTypeObj, ConnectionFactory connectionFactory) throws EnumValueUndefinedException {
        return new SubAccountEntry(order, amontDecompose, orderTypeObj, connectionFactory);
    }

    /**
     * 构造函数
     *
     * @param order             订单对象
     * @param amontDecompose    发生额分解类
     * @param orderTypeObj      订单类型对象
     * @param connectionFactory 数据库连接对象
     */
    private SubAccountEntry(OrderBase order, AmontDecompose amontDecompose, D_OrderType orderTypeObj, ConnectionFactory connectionFactory) throws EnumValueUndefinedException {
        this.orderTypeObj = orderTypeObj;
        this.connectionFactory = connectionFactory;
        this.amontDecompose = amontDecompose;
        this.order = order;
        doInit();
    }

    /**
     * 初始化虚拟账户余额变化规则
     */
    private void doInit() throws EnumValueUndefinedException {
        Map<String, Object> param = new HashMap<>();
        param.put(D_OrderTypeBalanceRule.class.getCanonicalName() + ".ordertypeid", orderTypeObj.getId());
        balanceRules = connectionFactory.doQueryForObjList(param, D_OrderTypeBalanceRule.class, null, "order by ${" + D_OrderTypeBalanceRule.class.getCanonicalName() + ".modifydate} desc");
        balanceRules = doFilterRules();
    }

    /**
     * 过滤规则
     *
     * @return 过滤后的规则
     */
    private List<DBTable> doFilterRules() throws EnumValueUndefinedException {
        List<DBTable> tmpRules = new ArrayList<>();
        Map<String, D_OrderTypeBalanceRule> tmpMap = new HashMap<>();
        for (DBTable dbTable : balanceRules) {
            D_OrderTypeBalanceRule balanceRule = (D_OrderTypeBalanceRule) dbTable;
            if (CommonTools.isNullStr(balanceRule.getBusinessid())) {
                if (!tmpMap.containsKey(balanceRule.getOldstatus().getName() + balanceRule.getNewstatus().getName() + balanceRule.getType().getValue() + balanceRule.getAmontruleid() + balanceRule.getAccounttype() + balanceRule.getBalancedirect().getValue())) {
                    tmpMap.put(balanceRule.getOldstatus().getName() + balanceRule.getNewstatus().getName() + balanceRule.getType().getValue() + balanceRule.getAmontruleid() + balanceRule.getAccounttype() + balanceRule.getBalancedirect().getValue(), balanceRule);
                }
            }
        }
        for (DBTable dbTable : balanceRules) {
            D_OrderTypeBalanceRule balanceRule = (D_OrderTypeBalanceRule) dbTable;
            if (!CommonTools.isNullStr(balanceRule.getBusinessid())) {
                String customerid = getCustomerId(balanceRule.getType());
                if (balanceRule.getBusinessid().equals(customerid)) {
                    if (tmpMap.containsKey(balanceRule.getOldstatus().getName() + balanceRule.getNewstatus().getName() + balanceRule.getType().getValue() + balanceRule.getAmontruleid() + balanceRule.getAccounttype() + balanceRule.getBalancedirect().getValue())) {
                        tmpMap.remove(balanceRule.getOldstatus().getName() + balanceRule.getNewstatus().getName() + balanceRule.getType().getValue() + balanceRule.getAmontruleid() + balanceRule.getAccounttype() + balanceRule.getBalancedirect().getValue());
                    }
                    tmpMap.put(balanceRule.getOldstatus().getName() + balanceRule.getNewstatus().getName() + balanceRule.getType().getValue() + balanceRule.getAmontruleid() + balanceRule.getAccounttype() + balanceRule.getBalancedirect().getValue(), balanceRule);
                }
            }
        }
        for (Map.Entry<String, D_OrderTypeBalanceRule> entry : tmpMap.entrySet()) {
            tmpRules.add(entry.getValue());
        }
        return tmpRules;
    }

    /**
     * 余额校验
     *
     * @return 记账结果对象
     */
    public AccountResult doValidate() {
        AccountResult result = new AccountResult();
        result.setStatus(ResultStatusEnum.success);
        try {
            try {
                double amont = BigDecimal.valueOf(order.getAmont()).setScale(2, BigDecimal.ROUND_UNNECESSARY).doubleValue();
                order.setAmont(amont);
            } catch (ArithmeticException ae) {
                throw new AccountException("订单发生额不符合规范，最多保留两位小数");
            }
            for (DBTable dbTable : balanceRules) {
                D_OrderTypeBalanceRule balanceRule = (D_OrderTypeBalanceRule) dbTable;
                ChangeAccountTypeEnum changeAccountTypeEnum = balanceRule.getType();
                ChangeBalanceDirectEnum balanceDirectEnum = balanceRule.getBalancedirect();
                if (balanceDirectEnum.equals(ChangeBalanceDirectEnum.Minus.getValue()) && !changeAccountTypeEnum.equals(ChangeAccountTypeEnum.Account.getValue())) {
                    String customerid = getCustomerId(changeAccountTypeEnum);
                    if (CommonTools.isNullStr(customerid)) {
                        throw new AccountException("【" + changeAccountTypeEnum.getDescript() + "】客户号获取失败");
                    }
                    double balance = SubAccountBalanceControl.getSubAccountBalance(customerid, changeAccountTypeEnum, balanceRule.getAccounttype(), connectionFactory);
                    double amont = amontDecompose.getAmontDetailById(balanceRule.getAmontruleid()).getAmont();
                    if (Double.compare(balance, amont) < 0) {
                        throw new AccountException("【" + changeAccountTypeEnum.getDescript() + "】账户余额不足");
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(ResultStatusEnum.failed);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 执行余额变动
     *
     * @return 记账结果对象
     */
    public AccountResult doBalanceChange() {
        AccountResult result = new AccountResult();
        result.setStatus(ResultStatusEnum.success);
        try {
            Map<String, AccountResult> resultMap = new HashMap<>();
            OrderStatusEnum oldStatus = order.getCurrOrderStatus(connectionFactory);
            if (oldStatus.equals(OrderStatusEnum.other.getValue())) {
                oldStatus = OrderStatusEnum.create;
            }
            OrderStatusEnum newStatus = order.getStatus();
            for (DBTable dbTable : balanceRules) {
                D_OrderTypeBalanceRule balanceRule = (D_OrderTypeBalanceRule) dbTable;
                if (oldStatus.equals(balanceRule.getOldstatus().getValue()) && newStatus.equals(balanceRule.getNewstatus().getValue())) {
                    ChangeAccountTypeEnum changeAccountTypeEnum = balanceRule.getType();
                    ChangeBalanceDirectEnum balanceDirectEnum = balanceRule.getBalancedirect();
                    String customerid = getCustomerId(changeAccountTypeEnum);
                    if (CommonTools.isNullStr(customerid)) {
                        throw new AccountException("【" + changeAccountTypeEnum.getDescript() + "】客户号获取失败");
                    }
                    double amont = amontDecompose.getAmontDetailById(balanceRule.getAmontruleid()).getAmont();
                    String key = changeAccountTypeEnum.getValue() + balanceRule.getAccounttype();
                    AccountResult changeResult;
                    if (resultMap.containsKey(key)) {
                        changeResult = resultMap.get(key);
                    } else {
                        double actamont = BigDecimal.valueOf(amont * order.getRatio()).setScale(2, DecimalProcessModeEnum.Half_EVEN.getMode()).doubleValue();
                        changeResult = SubAccountBalanceControl.changeBalance(customerid, amont, actamont, changeAccountTypeEnum, balanceRule.getAccounttype(), balanceDirectEnum, connectionFactory);
                        resultMap.put(key, changeResult);
                    }
                    if (changeResult.getStatus().equals(ResultStatusEnum.success.getCode())) {

                        if (!changeAccountTypeEnum.equals(ChangeAccountTypeEnum.Account)) {
                            if (Double.compare(changeResult.getAfterbalance(), 0.00D) < 0 || Double.compare(changeResult.getAftermoney(), 0.00D) < 0) {
                                throw new AccountException("【" + changeAccountTypeEnum.getDescript() + "】账户余额不足");
                            }
                        }

                        AccountTypeEnum accountTypeEnum = AccountTypeEnum.getEnum(balanceRule.getAccounttype());

                        Double resultAmont = result.getResultAmontMap(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.balance);
                        if (resultAmont != null) {
                            result.setResultAmontMap(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.balance, resultAmont + amont);
                        } else {
                            result.setResultAmontMap(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.balance, amont);
                        }

                        resultAmont = result.getResultAmontMap(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.money);
                        if (resultAmont != null) {
                            result.setResultAmontMap(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.money, resultAmont + amont);
                        } else {
                            result.setResultAmontMap(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.money, amont);
                        }

                        Double resultBeforBalance = result.getResultBeforBalance(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.balance);
                        if (resultBeforBalance == null) {
                            result.setResultBeforBalance(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.balance, changeResult.getBeforbalance());
                        }

                        resultBeforBalance = result.getResultBeforBalance(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.money);
                        if (resultBeforBalance == null) {
                            result.setResultBeforBalance(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.money, changeResult.getBeformoney());
                        }

                        result.setResultAfterBalance(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.balance, changeResult.getAfterbalance());
                        result.setResultAfterBalance(changeAccountTypeEnum, accountTypeEnum, AmontTypeEnum.money, changeResult.getAftermoney());
                    } else {
                        result = changeResult;
                        break;
                    }
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            result.setStatus(ResultStatusEnum.failed);
            result.setMessage(e.getMessage());
        }
        return result;
    }

    /**
     * 获取客户号
     *
     * @param changeAccountTypeEnum 账户类型
     * @return 客户号
     */
    private String getCustomerId(ChangeAccountTypeEnum changeAccountTypeEnum) {
        String customerid = null;
        switch (changeAccountTypeEnum) {
            case Account:
                customerid = order.getYyBusinessId();
                break;
            case Business:
                customerid = order.getBusinessid();
                break;
            case DisBusiness:
                customerid = order.getRecebusinessid();
                break;
            case Cust:
                customerid = order.getCustid();
                break;
            case DisCust:
                customerid = order.getRececustid();
                break;
        }
        return customerid;
    }

    private final Logger log = Logger.getLogger(this.getClass());

    /**
     * 订单对象
     */
    private OrderBase order;

    /**
     * 虚拟账户余额变化规则
     */
    private List<DBTable> balanceRules;

    /**
     * 订单类型字典配置
     */
    private D_OrderType orderTypeObj;

    /**
     * 数据库连接对象
     */
    private ConnectionFactory connectionFactory;

    /**
     * 发生额分解类
     */
    private AmontDecompose amontDecompose;

}

package com.olink.account.core.accounting;

import com.olink.account.enumration.BalanceDirectEnum;
import com.olink.account.enumration.ChangeAccountTypeEnum;
import com.olink.account.enumration.OrderStatusEnum;
import com.olink.account.enumration.ResultStatusEnum;
import com.olink.account.core.AccountException;
import com.olink.account.core.AccountResult;
import com.olink.account.core.amont.AmontDecompose;
import com.olink.account.model.trade.accounting.EntryItemParam;
import com.olink.account.model.trade.dictionary.D_AccountItem;
import com.olink.account.model.trade.dictionary.D_OrderType;
import com.olink.account.model.trade.dictionary.D_OrderTypeSubjectRule;
import com.olink.account.model.trade.order.OrderBase;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/31.
 * 会计分录
 */
public class AccountingEntry {

    /**
     * 获取会计分录实例
     *
     * @param order             订单对象
     * @param amontDecompose    发生额分解类
     * @param orderTypeObj      订单类型对象
     * @param connectionFactory 数据库连接对象
     * @return 分解实例
     */
    public static AccountingEntry getInstance(OrderBase order, AmontDecompose amontDecompose, D_OrderType orderTypeObj, ConnectionFactory connectionFactory) throws EnumValueUndefinedException, AccountException {
        return new AccountingEntry(order, amontDecompose, orderTypeObj, connectionFactory);
    }

    /**
     * 构造函数
     *
     * @param order             订单对象
     * @param amontDecompose    发生额分解类
     * @param orderTypeObj      订单类型对象
     * @param connectionFactory 数据库连接对象
     */
    private AccountingEntry(OrderBase order, AmontDecompose amontDecompose, D_OrderType orderTypeObj, ConnectionFactory connectionFactory) throws EnumValueUndefinedException, AccountException {
        this.orderTypeObj = orderTypeObj;
        this.connectionFactory = connectionFactory;
        this.amontDecompose = amontDecompose;
        this.order = order;
        doInit();
    }

    /**
     * 初始化会计分录规则
     */
    private void doInit() throws AccountException, EnumValueUndefinedException {
        Map<String, Object> param = new HashMap<>();
        param.put(D_OrderTypeSubjectRule.class.getCanonicalName() + ".ordertypeid", orderTypeObj.getId());
        subjectRules = connectionFactory.doQueryForObjList(param, D_OrderTypeSubjectRule.class, null, "order by ${" + D_OrderTypeSubjectRule.class.getCanonicalName() + ".modifydate} desc");
        subjectRules = doFilterRules();
        if (subjectRules.size() == 0) {
            throw new AccountException("会计科目分录规则未配置");
        }
    }

    /**
     * 过滤规则
     *
     * @return 过滤后的规则
     */
    private List<DBTable> doFilterRules() throws EnumValueUndefinedException, AccountException {
        List<DBTable> tmpRules = new ArrayList<>();
        Map<String, D_OrderTypeSubjectRule> tmpMap = new HashMap<>();
        for (DBTable dbTable : subjectRules) {
            D_OrderTypeSubjectRule subjectRule = (D_OrderTypeSubjectRule) dbTable;
            if (CommonTools.isNullStr(subjectRule.getBusinessid())) {
                throw new AccountException("会计科目分录规则没有指定B户客户号");
            }
            if (CommonTools.isNullStr(subjectRule.getPaytypecode())) {
                String customerid = getCustomerId(subjectRule.getType());
                if (subjectRule.getBusinessid().equals(customerid)) {
                    if (!tmpMap.containsKey(subjectRule.getOldstatus().getName() + subjectRule.getNewstatus().getName() + subjectRule.getType().getValue() + subjectRule.getBusinessid() + subjectRule.getAmontruleid() + subjectRule.getAccountsubitemid() + subjectRule.getBalancedirect().getValue())) {
                        tmpMap.put(subjectRule.getOldstatus().getName() + subjectRule.getNewstatus().getName() + subjectRule.getType().getValue() + subjectRule.getBusinessid() + subjectRule.getAmontruleid() + subjectRule.getAccountsubitemid() + subjectRule.getBalancedirect().getValue(), subjectRule);
                    }
                }
            }
        }
        for (DBTable dbTable : subjectRules) {
            D_OrderTypeSubjectRule subjectRule = (D_OrderTypeSubjectRule) dbTable;
            if (!CommonTools.isNullStr(subjectRule.getPaytypecode())) {
                String customerid = getCustomerId(subjectRule.getType());
                if (subjectRule.getBusinessid().equals(customerid)) {
                    if (tmpMap.containsKey(subjectRule.getOldstatus().getName() + subjectRule.getNewstatus().getName() + subjectRule.getType().getValue() + subjectRule.getBusinessid() + subjectRule.getAmontruleid() + subjectRule.getAccountsubitemid() + subjectRule.getBalancedirect().getValue())) {
                        tmpMap.remove(subjectRule.getOldstatus().getName() + subjectRule.getNewstatus().getName() + subjectRule.getType().getValue() + subjectRule.getBusinessid() + subjectRule.getAmontruleid() + subjectRule.getAccountsubitemid() + subjectRule.getBalancedirect().getValue());
                    }
                    tmpMap.put(subjectRule.getOldstatus().getName() + subjectRule.getNewstatus().getName() + subjectRule.getType().getValue() + subjectRule.getBusinessid() + subjectRule.getAmontruleid() + subjectRule.getAccountsubitemid() + subjectRule.getBalancedirect().getValue() + subjectRule.getPaytypecode(), subjectRule);
                }
            }
        }
        for (Map.Entry<String, D_OrderTypeSubjectRule> entry : tmpMap.entrySet()) {
            tmpRules.add(entry.getValue());
        }
        return tmpRules;
    }

    /**
     * 执行会计分录记账
     *
     * @return 记账结果对象
     */
    public AccountResult doAccounting() {
        AccountResult result = new AccountResult();
        result.setStatus(ResultStatusEnum.success);
        try {
            /* 校验借贷必相等Map */
            Map<String, Double> tmpBalanceMap = new HashMap<>();
            if (subjectRules.size() > 0) {
                OrderStatusEnum oldStatus = order.getCurrOrderStatus(connectionFactory);
                if (oldStatus.equals(OrderStatusEnum.other.getValue())) {
                    oldStatus = OrderStatusEnum.create;
                }
                OrderStatusEnum newStatus = order.getStatus();
                for (DBTable dbTable : subjectRules) {
                    D_OrderTypeSubjectRule subjectRule = (D_OrderTypeSubjectRule) dbTable;
                    if (oldStatus.equals(subjectRule.getOldstatus().getValue()) && newStatus.equals(subjectRule.getNewstatus().getValue())) {
                        boolean isNeedAccounting = false;
                        String payTypeCode = subjectRule.getPaytypecode();
                        if (CommonTools.isNullStr(payTypeCode)) {
                            isNeedAccounting = true;
                        } else {
                            if (payTypeCode.equals(order.getPaytype())) {
                                isNeedAccounting = true;
                            }
                        }
                        if (isNeedAccounting) {
                            ChangeAccountTypeEnum changeAccountTypeEnum = subjectRule.getType();
                            String customerid = getCustomerId(changeAccountTypeEnum);
                            if (CommonTools.isNullStr(customerid)) {
                                throw new AccountException("【" + changeAccountTypeEnum.getDescript() + "】客户号获取失败");
                            }
                            double amont = amontDecompose.getAmontDetailById(subjectRule.getAmontruleid()).getAmont();
                            /* 校验借贷必相等 start */
                            if (tmpBalanceMap.containsKey(customerid)) {
                                double tmp = tmpBalanceMap.get(customerid);
                                if (subjectRule.getBalancedirect().equals(BalanceDirectEnum.credits)) {
                                    tmp += amont;
                                } else {
                                    tmp -= amont;
                                }
                                tmpBalanceMap.put(customerid, tmp);
                            } else {
                                if (subjectRule.getBalancedirect().equals(BalanceDirectEnum.credits)) {
                                    tmpBalanceMap.put(customerid, amont);
                                } else {
                                    tmpBalanceMap.put(customerid, -amont);
                                }
                            }
                            /* 校验借贷必相等 end */
                            D_AccountItem accountsubitem = (D_AccountItem) D_AccountItem.getInstance(subjectRule.getAccountsubitemid(), D_AccountItem.class, null, connectionFactory);
                            AccountingEntryFlowControl.doRecord(order.getOrderNo(), customerid, amont, order.getAccountdate(), order.getStatementdate(), order.getBindaccountdate(), accountsubitem, subjectRule.getBalancedirect(), connectionFactory);
                        }
                    }
                }
                /* 执行校验借贷必相等 */
                for (Map.Entry<String, Double> entry : tmpBalanceMap.entrySet()) {
                    if (Double.compare(entry.getValue(), 0.00D) != 0) {
                        throw new AccountException("主体B户【" + entry.getKey() + "】会计记账借贷方不相等");
                    }
                }
            } else {
                List<EntryItemParam> entryItemParams = order.getEntryItemParams();
                if (entryItemParams != null) {
                    String customerid = order.getBusinessid();
                    double balance = 0.00D;
                    for (EntryItemParam entryItemParam : entryItemParams) {
                        BalanceDirectEnum balanceDirectEnum = entryItemParam.getBalanceDirect();
                        double amont = entryItemParam.getAmont();
                        if (balanceDirectEnum.equals(BalanceDirectEnum.credits)) {
                            balance += amont;
                        } else {
                            balance -= amont;
                        }
                        D_AccountItem accountsubitem = D_AccountItem.getAccountSubItem(entryItemParam.getItemcode(), entryItemParam.getSubitemcode(), connectionFactory);
                        if (accountsubitem == null) {
                            throw new AccountException("会计科目【" + entryItemParam.getItemcode() + entryItemParam.getSubitemcode() + "】不合法");
                        }
                        AccountingEntryFlowControl.doRecord(order.getOrderNo(), customerid, amont, order.getAccountdate(), order.getStatementdate(), order.getBindaccountdate(), accountsubitem, balanceDirectEnum, connectionFactory);
                    }
                    if (Double.compare(balance, 0.00D) != 0) {
                        throw new AccountException("主体B户【" + customerid + "】会计记账借贷方不相等");
                    }
                } else {
                    throw new AccountException("会计科目分录规则未配置");
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
        }
        return customerid;
    }

    private final Logger log = Logger.getLogger(this.getClass());

    /**
     * 订单对象
     */
    private OrderBase order;

    /**
     * 会计分录规则
     */
    private List<DBTable> subjectRules;

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

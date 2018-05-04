package com.olink.account.core.amont;

import com.olink.account.core.AccountException;
import com.olink.account.model.trade.dictionary.D_OrderType;
import com.olink.account.model.trade.dictionary.D_OrderTypeAmontDetailRule;
import com.olink.account.model.trade.order.OrderBase;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/10/31.
 * 发生额明细分解计算
 */
public class AmontDecompose {

    /**
     * 获取发生额分解实例
     *
     * @param order             订单对象
     * @param orderTypeObj      订单类型对象
     * @param connectionFactory 数据库连接对象
     * @return 分解实例
     */
    public static AmontDecompose getInstance(OrderBase order, D_OrderType orderTypeObj, ConnectionFactory connectionFactory) throws EnumValueUndefinedException, AccountException {
        return new AmontDecompose(order, orderTypeObj, connectionFactory);
    }

    /**
     * 构造函数
     *
     * @param order             订单对象
     * @param orderTypeObj      订单类型对象
     * @param connectionFactory 数据库连接对象
     */
    private AmontDecompose(OrderBase order, D_OrderType orderTypeObj, ConnectionFactory connectionFactory) throws EnumValueUndefinedException, AccountException {
        this.orderTypeObj = orderTypeObj;
        this.connectionFactory = connectionFactory;
        this.order = order;
        doInit();
    }

    /**
     * 初始化分解规则
     */
    private void doInit() throws EnumValueUndefinedException, AccountException {
        Map<String, Object> param = new HashMap<>();
        param.put(D_OrderTypeAmontDetailRule.class.getCanonicalName() + ".ordertypeid", orderTypeObj.getId());
        List<DBTable> amontDetailRules = connectionFactory.doQueryForObjList(param, D_OrderTypeAmontDetailRule.class, null, "order by ${" + D_OrderTypeAmontDetailRule.class.getCanonicalName() + ".sort} asc");
        AmontCalculation amontCalculation = new AmontCalculation(order, amontDetailRules, connectionFactory);
        amontDetailRules = amontCalculation.getAmontDetailRules();
        amontDetailMap = new HashMap<>();
        for (DBTable dbTable : amontDetailRules) {
            D_OrderTypeAmontDetailRule amontDetailRule = (D_OrderTypeAmontDetailRule) dbTable;
            amontDetailMap.put(amontDetailRule.getId(), amontDetailRule);
        }
        balance = amontCalculation.getBalance();
    }

    /**
     * 通过id获取分解的发生额
     *
     * @param id 发生额明细id
     * @return 发生额明细对象
     */
    public D_OrderTypeAmontDetailRule getAmontDetailById(String id) {
        return amontDetailMap.get(id);
    }

    public double getBalance() {
        return balance;
    }

    /**
     * 发生额计算结果
     */
    private double balance;

    /**
     * 订单对象
     */
    private OrderBase order;

    /**
     * 订单类型字典配置
     */
    private D_OrderType orderTypeObj;

    /**
     * 数据库连接对象
     */
    private ConnectionFactory connectionFactory;

    /**
     * 发生额明细，id=>D_OrderTypeAmontDetailRule
     */
    private Map<String, D_OrderTypeAmontDetailRule> amontDetailMap;

}

package com.olink.account.core.amont;

import com.olink.account.enumration.AmontRuleType;
import com.olink.account.enumration.CalculateModeEnum;
import com.olink.account.enumration.CalculateTypeEnum;
import com.olink.account.enumration.DecomposeTypeEnum;
import com.olink.account.core.AccountException;
import com.olink.account.model.trade.dictionary.D_OrderTypeAmontDetailRule;
import com.olink.account.model.trade.dictionary.D_OrderTypeAmontDetailStepRule;
import com.olink.account.model.trade.order.OrderBase;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/11/1.
 * 发生额计算
 */
class AmontCalculation {

    AmontCalculation(OrderBase order, List<DBTable> amontDetailRules, ConnectionFactory connectionFactory) throws EnumValueUndefinedException, AccountException {
        this.amontDetailRules = amontDetailRules;
        this.connectionFactory = connectionFactory;
        this.orderAmont = order.getAmont();
        this.orderActAmont = order.getActamont();
        this.diffAmont = Math.abs(orderAmont - orderActAmont);
        this.extamont1 = order.getExtamont1();
        this.extamont2 = order.getExtamont2();
        this.extamont3 = order.getExtamont3();
        this.extamont4 = order.getExtamont4();
        this.extamont5 = order.getExtamont5();
        this.extamont6 = order.getExtamont6();
        this.extamont7 = order.getExtamont7();
        this.extamont8 = order.getExtamont8();
        this.extamont9 = order.getExtamont9();
        this.extamont10 = order.getExtamont10();
        this.balance = orderAmont;
        this.actBalance = orderActAmont;
        this.diffBalance = Math.abs(orderAmont - orderActAmont);
        this.extBalance1 = this.extamont1;
        this.extBalance2 = this.extamont2;
        this.extBalance3 = this.extamont3;
        this.extBalance4 = this.extamont4;
        this.extBalance5 = this.extamont5;
        this.extBalance6 = this.extamont6;
        this.extBalance7 = this.extamont7;
        this.extBalance8 = this.extamont8;
        this.extBalance9 = this.extamont9;
        this.extBalance10 = this.extamont10;
        sortDetail = new HashMap<>();
        doCalculation();
    }

    /**
     * 发生额明细计算
     */
    private void doCalculation() throws EnumValueUndefinedException, AccountException {
        for (DBTable dbTable : amontDetailRules) {
            D_OrderTypeAmontDetailRule amontDetailRule = (D_OrderTypeAmontDetailRule) dbTable;
            if (amontDetailRule.getCalculatetype().equals(CalculateTypeEnum.step.getValue())) {
                Map<String, Object> stepparam = new HashMap<>();
                stepparam.put(D_OrderTypeAmontDetailStepRule.class.getCanonicalName() + ".amontruleid", amontDetailRule.getId());
                List<DBTable> amontDetailStepRules = connectionFactory.doQueryForObjList(stepparam, D_OrderTypeAmontDetailStepRule.class, null, "order by ${" + D_OrderTypeAmontDetailStepRule.class.getCanonicalName() + ".beginamont} asc");
                amontDetailRule.setStepRules(amontDetailStepRules);
            }
            doCalculat(amontDetailRule);
        }
        parseBalanceAndOrderAmont();
    }

    /**
     * 执行计算
     *
     * @param amontDetailRule 发生额明细规则
     */
    private void doCalculat(D_OrderTypeAmontDetailRule amontDetailRule) throws EnumValueUndefinedException, AccountException {
        calculatAmont(amontDetailRule);
        sortDetail.put(amontDetailRule.getSort(), amontDetailRule);
    }

    /**
     * 计算余额明细
     *
     * @param amontDetailRule 发生额明细规则
     */
    private void calculatAmont(D_OrderTypeAmontDetailRule amontDetailRule) throws EnumValueUndefinedException, AccountException {
        AmontRuleType amontRuleType = amontDetailRule.getRuletype();
        CalculateModeEnum calculateModeEnum = amontDetailRule.getCalculatemode();
        DecomposeTypeEnum decomposeTypeEnum = amontDetailRule.getDecomposetype();
        double basic;
        double amontBalance;
        switch (amontRuleType) {
            case amont:
                basic = orderAmont;
                amontBalance = balance;
                break;
            case actamont:
                basic = orderActAmont;
                amontBalance = actBalance;
                break;
            case difference:
                basic = diffAmont;
                amontBalance = diffBalance;
                break;
            case extamont1:
                basic = extamont1;
                amontBalance = extBalance1;
                break;
            case extamont2:
                basic = extamont2;
                amontBalance = extBalance2;
                break;
            case extamont3:
                basic = extamont3;
                amontBalance = extBalance3;
                break;
            case extamont4:
                basic = extamont4;
                amontBalance = extBalance4;
                break;
            case extamont5:
                basic = extamont5;
                amontBalance = extBalance5;
                break;
            case extamont6:
                basic = extamont6;
                amontBalance = extBalance6;
                break;
            case extamont7:
                basic = extamont7;
                amontBalance = extBalance7;
                break;
            case extamont8:
                basic = extamont8;
                amontBalance = extBalance8;
                break;
            case extamont9:
                basic = extamont9;
                amontBalance = extBalance9;
                break;
            case extamont10:
                basic = extamont10;
                amontBalance = extBalance10;
                break;
            default:
                throw new AccountException("发生额规则类型非法！");
        }
        if (amontDetailRule.getBasictype() > -1) {
            basic = ((D_OrderTypeAmontDetailRule) sortDetail.get(amontDetailRule.getBasictype())).getBalance();
        }
        double value = doCalculatBalance(basic, amontDetailRule);
        value = doFormat(value, amontDetailRule);
        if (calculateModeEnum.equals(CalculateModeEnum.calculate.getValue())) {
            if (decomposeTypeEnum.equals(DecomposeTypeEnum.inner.getValue())) {
                switch (amontRuleType) {
                    case amont:
                        balance -= value;
                        break;
                    case actamont:
                        actBalance -= value;
                        break;
                    case difference:
                        diffBalance -= value;
                        break;
                    case extamont1:
                        extBalance1 -= value;
                        break;
                    case extamont2:
                        extBalance2 -= value;
                        break;
                    case extamont3:
                        extBalance3 -= value;
                        break;
                    case extamont4:
                        extBalance4 -= value;
                        break;
                    case extamont5:
                        extBalance5 -= value;
                        break;
                    case extamont6:
                        extBalance6 -= value;
                        break;
                    case extamont7:
                        extBalance7 -= value;
                        break;
                    case extamont8:
                        extBalance8 -= value;
                        break;
                    case extamont9:
                        extBalance9 -= value;
                        break;
                    case extamont10:
                        extBalance10 -= value;
                        break;
                }
                amontBalance -= value;
            } else {
                switch (amontRuleType) {
                    case amont:
                        balance += value;
                        break;
                    case actamont:
                        actBalance += value;
                        break;
                    case difference:
                        diffBalance += value;
                        break;
                    case extamont1:
                        extBalance1 += value;
                        break;
                    case extamont2:
                        extBalance2 += value;
                        break;
                    case extamont3:
                        extBalance3 += value;
                        break;
                    case extamont4:
                        extBalance4 += value;
                        break;
                    case extamont5:
                        extBalance5 += value;
                        break;
                    case extamont6:
                        extBalance6 += value;
                        break;
                    case extamont7:
                        extBalance7 += value;
                        break;
                    case extamont8:
                        extBalance8 += value;
                        break;
                    case extamont9:
                        extBalance9 += value;
                        break;
                    case extamont10:
                        extBalance10 += value;
                        break;
                }
                amontBalance += value;
            }
        }
        amontDetailRule.setAmont(value);
        amontDetailRule.setBalance(amontBalance);
    }

    /**
     * 结算结果格式化
     *
     * @param amont           带计算数字
     * @param amontDetailRule 发生额明细规则
     * @return 目标数字
     */
    private double doFormat(double amont, D_OrderTypeAmontDetailRule amontDetailRule) throws EnumValueUndefinedException, AccountException {
        double result = BigDecimal.valueOf(amont).setScale(2, amontDetailRule.getDecimalprocess().getMode()).doubleValue();
        double min = amontDetailRule.getMin();
        double max = amontDetailRule.getMax();
        if (Double.compare(min, max) > 0) {
            throw new AccountException("发生额明细规则错误：最小值大于最大值");
        }
        if (Double.compare(min, 0.00D) > 0) {
            if (Double.compare(result, min) < 0) {
                result = min;
            }
        }
        if (Double.compare(max, 0.00D) > 0) {
            if (Double.compare(result, max) > 0) {
                result = max;
            }
        }
        return result;
    }

    /**
     * 计算余额结果
     *
     * @param basic           基数
     * @param amontDetailRule 发生额明细规则
     * @return 计算结果
     */
    private double doCalculatBalance(double basic, D_OrderTypeAmontDetailRule amontDetailRule) throws EnumValueUndefinedException, AccountException {
        AmontRuleType amontRuleType = amontDetailRule.getRuletype();
        double value;
        switch (amontDetailRule.getCalculatetype()) {
            case quota:
                value = amontDetailRule.getParam();
                break;
            case ratio:
                value = basic * amontDetailRule.getParam();
                break;
            case step:
                value = doCalculatStep(basic, amontDetailRule.getStepRules());
                break;
            case fullCut:
                value = doCalculatFullCutStep(basic, amontDetailRule.getStepRules());
                break;
            case balance:
                value = 0.00D;
                switch (amontRuleType) {
                    case amont:
                        value = balance;
                        break;
                    case actamont:
                        value = actBalance;
                        break;
                    case difference:
                        value = diffBalance;
                        break;
                    case extamont1:
                        value = extBalance1;
                        break;
                    case extamont2:
                        value = extBalance2;
                        break;
                    case extamont3:
                        value = extBalance3;
                        break;
                    case extamont4:
                        value = extBalance4;
                        break;
                    case extamont5:
                        value = extBalance5;
                        break;
                    case extamont6:
                        value = extBalance6;
                        break;
                    case extamont7:
                        value = extBalance7;
                        break;
                    case extamont8:
                        value = extBalance8;
                        break;
                    case extamont9:
                        value = extBalance9;
                        break;
                    case extamont10:
                        value = extBalance10;
                        break;
                }
                break;
            default:
                switch (amontRuleType) {
                    case amont:
                        value = orderAmont;
                        break;
                    case actamont:
                        value = orderActAmont;
                        break;
                    case difference:
                        value = diffAmont;
                        break;
                    case extamont1:
                        value = extamont1;
                        break;
                    case extamont2:
                        value = extamont2;
                        break;
                    case extamont3:
                        value = extamont3;
                        break;
                    case extamont4:
                        value = extamont4;
                        break;
                    case extamont5:
                        value = extamont5;
                        break;
                    case extamont6:
                        value = extamont6;
                        break;
                    case extamont7:
                        value = extamont7;
                        break;
                    case extamont8:
                        value = extamont8;
                        break;
                    case extamont9:
                        value = extamont9;
                        break;
                    case extamont10:
                        value = extamont10;
                        break;
                    default:
                        throw new AccountException("发生额规则类型非法！");
                }
        }
        return value;
    }

    /**
     * 执行阶梯计算
     *
     * @param basic                基数
     * @param amontDetailStepRules 阶梯规则
     * @return 计算结果
     */
    private double doCalculatStep(double basic, List<DBTable> amontDetailStepRules) throws AccountException, EnumValueUndefinedException {
        double result = 0;
        double lastEndAmont = 0;
        for (DBTable dbTable : amontDetailStepRules) {
            D_OrderTypeAmontDetailStepRule amontDetailStepRule = (D_OrderTypeAmontDetailStepRule) dbTable;
            double beginAmont = amontDetailStepRule.getBeginamont();
            double endAmont = amontDetailStepRule.getEndamont();
            if (Double.compare(beginAmont, lastEndAmont) != 0) {
                throw new AccountException("阶梯规则错误：开始额与上一条规则结束额不相等");
            }
            int comBegin = Double.compare(basic, beginAmont);
            if (comBegin >= 0) {
                double calculatNumber;
                if (Double.compare(endAmont, 0.00D) > 0) {
                    if (Double.compare(beginAmont, endAmont) > 0) {
                        throw new AccountException("阶梯规则错误：开始额大于结束额");
                    }
                    int comEnd = Double.compare(basic, endAmont);
                    if (comEnd <= 0) {
                        calculatNumber = basic - lastEndAmont;
                    } else {
                        calculatNumber = endAmont;
                    }
                } else {
                    calculatNumber = basic - lastEndAmont;
                }
                if (amontDetailStepRule.getCalculatetype().equals(CalculateTypeEnum.quota.getValue())) {
                    result += amontDetailStepRule.getParam();
                } else if (amontDetailStepRule.getCalculatetype().equals(CalculateTypeEnum.ratio.getValue())) {
                    result += calculatNumber * amontDetailStepRule.getParam();
                }
                lastEndAmont = endAmont;
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * 执行满减计算
     *
     * @param basic                基数
     * @param amontDetailStepRules 阶梯规则
     * @return 计算结果
     */
    private double doCalculatFullCutStep(double basic, List<DBTable> amontDetailStepRules) throws EnumValueUndefinedException, AccountException {
        double result = 0;
        double lastEndAmont = 0;
        for (DBTable dbTable : amontDetailStepRules) {
            D_OrderTypeAmontDetailStepRule amontDetailStepRule = (D_OrderTypeAmontDetailStepRule) dbTable;
            double beginAmont = amontDetailStepRule.getBeginamont();
            double endAmont = amontDetailStepRule.getEndamont();
            if (Double.compare(beginAmont, lastEndAmont) != 0) {
                throw new AccountException("阶梯规则错误：开始额与上一条规则结束额不相等");
            }
            int comBegin = Double.compare(basic, beginAmont);
            if (comBegin >= 0) {
                double calculatNumber;
                if (Double.compare(endAmont, 0.00D) > 0) {
                    if (Double.compare(beginAmont, endAmont) > 0) {
                        throw new AccountException("阶梯规则错误：开始额大于结束额");
                    }
                    int comEnd = Double.compare(basic, endAmont);
                    if (comEnd <= 0) {
                        calculatNumber = basic - lastEndAmont;
                    } else {
                        continue;
                    }
                } else {
                    calculatNumber = basic - lastEndAmont;
                }
                if (amontDetailStepRule.getCalculatetype().equals(CalculateTypeEnum.quota.getValue())) {
                    result += amontDetailStepRule.getParam();
                } else if (amontDetailStepRule.getCalculatetype().equals(CalculateTypeEnum.ratio.getValue())) {
                    result += calculatNumber * amontDetailStepRule.getParam();
                }
                lastEndAmont = endAmont;
            } else {
                break;
            }
        }
        return result;
    }

    /**
     * 设置余额/订单额类型的规则值
     */
    private void parseBalanceAndOrderAmont() throws EnumValueUndefinedException, AccountException {
        for (DBTable dbTable : amontDetailRules) {
            D_OrderTypeAmontDetailRule amontDetailRule = (D_OrderTypeAmontDetailRule) dbTable;
            AmontRuleType amontRuleType = amontDetailRule.getRuletype();
            double resultAmont = 0.00D;
            double resultBalance = 0.00D;
            switch (amontRuleType) {
                case amont:
                    resultAmont = orderAmont;
                    resultBalance = balance;
                    break;
                case actamont:
                    resultAmont = orderActAmont;
                    resultBalance = actBalance;
                    break;
                case difference:
                    resultAmont = diffAmont;
                    resultBalance = diffBalance;
                    break;
                case extamont1:
                    resultAmont = extamont1;
                    resultBalance = extBalance1;
                    break;
                case extamont2:
                    resultAmont = extamont2;
                    resultBalance = extBalance2;
                    break;
                case extamont3:
                    resultAmont = extamont3;
                    resultBalance = extBalance3;
                    break;
                case extamont4:
                    resultAmont = extamont4;
                    resultBalance = extBalance4;
                    break;
                case extamont5:
                    resultAmont = extamont5;
                    resultBalance = extBalance5;
                    break;
                case extamont6:
                    resultAmont = extamont6;
                    resultBalance = extBalance6;
                    break;
                case extamont7:
                    resultAmont = extamont7;
                    resultBalance = extBalance7;
                    break;
                case extamont8:
                    resultAmont = extamont8;
                    resultBalance = extBalance8;
                    break;
                case extamont9:
                    resultAmont = extamont9;
                    resultBalance = extBalance9;
                    break;
                case extamont10:
                    resultAmont = extamont10;
                    resultBalance = extBalance10;
                    break;
            }
            if (amontDetailRule.getCalculatetype().equals(CalculateTypeEnum.balance.getValue())) {
                amontDetailRule.setAmont(resultBalance);
                amontDetailRule.setBalance(resultBalance);
            } else if (amontDetailRule.getCalculatetype().equals(CalculateTypeEnum.orderAmont.getValue())) {
                amontDetailRule.setAmont(resultAmont);
                amontDetailRule.setBalance(resultAmont);
            }
        }
    }

    /**
     * 获取发生额明细
     *
     * @return 发生额明细
     */
    List<DBTable> getAmontDetailRules() {
        return amontDetailRules;
    }

    double getBalance() {
        return balance;
    }

    /**
     * 订单额
     */
    private double orderAmont;

    /**
     * 订单金额
     */
    private double orderActAmont;

    /**
     * 订单差额
     */
    private double diffAmont;

    /**
     * 扩展额1
     */
    private double extamont1;

    /**
     * 扩展额2
     */
    private double extamont2;

    /**
     * 扩展额3
     */
    private double extamont3;

    /**
     * 扩展额4
     */
    private double extamont4;

    /**
     * 扩展额5
     */
    private double extamont5;

    /**
     * 扩展额6
     */
    private double extamont6;

    /**
     * 扩展额7
     */
    private double extamont7;

    /**
     * 扩展额8
     */
    private double extamont8;

    /**
     * 扩展额9
     */
    private double extamont9;

    /**
     * 扩展额10
     */
    private double extamont10;

    /**
     * 发生额计算最终结果
     */
    private double balance;

    /**
     * 发生额计算最终结果
     */
    private double actBalance;

    /**
     * 差额计算最终结果
     */
    private double diffBalance;

    /**
     * 扩展额1计算最终结果
     */
    private double extBalance1;

    /**
     * 扩展额2计算最终结果
     */
    private double extBalance2;

    /**
     * 扩展额3计算最终结果
     */
    private double extBalance3;

    /**
     * 扩展额4计算最终结果
     */
    private double extBalance4;

    /**
     * 扩展额5计算最终结果
     */
    private double extBalance5;

    /**
     * 扩展额6计算最终结果
     */
    private double extBalance6;

    /**
     * 扩展额7计算最终结果
     */
    private double extBalance7;

    /**
     * 扩展额8计算最终结果
     */
    private double extBalance8;

    /**
     * 扩展额9计算最终结果
     */
    private double extBalance9;

    /**
     * 扩展额10计算最终结果
     */
    private double extBalance10;

    /**
     * 发生额明细规则
     */
    private List<DBTable> amontDetailRules;

    /**
     * 数据库连接对象
     */
    private ConnectionFactory connectionFactory;

    /**
     * 发生额明细，sort=>D_OrderTypeAmontDetailRule
     */
    private Map<Integer, DBTable> sortDetail;

}

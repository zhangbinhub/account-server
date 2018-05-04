package com.olink.account.trade.servers;

import com.olink.account.base.impl.BaseServer;
import com.olink.account.base.impl.BaseServerResult;
import com.olink.account.model.trade.order.*;
import com.olink.account.trade.servers.result.CustomerPayResult;
import com.olink.account.enumration.AccountTypeEnum;
import com.olink.account.enumration.CBindTypeEnum;
import com.olink.account.enumration.PayTypeEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.trade.servers.enumration.CustPayActionEnum;
import com.olink.account.trade.service.IMoneyService;
import com.olink.account.trade.service.impl.MoneyService;
import com.olink.account.utils.Utility;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import net.sf.json.JSONObject;

/**
 * Created by zhangbin on 2016/9/5.
 * C户信息维护、交易服务
 */
public class CustomerPayServer extends BaseServer {

    /**
     * 人民币服务
     */
    private IMoneyService moneyService = new MoneyService();

    /**
     * JSONString
     */
    private JSONObject data;

    public CustomerPayServer(HttpServletRequestAcp request) {
        super(request);
    }

    @Override
    public BaseServerResult doServer() throws ServerException {
        CustomerPayResult result;
        try {
            CustPayActionEnum action = CustPayActionEnum.getEnum(service.getAction());
            switch (action) {
                case cust_pay_recharge: //C户充值入账
                    result = custPayRecharge();
                    break;
                case cust_pay_applypayment: //C户支付给B户（申请）
                    result = custApplyPayPayment();
                    break;
                case cust_pay_payment: //C户支付给B户（确认）
                    result = custPayPayment();
                    break;
                case cust_pay_canclepayment: //C户支付给B户（取消）
                    result = custCanclePayPayment();
                    break;
                case cust_pay_applyrefund: //C户退款（申请）
                    result = custApplyPayRefund();
                    break;
                case cust_pay_refund: //C户退款（确认）
                    result = custPayRefund();
                    break;
                case cust_pay_canclerefund: //C户退款（取消）
                    result = custCanclePayRefund();
                    break;
                case cust_pay_applytransfer: //C户转账（申请）
                    result = custApplyTransfer();
                    break;
                case cust_pay_transfer: //C户转账（确认）
                    result = custTransfer();
                    break;
                case cust_pay_cancletransfer: //C户转账（取消）
                    result = custCancleTransfer();
                    break;
                case cust_pay_applycash: //C户余额提现（申请）
                    result = custApplyCash();
                    break;
                default:
                    result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "NO SUCH ACTION！");
                    break;
            }
        } catch (EnumValueUndefinedException e) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "NO SUCH ACTION！");
        }
        return result;
    }

    private CustomerPayResult custPayRecharge() throws ServerException {
        CustomerPayResult result;
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "充值金额不合法");
            }
        } else {
            return (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "充值金额为空");
        }
        BalanceRechargeOrder balanceRechargeOrder = CommonTools.jsonToBean(data, BalanceRechargeOrder.class);
        if (balanceRechargeOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balanceRechargeOrder.getBusinesstradeno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "商户交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceRechargeOrder.getCustid())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "用户客户号为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceRechargeOrder.getPaytype())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付类型为空");
            return result;
        }
        int comresult = Double.compare(balanceRechargeOrder.getAmont(), 0.01D);
        if (comresult < 0) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "充值金额最低为0.01元");
            return result;
        }
        if (CommonTools.isNullStr(balanceRechargeOrder.getTradeno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "第三方交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceRechargeOrder.getTradestatus())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "第三方交易状态为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceRechargeOrder.getBuyerid())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "购买人第三方id为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceRechargeOrder.getBuyername())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "购买人银行卡开户名称或第三方账户名称为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceRechargeOrder.getBuyeraccount())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "购买人第三方账号名为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        balanceRechargeOrder.setChannel(businessAccount.getChannel());
        if (moneyService.doApplyRecharge(balanceRechargeOrder)) {
            double[] balances = moneyService.doRecharge(balanceRechargeOrder);
            if (balances.length == 2) {
                result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
                result.setTotalbalance(balances[0]);
                result.setAvailablebalance(balances[1]);
            } else {
                moneyService.doCancleRecharge(balanceRechargeOrder.getOrderNo());
                result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "充值入账失败！");
            }
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "充值入账失败！");
        }
        return result;
    }

    private CustomerPayResult custApplyPayPayment() throws ServerException {
        String paytype = getPayTypeStr();
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付金额不合法");
            }
        } else {
            return (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付金额为空");
        }
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(paytype);
            switch (payTypeEnum) {
                case balance:
                    return doApplyBalancePay();
                default:
                    return doApplyNonBalancePay();
            }
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + paytype);
        }
    }

    private CustomerPayResult custPayPayment() throws ServerException {
        String paytype = getPayTypeStr();
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(paytype);
            switch (payTypeEnum) {
                case balance:
                    return doBalancePay();
                default:
                    return doNonBalancePay();
            }
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + paytype);
        }
    }

    private CustomerPayResult custCanclePayPayment() throws ServerException {
        String paytype = getPayTypeStr();
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(paytype);
            switch (payTypeEnum) {
                case balance:
                    return doCancleBalancePay();
                default:
                    return doCancleNonBalancePay();
            }
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + paytype);
        }
    }

    private CustomerPayResult custApplyPayRefund() throws ServerException {
        String paytype = getPayTypeStr();
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "退款金额不合法");
            }
        } else {
            return (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "退款金额为空");
        }
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(paytype);
            switch (payTypeEnum) {
                case balance:
                    return doApplyBalancePayRefund();
                default:
                    return doApplyNonBalancePayRefund();
            }
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + paytype);
        }
    }

    private CustomerPayResult custPayRefund() throws ServerException {
        String paytype = getPayTypeStr();
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(paytype);
            switch (payTypeEnum) {
                case balance:
                    return doBalancePayRefund();
                default:
                    return doNonBalancePayRefund();
            }
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + paytype);
        }
    }

    private CustomerPayResult custCanclePayRefund() throws ServerException {
        String paytype = getPayTypeStr();
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(paytype);
            switch (payTypeEnum) {
                case balance:
                    return doCancleBalancePayRefund();
                default:
                    return doCancleNonBalancePayRefund();
            }
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + paytype);
        }
    }

    private CustomerPayResult custApplyTransfer() throws ServerException {
        String paytype = getPayTypeStr();
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(paytype);
            switch (payTypeEnum) {
                case balance:
                    return doApplyBalanceTransfer();
                default:
                    return doApplyNonBalanceTransfer();
            }
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + paytype);
        }
    }

    private CustomerPayResult custTransfer() throws ServerException {
        String paytype = getPayTypeStr();
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(paytype);
            switch (payTypeEnum) {
                case balance:
                    return doBalanceTransfer();
                default:
                    return doNonBalanceTransfer();
            }
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + paytype);
        }
    }

    private CustomerPayResult custCancleTransfer() throws ServerException {
        String paytype = getPayTypeStr();
        try {
            PayTypeEnum payTypeEnum = PayTypeEnum.getEnum(paytype);
            switch (payTypeEnum) {
                case balance:
                    return doCancleBalanceTransfer();
                default:
                    return doCancleNonBalanceTransfer();
            }
        } catch (EnumValueUndefinedException e) {
            throw new ServerException("支付类型不匹配：" + paytype);
        }
    }

    private CustomerPayResult custApplyCash() throws ServerException {
        CustomerPayResult result;
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "提现金额不合法");
            }
        } else {
            return (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "提现金额为空");
        }
        BalanceCashOrder balanceCashOrder = CommonTools.jsonToBean(data, BalanceCashOrder.class);
        if (balanceCashOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balanceCashOrder.getBusinesstradeno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "商户交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceCashOrder.getCustid())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "用户客户号为空");
            return result;
        }
        int comresult = Double.compare(balanceCashOrder.getAmont(), 0.01D);
        if (comresult < 0) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "提现金额最低为0.01元");
            return result;
        }
        if (CommonTools.isNullStr(balanceCashOrder.getBindinfoid())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "客户提现账户为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceCashOrder.getPassword())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付密码为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        balanceCashOrder.setChannel(businessAccount.getChannel());
        if (moneyService.doApplyBalanceCash(balanceCashOrder)) {
            result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
            result.setMessage("提现申请成功！");
            result.setCustid(balanceCashOrder.getCustid());
            result.setOrderno(balanceCashOrder.getOrderNo());
            result.setAmont(balanceCashOrder.getAmont());
            result.setBeforebalance(balanceCashOrder.getBeforebalance());
            result.setAfterbalance(balanceCashOrder.getAfterbalance());
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "提现申请失败！");
        }
        return result;
    }

    private CustomerPayResult doApplyBalancePay() throws ServerException {
        CustomerPayResult result;
        BalancePayOrder balancePayOrder = CommonTools.jsonToBean(data, BalancePayOrder.class);
        if (balancePayOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balancePayOrder.getBusinesstradeno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "商户交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(balancePayOrder.getCustid())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "用户客户号为空");
            return result;
        }
        int comresult = Double.compare(balancePayOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付金额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(balancePayOrder.getPassword())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付密码为空");
            return result;
        }
        if (CommonTools.isNullStr(balancePayOrder.getDescription())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "订单描述为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        balancePayOrder.setBusinessid(businessAccount.getBusinessid());
        balancePayOrder.setBusinessname(businessAccount.getBusinessname());
        balancePayOrder.setBusinesssubaccountcode(businessAccount.getSubAccountByType(AccountTypeEnum.Change).getCode());
        balancePayOrder.setChannel(businessAccount.getChannel());
        if (moneyService.doApplyBalancePay(balancePayOrder)) {
            result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
            result.setMessage("支付申请成功！");
            result.setCustid(balancePayOrder.getCustid());
            result.setOrderno(balancePayOrder.getOrderNo());
            result.setAmont(balancePayOrder.getAmont());
            result.setBeforebalance(balancePayOrder.getBeforebalance());
            result.setAfterbalance(balancePayOrder.getAfterbalance());
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付申请失败！");
        }
        return result;
    }

    private CustomerPayResult doApplyNonBalancePay() throws ServerException {
        CustomerPayResult result;
        NonBalancePayOrder nonBalancePayOrder = CommonTools.jsonToBean(data, NonBalancePayOrder.class);
        if (nonBalancePayOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(nonBalancePayOrder.getBusinesstradeno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "商户交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalancePayOrder.getCustid())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "用户客户号为空");
            return result;
        }
        int comresult = Double.compare(nonBalancePayOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付金额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(nonBalancePayOrder.getDescription())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "订单描述为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        nonBalancePayOrder.setBusinessid(businessAccount.getBusinessid());
        nonBalancePayOrder.setBusinessname(businessAccount.getBusinessname());
        nonBalancePayOrder.setChannel(businessAccount.getChannel());
        if (moneyService.doApplyNonBalancePay(nonBalancePayOrder)) {
            result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
            result.setMessage("支付申请成功！");
            result.setCustid(nonBalancePayOrder.getCustid());
            result.setOrderno(nonBalancePayOrder.getOrderNo());
            result.setAmont(nonBalancePayOrder.getAmont());
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付申请失败！");
        }
        return result;
    }

    private CustomerPayResult doBalancePay() throws ServerException {
        CustomerPayResult result;
        BalancePayOrder balancePayOrder = CommonTools.jsonToBean(data, BalancePayOrder.class);
        if (balancePayOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balancePayOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "账务系统订单号为空");
            return result;
        }
        if (CommonTools.isNullStr(balancePayOrder.getPassword())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付密码为空");
            return result;
        }
        if (moneyService.doBalancePay(balancePayOrder)) {
            result = getPayResult(balancePayOrder);
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付失败！");
        }
        return result;
    }

    private CustomerPayResult doNonBalancePay() throws ServerException {
        CustomerPayResult result;
        NonBalancePayOrder nonBalancePayOrder = CommonTools.jsonToBean(data, NonBalancePayOrder.class);
        if (nonBalancePayOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(nonBalancePayOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "账务系统订单号为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalancePayOrder.getTradeno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "第三方交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalancePayOrder.getTradestatus())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "第三方交易状态为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalancePayOrder.getBuyerid())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "购买人第三方id为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalancePayOrder.getBuyername())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "购买人银行卡开户名称或第三方账户名称为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalancePayOrder.getBuyeraccount())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "购买人第三方账号名为空");
            return result;
        }
        if (moneyService.doNonBalancePay(nonBalancePayOrder)) {
            result = getPayResult(nonBalancePayOrder);
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付失败！");
        }
        return result;
    }

    private CustomerPayResult doCancleBalancePay() throws ServerException {
        CustomerPayResult result;
        BalancePayOrder balancePayOrder = CommonTools.jsonToBean(data, BalancePayOrder.class);
        if (balancePayOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balancePayOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "账务系统订单号为空");
            return result;
        }
        if (moneyService.doCancleBalancePay(balancePayOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
            result.setMessage("支付订单取消成功！");
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付取消失败！");
        }
        return result;
    }

    private CustomerPayResult doCancleNonBalancePay() throws ServerException {
        CustomerPayResult result;
        NonBalancePayOrder nonBalancePayOrder = CommonTools.jsonToBean(data, NonBalancePayOrder.class);
        if (nonBalancePayOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(nonBalancePayOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "账务系统订单号为空");
            return result;
        }
        if (moneyService.doCancleNonBalancePay(nonBalancePayOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
            result.setMessage("支付订单取消成功！");
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付取消失败！");
        }
        return result;
    }

    private CustomerPayResult doApplyBalancePayRefund() throws ServerException {
        CustomerPayResult result;
        BalanceRefundOrder balanceRefundOrder = CommonTools.jsonToBean(data, BalanceRefundOrder.class);
        if (balanceRefundOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balanceRefundOrder.getOrigorderno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "原支付订单号为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceRefundOrder.getBusinesstradeno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "商户交易号为空");
            return result;
        }
        int comresult = Double.compare(balanceRefundOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "退款金额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(balanceRefundOrder.getReason())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "退款原因为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        balanceRefundOrder.setBusinessid(businessAccount.getBusinessid());
        if (moneyService.doApplyBalancePayRefund(balanceRefundOrder)) {
            result = getRefundResult(balanceRefundOrder);
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "退款申请失败！");
        }
        return result;
    }

    private CustomerPayResult doApplyNonBalancePayRefund() throws ServerException {
        CustomerPayResult result;
        NonBalanceRefundOrder nonBalanceRefundOrder = CommonTools.jsonToBean(data, NonBalanceRefundOrder.class);
        if (nonBalanceRefundOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceRefundOrder.getOrigorderno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "原支付订单号为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceRefundOrder.getOrigtradeno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "原第三方交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceRefundOrder.getBusinesstradeno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "商户交易号为空");
            return result;
        }
        int comresult = Double.compare(nonBalanceRefundOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "退款金额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceRefundOrder.getReason())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "退款原因为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        nonBalanceRefundOrder.setBusinessid(businessAccount.getBusinessid());
        if (moneyService.doApplyNonBalancePayRefund(nonBalanceRefundOrder)) {
            result = getRefundResult(nonBalanceRefundOrder);
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "退款申请失败！");
        }
        return result;
    }

    private CustomerPayResult doBalancePayRefund() throws ServerException {
        CustomerPayResult result;
        BalanceRefundOrder balanceRefundOrder = CommonTools.jsonToBean(data, BalanceRefundOrder.class);
        if (balanceRefundOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balanceRefundOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "账务系统订单号为空");
            return result;
        }
        if (moneyService.doBalancePayRefund(balanceRefundOrder)) {
            result = getRefundResult(balanceRefundOrder);
            result.setMessage("退款成功！");
            result.setBeforebalance(balanceRefundOrder.getBeforebalance());
            result.setAfterbalance(balanceRefundOrder.getAfterbalance());
            result.setFinishdate(balanceRefundOrder.getLastmodifydate());
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "退款失败！");
        }
        return result;
    }

    private CustomerPayResult doNonBalancePayRefund() throws ServerException {
        CustomerPayResult result;
        NonBalanceRefundOrder nonBalanceRefundOrder = CommonTools.jsonToBean(data, NonBalanceRefundOrder.class);
        if (nonBalanceRefundOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceRefundOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "账务系统订单号为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceRefundOrder.getTradeno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "第三方交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceRefundOrder.getTradestatus())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "第三方交易状态为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceRefundOrder.getBuyerid())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "购买人第三方id为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceRefundOrder.getBuyername())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "购买人银行卡开户名称或第三方账户名称为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceRefundOrder.getBuyeraccount())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "购买人第三方账号名为空");
            return result;
        }
        if (moneyService.doNonBalancePayRefund(nonBalanceRefundOrder)) {
            result = getRefundResult(nonBalanceRefundOrder);
            result.setMessage("退款成功！");
            result.setFinishdate(nonBalanceRefundOrder.getLastmodifydate());
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "退款失败！");
        }
        return result;
    }

    private CustomerPayResult doCancleBalancePayRefund() throws ServerException {
        CustomerPayResult result;
        BalanceRefundOrder balanceRefundOrder = CommonTools.jsonToBean(data, BalanceRefundOrder.class);
        if (balanceRefundOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balanceRefundOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "账务系统订单号为空");
            return result;
        }
        if (moneyService.doCancleBalancePayRefund(balanceRefundOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
            result.setMessage("退款订单取消成功！");
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "退款取消失败！");
        }
        return result;
    }

    private CustomerPayResult doCancleNonBalancePayRefund() throws ServerException {
        CustomerPayResult result;
        NonBalanceRefundOrder nonBalanceRefundOrder = CommonTools.jsonToBean(data, NonBalanceRefundOrder.class);
        if (nonBalanceRefundOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceRefundOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "账务系统订单号为空");
            return result;
        }
        if (moneyService.doCancleNonBalancePayRefund(nonBalanceRefundOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
            result.setMessage("退款订单取消成功！");
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "退款取消失败！");
        }
        return result;
    }

    private CustomerPayResult doApplyBalanceTransfer() throws ServerException {
        CustomerPayResult result;
        BalanceTransferOrder balanceTransferOrder = CommonTools.jsonToBean(data, BalanceTransferOrder.class);
        if (balanceTransferOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balanceTransferOrder.getBusinesstradeno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "商户交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceTransferOrder.getCustid())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "用户客户号为空");
            return result;
        }
        int comresult = Double.compare(balanceTransferOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "转账金额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(balanceTransferOrder.getPassword())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "支付密码为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceTransferOrder.getRececustid())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "收款方客户号为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        balanceTransferOrder.setBusinessid(businessAccount.getBusinessid());
        balanceTransferOrder.setBusinessname(businessAccount.getBusinessname());
        balanceTransferOrder.setChannel(businessAccount.getChannel());
        balanceTransferOrder.setDescription("余额转账");
        if (moneyService.doApplyBalanceTransfer(balanceTransferOrder)) {
            result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
            result.setMessage("转账申请成功！");
            result.setCustid(balanceTransferOrder.getCustid());
            result.setRececustid(balanceTransferOrder.getRececustid());
            result.setOrderno(balanceTransferOrder.getOrderNo());
            result.setAmont(balanceTransferOrder.getAmont());
            result.setBeforebalance(balanceTransferOrder.getBeforebalance());
            result.setAfterbalance(balanceTransferOrder.getAfterbalance());
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "转账申请失败！");
        }
        return result;
    }

    private CustomerPayResult doApplyNonBalanceTransfer() throws ServerException {
        CustomerPayResult result;
        NonBalanceTransferOrder nonBalanceTransferOrder = CommonTools.jsonToBean(data, NonBalanceTransferOrder.class);
        if (nonBalanceTransferOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceTransferOrder.getBusinesstradeno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "商户交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceTransferOrder.getCustid())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "用户客户号为空");
            return result;
        }
        int comresult = Double.compare(nonBalanceTransferOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "转账金额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceTransferOrder.getRecetype())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "收款人账户类型为空");
            return result;
        } else {
            try {
                CBindTypeEnum.getEnum(nonBalanceTransferOrder.getRecetype());
            } catch (Exception e) {
                result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "收款人账户类型不匹配");
                return result;
            }
        }
        if (CommonTools.isNullStr(nonBalanceTransferOrder.getRecebankname())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "收款人开户行为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceTransferOrder.getRecename())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "开户行为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceTransferOrder.getReceaccount())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "收款人第三方账号名为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        nonBalanceTransferOrder.setBusinessid(businessAccount.getBusinessid());
        nonBalanceTransferOrder.setBusinessname(businessAccount.getBusinessname());
        nonBalanceTransferOrder.setChannel(businessAccount.getChannel());
        nonBalanceTransferOrder.setDescription("非余额转账");
        if (moneyService.doApplyNonBalanceTransfer(nonBalanceTransferOrder)) {
            result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
            result.setMessage("转账申请成功！");
            result.setCustid(nonBalanceTransferOrder.getCustid());
            result.setOrderno(nonBalanceTransferOrder.getOrderNo());
            result.setAmont(nonBalanceTransferOrder.getAmont());
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "转账申请失败！");
        }
        return result;
    }

    private CustomerPayResult doBalanceTransfer() throws ServerException {
        CustomerPayResult result;
        BalanceTransferOrder balanceTransferOrder = CommonTools.jsonToBean(data, BalanceTransferOrder.class);
        if (balanceTransferOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balanceTransferOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "账务系统订单号为空");
            return result;
        }
        if (moneyService.doBalanceTransfer(balanceTransferOrder)) {
            result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
            result.setMessage("转账成功！");
            result.setCustid(balanceTransferOrder.getCustid());
            result.setRececustid(balanceTransferOrder.getRececustid());
            result.setOrderno(balanceTransferOrder.getOrderNo());
            result.setAmont(balanceTransferOrder.getAmont());
            result.setFinishdate(balanceTransferOrder.getLastmodifydate());
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "转账失败！");
        }
        return result;
    }

    private CustomerPayResult doNonBalanceTransfer() throws ServerException {
        CustomerPayResult result;
        NonBalanceTransferOrder nonBalanceTransferOrder = CommonTools.jsonToBean(data, NonBalanceTransferOrder.class);
        if (nonBalanceTransferOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceTransferOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "账务系统订单号为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceTransferOrder.getTradeno())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "第三方交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceTransferOrder.getTradestatus())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "第三方交易状态为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceTransferOrder.getBuyerid())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "购买人第三方id为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceTransferOrder.getBuyername())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "购买人银行卡开户名称或第三方账户名称为空");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceTransferOrder.getBuyeraccount())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "购买人第三方账号名为空");
            return result;
        }
        if (moneyService.doNonBalanceTransfer(nonBalanceTransferOrder)) {
            result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
            result.setMessage("转账成功！");
            result.setCustid(nonBalanceTransferOrder.getCustid());
            result.setOrderno(nonBalanceTransferOrder.getOrderNo());
            result.setAmont(nonBalanceTransferOrder.getAmont());
            result.setFinishdate(nonBalanceTransferOrder.getLastmodifydate());
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "转账失败！");
        }
        return result;
    }

    private CustomerPayResult doCancleBalanceTransfer() throws ServerException {
        CustomerPayResult result;
        BalanceTransferOrder balanceTransferOrder = CommonTools.jsonToBean(data, BalanceTransferOrder.class);
        if (balanceTransferOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balanceTransferOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "账务系统订单号为空");
            return result;
        }
        if (moneyService.doCancleBalanceTransfer(balanceTransferOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
            result.setMessage("转账订单取消成功！");
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "转账取消失败！");
        }
        return result;
    }

    private CustomerPayResult doCancleNonBalanceTransfer() throws ServerException {
        CustomerPayResult result;
        NonBalanceTransferOrder nonBalanceTransferOrder = CommonTools.jsonToBean(data, NonBalanceTransferOrder.class);
        if (nonBalanceTransferOrder == null) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(nonBalanceTransferOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "账务系统订单号为空");
            return result;
        }
        if (moneyService.doCancleNonBalanceTransfer(nonBalanceTransferOrder.getOrderNo())) {
            result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
            result.setMessage("转账订单取消成功！");
        } else {
            result = (CustomerPayResult) Utility.getFailedResult(CustomerPayResult.class, "转账取消失败！");
        }
        return result;
    }

    private String getPayTypeStr() throws ServerException {
        String paytype;
        if (data.containsKey("paytype")) {
            paytype = data.getString("paytype");
            if (CommonTools.isNullStr(paytype)) {
                throw new ServerException("支付类型为空");
            }
        } else {
            throw new ServerException("支付类型为空");
        }
        return paytype;
    }

    private CustomerPayResult getPayResult(OrderBase payOrder) {
        CustomerPayResult result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
        result.setMessage("支付成功！");
        result.setCustid(payOrder.getCustid());
        result.setOrderno(payOrder.getOrderNo());
        result.setAmont(payOrder.getAmont());
        result.setFinishdate(payOrder.getLastmodifydate());
        return result;
    }

    private CustomerPayResult getRefundResult(OrderBase refundOrder) {
        CustomerPayResult result = (CustomerPayResult) Utility.getDefaultSuccussResult(CustomerPayResult.class);
        result.setMessage("退款申请成功！");
        result.setSuramont(refundOrder.getSuramont());
        result.setCustid(refundOrder.getCustid());
        result.setOrderno(refundOrder.getOrderNo());
        result.setAmont(refundOrder.getAmont());
        return result;
    }

}

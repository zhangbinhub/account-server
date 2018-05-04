package com.olink.account.trade.servers;

import com.olink.account.base.impl.BaseServer;
import com.olink.account.base.impl.BaseServerResult;
import com.olink.account.trade.servers.result.CurrencyResult;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.order.CustomCurrencyPayOrder;
import com.olink.account.model.trade.order.CustomCurrencyRechargeOrder;
import com.olink.account.model.trade.order.CustomCurrencyRefundOrder;
import com.olink.account.model.trade.order.OrderBase;
import com.olink.account.trade.servers.enumration.CurrencyActionEnum;
import com.olink.account.trade.service.ICurrencyService;
import com.olink.account.trade.service.impl.CurrencyService;
import com.olink.account.utils.Utility;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import net.sf.json.JSONObject;

/**
 * Created by zhangbin on 2016/10/4.
 * 自有币种交易接口
 */
public class CurrencyServer extends BaseServer {

    /**
     * 自有币种服务
     */
    private ICurrencyService currencyService = new CurrencyService();

    /**
     * JSONString
     */
    private JSONObject data;

    public CurrencyServer(HttpServletRequestAcp request) {
        super(request);
    }

    @Override
    public BaseServerResult doServer() throws ServerException {
        CurrencyResult result;
        try {
            CurrencyActionEnum action = CurrencyActionEnum.getEnum(service.getAction());
            switch (action) {
                case cust_currency_applyrecharge: //申请入账
                    result = doApplyRecharge();
                    break;
                case cust_currency_recharge: //确认入账
                    result = doRecharge();
                    break;
                case cust_currency_canclerecharge: //取消入账
                    result = doCancleRecharge();
                    break;
                case cust_currency_applypayment: //申请消费
                    result = doApplyPayment();
                    break;
                case cust_currency_payment: //确认消费
                    result = doPayment();
                    break;
                case cust_currency_canclepayment: //取消消费
                    result = doCanclePayment();
                    break;
                case cust_currency_applyrefund: //申请退款
                    result = doApplyRefund();
                    break;
                case cust_currency_refund: //确认退款
                    result = doRefund();
                    break;
                case cust_currency_canclerefund: //取消退款
                    result = doCancleRefund();
                    break;
                default:
                    result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "NO SUCH ACTION！");
                    break;
            }
        } catch (EnumValueUndefinedException e) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "NO SUCH ACTION！");
        }
        return result;
    }

    private CurrencyResult doApplyRecharge() throws ServerException {
        CurrencyResult result;
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "入账额不合法");
            }
        } else {
            return (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "入账额为空");
        }
        CustomCurrencyRechargeOrder customCurrencyRechargeOrder = CommonTools.jsonToBean(data, CustomCurrencyRechargeOrder.class);
        if (customCurrencyRechargeOrder == null) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyRechargeOrder.getBusinesstradeno())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "商户交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyRechargeOrder.getCustid())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "用户客户号为空");
            return result;
        }
        int comresult = Double.compare(customCurrencyRechargeOrder.getAmont(), 0.01D);
        if (comresult < 0) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "入账额最低为0.01");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyRechargeOrder.getDescription())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "订单描述为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        customCurrencyRechargeOrder.setBusinessid(businessAccount.getBusinessid());
        customCurrencyRechargeOrder.setBusinessname(businessAccount.getBusinessname());
        customCurrencyRechargeOrder.setChannel(businessAccount.getChannel());
        if (currencyService.doApplyCurrencyRecharge(customCurrencyRechargeOrder)) {
            result = getResult(customCurrencyRechargeOrder);
            result.setCurrencyname(customCurrencyRechargeOrder.getCurrencyname());
            result.setMessage("入账申请成功！");
        } else {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "入账申请失败！");
        }
        return result;
    }

    private CurrencyResult doRecharge() throws ServerException {
        CurrencyResult result;
        CustomCurrencyRechargeOrder customCurrencyRechargeOrder = CommonTools.jsonToBean(data, CustomCurrencyRechargeOrder.class);
        if (customCurrencyRechargeOrder == null) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyRechargeOrder.getOrderNo())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "账务系统订单号为空");
            return result;
        }
        if (currencyService.doCurrencyRecharge(customCurrencyRechargeOrder)) {
            result = getResult(customCurrencyRechargeOrder);
            result.setCurrencyname(customCurrencyRechargeOrder.getCurrencyname());
            result.setMessage("入账成功！");
            result.setFinishdate(customCurrencyRechargeOrder.getLastmodifydate());
        } else {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "入账失败！");
        }
        return result;
    }

    private CurrencyResult doCancleRecharge() throws ServerException {
        CurrencyResult result;
        CustomCurrencyRechargeOrder customCurrencyRechargeOrder = CommonTools.jsonToBean(data, CustomCurrencyRechargeOrder.class);
        if (customCurrencyRechargeOrder == null) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyRechargeOrder.getOrderNo())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "账务系统订单号为空");
            return result;
        }
        if (currencyService.doCancleCurrencyRecharge(customCurrencyRechargeOrder.getOrderNo())) {
            result = (CurrencyResult) Utility.getDefaultSuccussResult(CurrencyResult.class);
            result.setMessage("入账订单取消成功！");
        } else {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "取消入账失败！");
        }
        return result;
    }

    private CurrencyResult doApplyPayment() throws ServerException {
        CurrencyResult result;
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "消费额不合法");
            }
        } else {
            return (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "消费额为空");
        }
        CustomCurrencyPayOrder customCurrencyPayOrder = CommonTools.jsonToBean(data, CustomCurrencyPayOrder.class);
        if (customCurrencyPayOrder == null) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyPayOrder.getBusinesstradeno())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "商户交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyPayOrder.getCustid())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "用户客户号为空");
            return result;
        }
        int comresult = Double.compare(customCurrencyPayOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "消费额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyPayOrder.getDescription())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "订单描述为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        customCurrencyPayOrder.setBusinessid(businessAccount.getBusinessid());
        customCurrencyPayOrder.setBusinessname(businessAccount.getBusinessname());
        customCurrencyPayOrder.setChannel(businessAccount.getChannel());
        if (currencyService.doApplyCurrencyPayment(customCurrencyPayOrder)) {
            result = getResult(customCurrencyPayOrder);
            result.setCurrencyname(customCurrencyPayOrder.getCurrencyname());
            result.setMessage("消费申请成功！");
        } else {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "消费申请失败！");
        }
        return result;
    }

    private CurrencyResult doPayment() throws ServerException {
        CurrencyResult result;
        CustomCurrencyPayOrder customCurrencyPayOrder = CommonTools.jsonToBean(data, CustomCurrencyPayOrder.class);
        if (customCurrencyPayOrder == null) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyPayOrder.getOrderNo())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "账务系统订单号为空");
            return result;
        }
        if (currencyService.doCurrencyPayment(customCurrencyPayOrder)) {
            result = getResult(customCurrencyPayOrder);
            result.setCurrencyname(customCurrencyPayOrder.getCurrencyname());
            result.setMessage("消费成功！");
            result.setFinishdate(customCurrencyPayOrder.getLastmodifydate());
        } else {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "入账失败！");
        }
        return result;
    }

    private CurrencyResult doCanclePayment() throws ServerException {
        CurrencyResult result;
        CustomCurrencyPayOrder customCurrencyPayOrder = CommonTools.jsonToBean(data, CustomCurrencyPayOrder.class);
        if (customCurrencyPayOrder == null) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyPayOrder.getOrderNo())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "账务系统订单号为空");
            return result;
        }
        if (currencyService.doCancleCurrencyPayment(customCurrencyPayOrder.getOrderNo())) {
            result = (CurrencyResult) Utility.getDefaultSuccussResult(CurrencyResult.class);
            result.setMessage("消费订单取消成功！");
        } else {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "取消消费失败！");
        }
        return result;
    }

    private CurrencyResult doApplyRefund() throws ServerException {
        CurrencyResult result;
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "退款额不合法");
            }
        } else {
            return (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "退款额为空");
        }
        CustomCurrencyRefundOrder customCurrencyRefundOrder = CommonTools.jsonToBean(data, CustomCurrencyRefundOrder.class);
        if (customCurrencyRefundOrder == null) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyRefundOrder.getBusinesstradeno())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "商户交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyRefundOrder.getOrigorderno())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "原消费订单号为空");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyRefundOrder.getReason())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "退款原因为空");
            return result;
        }
        int comresult = Double.compare(customCurrencyRefundOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "退款额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyRefundOrder.getDescription())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "订单描述为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        customCurrencyRefundOrder.setBusinessid(businessAccount.getBusinessid());
        customCurrencyRefundOrder.setBusinessname(businessAccount.getBusinessname());
        customCurrencyRefundOrder.setChannel(businessAccount.getChannel());
        if (currencyService.doApplyCurrencyRefund(customCurrencyRefundOrder)) {
            result = getResult(customCurrencyRefundOrder);
            result.setCurrencyname(customCurrencyRefundOrder.getCurrencyname());
            result.setSuramont(customCurrencyRefundOrder.getSuramont());
            result.setMessage("退款申请成功！");
        } else {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "退款申请失败！");
        }
        return result;
    }

    private CurrencyResult doRefund() throws ServerException {
        CurrencyResult result;
        CustomCurrencyRefundOrder customCurrencyRefundOrder = CommonTools.jsonToBean(data, CustomCurrencyRefundOrder.class);
        if (customCurrencyRefundOrder == null) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyRefundOrder.getOrderNo())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "账务系统订单号为空");
            return result;
        }
        if (currencyService.doCurrencyRefund(customCurrencyRefundOrder)) {
            result = getResult(customCurrencyRefundOrder);
            result.setCurrencyname(customCurrencyRefundOrder.getCurrencyname());
            result.setSuramont(customCurrencyRefundOrder.getSuramont());
            result.setMessage("退款成功！");
            result.setFinishdate(customCurrencyRefundOrder.getLastmodifydate());
        } else {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "退款失败！");
        }
        return result;
    }

    private CurrencyResult doCancleRefund() throws ServerException {
        CurrencyResult result;
        CustomCurrencyRefundOrder customCurrencyRefundOrder = CommonTools.jsonToBean(data, CustomCurrencyRefundOrder.class);
        if (customCurrencyRefundOrder == null) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(customCurrencyRefundOrder.getOrderNo())) {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "账务系统订单号为空");
            return result;
        }
        if (currencyService.doCancleCurrencyRefund(customCurrencyRefundOrder.getOrderNo())) {
            result = (CurrencyResult) Utility.getDefaultSuccussResult(CurrencyResult.class);
            result.setMessage("退款订单取消成功！");
        } else {
            result = (CurrencyResult) Utility.getFailedResult(CurrencyResult.class, "取消退款失败！");
        }
        return result;
    }

    private CurrencyResult getResult(OrderBase order) {
        CurrencyResult result = (CurrencyResult) Utility.getDefaultSuccussResult(CurrencyResult.class);
        result.setCustid(order.getCustid());
        result.setOrderno(order.getOrderNo());
        result.setAmont(order.getAmont());
        return result;
    }

}

package com.olink.account.trade.servers;

import com.olink.account.base.impl.BaseServer;
import com.olink.account.base.impl.BaseServerResult;
import com.olink.account.enumration.AccountTypeEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.order.IntegralPayOrder;
import com.olink.account.model.trade.order.IntegralRefundOrder;
import com.olink.account.trade.servers.enumration.IntegralActionEnum;
import com.olink.account.trade.servers.result.IntegralResult;
import com.olink.account.trade.service.IIntegralService;
import com.olink.account.trade.service.impl.IntegralService;
import com.olink.account.utils.Utility;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import net.sf.json.JSONObject;

/**
 * Created by zhangbin on 2016/10/7.
 * 积分交易接口
 */
public class IntegralServer extends BaseServer {

    /**
     * 积分服务
     */
    private IIntegralService integralService = new IntegralService();

    /**
     * JSONString
     */
    private JSONObject data;

    public IntegralServer(HttpServletRequestAcp request) {
        super(request);
    }

    @Override
    public BaseServerResult doServer() throws ServerException {
        IntegralResult result;
        try {
            IntegralActionEnum action = IntegralActionEnum.getEnum(service.getAction());
            switch (action) {
                case cust_integral_applypayment: //申请消费
                    result = doApplyPayment();
                    break;
                case cust_integral_payment: //确认消费
                    result = doPayment();
                    break;
                case cust_integral_canclepayment: //取消消费
                    result = doCanclePayment();
                    break;
                case cust_integral_applyrefund: //申请退款
                    result = doApplyRefund();
                    break;
                case cust_integral_refund: //确认退款
                    result = doRefund();
                    break;
                case cust_integral_canclerefund: //取消退款
                    result = doCancleRefund();
                    break;
                default:
                    result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "NO SUCH ACTION！");
                    break;
            }
        } catch (EnumValueUndefinedException e) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "NO SUCH ACTION！");
        }
        return result;
    }

    private IntegralResult doApplyPayment() throws ServerException {
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (IntegralResult) Utility.getFailedResult(IntegralResult.class, "消费额不合法");
            }
        } else {
            return (IntegralResult) Utility.getFailedResult(IntegralResult.class, "消费额为空");
        }
        IntegralResult result;
        IntegralPayOrder payOrder = CommonTools.jsonToBean(data, IntegralPayOrder.class);
        if (payOrder == null) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(payOrder.getBusinesstradeno())) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "商户交易号为空");
            return result;
        }
        if (CommonTools.isNullStr(payOrder.getCustid())) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "用户客户号为空");
            return result;
        }
        int comresult = Double.compare(payOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "消费额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(payOrder.getDescription())) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "订单描述为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        payOrder.setBusinessid(businessAccount.getBusinessid());
        payOrder.setBusinessname(businessAccount.getBusinessname());
        payOrder.setBusinesssubaccountcode(businessAccount.getSubAccountByType(AccountTypeEnum.Change).getCode());
        payOrder.setChannel(businessAccount.getChannel());
        if (integralService.doApplyPayment(payOrder)) {
            result = (IntegralResult) Utility.getDefaultSuccussResult(IntegralResult.class);
            result.setMessage("消费申请成功！");
            result.setCustid(payOrder.getCustid());
            result.setOrderno(payOrder.getOrderNo());
            result.setAmont(payOrder.getAmont());
            result.setBeforebalance(payOrder.getBeforebalance());
            result.setAfterbalance(payOrder.getAfterbalance());
        } else {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "消费申请失败！");
        }
        return result;
    }

    private IntegralResult doPayment() throws ServerException {
        IntegralResult result;
        IntegralPayOrder payOrder = CommonTools.jsonToBean(data, IntegralPayOrder.class);
        if (payOrder == null) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(payOrder.getOrderNo())) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "账务系统订单号为空");
            return result;
        }
        if (integralService.doPayment(payOrder)) {
            result = (IntegralResult) Utility.getDefaultSuccussResult(IntegralResult.class);
            result.setMessage("消费成功！");
            result.setCustid(payOrder.getCustid());
            result.setOrderno(payOrder.getOrderNo());
            result.setAmont(payOrder.getAmont());
            result.setFinishdate(payOrder.getLastmodifydate());
        } else {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "消费失败！");
        }
        return result;
    }

    private IntegralResult doCanclePayment() throws ServerException {
        IntegralResult result;
        IntegralPayOrder payOrder = CommonTools.jsonToBean(data, IntegralPayOrder.class);
        if (payOrder == null) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(payOrder.getOrderNo())) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "账务系统订单号为空");
            return result;
        }
        if (integralService.doCanclePayment(payOrder.getOrderNo())) {
            result = (IntegralResult) Utility.getDefaultSuccussResult(IntegralResult.class);
            result.setMessage("消费订单取消成功！");
        } else {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "支付取消失败！");
        }
        return result;
    }

    private IntegralResult doApplyRefund() throws ServerException {
        IntegralResult result;
        IntegralRefundOrder refundOrder = CommonTools.jsonToBean(data, IntegralRefundOrder.class);
        if (refundOrder == null) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(refundOrder.getOrigorderno())) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "原消费订单号为空");
            return result;
        }
        if (CommonTools.isNullStr(refundOrder.getBusinesstradeno())) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "商户交易号为空");
            return result;
        }
        int comresult = Double.compare(refundOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "退款额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(refundOrder.getReason())) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "退款原因为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        refundOrder.setBusinessid(businessAccount.getBusinessid());
        if (integralService.doApplyRefund(refundOrder)) {
            result = (IntegralResult) Utility.getDefaultSuccussResult(IntegralResult.class);
            result.setMessage("退款申请成功！");
            result.setCustid(refundOrder.getCustid());
            result.setOrderno(refundOrder.getOrderNo());
            result.setAmont(refundOrder.getAmont());
        } else {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "退款申请失败！");
        }
        return result;
    }

    private IntegralResult doRefund() throws ServerException {
        IntegralResult result;
        IntegralRefundOrder refundOrder = CommonTools.jsonToBean(data, IntegralRefundOrder.class);
        if (refundOrder == null) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(refundOrder.getOrderNo())) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "账务系统订单号为空");
            return result;
        }
        if (integralService.doRefund(refundOrder)) {
            result = (IntegralResult) Utility.getDefaultSuccussResult(IntegralResult.class);
            result.setCustid(refundOrder.getCustid());
            result.setOrderno(refundOrder.getOrderNo());
            result.setAmont(refundOrder.getAmont());
            result.setMessage("退款成功！");
            result.setBeforebalance(refundOrder.getBeforebalance());
            result.setAfterbalance(refundOrder.getAfterbalance());
            result.setFinishdate(refundOrder.getLastmodifydate());
        } else {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "退款失败！");
        }
        return result;
    }

    private IntegralResult doCancleRefund() throws ServerException {
        IntegralResult result;
        IntegralRefundOrder refundOrder = CommonTools.jsonToBean(data, IntegralRefundOrder.class);
        if (refundOrder == null) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(refundOrder.getOrderNo())) {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "账务系统订单号为空");
            return result;
        }
        if (integralService.doCancleRefund(refundOrder.getOrderNo())) {
            result = (IntegralResult) Utility.getDefaultSuccussResult(IntegralResult.class);
            result.setMessage("退款订单取消成功！");
        } else {
            result = (IntegralResult) Utility.getFailedResult(IntegralResult.class, "退款取消失败！");
        }
        return result;
    }

}

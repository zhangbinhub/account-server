package com.olink.account.trade.servers;

import com.olink.account.base.impl.BaseBackServer;
import com.olink.account.base.impl.BaseServerResult;
import com.olink.account.enumration.PayTypeEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.accounting.EntryItemParam;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.order.BalanceSettlementOrder;
import com.olink.account.model.trade.order.IntegralSettlementOrder;
import com.olink.account.model.trade.order.InternalTradeOrder;
import com.olink.account.model.trade.order.NormalTradeOrder;
import com.olink.account.service.IBusinessService;
import com.olink.account.service.impl.BusinessService;
import com.olink.account.trade.servers.enumration.InnerActionEnum;
import com.olink.account.trade.servers.result.BackInnerResult;
import com.olink.account.trade.service.IAccountService;
import com.olink.account.trade.service.IIntegralService;
import com.olink.account.trade.service.IMoneyService;
import com.olink.account.trade.service.impl.AccountService;
import com.olink.account.trade.service.impl.IntegralService;
import com.olink.account.trade.service.impl.MoneyService;
import com.olink.account.utils.Utility;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by zhangbin on 2016/12/4.
 * 内部交易处理（B户处理及特殊交易）
 */
public class BackInnerServer extends BaseBackServer {

    /**
     * 人民币服务
     */
    private IMoneyService moneyService = new MoneyService();

    /**
     * JSONString
     */
    private JSONObject data;

    public BackInnerServer(HttpServletRequestAcp request) {
        super(request);
    }

    @Override
    public BaseServerResult doBackServer() throws ServerException {
        BackInnerResult result;
        try {
            InnerActionEnum action = InnerActionEnum.getEnum(service.getAction());
            switch (action) {
                case back_business_applyBalanceSettlement: //B户余额结算（申请）
                    result = doApplyBalanceSettlement();
                    break;
                case back_business_BalanceSettlement: //B户余额结算（确认）
                    result = doBalanceSettlement();
                    break;
                case back_business_cancleBalanceSettlement: //B户余额结算（取消）
                    result = doCancleBalanceSettlement();
                    break;
                case back_business_applyIntegralSettlement: //B户积分结算（申请）
                    result = doApplyIntegralSettlement();
                    break;
                case back_business_IntegralSettlement: //B户积分结算（确认）
                    result = doIntegralSettlement();
                    break;
                case back_business_cancleIntegralSettlement: //B户积分结算（取消）
                    result = doCancleIntegralSettlement();
                    break;
                case back_applyInternalTrade: //特殊交易（申请）
                    result = doApplyInternalTrade();
                    break;
                case back_reviewInternalTrade: //特殊交易（审核）
                    result = doReviewInternalTrade();
                    break;
                case back_InternalTrade: //特殊交易（确认）
                    result = doInternalTrade();
                    break;
                case back_cancleInternalTrade: //特殊交易（取消）
                    result = doCancleInternalTrade();
                    break;
                case back_business_normaltrade: //通用记账
                    result = doNormal();
                    break;
                default:
                    result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "NO SUCH ACTION！");
                    break;
            }
        } catch (EnumValueUndefinedException e) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "NO SUCH ACTION！");
        }
        return result;
    }

    private BackInnerResult doApplyBalanceSettlement() throws ServerException {
        BackInnerResult result;
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "结算金额不合法");
            }
        } else {
            return (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "结算金额为空");
        }
        BalanceSettlementOrder balanceSettlementOrder = CommonTools.jsonToBean(data, BalanceSettlementOrder.class);
        if (balanceSettlementOrder == null) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balanceSettlementOrder.getBusinessid())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "B户客户号为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceSettlementOrder.getBindinfoid())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "B户绑定信息id为空");
            return result;
        }
        int comresult = Double.compare(balanceSettlementOrder.getAmont(), 0.01D);
        if (comresult < 0) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "结算金额最低为0.01元");
            return result;
        }
        if (CommonTools.isNullStr(balanceSettlementOrder.getRemark())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "交易描述为空");
            return result;
        }
        if (moneyService.doApplyBalanceSettlement(balanceSettlementOrder)) {
            result = (BackInnerResult) Utility.getDefaultSuccussResult(BackInnerResult.class);
            result.setMessage("结算申请成功！");
            result.setOrderno(balanceSettlementOrder.getOrderNo());
            result.setAmont(balanceSettlementOrder.getAmont());
        } else {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "结算申请失败！");
        }
        return result;
    }

    private BackInnerResult doBalanceSettlement() throws ServerException {
        BackInnerResult result;
        BalanceSettlementOrder balanceSettlementOrder = CommonTools.jsonToBean(data, BalanceSettlementOrder.class);
        if (balanceSettlementOrder == null) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balanceSettlementOrder.getOrderNo())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "账务系统订单号为空");
            return result;
        }
        balanceSettlementOrder.setUserid(userid);
        if (moneyService.doBalanceSettlement(balanceSettlementOrder)) {
            result = (BackInnerResult) Utility.getDefaultSuccussResult(BackInnerResult.class);
            result.setMessage("余额结算成功！");
            result.setOrderno(balanceSettlementOrder.getOrderNo());
            result.setAmont(balanceSettlementOrder.getAmont());
            result.setFinishdate(balanceSettlementOrder.getLastmodifydate());
        } else {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "余额结算失败！");
        }
        return result;
    }

    private BackInnerResult doCancleBalanceSettlement() throws ServerException {
        BackInnerResult result;
        BalanceSettlementOrder balanceSettlementOrder = CommonTools.jsonToBean(data, BalanceSettlementOrder.class);
        if (balanceSettlementOrder == null) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balanceSettlementOrder.getOrderNo())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "账务系统订单号为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceSettlementOrder.getOpinion())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "取消原因不能为空");
            return result;
        }
        if (moneyService.doCancleBalanceSettlement(balanceSettlementOrder.getOrderNo(), balanceSettlementOrder.getOpinion(), userid)) {
            result = (BackInnerResult) Utility.getDefaultSuccussResult(BackInnerResult.class);
            result.setMessage("余额结算订单取消成功！");
        } else {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "余额结算取消失败！");
        }
        return result;
    }

    private BackInnerResult doApplyIntegralSettlement() throws ServerException {
        IIntegralService integralService = new IntegralService();
        BackInnerResult result;
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "结算额不合法");
            }
        } else {
            return (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "结算额为空");
        }
        IntegralSettlementOrder integralSettlementOrder = CommonTools.jsonToBean(data, IntegralSettlementOrder.class);
        if (integralSettlementOrder == null) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(integralSettlementOrder.getBusinessid())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "B户客户号为空");
            return result;
        }
        if (CommonTools.isNullStr(integralSettlementOrder.getBindinfoid())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "B户绑定信息id为空");
            return result;
        }
        int comresult = Double.compare(integralSettlementOrder.getAmont(), 0.01D);
        if (comresult < 0) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "结算额最低为0.01元");
            return result;
        }
        if (CommonTools.isNullStr(integralSettlementOrder.getRemark())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "交易描述为空");
            return result;
        }
        if (integralService.doApplyIntegralSettlement(integralSettlementOrder)) {
            result = (BackInnerResult) Utility.getDefaultSuccussResult(BackInnerResult.class);
            result.setMessage("结算申请成功！");
            result.setOrderno(integralSettlementOrder.getOrderNo());
            result.setAmont(integralSettlementOrder.getAmont());
        } else {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "结算申请失败！");
        }
        return result;
    }

    private BackInnerResult doIntegralSettlement() throws ServerException {
        IIntegralService integralService = new IntegralService();
        BackInnerResult result;
        IntegralSettlementOrder integralSettlementOrder = CommonTools.jsonToBean(data, IntegralSettlementOrder.class);
        if (integralSettlementOrder == null) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(integralSettlementOrder.getOrderNo())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "账务系统订单号为空");
            return result;
        }
        integralSettlementOrder.setUserid(userid);
        if (integralService.doIntegralSettlement(integralSettlementOrder)) {
            result = (BackInnerResult) Utility.getDefaultSuccussResult(BackInnerResult.class);
            result.setMessage("积分结算成功！");
            result.setOrderno(integralSettlementOrder.getOrderNo());
            result.setAmont(integralSettlementOrder.getAmont());
            result.setFinishdate(integralSettlementOrder.getLastmodifydate());
        } else {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "积分结算失败！");
        }
        return result;
    }

    private BackInnerResult doCancleIntegralSettlement() throws ServerException {
        IIntegralService integralService = new IntegralService();
        BackInnerResult result;
        IntegralSettlementOrder integralSettlementOrder = CommonTools.jsonToBean(data, IntegralSettlementOrder.class);
        if (integralSettlementOrder == null) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(integralSettlementOrder.getOrderNo())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "账务系统订单号为空");
            return result;
        }
        if (CommonTools.isNullStr(integralSettlementOrder.getOpinion())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "取消原因不能为空");
            return result;
        }
        if (integralService.doCancleIntegralSettlement(integralSettlementOrder.getOrderNo(), integralSettlementOrder.getOpinion(), userid)) {
            result = (BackInnerResult) Utility.getDefaultSuccussResult(BackInnerResult.class);
            result.setMessage("积分结算订单取消成功！");
        } else {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "积分结算取消失败！");
        }
        return result;
    }

    private BackInnerResult doApplyInternalTrade() throws ServerException {
        IAccountService accountService = new AccountService();
        BackInnerResult result;
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "交易额不合法");
            }
        } else {
            return (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "交易额为空");
        }
        InternalTradeOrder internalTradeOrder = CommonTools.jsonToBean(data, InternalTradeOrder.class);
        if (internalTradeOrder == null) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "data拼写错误!");
            return result;
        }
        if (internalTradeOrder.getOrdertype() == 0) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "订单类型为空");
            return result;
        }
        if (CommonTools.isNullStr(internalTradeOrder.getBusinessid())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "B户客户号为空");
            return result;
        }
        int comresult = Double.compare(internalTradeOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "交易额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(internalTradeOrder.getTradedescription())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "交易描述为空");
            return result;
        }
        if (accountService.doApplyInternalTrade(internalTradeOrder)) {
            result = (BackInnerResult) Utility.getDefaultSuccussResult(BackInnerResult.class);
            result.setMessage("内部交易申请成功！");
            result.setOrderno(internalTradeOrder.getOrderNo());
            result.setAmont(internalTradeOrder.getAmont());
        } else {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "内部交易申请失败！");
        }
        return result;
    }

    private BackInnerResult doReviewInternalTrade() throws ServerException, EnumValueUndefinedException {
        IAccountService accountService = new AccountService();
        BackInnerResult result;
        InternalTradeOrder internalTradeOrder = CommonTools.jsonToBean(data, InternalTradeOrder.class);
        if (internalTradeOrder == null) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(internalTradeOrder.getOrderNo())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "账务系统订单号为空");
            return result;
        }
        internalTradeOrder.setUserid(userid);
        if (accountService.doReviewInternalTrade(internalTradeOrder.getOrderNo(), userid, internalTradeOrder.getOpinion(), internalTradeOrder.getReviewresult(), internalTradeOrder.getCheckstatus())) {
            result = (BackInnerResult) Utility.getDefaultSuccussResult(BackInnerResult.class);
            result.setMessage("内部交易复核成功！");
        } else {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "内部交易复核失败！");
        }
        return result;
    }

    private BackInnerResult doInternalTrade() throws ServerException {
        IAccountService accountService = new AccountService();
        BackInnerResult result;
        InternalTradeOrder internalTradeOrder = CommonTools.jsonToBean(data, InternalTradeOrder.class);
        if (internalTradeOrder == null) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(internalTradeOrder.getOrderNo())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "账务系统订单号为空");
            return result;
        }
        internalTradeOrder.setUserid(userid);
        if (accountService.doInternalTrade(internalTradeOrder)) {
            result = (BackInnerResult) Utility.getDefaultSuccussResult(BackInnerResult.class);
            result.setMessage("内部交易成功！");
            result.setOrderno(internalTradeOrder.getOrderNo());
            result.setAmont(internalTradeOrder.getAmont());
            result.setFinishdate(internalTradeOrder.getLastmodifydate());
        } else {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "内部交易失败！");
        }
        return result;
    }

    private BackInnerResult doCancleInternalTrade() throws ServerException {
        IAccountService accountService = new AccountService();
        BackInnerResult result;
        InternalTradeOrder internalTradeOrder = CommonTools.jsonToBean(data, InternalTradeOrder.class);
        if (internalTradeOrder == null) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(internalTradeOrder.getOrderNo())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "账务系统订单号为空");
            return result;
        }
        if (CommonTools.isNullStr(internalTradeOrder.getOpinion())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "取消原因不能为空");
            return result;
        }
        if (accountService.doCancleInternalTrade(internalTradeOrder.getOrderNo(), internalTradeOrder.getOpinion(), userid)) {
            result = (BackInnerResult) Utility.getDefaultSuccussResult(BackInnerResult.class);
            result.setMessage("内部交易取消成功！");
        } else {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "内部交易取消失败！");
        }
        return result;
    }

    private BackInnerResult doNormal() throws ServerException {
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "订单金额不合法");
            }
        } else {
            return (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "订单金额为空");
        }
        BackInnerResult result;
        NormalTradeOrder normalTradeOrder = CommonTools.jsonToBean(data, NormalTradeOrder.class);
        if (normalTradeOrder == null) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "data拼写错误!");
            return result;
        }
        int comresult = Double.compare(normalTradeOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "订单金额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(normalTradeOrder.getTradedescription())) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "订单描述为空");
            return result;
        }
        if (!data.containsKey("entryitemparams")) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "会计记账参数为空");
            return result;
        }
        List<EntryItemParam> entryItemParams = CommonTools.jsonToList(data.getJSONArray("entryitemparams"), EntryItemParam.class);
        normalTradeOrder.setEntryItemParams(entryItemParams);
        if (normalTradeOrder.getEntryItemParams() == null || normalTradeOrder.getEntryItemParams().isEmpty()) {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "会计记账参数为空");
            return result;
        }
        for (EntryItemParam entryItemParam : normalTradeOrder.getEntryItemParams()) {
            int entryamont = Double.compare(entryItemParam.getAmont(), 0.00D);
            if (entryamont <= 0) {
                result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "会计记账金额为零或负数");
                return result;
            }
            if (CommonTools.isNullStr(entryItemParam.getItemcode())) {
                result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "第三级科目编号为空");
                return result;
            }
            if (CommonTools.isNullStr(entryItemParam.getSubitemcode())) {
                result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "科目下立子账户序号为空");
                return result;
            }
            try {
                entryItemParam.getBalanceDirect();
            } catch (EnumValueUndefinedException e) {
                result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "会计记账类型不合法");
                return result;
            }
        }
        IBusinessService businessService = new BusinessService();
        BusinessAccount businessAccount = businessService.findBusinessAccountById(customerid);
        normalTradeOrder.setBusinessid(businessAccount.getId());
        normalTradeOrder.setBusinessname(businessAccount.getBusinessname());
        normalTradeOrder.setChannel(businessAccount.getChannel());
        normalTradeOrder.setCustid("");
        normalTradeOrder.setPaytype(PayTypeEnum.otherspay.getName());
        normalTradeOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        normalTradeOrder.setDescription("通用记账交易");
        entryItemParams = normalTradeOrder.getEntryItemParams();
        normalTradeOrder.setEntryItemParams(null);
        if (moneyService.doApplyNormalTrade(normalTradeOrder)) {
            normalTradeOrder.setEntryItemParams(entryItemParams);
            if (moneyService.doNormalTrade(normalTradeOrder)) {
                result = (BackInnerResult) Utility.getDefaultSuccussResult(BackInnerResult.class);
                result.setMessage("通用记账成功！");
                result.setOrderno(normalTradeOrder.getOrderNo());
                result.setFinishdate(normalTradeOrder.getLastmodifydate());
            } else {
                moneyService.doCancleNormalTrade(normalTradeOrder.getOrderNo());
                result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "通用记账失败！");
            }
        } else {
            result = (BackInnerResult) Utility.getFailedResult(BackInnerResult.class, "通用记账失败！");
        }
        return result;
    }

}

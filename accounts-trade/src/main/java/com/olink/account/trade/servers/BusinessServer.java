package com.olink.account.trade.servers;

import com.olink.account.base.impl.BaseServer;
import com.olink.account.base.impl.BaseServerResult;
import com.olink.account.enumration.PayTypeEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.accounting.EntryItemParam;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.order.NormalTradeOrder;
import com.olink.account.model.trade.order.ShopSettlementOrder;
import com.olink.account.trade.servers.enumration.BusinessActionEnum;
import com.olink.account.trade.servers.result.BusinessResult;
import com.olink.account.trade.service.IMoneyService;
import com.olink.account.trade.service.impl.MoneyService;
import com.olink.account.utils.Utility;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by zhangbin on 2016/12/4.
 * B户交易接口
 */
public class BusinessServer extends BaseServer {

    /**
     * JSONString
     */
    private JSONObject data;

    /**
     * 积分服务
     */
    private IMoneyService moneyService = new MoneyService();

    public BusinessServer(HttpServletRequestAcp request) {
        super(request);
    }

    @Override
    public BaseServerResult doServer() throws ServerException {
        BusinessResult result;
        try {
            BusinessActionEnum action = BusinessActionEnum.getEnum(service.getAction());
            switch (action) {
                case shop_applysettlement: //申请结算
                    result = doApplySettlement();
                    break;
                case shop_settlement: //确认结算
                    result = doSettlement();
                    break;
                case shop_canclesettlement: //取消结算
                    result = doCancleSettlement();
                    break;
                case business_normaltrade: //执行通用记账
                    result = doNormal();
                    break;
                default:
                    result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "NO SUCH ACTION！");
                    break;
            }
        } catch (EnumValueUndefinedException e) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "NO SUCH ACTION！");
        }
        return result;
    }

    private BusinessResult doApplySettlement() throws ServerException {
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (BusinessResult) Utility.getFailedResult(BusinessResult.class, "结算金额不合法");
            }
        } else {
            return (BusinessResult) Utility.getFailedResult(BusinessResult.class, "结算金额为空");
        }
        if (!data.containsKey("rate")) {
            return (BusinessResult) Utility.getFailedResult(BusinessResult.class, "结算费率为空");
        }
        BusinessResult result;
        ShopSettlementOrder settlementOrder = CommonTools.jsonToBean(data, ShopSettlementOrder.class);
        if (settlementOrder == null) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(settlementOrder.getShopno())) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "二级商户号为空");
            return result;
        }
        if (CommonTools.isNullStr(settlementOrder.getBusinesstradeno())) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "商户交易号为空");
            return result;
        }
        int comresult = Double.compare(settlementOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "结算金额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(settlementOrder.getAccounttype())) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "结算账户类型为空");
            return result;
        }
        if (CommonTools.isNullStr(settlementOrder.getAccountname())) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "二级商户账户名称为空");
            return result;
        }
        if (CommonTools.isNullStr(settlementOrder.getAccount())) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "二级商户第三方账号名为空");
            return result;
        }
        if (CommonTools.isNullStr(settlementOrder.getPlansettlementdate())) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "二级商户计划结算时间为空");
            return result;
        }
        if (CommonTools.isNullStr(settlementOrder.getRemark())) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "订单描述为空");
            return result;
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        settlementOrder.setBusinessid(businessAccount.getId());
        settlementOrder.setBusinessname(businessAccount.getBusinessname());
        settlementOrder.setChannel(businessAccount.getChannel());
        settlementOrder.setDescription("二级商户结算");
        settlementOrder.setRatio(1.000000D - settlementOrder.getRate());
        if (moneyService.doApplyShopSettlement(settlementOrder)) {
            result = (BusinessResult) Utility.getDefaultSuccussResult(BusinessResult.class);
            result.setMessage("二级商户结算申请成功！");
            result.setOrderno(settlementOrder.getOrderNo());
            result.setAmont(settlementOrder.getAmont());
            result.setRate(settlementOrder.getRate());
            result.setShopno(settlementOrder.getShopno());
        } else {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "二级商户结算申请失败！");
        }
        return result;
    }

    private BusinessResult doSettlement() throws ServerException {
        BusinessResult result;
        ShopSettlementOrder settlementOrder = CommonTools.jsonToBean(data, ShopSettlementOrder.class);
        if (settlementOrder == null) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(settlementOrder.getOrderNo())) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "账务系统订单号为空");
            return result;
        }
        if (moneyService.doShopSettlement(settlementOrder)) {
            result = (BusinessResult) Utility.getDefaultSuccussResult(BusinessResult.class);
            result.setMessage("二级商户结算成功！");
            result.setOrderno(settlementOrder.getOrderNo());
            result.setAmont(settlementOrder.getAmont());
            result.setRate(settlementOrder.getRate());
            result.setShopno(settlementOrder.getShopno());
            result.setFinishdate(settlementOrder.getLastmodifydate());
        } else {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "二级商户结算失败！");
        }
        return result;
    }

    private BusinessResult doCancleSettlement() throws ServerException {
        BusinessResult result;
        ShopSettlementOrder settlementOrder = CommonTools.jsonToBean(data, ShopSettlementOrder.class);
        if (settlementOrder == null) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(settlementOrder.getOrderNo())) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "账务系统订单号为空");
            return result;
        }
        if (moneyService.doCancleShopSettlement(settlementOrder.getOrderNo())) {
            result = (BusinessResult) Utility.getDefaultSuccussResult(BusinessResult.class);
            result.setMessage("二级商户结算取消成功！");
        } else {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "二级商户结算取消失败！");
        }
        return result;
    }

    private BusinessResult doNormal() throws ServerException {
        if (data.containsKey("amont")) {
            if (!validateAmont(data.optString("amont"))) {
                return (BusinessResult) Utility.getFailedResult(BusinessResult.class, "订单金额不合法");
            }
        } else {
            return (BusinessResult) Utility.getFailedResult(BusinessResult.class, "订单金额为空");
        }
        BusinessResult result;
        NormalTradeOrder normalTradeOrder = CommonTools.jsonToBean(data, NormalTradeOrder.class);
        if (normalTradeOrder == null) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(normalTradeOrder.getBusinesstradeno())) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "商户交易号为空");
            return result;
        }
        int comresult = Double.compare(normalTradeOrder.getAmont(), 0.00D);
        if (comresult <= 0) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "订单金额为零或负数");
            return result;
        }
        if (CommonTools.isNullStr(normalTradeOrder.getTradedescription())) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "订单描述为空");
            return result;
        }
        if (!data.containsKey("entryitemparams")) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "会计记账参数为空");
            return result;
        }
        List<EntryItemParam> entryItemParams = CommonTools.jsonToList(data.getJSONArray("entryitemparams"), EntryItemParam.class);
        normalTradeOrder.setEntryItemParams(entryItemParams);
        if (normalTradeOrder.getEntryItemParams() == null || normalTradeOrder.getEntryItemParams().isEmpty()) {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "会计记账参数为空");
            return result;
        }
        for (EntryItemParam entryItemParam : normalTradeOrder.getEntryItemParams()) {
            int entryamont = Double.compare(entryItemParam.getAmont(), 0.00D);
            if (entryamont <= 0) {
                result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "会计记账金额为零或负数");
                return result;
            }
            if (CommonTools.isNullStr(entryItemParam.getItemcode())) {
                result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "第三级科目编号为空");
                return result;
            }
            if (CommonTools.isNullStr(entryItemParam.getSubitemcode())) {
                result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "科目下立子账户序号为空");
                return result;
            }
            try {
                entryItemParam.getBalanceDirect();
            } catch (EnumValueUndefinedException e) {
                result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "会计记账类型不合法");
                return result;
            }
        }
        BusinessAccount businessAccount = getCurrBusinessAccount();
        normalTradeOrder.setBusinessid(businessAccount.getId());
        normalTradeOrder.setBusinessname(businessAccount.getBusinessname());
        normalTradeOrder.setChannel(businessAccount.getChannel());
        normalTradeOrder.setCustid("");
        normalTradeOrder.setPaytype(PayTypeEnum.otherspay.getName());
        normalTradeOrder.setPaytypename(PayTypeEnum.otherspay.getDescription());
        normalTradeOrder.setDescription("B户通用记账交易");
        if (moneyService.doApplyNormalTrade(normalTradeOrder)) {
            normalTradeOrder.setEntryItemParams(null);
            if (moneyService.doNormalTrade(normalTradeOrder)) {
                result = (BusinessResult) Utility.getDefaultSuccussResult(BusinessResult.class);
                result.setMessage("B户通用记账成功！");
                result.setOrderno(normalTradeOrder.getOrderNo());
                result.setFinishdate(normalTradeOrder.getLastmodifydate());
            } else {
                moneyService.doCancleNormalTrade(normalTradeOrder.getOrderNo());
                result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "B户通用记账失败！");
            }
        } else {
            result = (BusinessResult) Utility.getFailedResult(BusinessResult.class, "B户通用记账失败！");
        }
        return result;
    }

}

package com.olink.account.trade.servers;

import com.olink.account.base.impl.BaseBackServer;
import com.olink.account.base.impl.BaseServerResult;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.order.BalanceCashOrder;
import com.olink.account.trade.servers.enumration.CustPayActionEnum;
import com.olink.account.trade.servers.result.BackCustomerResult;
import com.olink.account.trade.service.IMoneyService;
import com.olink.account.trade.service.impl.MoneyService;
import com.olink.account.utils.Utility;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import net.sf.json.JSONObject;

/**
 * Created by zhangbin on 2016/10/8.
 * C户交易接口（后台）
 */
public class BackCustomerServer extends BaseBackServer {

    /**
     * C户服务
     */
    private IMoneyService moneyService = new MoneyService();

    /**
     * JSONString
     */
    private JSONObject data;

    public BackCustomerServer(HttpServletRequestAcp request) {
        super(request);
    }

    @Override
    public BaseServerResult doBackServer() throws ServerException {
        BackCustomerResult result;
        try {
            CustPayActionEnum action = CustPayActionEnum.getEnum(service.getAction());
            switch (action) {
                case back_cust_cash: //C户余额提现（确认）
                    result = custCash();
                    break;
                case back_cust_canclecash: //C户余额提现（取消）
                    result = custCancleCash();
                    break;
                default:
                    result = (BackCustomerResult) Utility.getFailedResult(BackCustomerResult.class, "NO SUCH ACTION！");
                    break;
            }
        } catch (EnumValueUndefinedException e) {
            result = (BackCustomerResult) Utility.getFailedResult(BackCustomerResult.class, "NO SUCH ACTION！");
        }
        return result;
    }

    private BackCustomerResult custCash() throws ServerException {
        BackCustomerResult result;
        BalanceCashOrder balanceCashOrder = CommonTools.jsonToBean(data, BalanceCashOrder.class);
        if (balanceCashOrder == null) {
            result = (BackCustomerResult) Utility.getFailedResult(BackCustomerResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balanceCashOrder.getOrderNo())) {
            result = (BackCustomerResult) Utility.getFailedResult(BackCustomerResult.class, "账务系统订单号为空");
            return result;
        }
        balanceCashOrder.setUserid(userid);
        if (moneyService.doBalanceCash(balanceCashOrder)) {
            result = (BackCustomerResult) Utility.getDefaultSuccussResult(BackCustomerResult.class);
            result.setMessage("提现成功！");
            result.setCustid(balanceCashOrder.getCustid());
            result.setOrderno(balanceCashOrder.getOrderNo());
            result.setAmont(balanceCashOrder.getAmont());
            result.setFinishdate(balanceCashOrder.getLastmodifydate());
        } else {
            result = (BackCustomerResult) Utility.getFailedResult(BackCustomerResult.class, "提现失败！");
        }
        return result;
    }

    private BackCustomerResult custCancleCash() throws ServerException {
        BackCustomerResult result;
        BalanceCashOrder balanceCashOrder = CommonTools.jsonToBean(data, BalanceCashOrder.class);
        if (balanceCashOrder == null) {
            result = (BackCustomerResult) Utility.getFailedResult(BackCustomerResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(balanceCashOrder.getOrderNo())) {
            result = (BackCustomerResult) Utility.getFailedResult(BackCustomerResult.class, "账务系统订单号为空");
            return result;
        }
        if (CommonTools.isNullStr(balanceCashOrder.getOpinion())) {
            result = (BackCustomerResult) Utility.getFailedResult(BackCustomerResult.class, "取消原因不能为空");
            return result;
        }
        if (moneyService.doCancleBalanceCash(balanceCashOrder.getOrderNo(), balanceCashOrder.getOpinion(), userid)) {
            result = (BackCustomerResult) Utility.getDefaultSuccussResult(BackCustomerResult.class);
            result.setMessage("提现订单取消成功！");
        } else {
            result = (BackCustomerResult) Utility.getFailedResult(BackCustomerResult.class, "提现取消失败！");
        }
        return result;
    }

}

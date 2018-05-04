package com.olink.account.trade.servers;

import com.olink.account.base.impl.BaseServer;
import com.olink.account.base.impl.BaseServerResult;
import com.olink.account.enumration.CBindTypeEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.customer.CustAccount;
import com.olink.account.model.trade.customer.CustBindInfo;
import com.olink.account.service.ICustomerService;
import com.olink.account.service.impl.CustomerService;
import com.olink.account.trade.servers.enumration.CustMngActionEnum;
import com.olink.account.trade.servers.result.CustomerMngResult;
import com.olink.account.utils.Utility;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import net.sf.json.JSONObject;

/**
 * Created by zhangbin on 2016/12/9.
 * 信息维护
 */
public class CustomerMngServer extends BaseServer {

    /**
     * C户服务
     */
    private ICustomerService customerService = new CustomerService();

    /**
     * JSONString
     */
    private JSONObject data;

    /**
     * IDS 用,分割 eg:a,b,c
     */
    private String ids;

    public CustomerMngServer(HttpServletRequestAcp request) {
        super(request);
    }

    @Override
    public BaseServerResult doServer() throws ServerException {
        CustomerMngResult result;
        try {
            CustMngActionEnum action = CustMngActionEnum.getEnum(service.getAction());
            switch (action) {
                case cust_register: //C户添加
                    result = custRegister();
                    break;
                case cust_telephone_modify:
                    result = custTelephoneMod();
                    break;
                case cust_bindinfo_add: //c户绑定信息添加
                    result = custBindinfoAdd();
                    break;
                case cust_bindinfo_del: //C户绑定信息删除，逻辑删除
                    result = custBindinfoDel();
                    break;
                case cust_bindinfo_defualt: //C户绑定信息设置为默认
                    result = custBindinfoDefault();
                    break;
                case cust_pay_password_set: //支付密码设置
                    result = custPayPwdSet();
                    break;
                case cust_pay_password_modify: //支付密码修改
                    result = custPayPwdMod();
                    break;
                default:
                    result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "NO SUCH ACTION！");
                    break;
            }
        } catch (EnumValueUndefinedException e) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "NO SUCH ACTION！");
        }
        return result;
    }

    /**
     * C户添加
     *
     * @return 结果
     */
    private CustomerMngResult custRegister() throws ServerException {
        CustomerMngResult result;
        if (data == null) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "data为空!");
        } else {
            CustAccount custAccount_p = CommonTools.jsonToBean(data, CustAccount.class);
            if (custAccount_p == null) {
                result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "data拼写错误!");
                return result;
            }
            if (CommonTools.isNullStr(custAccount_p.getTelephone())) {
                result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "手机号为空");
                return result;
            }
            if (CommonTools.isNullStr(custAccount_p.getLoginpwd())) {
                result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "账务系统登录密码为空");
                return result;
            }
            BusinessAccount businessAccount = getCurrBusinessAccount();
            custAccount_p.setChannel(businessAccount.getChannel());
            CustAccount custAccount = customerService.doCreateCustAccount(custAccount_p);
            if (custAccount != null) {
                result = (CustomerMngResult) Utility.getDefaultSuccussResult(CustomerMngResult.class);
                result.setCustid(custAccount.getCustid());
            } else {
                result = (CustomerMngResult) Utility.getDefaultFailedResult(CustomerMngResult.class);
                result.setCustid("");
            }
        }
        return result;
    }

    /**
     * C户手机号变更
     *
     * @return 结果
     */
    private CustomerMngResult custTelephoneMod() throws ServerException {
        CustomerMngResult result;
        if (data == null) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "data为空!");
        } else {
            CustAccount custAccount = getCustAccount();
            if (custAccount == null) {
                result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "data拼写错误!");
                return result;
            }
            if (CommonTools.isNullStr(custAccount.getTelephone())) {
                result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "原手机号为空");
                return result;
            }
            if (CommonTools.isNullStr(custAccount.getLoginpwd())) {
                result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "原登录密码为空");
                return result;
            }
            if (CommonTools.isNullStr(custAccount.getTelephone_new())) {
                result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "新手机号为空");
                return result;
            }
            if (CommonTools.isNullStr(custAccount.getLoginpwd_new())) {
                result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "新登录密码为空");
                return result;
            }
            if (customerService.doModifyTelephone(custAccount)) {
                result = (CustomerMngResult) Utility.getDefaultSuccussResult(CustomerMngResult.class);
            } else {
                result = (CustomerMngResult) Utility.getDefaultFailedResult(CustomerMngResult.class);
            }
        }
        return result;
    }

    /**
     * C户添加绑定信息
     *
     * @return 结果
     */
    private CustomerMngResult custBindinfoAdd() throws ServerException {
        CustomerMngResult result;
        if (data == null) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "data为空!");
        } else {
            CustBindInfo bindInfo = getCustBindInfo();
            if (bindInfo == null) {
                result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "data拼写错误!");
                return result;
            }
            try {
                CBindTypeEnum cBindTypeEnum = CBindTypeEnum.getEnum(bindInfo.getType());
                if (cBindTypeEnum.equals(CBindTypeEnum.BankCard.getValue())) {
                    if (CommonTools.isNullStr(bindInfo.getBank())) {
                        result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "开户银行为空!");
                        return result;
                    }
                }
                if (CommonTools.isNullStr(bindInfo.getAccountname())) {
                    result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "账户名称为空!");
                    return result;
                }
                if (CommonTools.isNullStr(bindInfo.getAccount())) {
                    result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "账号为空!");
                    return result;
                }
            } catch (EnumValueUndefinedException e) {
                result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "绑定类型不正确!");
                return result;
            }
            BusinessAccount businessAccount = getCurrBusinessAccount();
            bindInfo.setChannel(businessAccount.getChannel());
            bindInfo.setSort(99);
            if (customerService.doAddCustBindInfo(bindInfo)) {
                result = (CustomerMngResult) Utility.getDefaultSuccussResult(CustomerMngResult.class);
            } else {
                result = (CustomerMngResult) Utility.getDefaultFailedResult(CustomerMngResult.class);
            }
        }
        return result;
    }

    /**
     * C户绑定信息删除，逻辑删除
     *
     * @return 结果
     */
    private CustomerMngResult custBindinfoDel() {
        CustomerMngResult result;
        if (CommonTools.isNullStr(ids)) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "ids为空!");
            return result;
        }
        if (customerService.doDelCustBinfInfo(ids)) {
            result = (CustomerMngResult) Utility.getDefaultSuccussResult(CustomerMngResult.class);
        } else {
            result = (CustomerMngResult) Utility.getDefaultFailedResult(CustomerMngResult.class);
        }
        return result;
    }

    /**
     * C户绑定信息设置为默认
     *
     * @return 结果
     */
    private CustomerMngResult custBindinfoDefault() throws ServerException {
        CustomerMngResult result;
        if (data == null) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "data为空!");
            return result;
        }
        CustBindInfo bindInfo = getCustBindInfo();
        if (bindInfo == null) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "data拼写错误!");
            return result;
        }
        try {
            CBindTypeEnum.getEnum(bindInfo.getType());
        } catch (EnumValueUndefinedException e) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "绑定类型不正确!");
            return result;
        }
        if (customerService.setDefaultCustBindInfo(bindInfo)) {
            result = (CustomerMngResult) Utility.getDefaultSuccussResult(CustomerMngResult.class);
        } else {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "设置默认失败!");
        }
        return result;
    }

    /**
     * 设置支付密码
     *
     * @return 结果
     */
    private CustomerMngResult custPayPwdSet() throws ServerException {
        CustomerMngResult result;
        if (data == null) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "data为空!");
            return result;
        }
        CustAccount custAccount = getCustAccount();
        if (custAccount == null) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(custAccount.getPassword())) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "支付密码为空!");
            return result;
        }
        if (customerService.doSetCustPayPassword(custAccount)) {
            result = (CustomerMngResult) Utility.getDefaultSuccussResult(CustomerMngResult.class);
        } else {
            result = (CustomerMngResult) Utility.getDefaultFailedResult(CustomerMngResult.class);
        }
        return result;
    }

    /**
     * C户支付密码修改
     *
     * @return 结果
     */
    private CustomerMngResult custPayPwdMod() throws ServerException {
        CustomerMngResult result;
        if (data == null) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "data为空!");
            return result;
        }
        CustAccount custAccount = getCustAccount();
        if (custAccount == null) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(custAccount.getPassword_new())) {
            result = (CustomerMngResult) Utility.getFailedResult(CustomerMngResult.class, "新支付密码为空");
            return result;
        }
        if (customerService.doModCustPayPassword(custAccount)) {
            result = (CustomerMngResult) Utility.getDefaultSuccussResult(CustomerMngResult.class);
        } else {
            result = (CustomerMngResult) Utility.getDefaultFailedResult(CustomerMngResult.class);
        }
        return result;
    }

    /**
     * 解析C户账户信息
     *
     * @return C户账户信息对象
     */
    private CustAccount getCustAccount() {
        return CommonTools.jsonToBean(data, CustAccount.class);
    }

    /**
     * 解析C户账户绑定信息
     *
     * @return C户账户绑定信息对象
     */
    private CustBindInfo getCustBindInfo() {
        return CommonTools.jsonToBean(data, CustBindInfo.class);
    }

}

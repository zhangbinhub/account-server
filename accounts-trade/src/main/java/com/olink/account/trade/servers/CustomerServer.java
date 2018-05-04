package com.olink.account.trade.servers;

import com.olink.account.base.impl.BaseServer;
import com.olink.account.base.impl.BaseServerResult;
import com.olink.account.trade.servers.enumration.CustActionEnum;
import com.olink.account.enumration.*;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.BusinessBindInfo;
import com.olink.account.model.trade.customer.CustAccount;
import com.olink.account.model.trade.customer.CustBindInfo;
import com.olink.account.model.trade.dictionary.Dictionary;
import com.olink.account.trade.model.respjson.YYBusinessAccount;
import com.olink.account.trade.servers.result.CustomerResult;
import com.olink.account.service.IBusinessService;
import com.olink.account.service.ICustomerService;
import com.olink.account.service.IDictionaryService;
import com.olink.account.service.impl.BusinessService;
import com.olink.account.service.impl.CustomerService;
import com.olink.account.service.impl.DictionaryService;
import com.olink.account.utils.Utility;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import java.util.List;

/**
 * Created by Shepherd on 2016-08-24.
 * C户信息查询服务
 */
public class CustomerServer extends BaseServer {

    public CustomerServer(HttpServletRequestAcp request) {
        super(request);
    }

    /**
     * C户服务
     */
    private ICustomerService customerService = new CustomerService();

    /**
     * JSONString
     */
    private JSONObject data;

    /**
     * 客户编号
     */
    private String custid;

    /**
     * C户电话号码
     */
    private String telephone;

    /**
     * B户账户类型编码
     */
    private String accounttype;

    @Override
    public BaseServerResult doServer() throws ServerException {
        CustomerResult result;
        try {
            CustActionEnum action = CustActionEnum.getEnum(service.getAction());
            switch (action) {
                case cust_desc_custid: //通过custid查询C户信息
                    result = custDescCustid();
                    break;
                case cust_desc_telephone: //根据C户电话号码获取C户信息
                    result = custDescTelephone();
                    break;
                case cust_desc_bindinfo: //通过C户客户号获取可用的C户绑定信息
                    result = custDescBindinfo();
                    break;
                case cust_bindinfo_type://获取可用的C户绑定类型
                    result = custBindinfoType();
                    break;
                case cust_pay_password_check: //检测是否需要设置支付密码
                    result = custPayPwdCheck();
                    break;
                case cust_pay_password_validate: //支付密码正确性验证
                    result = custPayPwdValidate();
                    break;
                case cust_pay_business_bindinfo_type://获取可用的B户收款账号类型
                    result = custPayGetBusinessReceiptType();
                    break;
                case cust_pay_business_receipt_account: //获取运营方收款账号信息
                    result = custPayGetReceiptAccount();
                    break;
                default:
                    result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "NO SUCH ACTION！");
                    break;
            }
        } catch (EnumValueUndefinedException e) {
            result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "NO SUCH ACTION！");
        }
        return result;
    }

    /**
     * 通过C户客户号查询C户信息
     *
     * @return 结果
     */
    private CustomerResult custDescCustid() throws ServerException {
        if (CommonTools.isNullStr(custid)) {
            return (CustomerResult) Utility.getFailedResult(CustomerResult.class, "custid为空!");
        }
        CustAccount custAccount = customerService.findCustAccountByCustId(custid);
        return returnCustAccount(custAccount);
    }

    /**
     * 根据C户电话号码获取C户信息
     *
     * @return 结果
     */
    private CustomerResult custDescTelephone() throws ServerException {
        if (CommonTools.isNullStr(telephone)) {
            return (CustomerResult) Utility.getFailedResult(CustomerResult.class, "telephone为空!");
        }
        CustAccount custAccount = customerService.findCustAccountByTelephone(telephone);
        return returnCustAccount(custAccount);
    }

    private CustomerResult custDescBindinfo() throws ServerException {
        CustomerResult result;
        if (CommonTools.isNullStr(custid)) {
            result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "custid为空!");
            return result;
        }
        List<DBTable> bindInfos = customerService.findCustBindInfoByCustId(custid);
        JSONArray array = new JSONArray();
        for (DBTable dbTable : bindInfos) {
            CustBindInfo bindInfo = (CustBindInfo) dbTable;
            if (bindInfo.getIsdel() == DeleteEnum.notDelete.getValue() && bindInfo.getStatus() == StatusEnum.activate.getValue()) {
                JSONObject item = new JSONObject();
                try {
                    item.put("id", bindInfo.getId());
                    item.put("type", CBindTypeEnum.getEnum(bindInfo.getType()).getName());
                    item.put("bank", bindInfo.getBank());
                    item.put("accountname", bindInfo.getAccountname());
                    item.put("account", bindInfo.getAccount());
                    item.put("default", bindInfo.getIsdefault());
                } catch (EnumValueUndefinedException e) {
                    array.clear();
                    break;
                }
                array.add(item);
            }
        }
        result = (CustomerResult) Utility.getDefaultSuccussResult(CustomerResult.class);
        result.setDatas(array);
        return result;
    }

    /**
     * 构建C户账户查询返回
     *
     * @param custAccount C户信息对象
     * @return 结果对象
     */
    private CustomerResult returnCustAccount(CustAccount custAccount) {
        CustomerResult result;
        if (custAccount != null) {
            result = (CustomerResult) Utility.getDefaultSuccussResult(CustomerResult.class);
            result.setData(CommonTools.beanToJson(custAccount, new String[]{"password", "password_new", "loginpwd", "loginpwd_new", "telephone_new", "userid", "edituserid", "authuserid"}));
        } else {
            result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "找不到账户信息");
        }
        return result;
    }

    /**
     * 获取可用的C户绑定类型
     *
     * @return 结果
     */
    private CustomerResult custBindinfoType() {
        CustomerResult result = (CustomerResult) Utility.getDefaultSuccussResult(CustomerResult.class);
        IDictionaryService dictionaryService = new DictionaryService();
        List<DBTable> dictionaries = dictionaryService.findDictionaryAvailableList(DictionaryTableEnum.custBindType, null, null);
        JSONArray array = buildDicArray(dictionaries);
        result.setDatas(array);
        return result;
    }

    /**
     * 检测是否需要设置支付密码
     *
     * @return 结果
     */
    private CustomerResult custPayPwdCheck() throws ServerException {
        CustomerResult result;
        if (CommonTools.isNullStr(custid)) {
            result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "custid参数为空!");
            return result;
        }
        CustAccount custAccount = customerService.findCustAccountByCustId(custid);
        if (custAccount == null) {
            result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "用户不存在！");
            return result;
        }
        String password = custAccount.getPassword();
        if (!CommonTools.isNullStr(password)) {
            result = (CustomerResult) Utility.getDefaultSuccussResult(CustomerResult.class);
            result.setMessage("支付密码已设置");
        } else {
            result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "支付密码未设置");
        }
        return result;
    }

    /**
     * 验证支付密码
     *
     * @return 结果
     */
    private CustomerResult custPayPwdValidate() throws ServerException {
        CustomerResult result;
        if (data == null) {
            result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "data为空!");
            return result;
        }
        CustAccount custAccount = getCustAccount();
        if (custAccount == null) {
            result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "data拼写错误!");
            return result;
        }
        if (CommonTools.isNullStr(custAccount.getCustid())) {
            result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "C户客户号为空");
            return result;
        }
        if (CommonTools.isNullStr(custAccount.getPassword())) {
            result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "支付密码为空");
            return result;
        }
        if (customerService.doValidatePayPassword(custAccount)) {
            result = (CustomerResult) Utility.getDefaultSuccussResult(CustomerResult.class);
        } else {
            result = (CustomerResult) Utility.getDefaultFailedResult(CustomerResult.class);
        }
        return result;
    }

    private CustomerResult custPayGetBusinessReceiptType() {
        CustomerResult result = (CustomerResult) Utility.getDefaultSuccussResult(CustomerResult.class);
        IBusinessService businessService = new BusinessService();
        List<DBTable> dictionaries = businessService.findYYBusinessBindInfoType();
        JSONArray array = buildDicArray(dictionaries);
        result.setDatas(array);
        return result;
    }

    private CustomerResult custPayGetReceiptAccount() {
        CustomerResult result;
        IBusinessService businessService = new BusinessService();
        BusinessBindInfo businessBindInfo = businessService.findYYBusinessBindInfo(accounttype);
        if (businessBindInfo == null) {
            result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "找不到可用的收款账户信息！");
            return result;
        }
        result = (CustomerResult) Utility.getDefaultSuccussResult(CustomerResult.class);
        YYBusinessAccount yyBusinessAccount = new YYBusinessAccount();
        try {
            switch (BBindTypeEnum.getEnum(businessBindInfo.getType())) {
                case BankCard:
                    yyBusinessAccount.setName(businessBindInfo.getName());
                    yyBusinessAccount.setCardno(businessBindInfo.getAccount());
                    yyBusinessAccount.setTelephone(businessBindInfo.getTelephone());
                    result.setData(CommonTools.beanToJson(yyBusinessAccount));
                    break;
                case WeiXinOpen:
                case WeiXinPublic:
                    yyBusinessAccount.setPartner(businessBindInfo.getPartner());
                    yyBusinessAccount.setPartnerkey(businessBindInfo.getPartnerkey());
                    yyBusinessAccount.setAppid(businessBindInfo.getAppid());
                    yyBusinessAccount.setAppsecret(businessBindInfo.getAppsecret());
                    yyBusinessAccount.setAppsingn(businessBindInfo.getAppsingn());
                    yyBusinessAccount.setBundleid(businessBindInfo.getBundleid());
                    yyBusinessAccount.setPackages(businessBindInfo.getPackagename());
                    result.setData(CommonTools.beanToJson(yyBusinessAccount));
                    break;
                case AliPay:
                    yyBusinessAccount.setPartner(businessBindInfo.getPartner());
                    yyBusinessAccount.setPartnerkey(businessBindInfo.getPartnerkey());
                    yyBusinessAccount.setSeller_email(businessBindInfo.getSeller_email());
                    yyBusinessAccount.setSeller_id(businessBindInfo.getSeller_id());
                    yyBusinessAccount.setPrivate_key(businessBindInfo.getPrivate_key());
                    result.setData(CommonTools.beanToJson(yyBusinessAccount));
                    break;
                default:
                    result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "找不到可用的收款账户信息！");
            }
        } catch (EnumValueUndefinedException e) {
            result = (CustomerResult) Utility.getFailedResult(CustomerResult.class, "找不到可用的收款账户信息！");
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

    private JSONArray buildDicArray(List<DBTable> dictionaries) {
        JSONArray array = new JSONArray();
        for (DBTable dbTable : dictionaries) {
            Dictionary dictionary = (Dictionary) dbTable;
            JSONObject item = new JSONObject();
            item.put("name", dictionary.getName());
            item.put("typecode", dictionary.getCode());
            array.add(item);
        }
        return array;
    }

}

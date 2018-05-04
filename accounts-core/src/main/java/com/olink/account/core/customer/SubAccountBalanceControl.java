package com.olink.account.core.customer;

import com.olink.account.enumration.ChangeAccountTypeEnum;
import com.olink.account.enumration.ChangeBalanceDirectEnum;
import com.olink.account.enumration.ResultStatusEnum;
import com.olink.account.enumration.StatusEnum;
import com.olink.account.core.AccountException;
import com.olink.account.core.AccountResult;
import com.olink.account.model.trade.customer.BusinessSubAccount;
import com.olink.account.model.trade.customer.CustChangeLog;
import com.olink.account.model.trade.customer.CustSubAccount;
import com.olink.account.model.trade.customer.SubAccount;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.entity.DBTable;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/11/1.
 * 余额控制
 */
class SubAccountBalanceControl {

    /**
     * 获取虚拟账户余额
     *
     * @param customerid            客户号
     * @param changeAccountTypeEnum 账户类型
     * @param accountTypeCode       虚拟账户类型编码
     * @param connectionFactory     数据库连接对象
     * @return 余额
     */
    static double getSubAccountBalance(String customerid, ChangeAccountTypeEnum changeAccountTypeEnum, String accountTypeCode, ConnectionFactory connectionFactory) {
        double result = 0.00D;
        Map<String, Object> param = new HashMap<>();
        switch (changeAccountTypeEnum) {
            case Account:
            case Business:
            case DisBusiness:
                param.put(BusinessSubAccount.class.getCanonicalName() + ".businessid", customerid);
                param.put(BusinessSubAccount.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
                List<DBTable> businessSubAccounts = connectionFactory.doQueryForObjList(param, BusinessSubAccount.class, null, " for update");
                for (DBTable dbTable : businessSubAccounts) {
                    BusinessSubAccount businessSubAccount = (BusinessSubAccount) dbTable;
                    if (businessSubAccount.getType().equals(accountTypeCode)) {
                        result = businessSubAccount.getBalance();
                        break;
                    }
                }
                break;
            case Cust:
            case DisCust:
                param.put(CustSubAccount.class.getCanonicalName() + ".custid", customerid);
                param.put(CustSubAccount.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
                List<DBTable> custSubAccounts = connectionFactory.doQueryForObjList(param, CustSubAccount.class, null, " for update");
                for (DBTable dbTable : custSubAccounts) {
                    CustSubAccount custSubAccount = (CustSubAccount) dbTable;
                    if (custSubAccount.getType().equals(accountTypeCode)) {
                        result = custSubAccount.getBalance();
                        break;
                    }
                }
                break;
        }
        return result;
    }

    /**
     * 修改虚拟账户余额
     *
     * @param customerid              客户号
     * @param amont                   变动额
     * @param actamont                实际变动金额
     * @param changeAccountTypeEnum   账户类型
     * @param accountTypeCode         虚拟账户编码
     * @param changeBalanceDirectEnum 变动方向
     * @param connectionFactory       数据库连接对象
     * @return 记账结果对象
     */
    static AccountResult changeBalance(String customerid, double amont, double actamont, ChangeAccountTypeEnum changeAccountTypeEnum, String accountTypeCode, ChangeBalanceDirectEnum changeBalanceDirectEnum, ConnectionFactory connectionFactory) throws AccountException {
        AccountResult result = new AccountResult();
        SubAccount subAccount = null;
        Map<String, Object> param = new HashMap<>();
        switch (changeAccountTypeEnum) {
            case Account:
            case Business:
            case DisBusiness:
                param.put(BusinessSubAccount.class.getCanonicalName() + ".businessid", customerid);
                param.put(BusinessSubAccount.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
                List<DBTable> businessSubAccounts = connectionFactory.doQueryForObjList(param, BusinessSubAccount.class, null, " for update");
                for (DBTable dbTable : businessSubAccounts) {
                    BusinessSubAccount businessSubAccount = (BusinessSubAccount) dbTable;
                    if (businessSubAccount.getType().equals(accountTypeCode)) {
                        subAccount = businessSubAccount;
                        break;
                    }
                }
                break;
            case Cust:
            case DisCust:
                param.put(CustSubAccount.class.getCanonicalName() + ".custid", customerid);
                param.put(CustSubAccount.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
                List<DBTable> custSubAccounts = connectionFactory.doQueryForObjList(param, CustSubAccount.class, null, " for update");
                for (DBTable dbTable : custSubAccounts) {
                    CustSubAccount custSubAccount = (CustSubAccount) dbTable;
                    if (custSubAccount.getType().equals(accountTypeCode)) {
                        subAccount = custSubAccount;
                        break;
                    }
                }
                break;
        }
        if (subAccount != null) {
            return doChange(subAccount, amont, actamont, changeBalanceDirectEnum, connectionFactory);
        } else {
            result.setStatus(ResultStatusEnum.failed);
            result.setMessage("找不到虚拟账户");
            return result;
        }
    }

    /**
     * 变更账户余额
     *
     * @param subAccount              虚拟账户对象
     * @param amont                   变动额
     * @param actamont                实际变动金额
     * @param changeBalanceDirectEnum 变动方向
     * @param connectionFactory       数据库连接对象
     * @return 记账结果对象
     */
    private static AccountResult doChange(SubAccount subAccount, double amont, double actamont, ChangeBalanceDirectEnum changeBalanceDirectEnum, ConnectionFactory connectionFactory) {
        AccountResult result = new AccountResult();
        result.setBeforbalance(subAccount.getBalance());
        result.setBeformoney(subAccount.getMoney());
        switch (changeBalanceDirectEnum) {
            case Plus:
                subAccount.setBalance(subAccount.getBalance() + amont);
                subAccount.setMoney(subAccount.getMoney() + actamont);
                break;
            case Minus:
                subAccount.setBalance(subAccount.getBalance() - amont);
                subAccount.setMoney(subAccount.getMoney() - actamont);
                break;
            default:
                result.setStatus(ResultStatusEnum.failed);
                result.setMessage("余额操作非法！");
                return result;
        }
        subAccount.addUpdateIncludes(new String[]{"balance", "money"});
        if (!subAccount.doUpdate(connectionFactory)) {
            log.error("修改账户余额失败：code=" + subAccount.getCode());
            result.setStatus(ResultStatusEnum.failed);
            result.setMessage("修改账户余额失败");
        } else {
            if (subAccount.getClass().equals(CustSubAccount.class)) {
                CustSubAccount custSubAccount = (CustSubAccount) subAccount;
                CustChangeLog custChangeLog = new CustChangeLog();
                custChangeLog.setCustid(custSubAccount.getCustid());
                custChangeLog.setType(custSubAccount.getType());
                custChangeLog.setChangedate(CommonTools.getNowTimeString());
                custChangeLog.setChangedirect(changeBalanceDirectEnum);
                custChangeLog.setAmont(amont);
                custChangeLog.setMoney(actamont);
                custChangeLog.doCreate(connectionFactory);
            }
            result.setAfterbalance(subAccount.getBalance());
            result.setAftermoney(subAccount.getMoney());
            result.setStatus(ResultStatusEnum.success);
        }
        return result;
    }

    private static final Logger log = Logger.getLogger(SubAccountBalanceControl.class);

}

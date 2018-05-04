package com.olink.account.core.accounting;

import com.olink.account.enumration.AccountItemTypeEnum;
import com.olink.account.enumration.BalanceDirectEnum;
import com.olink.account.core.AccountException;
import com.olink.account.model.trade.accounting.EntryItemFlow;
import com.olink.account.model.trade.dictionary.D_AccountItem;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

/**
 * Created by zhangbin on 2016/11/2.
 * 会计分录流水控制
 */
class AccountingEntryFlowControl {

    /**
     * 创建会计分录流水
     *
     * @param orderno           交易订单号
     * @param businessid        会计主体B户id
     * @param amont             发生额
     * @param accountdate       会计日期
     * @param statementdate     会计出账日期
     * @param bindaccountdate   会计扎账日期
     * @param accountsubitem    第四级科目对象
     * @param balanceDirectEnum 余额方向
     * @param connectionFactory 数据库连接对象
     */
    static void doRecord(String orderno, String businessid, double amont, String accountdate, String statementdate, String bindaccountdate, D_AccountItem accountsubitem, BalanceDirectEnum balanceDirectEnum, ConnectionFactory connectionFactory) throws AccountException, EnumValueUndefinedException {
        D_AccountItem accountitemthird = (D_AccountItem) accountsubitem.getParentObj(connectionFactory);
        D_AccountItem accountitemsecd = (D_AccountItem) accountitemthird.getParentObj(connectionFactory);
        D_AccountItem accountitemfirst = (D_AccountItem) accountitemsecd.getParentObj(connectionFactory);
        EntryItemFlow entryItemFlow = new EntryItemFlow();
        entryItemFlow.setOrderno(orderno);
        entryItemFlow.setBusinessid(businessid);
        entryItemFlow.setAccountitemfirstname(accountitemfirst.getName());
        entryItemFlow.setAccountitemfirstcode(accountitemfirst.getCode());
        entryItemFlow.setAccountitemsecdname(accountitemsecd.getName());
        entryItemFlow.setAccountitemsecdcode(accountitemsecd.getCode());
        entryItemFlow.setAccountitemthirdname(accountitemthird.getName());
        entryItemFlow.setAccountitemthirdcode(accountitemthird.getCode());
        entryItemFlow.setAccountsubitemname(accountsubitem.getName());
        entryItemFlow.setAccountsubitemcode(accountsubitem.getCode());
        entryItemFlow.setType(AccountItemTypeEnum.getEnum(Integer.valueOf(accountsubitem.getField1())));
        entryItemFlow.setBalancedirect(balanceDirectEnum);
        entryItemFlow.setAmont(amont);
        entryItemFlow.setAccountdate(accountdate);
        entryItemFlow.setStatementdate(statementdate);
        entryItemFlow.setBindaccountdate(bindaccountdate);
        entryItemFlow.setCreatedate(CommonTools.getNowTimeString());
        if (!entryItemFlow.doCreate(connectionFactory)) {
            throw new AccountException("创建内部科目流水失败");
        }
    }

}

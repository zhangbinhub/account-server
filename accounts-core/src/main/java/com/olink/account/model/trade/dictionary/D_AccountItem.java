package com.olink.account.model.trade.dictionary;

import com.olink.account.enumration.StatusEnum;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.annotation.ADBTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/9/8.
 * 会计科目
 */
@ADBTable(tablename = "acc_dic_account_item")
public class D_AccountItem extends Dictionary {

    public String getParentcode() {
        return parentcode;
    }

    public void setParentcode(String parentcode) {
        this.parentcode = parentcode;
    }

    private String parentcode;

    /**
     * 获取可用的会计科目下立子账户
     *
     * @param parentcode        第三级科目编码
     * @param code              第四级序号
     * @param connectionFactory 数据库连接对象
     * @return 会计科目子账户对象
     */
    public static D_AccountItem getAccountSubItem(String parentcode, String code, ConnectionFactory connectionFactory) {
        Map<String, Object> params = new HashMap<>();
        params.put(D_AccountItem.class.getCanonicalName() + ".code", parentcode);
        params.put(D_AccountItem.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
        D_AccountItem accountItem;
        if (connectionFactory == null) {
            accountItem = (D_AccountItem) getInstance(params, D_AccountItem.class, null);
        } else {
            accountItem = (D_AccountItem) getInstance(params, D_AccountItem.class, null, connectionFactory);
        }
        D_AccountItem accountSubItem;
        params.clear();
        params.put(D_AccountItem.class.getCanonicalName() + ".parentid", accountItem.getId());
        params.put(D_AccountItem.class.getCanonicalName() + ".code", code);
        params.put(D_AccountItem.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
        if (connectionFactory == null) {
            accountSubItem = (D_AccountItem) getInstance(params, D_AccountItem.class, null);
        } else {
            accountSubItem = (D_AccountItem) getInstance(params, D_AccountItem.class, null, connectionFactory);
        }
        return accountSubItem;
    }

    /**
     * 获取可用的会计科目下立子账户
     *
     * @param parentcode 第三级科目编码
     * @param code       第四级序号
     * @return 会计科目子账户对象
     */
    public static D_AccountItem getAccountSubItem(String parentcode, String code) {
        return getAccountSubItem(parentcode, code, null);
    }

}

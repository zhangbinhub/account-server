package com.olink.account.model.trade.customer;

import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;

import java.math.BigDecimal;

/**
 * C户子表子账户信息
 * Created by Shepherd on 2016-08-24.
 */
@ADBTable(tablename = "acc_cust_subaccount")
public class CustSubAccount extends SubAccount {

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public double getRatio() {
        return ratio.doubleValue();
    }

    public void setRatio(double ratio) {
        this.ratio = BigDecimal.valueOf(ratio);
    }

    @ADBTableField(name = "custid", fieldType = DBTableFieldType.String)
    private String custid;//	C户客户号

    private BigDecimal ratio;// 兑换比例

    public CustSubAccount() {
        super();
    }

    public CustSubAccount(String id, String custid, String code, int status, String type, String createdate, double balance, double money) {
        this.setId(id);
        this.setCode(code);
        this.setStatus(status);
        this.setType(type);
        this.setCreatedate(createdate);
        this.setBalance(balance);
        this.setMoney(money);
        this.custid = custid;
    }
}

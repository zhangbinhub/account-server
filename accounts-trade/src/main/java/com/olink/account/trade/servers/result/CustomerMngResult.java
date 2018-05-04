package com.olink.account.trade.servers.result;

import com.olink.account.base.impl.BaseServerResult;

/**
 * Created by zhangbin on 2016/12/9.
 * 信息维护结果
 */
public class CustomerMngResult extends BaseServerResult {

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    private String custid;

}

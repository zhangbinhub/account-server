package com.olink.account.base.impl;

import com.olink.account.base.IBaseServer;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.packages.Service;
import com.olink.account.service.IBusinessService;
import com.olink.account.service.impl.BusinessService;
import com.olink.account.utils.Utility;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;

/**
 * Created by zhang on 2016/7/22.
 * 接口基类
 */
public abstract class BaseServer implements IBaseServer {

    protected HttpServletRequestAcp request;

    protected String serverno;

    private String customerid;

    protected String validatecode;

    protected Service service;


    public BaseServer(HttpServletRequestAcp request) {
        this.request = request;
    }

    protected boolean validateAmont(String amont) {
        return Utility.isAvailableAmont(amont);
    }

    protected BusinessAccount getCurrBusinessAccount() {
        IBusinessService businessService = new BusinessService();
        return businessService.findBusinessAccountById(customerid);
    }

}

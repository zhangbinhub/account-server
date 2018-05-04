package com.olink.account.base.impl;

import com.olink.account.base.IBaseServerResult;
import com.olink.account.enumration.ResultStatusEnum;

/**
 * Created by zhang on 2016/7/23.
 * 接口处理结果类,head
 */
public class BaseServerResult implements IBaseServerResult {

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getValidatecode() {
        return validatecode;
    }

    public void setValidatecode(String validatecode) {
        this.validatecode = validatecode;
    }

    protected int status = ResultStatusEnum.success.getCode();

    private String message;

    private String validatecode;

}

package com.olink.account.core;

import pers.acp.tools.exceptions.base.BaseException;

/**
 * Created by zhangbin on 2016/11/1.
 * 核心记账异常
 */
public class AccountException extends BaseException {

    public AccountException(String message) {
        super(message);
    }
}

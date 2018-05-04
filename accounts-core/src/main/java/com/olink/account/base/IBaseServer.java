package com.olink.account.base;

import com.olink.account.base.impl.BaseServerResult;
import com.olink.account.exception.ServerException;

/**
 * Created by zhangbin on 2016/9/8.
 * 接口服务
 */
public interface IBaseServer {

    /**
     * 接口处理方法
     *
     * @return 接口处理结果
     */
    BaseServerResult doServer() throws ServerException;

}

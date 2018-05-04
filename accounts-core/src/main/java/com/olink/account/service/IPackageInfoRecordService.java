package com.olink.account.service;

import com.olink.account.base.IBaseTradeService;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;

/**
 * Created by zhangbin on 2016/9/17.
 * 请求报文记录实体
 */
public interface IPackageInfoRecordService extends IBaseTradeService {

    /**
     * 检查报文是否重复提交
     *
     * @param request        http请求对象
     * @param validatecode   根据报文内容计算的报文验证编码
     * @param minSpacingTime 重复请求的最小间隔时间，单位毫秒，默认2分钟：120000
     * @return 报文在规定时间内是否重复提交
     */
    boolean doCheckRepeat(HttpServletRequestAcp request, String validatecode, long minSpacingTime);

    /**
     * 删除报文提交记录
     *
     * @param validatecode 根据报文内容计算的报文验证编码
     */
    void doDeleteRecord(String validatecode);
}

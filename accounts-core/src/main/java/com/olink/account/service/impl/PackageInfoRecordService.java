package com.olink.account.service.impl;

import com.olink.account.base.impl.BaseTradeService;
import com.olink.account.dao.IPackageInfoRecordDao;
import com.olink.account.dao.impl.PackageInfoRecordDao;
import com.olink.account.service.IPackageInfoRecordService;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;

/**
 * Created by zhangbin on 2016/9/17.
 * 请求报文记录实体
 */
public class PackageInfoRecordService extends BaseTradeService implements IPackageInfoRecordService {

    private IPackageInfoRecordDao dao = new PackageInfoRecordDao();

    @Override
    public boolean doCheckRepeat(HttpServletRequestAcp request, String validatecode, long minSpacingTime) {
        return dao.doCheckRepeat(request, validatecode, minSpacingTime);
    }

    @Override
    public void doDeleteRecord(String validatecode) {
        dao.doDeleteRecord(validatecode);
    }

}

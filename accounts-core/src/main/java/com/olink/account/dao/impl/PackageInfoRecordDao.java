package com.olink.account.dao.impl;

import com.olink.account.base.impl.BaseTradeDao;
import com.olink.account.dao.IPackageInfoRecordDao;
import com.olink.account.model.trade.packages.PackageInfoRecord;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;
import pers.acp.communications.server.http.servlet.tools.IpTools;
import pers.acp.tools.common.DBConTools;

import java.util.Date;

/**
 * Created by zhangbin on 2016/9/17.
 * 请求报文记录实体
 */
public class PackageInfoRecordDao extends BaseTradeDao implements IPackageInfoRecordDao {

    public PackageInfoRecordDao() {
        super();
    }

    public PackageInfoRecordDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public void doDelete(final long minSpacingTime) {
        long nowtime = new Date().getTime();
        PackageInfoRecord packageInfoRecord = new PackageInfoRecord();
        String sql = "delete from " + packageInfoRecord.getCurrTableName() + " where receivetime<=" + (nowtime - minSpacingTime);
        dbTools.doUpdate(sql);
    }

    @Override
    public void doDeleteRecord(String validatecode) {
        PackageInfoRecord packageInfoRecord = (PackageInfoRecord) PackageInfoRecord.getInstance(validatecode, PackageInfoRecord.class, null, dbTools.getDbcon());
        if (packageInfoRecord != null) {
            packageInfoRecord.doDelete(dbTools.getDbcon());
        }
    }

    @Override
    public boolean doCheckRepeat(HttpServletRequestAcp request, String validatecode, long minSpacingTime) {
        long nowtime = new Date().getTime();
        PackageInfoRecord packageInfoRecord = (PackageInfoRecord) PackageInfoRecord.getInstance(validatecode, PackageInfoRecord.class, null, dbTools.getDbcon());
        if (packageInfoRecord != null && packageInfoRecord.getReceivetime() + minSpacingTime > nowtime) {
            return true;
        } else {
            doDelete(minSpacingTime);
            packageInfoRecord = new PackageInfoRecord();
            packageInfoRecord.setValidatecode(validatecode);
            packageInfoRecord.setReceivetime(nowtime);
            packageInfoRecord.setClientip(IpTools.getRemoteRealIP(request));
            return !packageInfoRecord.doCreate(dbTools.getDbcon());
        }
    }
}

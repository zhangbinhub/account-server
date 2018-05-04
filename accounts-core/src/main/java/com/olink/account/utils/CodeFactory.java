package com.olink.account.utils;

import com.olink.account.enumration.CustomerType;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.CustomerIdSerial;
import com.olink.account.model.trade.dictionary.Dictionary;
import org.apache.log4j.Logger;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.common.DBConTools;
import pers.acp.tools.security.key.KeyManagement;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2017/5/9.
 * 编码工厂
 */
public class CodeFactory {

    private static final Logger log = Logger.getLogger(CodeFactory.class);

    /**
     * 生成C户客户号
     *
     * @return C户客户号
     */
    public static String generateCustId(String regioncode, DBConTools dbConTools) {
        String timestamp = CommonTools.getDateTimeString(new Date(), "yyyyMMddHHmmssSSS");
        Map<String, Object> param = new HashMap<>();
        param.put(CustomerIdSerial.class.getCanonicalName() + ".regioncode", regioncode);
        param.put(CustomerIdSerial.class.getCanonicalName() + ".custtype", CustomerType.customer.getValue());
        param.put(CustomerIdSerial.class.getCanonicalName() + ".timestamp", timestamp);
        CustomerIdSerial customerIdSerial = (CustomerIdSerial) CustomerIdSerial.getInstanceByLock(param, CustomerIdSerial.class, null, dbConTools.getDbcon());
        int number = 1;
        if (customerIdSerial != null) {
            number = customerIdSerial.getNumber();
            customerIdSerial.setNumber(number + 1);
            if (!customerIdSerial.doUpdate(dbConTools.getDbcon())) {
                log.error("生成客户号失败！");
                return null;
            }
        } else {
            customerIdSerial = new CustomerIdSerial();
            customerIdSerial.setRegioncode(regioncode);
            customerIdSerial.setCusttype(CustomerType.customer);
            customerIdSerial.setTimestamp(timestamp);
            customerIdSerial.setNumber(number + 1);
            customerIdSerial.setCreatedate(CommonTools.getNowTimeString());
            customerIdSerial.setModifydate(CommonTools.getNowTimeString());
            if (!customerIdSerial.doCreate(dbConTools.getDbcon())) {
                log.error("生成客户号失败！");
                return null;
            }
        }
        return regioncode + CustomerType.customer.getValue() + CommonTools.getDateTimeString(new Date(), "yyyyMMddHHmmssSSS") + CommonTools.strFillIn(number + "", 3, 0, "0");
    }

    /**
     * 生成C户虚拟账户编码
     *
     * @param custid      C户客户号
     * @param accountType 虚拟账户类型
     * @return 虚拟账户编码
     */
    public static String generateCustSubCode(String custid, Dictionary accountType) {
        return custid + accountType.getCode() + (int) (Math.random() * 900 + 100);
    }

    /**
     * 生成订单号
     *
     * @param ordertype 订单类型
     * @return 订单号
     */
    public static String generateOrderNo(int ordertype) {
        String ordertypeStr = CommonTools.strFillIn(ordertype + "", 4, 0, "0");
        return ordertypeStr + CommonTools.getDateTimeString(new Date(), "yyMMddHHmmssSSS") + KeyManagement.getRandomString(KeyManagement.RANDOM_NUMBER, 6);
    }

    /**
     * 生成B户内部账号
     *
     * @param channel              B户渠道号
     * @param accountitemfirstcode 一级科目编码
     * @param accountitemsecdcode  二级科目编码
     * @param accountitemthirdcode 三级科目编码
     * @param accountsubitemcode   科目下立子账户编码
     * @return B户内部账号
     */
    public static String generateBusinessInnerId(String channel, String accountitemfirstcode, String accountitemsecdcode, String accountitemthirdcode, String accountsubitemcode) throws ServerException {
        if (!accountitemsecdcode.startsWith(accountitemfirstcode)) {
            throw new ServerException("科目编码不符合规范：二级科目编号前缀必须是一级科目编号");
        }
        if (!accountitemthirdcode.startsWith(accountitemsecdcode)) {
            throw new ServerException("科目编码不符合规范：三级科目编号前缀必须是二级科目编号");
        }
        return channel + accountitemthirdcode + accountsubitemcode;
    }

}

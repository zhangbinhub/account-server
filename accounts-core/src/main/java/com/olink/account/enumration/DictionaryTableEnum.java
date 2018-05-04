package com.olink.account.enumration;

import com.olink.account.model.trade.dictionary.*;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Shepherd on 2016-08-08.
 * 数据字典表
 */
public enum DictionaryTableEnum {

    /**
     * 账务系统参数配置
     */
    sysParam(D_SysParam.class, 0),

    /**
     * 会计科目表
     */
    accountItem(D_AccountItem.class, 1),

    /**
     * 行政区域
     */
    region(D_Region.class, 2),

    /**
     * 证件类型
     */
    certType(D_CertType.class, 3),

    /**
     * 行业分类
     */
    industry(D_Industry.class, 4),

    /**
     * 虚拟账户类型
     */
    accountType(D_AccountType.class, 5),

    /**
     * B户绑定类型
     */
    businessBindType(D_BusinessBindType.class, 6),

    /**
     * C户绑定类型
     */
    custBindType(D_CustBindType.class, 7),

    /**
     * 支付方式
     */
    payType(D_PayType.class, 8),

    /**
     * 订单类型
     */
    orderType(D_OrderType.class, 9),

    /**
     * 订单状态
     */
    orderStatus(D_OrderStatus.class, 10),

    /**
     * 日终批处理配置
     */
    batch(D_Batch.class, 11),

    /**
     * 日期配置
     */
    dateParam(D_DateParam.class, 12);

    private Class<? extends Dictionary> cls;

    private Integer value;

    private static Map<Integer, DictionaryTableEnum> map;

    static {
        map = new HashMap<>();
        for (DictionaryTableEnum type : values()) {
            map.put(type.getValue(), type);
        }
    }

    DictionaryTableEnum(Class<? extends Dictionary> cls, Integer value) {
        this.cls = cls;
        this.value = value;
    }

    public Integer getValue() {
        return this.value;
    }

    public Class<? extends Dictionary> getDictionaryClass() {
        return this.cls;
    }

    public Boolean equals(Integer integer) {
        return this.value.equals(integer);
    }

    public static DictionaryTableEnum getEnum(Integer value) throws EnumValueUndefinedException {
        if (map.containsKey(value)) {
            return map.get(value);
        } else {
            throw new EnumValueUndefinedException(DictionaryTableEnum.class, value);
        }
    }
}

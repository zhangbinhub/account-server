package com.olink.account.model.trade.dictionary;

import com.olink.account.enumration.StatusEnum;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/11/21.
 * 账务系统参数
 */
@ADBTable(tablename = "acc_dic_sysparam")
public class D_SysParam extends Dictionary {

    /**
     * 获取可用的系统参数值
     *
     * @param paramCode         参数编码
     * @param connectionFactory 数据库连接对象
     * @return 参数值
     */
    public static String getSysParamAvailable(String paramCode, ConnectionFactory connectionFactory) {
        Map<String, Object> params = new HashMap<>();
        params.put(D_SysParam.class.getCanonicalName() + ".name", paramCode);
        params.put(D_SysParam.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
        D_SysParam sysParam;
        if (connectionFactory == null) {
            sysParam = (D_SysParam) getInstance(params, D_SysParam.class, null);
        } else {
            sysParam = (D_SysParam) getInstance(params, D_SysParam.class, null, connectionFactory);
        }
        String value = "";
        if (sysParam != null) {
            value = sysParam.getCode().trim();
        }
        return value;
    }

    /**
     * 获取可用的系统参数值
     *
     * @param paramCode 参数编码
     * @return 参数值
     */
    public static String getSysParamAvailable(String paramCode) {
        return getSysParamAvailable(paramCode, null);
    }

    /**
     * 判断系统参数是否可用
     *
     * @param paramCode         参数编码
     * @param connectionFactory 数据库连接对象
     * @return true|false
     */
    public static boolean isSysParamAvailable(String paramCode, ConnectionFactory connectionFactory) {
        Map<String, Object> params = new HashMap<>();
        params.put(D_SysParam.class.getCanonicalName() + ".name", paramCode);
        D_SysParam sysParam;
        if (connectionFactory == null) {
            sysParam = (D_SysParam) getInstance(params, D_SysParam.class, null);
        } else {
            sysParam = (D_SysParam) getInstance(params, D_SysParam.class, null, connectionFactory);
        }
        if (sysParam != null) {
            try {
                return sysParam.getStatus().equals(StatusEnum.activate);
            } catch (EnumValueUndefinedException e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 判断系统参数是否可用
     *
     * @param paramCode 参数编码
     * @return true|false
     */
    public static boolean isSysParamAvailable(String paramCode) {
        return isSysParamAvailable(paramCode, null);
    }

    /**
     * 获取外部交易是否可用
     *
     * @param connectionFactory 数据库连接对象
     * @return true|false
     */
    public static boolean getOuterTradeEnabled(ConnectionFactory connectionFactory) {
        String value = getSysParamAvailable("outertrade", connectionFactory).toLowerCase();
        if ("true".equals(value) || "false".equals(value)) {
            return Boolean.valueOf(value);
        } else {
            return false;
        }
    }

    /**
     * 获取外部交易是否可用
     *
     * @return true|false
     */
    public static boolean getOuterTradeEnabled() {
        return getOuterTradeEnabled(null);
    }

}

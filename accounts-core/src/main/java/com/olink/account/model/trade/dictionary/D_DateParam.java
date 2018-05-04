package com.olink.account.model.trade.dictionary;

import com.olink.account.enumration.StatusEnum;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.annotation.ADBTable;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhangbin on 2016/12/15.
 * 日期参数
 */
@ADBTable(tablename = "acc_dic_dateparam")
public class D_DateParam extends Dictionary {

    /**
     * 获取可用的参数对象
     *
     * @param paramCode         参数编码
     * @param connectionFactory 数据库连接对象
     * @return 参数对象
     */
    public static D_DateParam getDictionaryAvailable(String paramCode, ConnectionFactory connectionFactory) {
        Map<String, Object> params = new HashMap<>();
        params.put(D_DateParam.class.getCanonicalName() + ".name", paramCode);
        params.put(D_DateParam.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
        D_DateParam dateParam;
        if (connectionFactory == null) {
            dateParam = (D_DateParam) getInstance(params, D_DateParam.class, null);
        } else {
            dateParam = (D_DateParam) getInstance(params, D_DateParam.class, null, connectionFactory);
        }
        return dateParam;
    }

    /**
     * 获取可用的参数值
     *
     * @param paramCode         参数编码
     * @param connectionFactory 数据库连接对象
     * @return 参数值
     */
    private static String getParamAvailable(String paramCode, ConnectionFactory connectionFactory) {
        D_DateParam dateParam = getDictionaryAvailable(paramCode, connectionFactory);
        String value = "";
        if (dateParam != null) {
            value = dateParam.getCode().trim();
        }
        return value;
    }

    /**
     * 获取系统会计日期对象
     *
     * @param connectionFactory 数据库连接对象
     * @return 系统会计日期对象
     */
    public static D_DateParam getAccountDateObj(ConnectionFactory connectionFactory) {
        return getDictionaryAvailable("accountdate", connectionFactory);
    }

    /**
     * 获取系统会计日期对象
     *
     * @return 系统会计日期对象
     */
    public static D_DateParam getAccountDateObj() {
        return getAccountDateObj(null);
    }

    /**
     * 获取系统会计日期
     *
     * @param connectionFactory 数据库连接对象
     * @return 会计日期
     */
    public static String getAccountDate(ConnectionFactory connectionFactory) {
        return getParamAvailable("accountdate", connectionFactory);
    }

    /**
     * 获取系统会计日期
     *
     * @return 会计日期
     */
    public static String getAccountDate() {
        return getAccountDate(null);
    }

    /**
     * 获取系统会计出账日号
     *
     * @param connectionFactory 数据库连接对象
     * @return 会计出账日号
     */
    public static String getStatementDateNo(ConnectionFactory connectionFactory) {
        return getParamAvailable("statementdateno", connectionFactory);
    }

    /**
     * 获取系统会计出账日号
     *
     * @return 会计出账日号
     */
    public static String getStatementDateNo() {
        return getStatementDateNo(null);
    }

    /**
     * 获取系统会计扎账日号
     *
     * @param connectionFactory 数据库连接对象
     * @return 会计扎账日号
     */
    public static String getBindAccountDateNo(ConnectionFactory connectionFactory) {
        return getParamAvailable("bindaccountdateno", connectionFactory);
    }

    /**
     * 获取系统会计扎账日号
     *
     * @return 会计扎账日号
     */
    public static String getBindAccountDateNo() {
        return getBindAccountDateNo(null);
    }

}

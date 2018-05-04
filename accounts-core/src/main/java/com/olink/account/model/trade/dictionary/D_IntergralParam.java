package com.olink.account.model.trade.dictionary;

import com.olink.account.enumration.IntergralParamTypeEnum;
import com.olink.account.enumration.StatusEnum;
import com.olink.account.enumration.ValidityDateTypeEnum;
import com.olink.account.utils.DBAccess;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.ConnectionFactory;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/12/15.
 * 积分参数
 */
@ADBTable(tablename = "acc_dic_intergralparam")
public class D_IntergralParam extends Dictionary {

    /**
     * 获取可用的参数值
     *
     * @param intergralParamTypeEnum 参数类型
     * @param connectionFactory      数据库连接对象
     * @return 参数值
     */
    private static String getParamAvailable(IntergralParamTypeEnum intergralParamTypeEnum, ConnectionFactory connectionFactory) {
        Map<String, Object> params = new HashMap<>();
        params.put(D_IntergralParam.class.getCanonicalName() + ".type", intergralParamTypeEnum.getValue() + "");
        params.put(D_IntergralParam.class.getCanonicalName() + ".status", StatusEnum.activate.getValue());
        if (connectionFactory == null) {
            connectionFactory = DBAccess.getTradeInstance().getDBTool().getDbcon();
        }
        List<DBTable> intergralParamList = connectionFactory.doQueryForObjList(params, D_IntergralParam.class, null, "order by ${" + D_IntergralParam.class.getCanonicalName() + ".field2} desc");
        String sysdate = CommonTools.getNowString();
        String accdate = D_DateParam.getAccountDate(connectionFactory);
        String value = "";
        for (DBTable dbTable : intergralParamList) {
            D_IntergralParam intergralParam = (D_IntergralParam) dbTable;
            if ((ValidityDateTypeEnum.accountdate.getValue() + "").equals(intergralParam.getField1())) {
                if (intergralParam.getField2().trim().compareTo(accdate) <= 0) {
                    value = intergralParam.getCode().trim();
                    break;
                }
            }
            if ((ValidityDateTypeEnum.sysdate.getValue() + "").equals(intergralParam.getField1())) {
                if (intergralParam.getField2().trim().compareTo(sysdate) <= 0) {
                    value = intergralParam.getCode().trim();
                    break;
                }
            }
        }
        return value;
    }

    /**
     * 获取积分兑换比例
     *
     * @param connectionFactory 数据库连接对象
     * @return 积分兑换比例
     */
    public static double getIntegralRatio(ConnectionFactory connectionFactory) {
        double ratio = 1.00D;
        String value = getParamAvailable(IntergralParamTypeEnum.ratio, connectionFactory);
        if (!CommonTools.isNullStr(value)) {
            try {
                ratio = Double.valueOf(value);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return ratio;
    }

    /**
     * 获取积分兑换比例
     *
     * @return 积分兑换比例
     */
    public static double getIntegralRatio() {
        return getIntegralRatio(null);
    }

}

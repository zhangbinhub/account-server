package com.olink.account.model.trade.dictionary;

import com.olink.account.enumration.CalculateTypeEnum;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.math.BigDecimal;

/**
 * Created by zhangbin on 2016/10/25.
 * 订单发生额阶梯分解规则
 */
@ADBTable(tablename = "acc_dic_ordertype_amontdetailrule_steprule")
public class D_OrderTypeAmontDetailStepRule extends DBTable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAmontruleid() {
        return amontruleid;
    }

    public void setAmontruleid(String amontruleid) {
        this.amontruleid = amontruleid;
    }

    public double getBeginamont() {
        return beginamont.doubleValue();
    }

    public void setBeginamont(double beginamont) {
        this.beginamont = BigDecimal.valueOf(beginamont);
    }

    public double getEndamont() {
        return endamont.doubleValue();
    }

    public void setEndamont(double endamont) {
        this.endamont = BigDecimal.valueOf(endamont);
    }

    public CalculateTypeEnum getCalculatetype() throws EnumValueUndefinedException {
        return CalculateTypeEnum.getEnum(calculatetype);
    }

    public void setCalculatetype(CalculateTypeEnum calculatetype) {
        this.calculatetype = calculatetype.getValue();
    }

    public double getParam() {
        return param.doubleValue();
    }

    public void setParam(double param) {
        this.param = BigDecimal.valueOf(param);
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getModifydate() {
        return modifydate;
    }

    public void setModifydate(String modifydate) {
        this.modifydate = modifydate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "amontruleid", fieldType = DBTableFieldType.String, allowNull = false)
    private String amontruleid;//发生额分解规则id

    @ADBTableField(name = "beginamont", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal beginamont;//开始额

    @ADBTableField(name = "endamont", fieldType = DBTableFieldType.Decimal)
    private BigDecimal endamont;//结束额

    @ADBTableField(name = "calculatetype", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int calculatetype;//计算方式

    @ADBTableField(name = "param", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal param;//计算参数

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;//	创建时间

    @ADBTableField(name = "modifydate", fieldType = DBTableFieldType.String, allowNull = false)
    private String modifydate;//	修改时间

    @ADBTableField(name = "userid", fieldType = DBTableFieldType.String, allowNull = false)
    private String userid;//	创建人

}
package com.olink.account.model.trade.dictionary;

import com.olink.account.enumration.*;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by zhangbin on 2016/10/25.
 * 订单发生额分解规则
 */
@ADBTable(tablename = "acc_dic_ordertype_amontdetailrule")
public class D_OrderTypeAmontDetailRule extends DBTable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrdertypeid() {
        return ordertypeid;
    }

    public void setOrdertypeid(String ordertypeid) {
        this.ordertypeid = ordertypeid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AmontRuleType getRuletype() throws EnumValueUndefinedException {
        return AmontRuleType.getEnum(ruletype);
    }

    public void setRuletype(AmontRuleType ruletype) {
        this.ruletype = ruletype.getValue();
    }

    public CalculateTypeEnum getCalculatetype() throws EnumValueUndefinedException {
        return CalculateTypeEnum.getEnum(calculatetype);
    }

    public void setCalculatetype(CalculateTypeEnum calculatetype) {
        this.calculatetype = calculatetype.getValue();
    }

    public CalculateModeEnum getCalculatemode() throws EnumValueUndefinedException {
        return CalculateModeEnum.getEnum(calculatemode);
    }

    public void setCalculatemode(CalculateModeEnum calculatemode) {
        this.calculatemode = calculatemode.getValue();
    }

    public int getBasictype() {
        return basictype;
    }

    public void setBasictype(int basictype) {
        this.basictype = basictype;
    }

    public double getParam() {
        return param.doubleValue();
    }

    public void setParam(double param) {
        this.param = BigDecimal.valueOf(param);
    }

    public double getMin() {
        return min.doubleValue();
    }

    public void setMin(double min) {
        this.min = BigDecimal.valueOf(min);
    }

    public double getMax() {
        return max.doubleValue();
    }

    public void setMax(double max) {
        this.max = BigDecimal.valueOf(max);
    }

    public DecomposeTypeEnum getDecomposetype() throws EnumValueUndefinedException {
        return DecomposeTypeEnum.getEnum(decomposetype);
    }

    public void setDecomposetype(DecomposeTypeEnum decomposetype) {
        this.decomposetype = decomposetype.getValue();
    }

    public DecimalProcessModeEnum getDecimalprocess() throws EnumValueUndefinedException {
        return DecimalProcessModeEnum.getEnum(decimalprocess);
    }

    public void setDecimalprocess(DecimalProcessModeEnum decimalprocess) {
        this.decimalprocess = decimalprocess.getValue();
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
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

    public double getAmont() {
        return amont;
    }

    public void setAmont(double amont) {
        this.amont = amont;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public List<DBTable> getStepRules() {
        return stepRules;
    }

    public void setStepRules(List<DBTable> stepRules) {
        this.stepRules = stepRules;
    }

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "ordertypeid", fieldType = DBTableFieldType.String, allowNull = false)
    private String ordertypeid;//订单类型id

    @ADBTableField(name = "name", fieldType = DBTableFieldType.String, allowNull = false)
    private String name;//名称

    @ADBTableField(name = "ruletype", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int ruletype;//名称

    @ADBTableField(name = "calculatetype", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int calculatetype;//发生额计算方式

    @ADBTableField(name = "calculatemode", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int calculatemode;//发生额计算模式

    @ADBTableField(name = "basictype", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int basictype;//计算基数类型

    @ADBTableField(name = "param", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal param;//计算参数

    @ADBTableField(name = "min", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal min;//最小值

    @ADBTableField(name = "max", fieldType = DBTableFieldType.Decimal, allowNull = false)
    private BigDecimal max;//最大值

    @ADBTableField(name = "decomposetype", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int decomposetype;//分解类型

    @ADBTableField(name = "decimalprocess", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int decimalprocess;//小数处理

    @ADBTableField(name = "sort", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int sort;//计算序号

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;//	创建时间

    @ADBTableField(name = "modifydate", fieldType = DBTableFieldType.String, allowNull = false)
    private String modifydate;//	修改时间

    @ADBTableField(name = "userid", fieldType = DBTableFieldType.String, allowNull = false)
    private String userid;//	创建人

    private double amont;//发生额

    private double balance;//计算后余额

    private List<DBTable> stepRules;//阶梯分解规则

}
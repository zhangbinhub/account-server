package com.olink.account.model.batch.info;

import com.olink.account.enumration.StatusEnum;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

/**
 * Created by zhangbin on 2016/11/18.
 * 历史数据表对应关系
 */
@ADBTable(tablename = "acc_batch_histables")
public class BatchHisTables extends DBTable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public StatusEnum getStatus() throws EnumValueUndefinedException {
        return StatusEnum.getEnum(status);
    }

    public void setStatus(StatusEnum status) {
        this.status = status.getValue();
    }

    public String getParent() {
        return parent;
    }

    public void setParent(String parent) {
        this.parent = parent;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
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

    public String getField1() {
        return field1;
    }

    public void setField1(String field1) {
        this.field1 = field1;
    }

    public String getField2() {
        return field2;
    }

    public void setField2(String field2) {
        this.field2 = field2;
    }

    public String getField3() {
        return field3;
    }

    public void setField3(String field3) {
        this.field3 = field3;
    }

    public String getField4() {
        return field4;
    }

    public void setField4(String field4) {
        this.field4 = field4;
    }

    public String getField5() {
        return field5;
    }

    public void setField5(String field5) {
        this.field5 = field5;
    }

    public String getField6() {
        return field6;
    }

    public void setField6(String field6) {
        this.field6 = field6;
    }

    public String getField7() {
        return field7;
    }

    public void setField7(String field7) {
        this.field7 = field7;
    }

    public String getField8() {
        return field8;
    }

    public void setField8(String field8) {
        this.field8 = field8;
    }

    public String getField9() {
        return field9;
    }

    public void setField9(String field9) {
        this.field9 = field9;
    }

    public String getField10() {
        return field10;
    }

    public void setField10(String field10) {
        this.field10 = field10;
    }

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "name", fieldType = DBTableFieldType.String, allowNull = false)
    private String name;//	名称

    @ADBTableField(name = "code", fieldType = DBTableFieldType.String, allowNull = false)
    private String code;//	编码

    @ADBTableField(name = "remark", fieldType = DBTableFieldType.String)
    private String remark;//	备注

    @ADBTableField(name = "type", fieldType = DBTableFieldType.String)
    private String type;//类型

    @ADBTableField(name = "status", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int status;//	状态 defualt 0

    @ADBTableField(name = "parent", fieldType = DBTableFieldType.String)
    private String parent;//	上级名称

    @ADBTableField(name = "parentid", fieldType = DBTableFieldType.String)
    private String parentid;//	上级ID

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;//	创建时间

    @ADBTableField(name = "modifydate", fieldType = DBTableFieldType.String)
    private String modifydate;//	修改时间

    @ADBTableField(name = "userid", fieldType = DBTableFieldType.String)
    private String userid;//	创建人

    @ADBTableField(name = "field1", fieldType = DBTableFieldType.String)
    private String field1;//	扩展字段1

    @ADBTableField(name = "field2", fieldType = DBTableFieldType.String)
    private String field2;//	扩展字段2

    @ADBTableField(name = "field3", fieldType = DBTableFieldType.String)
    private String field3;//	扩展字段3

    @ADBTableField(name = "field4", fieldType = DBTableFieldType.String)
    private String field4;//	扩展字段4

    @ADBTableField(name = "field5", fieldType = DBTableFieldType.String)
    private String field5;//	扩展字段5

    @ADBTableField(name = "field6", fieldType = DBTableFieldType.String)
    private String field6;//	扩展字段6

    @ADBTableField(name = "field7", fieldType = DBTableFieldType.String)
    private String field7;//	扩展字段7

    @ADBTableField(name = "field8", fieldType = DBTableFieldType.String)
    private String field8;//	扩展字段8

    @ADBTableField(name = "field9", fieldType = DBTableFieldType.String)
    private String field9;//	扩展字段9

    @ADBTableField(name = "field10", fieldType = DBTableFieldType.String)
    private String field10;//	扩展字段10

}

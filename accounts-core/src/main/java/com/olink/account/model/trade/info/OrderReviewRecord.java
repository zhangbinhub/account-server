package com.olink.account.model.trade.info;

import com.olink.account.enumration.ReviewResultEnum;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

/**
 * Created by zhangbin on 2016/10/10.
 * 订单审核记录表
 */
@ADBTable(tablename = "acc_order_reviewrecord")
public class OrderReviewRecord extends DBTable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(String orderNo) {
        this.orderNo = orderNo;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public ReviewResultEnum getResult() throws EnumValueUndefinedException {
        return ReviewResultEnum.getEnum(result);
    }

    public void setResult(ReviewResultEnum result) {
        this.result = result.getValue();
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountdate() {
        return accountdate;
    }

    public void setAccountdate(String accountdate) {
        this.accountdate = accountdate;
    }

    public String getReviewdate() {
        return reviewdate;
    }

    public void setReviewdate(String reviewdate) {
        this.reviewdate = reviewdate;
    }

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "orderNo", fieldType = DBTableFieldType.String, allowNull = false)
    private String orderNo;

    @ADBTableField(name = "content", fieldType = DBTableFieldType.String, allowNull = false)
    private String content;

    @ADBTableField(name = "result", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int result;

    @ADBTableField(name = "userid", fieldType = DBTableFieldType.String, allowNull = false)
    private String userid;

    @ADBTableField(name = "username", fieldType = DBTableFieldType.String, allowNull = false)
    private String username;

    @ADBTableField(name = "accountdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountdate;

    @ADBTableField(name = "reviewdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String reviewdate;
}

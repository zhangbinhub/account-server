package com.olink.account.model.trade.customer;

import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;

/**
 * Created by zhangbin on 2016/9/7.
 * B户绑定信息表
 */
@ADBTable(tablename = "acc_business_bindinfo")
public class BusinessBindInfo extends DBTable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBusinessid() {
        return businessid;
    }

    public void setBusinessid(String businessid) {
        this.businessid = businessid;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getCerttype() {
        return certtype;
    }

    public void setCerttype(String certtype) {
        this.certtype = certtype;
    }

    public String getCertno() {
        return certno;
    }

    public void setCertno(String certno) {
        this.certno = certno;
    }

    public String getPartner() {
        return partner;
    }

    public void setPartner(String partner) {
        this.partner = partner;
    }

    public String getPartnerkey() {
        return partnerkey;
    }

    public void setPartnerkey(String partnerkey) {
        this.partnerkey = partnerkey;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getAppsecret() {
        return appsecret;
    }

    public void setAppsecret(String appsecret) {
        this.appsecret = appsecret;
    }

    public String getAppsingn() {
        return appsingn;
    }

    public void setAppsingn(String appsingn) {
        this.appsingn = appsingn;
    }

    public String getBundleid() {
        return bundleid;
    }

    public void setBundleid(String bundleid) {
        this.bundleid = bundleid;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getSeller_email() {
        return seller_email;
    }

    public void setSeller_email(String seller_email) {
        this.seller_email = seller_email;
    }

    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    public String getPrivate_key() {
        return private_key;
    }

    public void setPrivate_key(String private_key) {
        this.private_key = private_key;
    }

    public String getEdituserid() {
        return edituserid;
    }

    public void setEdituserid(String edituserid) {
        this.edituserid = edituserid;
    }

    public String getAuthuserid() {
        return authuserid;
    }

    public void setAuthuserid(String authuserid) {
        this.authuserid = authuserid;
    }

    @ADBTablePrimaryKey(name = "id")
    private String id;

    @ADBTableField(name = "businessid", fieldType = DBTableFieldType.String, allowNull = false)
    private String businessid;

    @ADBTableField(name = "type", fieldType = DBTableFieldType.String, allowNull = false)
    private String type;

    @ADBTableField(name = "isdefault", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int isdefault;

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;

    @ADBTableField(name = "status", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int status;

    @ADBTableField(name = "bankname", fieldType = DBTableFieldType.String)
    private String bankname;

    @ADBTableField(name = "name", fieldType = DBTableFieldType.String)
    private String name;

    @ADBTableField(name = "telephone", fieldType = DBTableFieldType.String)
    private String telephone;

    @ADBTableField(name = "account", fieldType = DBTableFieldType.String)
    private String account;

    @ADBTableField(name = "certtype", fieldType = DBTableFieldType.String)
    private String certtype;

    @ADBTableField(name = "certno", fieldType = DBTableFieldType.String)
    private String certno;

    @ADBTableField(name = "partner", fieldType = DBTableFieldType.String)
    private String partner;

    @ADBTableField(name = "partnerkey", fieldType = DBTableFieldType.String)
    private String partnerkey;

    @ADBTableField(name = "appid", fieldType = DBTableFieldType.String)
    private String appid;

    @ADBTableField(name = "appsecret", fieldType = DBTableFieldType.String)
    private String appsecret;

    @ADBTableField(name = "appsingn", fieldType = DBTableFieldType.String)
    private String appsingn;

    @ADBTableField(name = "bundleid", fieldType = DBTableFieldType.String)
    private String bundleid;

    @ADBTableField(name = "package", fieldType = DBTableFieldType.String)
    private String packagename;

    @ADBTableField(name = "seller_email", fieldType = DBTableFieldType.String)
    private String seller_email;

    @ADBTableField(name = "seller_id", fieldType = DBTableFieldType.String)
    private String seller_id;

    @ADBTableField(name = "private_key", fieldType = DBTableFieldType.String)
    private String private_key;

    @ADBTableField(name = "edituserid", fieldType = DBTableFieldType.String)
    private String edituserid;//编辑人id

    @ADBTableField(name = "authuserid", fieldType = DBTableFieldType.String)
    private String authuserid;//授权人id

}

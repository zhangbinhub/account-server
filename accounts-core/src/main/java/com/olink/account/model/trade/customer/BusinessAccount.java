package com.olink.account.model.trade.customer;

import com.olink.account.enumration.AccountTypeEnum;
import com.olink.account.exception.ServerException;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;

import java.util.List;

/**
 * Created by zhangbin on 2016/9/7.
 * B户信息表
 */
@ADBTable(tablename = "acc_business_account")
public class BusinessAccount extends DBTable {

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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getBusinessname() {
        return businessname;
    }

    public void setBusinessname(String businessname) {
        this.businessname = businessname;
    }

    public String getRegioncode() {
        return regioncode;
    }

    public void setRegioncode(String regioncode) {
        this.regioncode = regioncode;
    }

    public String getRegionname() {
        return regionname;
    }

    public void setRegionname(String regionname) {
        this.regionname = regionname;
    }

    public String getIndustrycode() {
        return industrycode;
    }

    public void setIndustrycode(String industrycode) {
        this.industrycode = industrycode;
    }

    public String getIndustryname() {
        return industryname;
    }

    public void setIndustryname(String industryname) {
        this.industryname = industryname;
    }

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    public String getBusinesslicense() {
        return businesslicense;
    }

    public void setBusinesslicense(String businesslicense) {
        this.businesslicense = businesslicense;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getCertpicture() {
        return certpicture;
    }

    public void setCertpicture(String certpicture) {
        this.certpicture = certpicture;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getBankaccount() {
        return bankaccount;
    }

    public void setBankaccount(String bankaccount) {
        this.bankaccount = bankaccount;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
    }

    public String getBusinesskey() {
        return businesskey;
    }

    public void setBusinesskey(String businesskey) {
        this.businesskey = businesskey;
    }

    public String getPublickey() {
        return publickey;
    }

    public void setPublickey(String publickey) {
        this.publickey = publickey;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<DBTable> getSubAccounts() {
        return subAccounts;
    }

    public void setSubAccounts(List<DBTable> subAccounts) {
        this.subAccounts = subAccounts;
    }

    public List<DBTable> getBindInfos() {
        return bindInfos;
    }

    public void setBindInfos(List<DBTable> bindInfos) {
        this.bindInfos = bindInfos;
    }

    public String getSettlementtype() {
        return settlementtype;
    }

    public void setSettlementtype(String settlementtype) {
        this.settlementtype = settlementtype;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    @ADBTableField(name = "userid", fieldType = DBTableFieldType.String, allowNull = false)
    private String userid;

    @ADBTableField(name = "businessname", fieldType = DBTableFieldType.String, allowNull = false)
    private String businessname;

    @ADBTableField(name = "regioncode", fieldType = DBTableFieldType.String, allowNull = false)
    private String regioncode;

    @ADBTableField(name = "regionname", fieldType = DBTableFieldType.String, allowNull = false)
    private String regionname;

    @ADBTableField(name = "industrycode", fieldType = DBTableFieldType.String, allowNull = false)
    private String industrycode;

    @ADBTableField(name = "industryname", fieldType = DBTableFieldType.String, allowNull = false)
    private String industryname;

    @ADBTableField(name = "isdefault", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int isdefault;

    @ADBTableField(name = "businesslicense", fieldType = DBTableFieldType.String, allowNull = false)
    private String businesslicense;

    @ADBTableField(name = "name", fieldType = DBTableFieldType.String, allowNull = false)
    private String name;

    @ADBTableField(name = "certtype", fieldType = DBTableFieldType.String, allowNull = false)
    private String certtype;

    @ADBTableField(name = "certno", fieldType = DBTableFieldType.String, allowNull = false)
    private String certno;

    @ADBTableField(name = "certpicture", fieldType = DBTableFieldType.String, allowNull = false)
    private String certpicture;

    @ADBTableField(name = "telephone", fieldType = DBTableFieldType.String, allowNull = false)
    private String telephone;

    @ADBTableField(name = "bankaccount", fieldType = DBTableFieldType.String)
    private String bankaccount;

    @ADBTableField(name = "accountname", fieldType = DBTableFieldType.String)
    private String accountname;

    @ADBTableField(name = "bank", fieldType = DBTableFieldType.String)
    private String bank;

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;

    @ADBTableField(name = "creator", fieldType = DBTableFieldType.String, allowNull = false)
    private String creator;

    @ADBTableField(name = "custid", fieldType = DBTableFieldType.String)
    private String custid;

    @ADBTableField(name = "businesskey", fieldType = DBTableFieldType.String)
    private String businesskey;

    @ADBTableField(name = "publickey", fieldType = DBTableFieldType.String)
    private String publickey;

    @ADBTableField(name = "channel", fieldType = DBTableFieldType.String, allowNull = false)
    private String channel;

    @ADBTableField(name = "status", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int status;

    @ADBTableField(name = "settlementtype", fieldType = DBTableFieldType.String, allowNull = false)
    private String settlementtype;

    @ADBTableField(name = "email", fieldType = DBTableFieldType.String, allowNull = false)
    private String email;

    @ADBTableField(name = "edituserid", fieldType = DBTableFieldType.String)
    private String edituserid;//编辑人id

    @ADBTableField(name = "authuserid", fieldType = DBTableFieldType.String)
    private String authuserid;//授权人id

    private List<DBTable> subAccounts;

    private List<DBTable> bindInfos;

    /**
     * 根据虚拟账户类型获取子账户对象
     *
     * @param accountTypeEnum 账户类型
     * @return 账户对象
     */
    public BusinessSubAccount getSubAccountByType(AccountTypeEnum accountTypeEnum) throws ServerException {
        if (subAccounts != null && !subAccounts.isEmpty()) {
            for (DBTable dbTable : subAccounts) {
                BusinessSubAccount businessSubAccount = (BusinessSubAccount) dbTable;
                if (businessSubAccount.getType().equals(accountTypeEnum.getType())) {
                    return businessSubAccount;
                }
            }
        }
        throw new ServerException("找不到子账户：" + accountTypeEnum.getName());
    }
}

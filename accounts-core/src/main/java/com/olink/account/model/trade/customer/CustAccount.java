package com.olink.account.model.trade.customer;

import com.olink.account.enumration.AccountTypeEnum;
import com.olink.account.exception.ServerException;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.dbconnection.entity.DBTablePrimaryKeyType;

import java.util.List;

/**
 * C户信息表
 * Created by Shepherd on 2016-08-24.
 */
@ADBTable(tablename = "acc_cust_account")
public class CustAccount extends DBTable {

    @ADBTablePrimaryKey(name = "custid", pKeyType = DBTablePrimaryKeyType.String)
    private String custid;//	C户客户号

    @ADBTableField(name = "userid", fieldType = DBTableFieldType.String, allowNull = false)
    private String userid;//	用户id

    @ADBTableField(name = "nickname", fieldType = DBTableFieldType.String)
    private String nickname;//	昵称

    @ADBTableField(name = "name", fieldType = DBTableFieldType.String)
    private String name;//	姓名

    @ADBTableField(name = "certtype", fieldType = DBTableFieldType.String)
    private String certtype;//	证件类型

    @ADBTableField(name = "certno", fieldType = DBTableFieldType.String)
    private String certno;//	证件号码

    @ADBTableField(name = "status", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int status;//	状态

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;//	创建时间

    @ADBTableField(name = "channel", fieldType = DBTableFieldType.String, allowNull = false)
    private String channel;//	渠道

    @ADBTableField(name = "telephone", fieldType = DBTableFieldType.String, allowNull = false)
    private String telephone;//	电话号码

    @ADBTableField(name = "password", fieldType = DBTableFieldType.String)
    private String password;//支付密码

    @ADBTableField(name = "edituserid", fieldType = DBTableFieldType.String)
    private String edituserid;//编辑人id

    @ADBTableField(name = "authuserid", fieldType = DBTableFieldType.String)
    private String authuserid;//授权人id

    private String telephone_new;// 新电话号码

    private String password_new;// 新支付密码

    private String loginpwd;//登录密码

    private String loginpwd_new;//新登录密码

    private List<DBTable> subAccount;//子账户

    public CustAccount() {
        super();
    }

    public CustAccount(String custid, String userid, String nickname, String name, String certtype,
                       String certno, int status, String createdate, String channel,
                       String telephone, String password, String loginpwd, List<DBTable> subAccount) {
        this.custid = custid;
        this.userid = userid;
        this.nickname = nickname;
        this.name = name;
        this.certtype = certtype;
        this.certno = certno;
        this.status = status;
        this.createdate = createdate;
        this.channel = channel;
        this.telephone = telephone;
        this.loginpwd = loginpwd;
        this.password = password;
        this.subAccount = subAccount;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCustid() {
        return custid;
    }

    public void setCustid(String custid) {
        this.custid = custid;
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

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getTelephone_new() {
        return telephone_new;
    }

    public void setTelephone_new(String telephone_new) {
        this.telephone_new = telephone_new;
    }

    public String getLoginpwd() {
        return loginpwd;
    }

    public void setLoginpwd(String loginpwd) {
        this.loginpwd = loginpwd;
    }

    public String getLoginpwd_new() {
        return loginpwd_new;
    }

    public void setLoginpwd_new(String loginpwd_new) {
        this.loginpwd_new = loginpwd_new;
    }

    public String getPassword_new() {
        return password_new;
    }

    public void setPassword_new(String password_new) {
        this.password_new = password_new;
    }

    public List<DBTable> getSubAccount() {
        return subAccount;
    }

    public void setSubAccount(List<DBTable> subAccount) {
        this.subAccount = subAccount;
    }

    /**
     * 根据虚拟账户类型获取子账户对象
     *
     * @param accountTypeEnum 账户类型
     * @return 账户对象
     */
    public CustSubAccount getSubAccountByType(AccountTypeEnum accountTypeEnum) throws ServerException {
        if (subAccount != null && !subAccount.isEmpty()) {
            for (DBTable dbTable : subAccount) {
                CustSubAccount custSubAccount = (CustSubAccount) dbTable;
                if (custSubAccount.getType().equals(accountTypeEnum.getType())) {
                    return custSubAccount;
                }
            }
        }
        throw new ServerException("找不到子账户：" + accountTypeEnum.getName());
    }
}

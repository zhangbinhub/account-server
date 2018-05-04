package com.olink.account.model.batch.info;

import com.olink.account.enumration.BatchTaskStatus;
import com.olink.account.enumration.BatchTaskType;
import pers.acp.tools.dbconnection.annotation.ADBTable;
import pers.acp.tools.dbconnection.annotation.ADBTableField;
import pers.acp.tools.dbconnection.annotation.ADBTablePrimaryKey;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.dbconnection.entity.DBTableFieldType;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

/**
 * Created by zhangbin on 2016/12/30.
 * 任务处理情况
 */
@ADBTable(tablename = "acc_batch_situation")
public class BatchSituation extends DBTable {

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTaskname() {
        return taskname;
    }

    public void setTaskname(String taskname) {
        this.taskname = taskname;
    }

    public String getTaskclassname() {
        return taskclassname;
    }

    public void setTaskclassname(String taskclassname) {
        this.taskclassname = taskclassname;
    }

    public String getTaskpackagename() {
        return taskpackagename;
    }

    public void setTaskpackagename(String taskpackagename) {
        this.taskpackagename = taskpackagename;
    }

    public int getTaskno() {
        return taskno;
    }

    public void setTaskno(int taskno) {
        this.taskno = taskno;
    }

    public BatchTaskType getTasktype() throws EnumValueUndefinedException {
        return BatchTaskType.getEnum(tasktype);
    }

    public void setTasktype(BatchTaskType tasktype) {
        this.tasktype = tasktype.getValue();
    }

    public String getPackagelastmodifydate() {
        return packagelastmodifydate;
    }

    public void setPackagelastmodifydate(String packagelastmodifydate) {
        this.packagelastmodifydate = packagelastmodifydate;
    }

    public String getPrevtaskclassname() {
        return prevtaskclassname;
    }

    public void setPrevtaskclassname(String prevtaskclassname) {
        this.prevtaskclassname = prevtaskclassname;
    }

    public Integer getPrevtaskno() {
        return prevtaskno;
    }

    public void setPrevtaskno(Integer prevtaskno) {
        this.prevtaskno = prevtaskno;
    }

    public BatchTaskStatus getStatus() throws EnumValueUndefinedException {
        return BatchTaskStatus.getEnum(status);
    }

    public void setStatus(BatchTaskStatus status) {
        this.status = status.getValue();
    }

    public String getAccountdate() {
        return accountdate;
    }

    public void setAccountdate(String accountdate) {
        this.accountdate = accountdate;
    }

    public String getLastmodifydate() {
        return lastmodifydate;
    }

    public void setLastmodifydate(String lastmodifydate) {
        this.lastmodifydate = lastmodifydate;
    }

    public String getCreatedate() {
        return createdate;
    }

    public void setCreatedate(String createdate) {
        this.createdate = createdate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

    @ADBTableField(name = "taskname", fieldType = DBTableFieldType.String, allowNull = false)
    private String taskname;

    @ADBTableField(name = "taskclassname", fieldType = DBTableFieldType.String, allowNull = false)
    private String taskclassname;

    @ADBTableField(name = "taskpackagename", fieldType = DBTableFieldType.String, allowNull = false)
    private String taskpackagename;

    @ADBTableField(name = "taskno", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int taskno;

    @ADBTableField(name = "tasktype", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int tasktype;

    @ADBTableField(name = "packagelastmodifydate", fieldType = DBTableFieldType.String, allowNull = false)
    private String packagelastmodifydate;

    @ADBTableField(name = "prevtaskclassname", fieldType = DBTableFieldType.String)
    private String prevtaskclassname;

    @ADBTableField(name = "prevtaskno", fieldType = DBTableFieldType.Integer)
    private Integer prevtaskno;

    @ADBTableField(name = "status", fieldType = DBTableFieldType.Integer, allowNull = false)
    private int status;

    @ADBTableField(name = "accountdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String accountdate;

    @ADBTableField(name = "lastmodifydate", fieldType = DBTableFieldType.String, allowNull = false)
    private String lastmodifydate;

    @ADBTableField(name = "createdate", fieldType = DBTableFieldType.String, allowNull = false)
    private String createdate;

    @ADBTableField(name = "description", fieldType = DBTableFieldType.String)
    private String description;

    @ADBTableField(name = "edituserid", fieldType = DBTableFieldType.String)
    private String edituserid;

    @ADBTableField(name = "authuserid", fieldType = DBTableFieldType.String)
    private String authuserid;

}

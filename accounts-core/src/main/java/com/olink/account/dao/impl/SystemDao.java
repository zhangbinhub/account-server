package com.olink.account.dao.impl;

import com.olink.account.base.impl.BaseKernelDao;
import com.olink.account.dao.ISystemDao;
import com.olink.account.enumration.StatusEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.kernel.user.SysRole;
import com.olink.account.model.kernel.user.SysUser;
import com.olink.account.model.kernel.user.SysUserLoginRecord;
import com.olink.account.model.kernel.user.SysUserRoleSet;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.common.DBConTools;
import pers.acp.tools.dbconnection.entity.DBTable;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Shepherd on 2016-08-25.
 * 系统用户持久化
 */
public class SystemDao extends BaseKernelDao implements ISystemDao {

    private final Logger log = Logger.getLogger(this.getClass());

    public SystemDao() {
        super();
    }

    public SystemDao(DBConTools dbTools) {
        super(dbTools);
    }

    @Override
    public String validatePassword(String loginno, String password) throws ServerException {
        Map<String, Object> param = new HashMap<>();
        param.put(SysUser.class.getCanonicalName() + ".loginno", loginno);
        param.put(SysUser.class.getCanonicalName() + ".password", password);
        param.put(SysUser.class.getCanonicalName() + ".status", StatusEnum.activate);
        SysUser sysUser = (SysUser) SysUser.getInstance(param, SysUser.class, null, dbTools.getDbcon());
        if (sysUser == null) {
            throw new ServerException("账号密码校验失败");
        }
        return sysUser.getId();
    }

    @Override
    public boolean validatePower(String appid, String userid) {
        Map<String, Object> param = new HashMap<>();
        param.put(SysRole.class.getCanonicalName() + ".appid", appid);
        List<DBTable> sysRoles = dbTools.getDataObjListBySql(param, SysRole.class, null, null);
        for (DBTable dbTable : sysRoles) {
            SysRole sysRole = (SysRole) dbTable;
            Map<String, Object> params = new HashMap<>();
            params.put(SysUserRoleSet.class.getCanonicalName() + ".roleid", sysRole.getId());
            params.put(SysUserRoleSet.class.getCanonicalName() + ".userid", userid);
            SysUserRoleSet sysUserRoleSet = (SysUserRoleSet) SysUserRoleSet.getInstance(params, SysUserRoleSet.class, null, dbTools.getDbcon());
            if (sysUserRoleSet != null) {
                return true;
            }
        }
        return false;
    }

    @Override
    public List<DBTable> findRoleListByName(String rolenames) {
        String irolenames = "'" + rolenames.replace(",", "','") + "'";
        String sqlStr = " and ${" + SysRole.class.getCanonicalName() + ".name} in(" + irolenames + ")";
        return dbTools.getDataObjListBySql(null, SysRole.class, null, sqlStr);
    }

    @Override
    public SysUser findUserById(String userid) {
        return (SysUser) SysUser.getInstance(userid, SysUser.class, null, dbTools.getDbcon());
    }

    @Override
    public boolean doDelUserRoleSet(String userid) {
        boolean retval = true;
        String sqlStr = " delete from " + new SysUserRoleSet().getCurrTableName() + " where userid='" + userid + "'";
        if (!dbTools.doUpdate(sqlStr)) {
            retval = false;
        }
        return retval;
    }

    @Override
    public boolean createUser(SysUser sysUser, String rolenames) {
        boolean retval = true;
        if (CommonTools.isNullStr(sysUser.getId())) {
            sysUser.setId(CommonTools.getUuid());
        }
        try {
            SysUser oldSysUser = (SysUser) SysUser.getInstanceByLock(sysUser.getId(), SysUser.class, null, dbTools.getDbcon());
            if (oldSysUser != null) {
                log.info("用户已存在：" + oldSysUser.getLoginno());
                if (!doDelUserRoleSet(oldSysUser.getId())) {//删除关联信息
                    log.error("删除关联信息失败!");
                    retval = false;
                }
            } else {
                if (!sysUser.doCreate(dbTools.getDbcon())) {
                    log.error("创建用户失败!");
                    retval = false;
                }
            }
            if (retval) {
                log.info("用户处理成功!正在处理角色");
                List<DBTable> sysRoles = findRoleListByName(rolenames);
                if (sysRoles != null && sysRoles.size() > 0) {
                    for (DBTable dbTable : sysRoles) {
                        SysRole sysRole = (SysRole) dbTable;
                        SysUserRoleSet sysUserRoleSet = new SysUserRoleSet(sysUser.getId(), sysRole.getId());
                        if (!sysUserRoleSet.doCreate(dbTools.getDbcon())) {
                            log.error("用户角色关联建立失败!");
                            retval = false;
                            break;
                        }
                    }
                } else {
                    log.error("角色不存在");
                    retval = false;
                }
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            retval = false;
        }
        return retval;
    }

    @Override
    public boolean doModifyLoginNo(SysUser sysUser) {
        sysUser.addUpdateIncludes(new String[]{"loginno", "password"});
        return sysUser.doUpdate(dbTools.getDbcon());
    }

    @Override
    public List<DBTable> findLoginRecordByYearMonth(String yearmonth) {
        return dbTools.getDataObjListBySql(null, SysUserLoginRecord.class, null, " and ${" + SysUserLoginRecord.class.getCanonicalName() + ".logindate} like '" + yearmonth + "%'");
    }

    @Override
    public boolean doDeleteUserLoginRecord(SysUserLoginRecord sysUserLoginRecord) {
        return sysUserLoginRecord.doDelete(dbTools.getDbcon());
    }

}

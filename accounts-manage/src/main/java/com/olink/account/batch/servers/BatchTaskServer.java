package com.olink.account.batch.servers;

import com.olink.account.base.impl.BaseBackServer;
import com.olink.account.base.impl.BaseServerResult;
import com.olink.account.batch.servers.enumration.BatchTaskActionEnum;
import com.olink.account.batch.servers.result.BatchTaskServerResult;
import com.olink.account.batch.task.BaseBatchTask;
import com.olink.account.batch.task.BatchTaskResult;
import com.olink.account.batch.task.TaskManager;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.dictionary.D_Batch;
import com.olink.account.service.ISysUserService;
import com.olink.account.service.impl.SysUserService;
import com.olink.account.utils.Utility;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.exceptions.EnumValueUndefinedException;

import java.lang.reflect.Field;

/**
 * Created by zhangbin on 2017/1/5.
 * 批量任务接口
 */
public class BatchTaskServer extends BaseBackServer {

    /**
     * 授权人登录账号
     */
    private String authloginno;

    /**
     * 授权人登录密码
     */
    private String authpassword;

    /**
     * 任务类名
     */
    private String taskclassname;

    /**
     * 任务执行的会计日期
     */
    private String accountdate;

    public BatchTaskServer(HttpServletRequestAcp request) {
        super(request);
    }

    @Override
    public BaseServerResult doBackServer() throws ServerException {
        BatchTaskServerResult result;
        try {
            BatchTaskActionEnum action = BatchTaskActionEnum.getEnum(service.getAction());
            switch (action) {
                case batchtask_excute: //手动执行单个任务
                    result = doExcuteBatchTask();
                    break;
                default:
                    result = (BatchTaskServerResult) Utility.getFailedResult(BatchTaskServerResult.class, "NO SUCH ACTION！");
                    break;
            }
        } catch (EnumValueUndefinedException e) {
            result = (BatchTaskServerResult) Utility.getFailedResult(BatchTaskServerResult.class, "NO SUCH ACTION！");
        }
        return result;
    }

    private BatchTaskServerResult doExcuteBatchTask() throws ServerException {
        BatchTaskServerResult result;
        if (CommonTools.isNullStr(authloginno)) {
            result = (BatchTaskServerResult) Utility.getFailedResult(BatchTaskServerResult.class, "授权人账号为空");
            return result;
        }
        if (CommonTools.isNullStr(authpassword)) {
            result = (BatchTaskServerResult) Utility.getFailedResult(BatchTaskServerResult.class, "授权人密码为空");
            return result;
        }
        if (CommonTools.isNullStr(taskclassname)) {
            result = (BatchTaskServerResult) Utility.getFailedResult(BatchTaskServerResult.class, "任务类名为空");
            return result;
        }
        if (CommonTools.isNullStr(accountdate)) {
            result = (BatchTaskServerResult) Utility.getFailedResult(BatchTaskServerResult.class, "会计日期为空");
            return result;
        }
        try {
            if (TaskManager.initTaskList()) {
                ISysUserService sysUserService = new SysUserService();
                String authuserid = sysUserService.validatePassword(authloginno, authpassword);
                if (!validatePermissions(appid, authuserid, service.getAction() + "_power")) {
                    result = (BatchTaskServerResult) Utility.getFailedResult(BatchTaskServerResult.class, "操作授权失败");
                    return result;
                }
                D_Batch batchConfig = TaskManager.getBatchConfig(taskclassname);
                if (batchConfig == null) {
                    result = (BatchTaskServerResult) Utility.getFailedResult(BatchTaskServerResult.class, "任务类名不正确");
                    return result;
                }
                BaseBatchTask baseBatchTask = TaskManager.loadTask(batchConfig, accountdate, null, true);
                // 注入 手动处罚人id 和 授权人id start
                Field filed1 = BaseBatchTask.class.getDeclaredField("edituserid");
                filed1.setAccessible(true);
                filed1.set(baseBatchTask, userid);
                Field filed2 = BaseBatchTask.class.getDeclaredField("authuserid");
                filed2.setAccessible(true);
                filed2.set(baseBatchTask, authuserid);
                // 注入 手动处罚人id 和 授权人id end
                BatchTaskResult batchTaskResult;
                if (baseBatchTask.beginTask()) {
                    baseBatchTask.run();
                    Thread thread = new Thread(baseBatchTask);
                    thread.setDaemon(true);
                    thread.start();
                    batchTaskResult = BatchTaskResult.getSuccessResult();
                } else {
                    batchTaskResult = baseBatchTask.getBatchTaskResult();
                }
                if (batchTaskResult.isPass()) {
                    result = (BatchTaskServerResult) Utility.getDefaultSuccussResult(BatchTaskServerResult.class);
                    result.setMessage("任务已开始执行，请稍后查询执行结果！");
                } else {
                    result = (BatchTaskServerResult) Utility.getFailedResult(BatchTaskServerResult.class, batchTaskResult.getMessage());
                }
            } else {
                result = (BatchTaskServerResult) Utility.getFailedResult(BatchTaskServerResult.class, "初始化任务配置失败！");
            }
            return result;
        } catch (Exception e) {
            throw new ServerException(e.getMessage());
        }
    }

}

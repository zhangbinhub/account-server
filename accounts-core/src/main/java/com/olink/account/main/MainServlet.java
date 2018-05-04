package com.olink.account.main;

import com.olink.account.base.IBaseServer;
import com.olink.account.base.impl.BaseServerResult;
import com.olink.account.config.ServerConfig;
import com.olink.account.enumration.DefaultEnum;
import com.olink.account.enumration.StatusEnum;
import com.olink.account.exception.ServerException;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.service.IBusinessService;
import com.olink.account.service.IDictionaryService;
import com.olink.account.service.impl.BusinessService;
import com.olink.account.service.impl.DictionaryService;
import com.olink.account.utils.Utility;
import com.olink.account.validate.PackageValidateFactory;
import pers.acp.communications.server.http.config.HttpConfig;
import pers.acp.communications.server.http.servlet.base.BaseServletHandle;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;
import pers.acp.communications.server.http.servlet.handle.HttpServletResponseAcp;
import pers.acp.communications.server.http.servlet.tools.ServletTools;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.config.instance.SystemConfig;
import pers.acp.tools.exceptions.ConfigException;
import pers.acp.tools.task.threadpool.basetask.BaseThreadTask;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by zhang on 2016/7/22.
 * 请求入口类
 */
public class MainServlet extends BaseServletHandle {

    private final Logger log = Logger.getLogger(this.getClass());// 日志对象

    private JSONObject packageInfo;

    public MainServlet(HttpServletRequestAcp request, HttpServletResponseAcp response) throws ConfigException {
        super(request, response);
    }

    public void main() {
        BaseServerResult serverResult;
        try {
            if (!getParamsContent()) {
                throw new ServerException("请求非法！");
            }
            SystemConfig systemConfig = SystemConfig.getInstance();
            if (systemConfig.getThreadPool().isEnabled()) {
                serverResult = (BaseServerResult) CommonTools.excuteTaskInThreadPool(new BaseThreadTask("接口请求调度") {
                    @Override
                    public boolean beforeExcuteFun() {
                        return true;
                    }

                    @Override
                    public Object excuteFun() {
                        return doDispatch();
                    }

                    @Override
                    public void afterExcuteFun(Object o) {
                        //每次 server 调用完毕后执行的操作
                    }
                });
            } else {
                serverResult = doDispatch();
            }
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            serverResult = new BaseServerResult();
            serverResult.setStatus(-1);
            serverResult.setMessage("系统异常：" + e.getMessage());
        }
        doResponse(serverResult);
    }

    /**
     * 获取请求报文
     *
     * @return 成功或失败
     */
    private boolean getParamsContent() throws ConfigException {
        ServerConfig serverConfig = ServerConfig.getInstance();
        String content = ServletTools.getRequestContent(request);
        if (CommonTools.isNullStr(content)) {
            if (serverConfig.isAllowGet()) {
                if (serverConfig.getJsonKey() != null) {
                    content = request.getParameter(serverConfig.getJsonKey());
                }
            }
        }
        if (CommonTools.isNullStr(content)) {
            return false;
        }
        packageInfo = CommonTools.getJsonObjectFromStr(content);
        if (packageInfo.isEmpty() || !packageInfo.containsKey("head") || !packageInfo.getJSONObject("head").containsKey("serverno")) {
            return false;
        }
        if (serverConfig.isPackageLog()) {
            log.info("接收报文：" + content);
        }
        return true;
    }

    /**
     * 读配置文件，根据参数serverno执行serverClass调度
     *
     * @return serverClass执行结果
     */
    private BaseServerResult doDispatch() {
        BaseServerResult serverResult = null;
        String validatekey = "";
        String validatecode = "";
        boolean isValidate = false;
        boolean isCheckRepeat = false;
        try {
            ServerConfig serverConfig = ServerConfig.getInstance();
            JSONObject head = packageInfo.getJSONObject("head");
            String serverno = head.getString("serverno");
            List<ServerConfig.Server> servers = serverConfig.getServer();
            for (ServerConfig.Server server : servers) {
                if (server.getServerno().equals(serverno)) {
                    IDictionaryService dictionaryService = new DictionaryService();
                    if (server.isValidateAccountDate()) {
                        if (!dictionaryService.validateAccountDate()) {
                            throw new ServerException("系统会计日期异常，请联系系统管理员！");
                        }
                    }
                    if (!server.isBack()) {
                        if (!dictionaryService.isOuterTradeEnabled()) {
                            throw new ServerException("系统正在维护中，请稍后再试！");
                        }
                    }
                    isValidate = server.isValidate();
                    isCheckRepeat = server.isCheckRepeat();
                    if (isValidate || isCheckRepeat) {
                        IBusinessService businessService = new BusinessService();
                        if (!head.containsKey("customerid")) {
                            serverResult = Utility.getFailedResult(null, "请求非法，customerid字段为空！");
                            break;
                        }
                        String customerid = head.getString("customerid");
                        if (CommonTools.isNullStr(customerid)) {
                            serverResult = Utility.getFailedResult(null, "请求非法，customerid字段非法！");
                            break;
                        }
                        BusinessAccount currbusinessAccount = businessService.findBusinessAccountById(customerid);
                        if (currbusinessAccount == null) {
                            serverResult = Utility.getFailedResult(null, "请求非法，customerid字段非法！");
                            break;
                        }
                        if (!server.isBack()) {
                            if (currbusinessAccount.getIsdefault() == DefaultEnum.isdefault.getValue() || currbusinessAccount.getStatus() != StatusEnum.activate.getValue()) {
                                serverResult = Utility.getFailedResult(null, "请求非法，找不到可用的B类账户");
                                break;
                            }
                        } else {
                            if (currbusinessAccount.getIsdefault() == DefaultEnum.notdefault.getValue() || currbusinessAccount.getStatus() != StatusEnum.activate.getValue()) {
                                serverResult = Utility.getFailedResult(null, "请求非法，找不到账务平台B类账户");
                                break;
                            }
                        }
                        validatekey = PackageValidateFactory.getValidateKey(packageInfo);
                        validatecode = PackageValidateFactory.doValidate(packageInfo, validatekey);
                        if (!head.containsKey("validatecode")) {
                            serverResult = Utility.getFailedResult(null, "请求非法，validatecode字段为空！");
                            break;
                        }
                        if (isValidate && !validatecode.equals(head.getString("validatecode"))) {
                            serverResult = Utility.getFailedResult(null, "请求非法，报文校验失败！");
                            break;
                        }
                        if (isCheckRepeat) {
                            long minSpacingTime = 120000;
                            if (server.getMinSpacingTime() > 0) {
                                minSpacingTime = server.getMinSpacingTime();
                            }
                            if (PackageValidateFactory.checkRepeat(request, validatecode, minSpacingTime)) {
                                serverResult = Utility.getFailedResult(null, "系统正忙，请稍后再试！");
                                break;
                            }
                        }
                        if (server.isBack()) {
                            HttpConfig httpConfig = HttpConfig.getInstance();
                            if (!packageInfo.getJSONObject("head").containsKey("appid")) {
                                packageInfo.getJSONObject("head").put("appid", packageInfo.getString(httpConfig.getAppIdName()));
                            }
                            if (!packageInfo.getJSONObject("head").containsKey("userid")) {
                                packageInfo.getJSONObject("head").put("userid", packageInfo.getString(httpConfig.getOperatorIdName()));
                            }
                        }
                    }
                    String serverClass = server.getServerClass();
                    Class<?> cls = Class.forName(serverClass);
                    Class<?>[] parameterTypes = {HttpServletRequestAcp.class};
                    Object[] parameters = {request};
                    Constructor<?> constructor = cls.getConstructor(parameterTypes);
                    Object instance = constructor.newInstance(parameters);
                    IBaseServer serverinstance = (IBaseServer) doFillInFields(cls, instance);
                    serverResult = serverinstance.doServer();
                    if (serverResult == null) {
                        serverResult = Utility.getFailedResult(null, "系统异常！");
                    }
                    break;
                }
            }
            if (serverResult == null) {
                serverResult = Utility.getFailedResult(null, "请求非法，找不到对应的服务接口！");
            }
        } catch (Exception e) {
            if (e instanceof InvocationTargetException) {
                e = (Exception) ((InvocationTargetException) e).getTargetException();
            }
            log.error("系统异常：" + e.getMessage());
            serverResult = Utility.getFailedResult(null, e.getMessage());
        }
        if (isValidate || isCheckRepeat) {
            if (isValidate && !CommonTools.isNullStr(validatekey)) {
                serverResult.setValidatecode(PackageValidateFactory.doValidateResult(serverResult, validatekey));
            }
            if (isCheckRepeat && !CommonTools.isNullStr(validatecode)) {
                PackageValidateFactory.doDeleteRepeatRecord(validatecode);
            }
        }
        return serverResult;
    }

    /**
     * 参数填充进实例对象中
     *
     * @param cls      类
     * @param instance 实例对象
     * @return 实例对象
     */
    private Object doFillInFields(Class<?> cls, Object instance) throws Exception {
        JSONObject head = packageInfo.getJSONObject("head");
        instance = CommonTools.jsonToBean(head, cls.getSuperclass(), instance);
        if (packageInfo.containsKey("body") && packageInfo.getJSONObject("body") != null) {
            JSONObject body = packageInfo.getJSONObject("body");
            if (!body.isEmpty()) {
                instance = CommonTools.jsonToBean(body, cls, instance);
            }
        }
        return instance;
    }

    /**
     * 返回响应报文
     */
    private void doResponse(BaseServerResult serverResult) {
        try {
            ServerConfig serverConfig = ServerConfig.getInstance();
            JSONObject json = PackageValidateFactory.doPackege(serverResult);
            String retStr = json.toString();
            if (serverConfig.isPackageLog()) {
                log.info("响应报文：" + retStr);
            }
            response.doReturn(retStr);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
    }

}

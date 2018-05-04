package com.olink.account.validate;

import com.olink.account.base.impl.BaseServerResult;
import com.olink.account.service.IBusinessService;
import com.olink.account.service.impl.BusinessService;
import com.olink.account.service.IPackageInfoRecordService;
import com.olink.account.service.impl.PackageInfoRecordService;
import pers.acp.communications.server.http.servlet.handle.HttpServletRequestAcp;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.security.MD5Utils;
import net.sf.json.JSONObject;

import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by zhangbin on 2016/7/24.
 * 报文摘要验证类
 */
public class PackageValidateFactory {

    /**
     * 获取摘要算法的唯一key
     *
     * @param packageInfo 报文对象
     * @return key
     */
    public static String getValidateKey(JSONObject packageInfo) {
        JSONObject head = packageInfo.getJSONObject("head");
        IBusinessService businessService = new BusinessService();
        return businessService.getBusinessKeyById(head.getString("customerid"));
    }

    /**
     * 报文摘要验证
     *
     * @param packageInfo 报文对象
     * @param validatekey 校验密钥
     * @return 验证编码
     */
    public static String doValidate(JSONObject packageInfo, String validatekey) {
        JSONObject head = packageInfo.getJSONObject("head");
        SortedMap<String, String> sortParam = new TreeMap<>();
        for (Object key : head.keySet()) {
            sortParam.put(key.toString().toLowerCase(), head.get(key).toString());
        }
        StringBuilder validateStr = new StringBuilder();
        sortParam.entrySet().stream().filter(entry -> !entry.getKey().equals("validatecode") && entry.getValue() != null && !entry.getValue().equals("")).forEach(entry -> {
            if (validateStr.length() > 0) {
                validateStr.append("&");
            }
            validateStr.append(entry.getKey()).append("=").append(entry.getValue());
        });
        if (packageInfo.containsKey("body")) {
            if (packageInfo.getJSONObject("body") != null) {
                validateStr.append("&").append(packageInfo.getJSONObject("body").toString());
            }
        }
        validateStr.append("&").append(validatekey);
        return MD5Utils.encrypt(validateStr.toString()).toUpperCase();
    }

    /**
     * 结果对象进行摘要验证
     *
     * @param serverResult 返回结果对象
     * @param validatekey  校验密钥
     * @return 验证编码
     */
    public static String doValidateResult(BaseServerResult serverResult, String validatekey) {
        JSONObject packageInfo = doPackege(serverResult);
        return doValidate(packageInfo, validatekey);
    }

    /**
     * 返回结果打包
     *
     * @param serverResult 返回结果对象
     * @return json对象
     */
    public static JSONObject doPackege(BaseServerResult serverResult) {
        JSONObject json = new JSONObject();
        JSONObject head;
        if (serverResult.getClass().equals(BaseServerResult.class)) {
            head = CommonTools.beanToJson(serverResult);
        } else {
            head = CommonTools.beanToJson(serverResult.getClass().getSuperclass(), serverResult);
            JSONObject body = CommonTools.beanToJson(serverResult.getClass(), serverResult);
            if (!body.isEmpty() && !body.isNullObject()) {
                json.put("body", body);
            }
        }
        json.put("head", head);
        return json;
    }

    /**
     * 检查报文是否重复提交
     *
     * @param request        http请求对象
     * @param validatecode   根据报文内容计算的报文验证编码
     * @param minSpacingTime 重复请求的最小间隔时间，单位毫秒，默认2分钟：120000
     * @return 报文在规定时间内是否重复提交
     */
    public static boolean checkRepeat(HttpServletRequestAcp request, String validatecode, long minSpacingTime) {
        IPackageInfoRecordService packageInfoRecordService = new PackageInfoRecordService();
        return packageInfoRecordService.doCheckRepeat(request, validatecode, minSpacingTime);
    }

    /**
     * 删除报文提交记录
     *
     * @param validatecode 根据报文内容计算的报文验证编码
     */
    public static void doDeleteRepeatRecord(String validatecode) {
        IPackageInfoRecordService packageInfoRecordService = new PackageInfoRecordService();
        packageInfoRecordService.doDeleteRecord(validatecode);
    }

    /**
     * 校验报文是否正确
     *
     * @param packageInfo 报文对象
     * @param validatekey 校验密钥
     * @return true|false
     */
    public static boolean checkValidate(JSONObject packageInfo, String validatekey) {
        String validatecode = packageInfo.getJSONObject("head").getString("validatecode");
        String code = doValidate(packageInfo, validatekey);
        return validatecode.equals(code);
    }

}

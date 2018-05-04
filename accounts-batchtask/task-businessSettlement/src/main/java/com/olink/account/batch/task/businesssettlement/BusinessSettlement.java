package com.olink.account.batch.task.businesssettlement;

import com.olink.account.batch.config.SystemProperties;
import com.olink.account.batch.exception.BatchException;
import com.olink.account.batch.task.BaseBatchTask;
import com.olink.account.batch.task.BatchTaskResult;
import com.olink.account.enumration.AccountTypeEnum;
import com.olink.account.enumration.DefaultEnum;
import com.olink.account.enumration.ResultStatusEnum;
import com.olink.account.model.trade.customer.BusinessAccount;
import com.olink.account.model.trade.customer.BusinessSubAccount;
import com.olink.account.model.trade.dictionary.D_Batch;
import com.olink.account.service.IBusinessService;
import com.olink.account.service.impl.BusinessService;
import com.olink.account.validate.PackageValidateFactory;
import net.sf.json.JSONObject;
import pers.acp.communications.client.http.HttpServerClient;
import pers.acp.communications.client.http.HttpsServerClient;
import pers.acp.tools.common.CommonTools;

import java.util.List;

/**
 * Created by zhangbin on 2017/5/3.
 * B户结算订单申请任务
 */
public class BusinessSettlement extends BaseBatchTask {

    private IBusinessService businessService = new BusinessService();

    /**
     * 任务构造函数
     *
     * @param accountDate           会计日期
     * @param packagelastmodifydate 组件包最后修改时间
     * @param taskName              任务名称
     * @param batchConfig           任务配置类
     * @param prevTaskNo            前置任务序号
     * @param needExecuteImmediate  是否需要立即执行
     */
    public BusinessSettlement(String accountDate, String packagelastmodifydate, String taskName, D_Batch batchConfig, Integer prevTaskNo, boolean needExecuteImmediate) {
        super(accountDate, packagelastmodifydate, taskName, batchConfig, prevTaskNo, needExecuteImmediate);
    }

    @Override
    public boolean beforeExcute() throws Exception {
        return true;
    }

    @Override
    public BatchTaskResult doExcuteTask() throws Exception {
        BusinessAccount yyBusinessAccount = businessService.findYYBusinessAccount();
        List<BusinessAccount> businessAccounts = businessService.findBusinessAccount();
        for (BusinessAccount businessAccount : businessAccounts) {
            if (businessAccount.getIsdefault() == DefaultEnum.isdefault.getValue()) {
                continue;
            }
            BusinessSubAccount subAccountBalance = businessAccount.getSubAccountByType(AccountTypeEnum.Change);
            if (Double.compare(subAccountBalance.getBalance(), 0.00D) > 0) {
                JSONObject balanceResult = doApplySettlement("back_business_applyBalanceSettlement", yyBusinessAccount, businessAccount, subAccountBalance, "余额结算");
                if (!isSuccessFull(balanceResult)) {
                    throw new BatchException("余额结算申请请求失败：" + balanceResult.toString());
                }
            }
            BusinessSubAccount subAccountIntegral = businessAccount.getSubAccountByType(AccountTypeEnum.Integral);
            if (Double.compare(subAccountIntegral.getBalance(), 0.00D) > 0) {
                JSONObject integralResult = doApplySettlement("back_business_applyIntegralSettlement", yyBusinessAccount, businessAccount, subAccountIntegral, "积分结算");
                if (!isSuccessFull(integralResult)) {
                    throw new BatchException("积分结算申请请求失败：" + integralResult.toString());
                }
            }
        }
        return BatchTaskResult.getSuccessResult();
    }

    @Override
    public void afterExcute(BatchTaskResult batchTaskResult) throws Exception {

    }

    /**
     * 获取批量系统配置实例
     *
     * @return 批量系统配置实例
     * @throws BatchException 批量系统异常
     */
    private SystemProperties getSystemProperties() throws BatchException {
        SystemProperties systemProperties = SystemProperties.getInstance();
        if (systemProperties == null) {
            throw new BatchException("无法加载 system properties!");
        }
        return systemProperties;
    }

    /**
     * 结算申请
     *
     * @param actionname         接口名称
     * @param yyBusinessAccount  运营B户
     * @param businessAccount    B户
     * @param businessSubAccount B户可用余额子账户
     * @param remark             备注信息
     * @return 请求结果
     */
    private JSONObject doApplySettlement(String actionname, BusinessAccount yyBusinessAccount, BusinessAccount businessAccount, BusinessSubAccount businessSubAccount, String remark) throws BatchException {
        SystemProperties systemProperties = getSystemProperties();
        JSONObject jsonObject = new JSONObject();

        JSONObject head = new JSONObject();
        JSONObject service = new JSONObject();
        service.put("action", actionname);
        head.put("serverno", "back_1001");
        head.put("customerid", yyBusinessAccount.getId());
        head.put("service", service);

        JSONObject body = new JSONObject();
        JSONObject data = new JSONObject();
        data.put("businessid", businessAccount.getBusinessid());
        data.put("amont", businessSubAccount.getBalance());
        data.put("bindinfoid", businessService.findBusinessDefaultBindInfoByBusinessId(businessAccount.getBusinessid()));
        data.put("remark", businessAccount.getBusinessname() + " " + remark);
        body.put("data", data);

        jsonObject.put("appid", systemProperties.getAppId());
        jsonObject.put("operatorId", systemProperties.getOperatorId());
        jsonObject.put("head", head);
        jsonObject.put("body", body);

        jsonObject.getJSONObject("head").put("validatecode", PackageValidateFactory.doValidate(jsonObject, businessAccount.getBusinesskey()));

        JSONObject result = doRequest(jsonObject);
        if (PackageValidateFactory.checkValidate(result, businessAccount.getBusinesskey())) {
            return result;
        } else {
            throw new BatchException("响应报文异常，校验不通过");
        }
    }

    /**
     * 执行接口请求调用
     *
     * @param jsonObject 请求参数
     * @return 请求响应
     * @throws BatchException 批量系统异常
     */
    private JSONObject doRequest(JSONObject jsonObject) throws BatchException {
        SystemProperties systemProperties = getSystemProperties();
        String tradeHost = systemProperties.getTradeSysHost();
        String response;
        if (tradeHost.startsWith("http:")) {
            HttpServerClient client = new HttpServerClient();
            client.setUrl(tradeHost);
            response = client.doHttpPostJSONStr(jsonObject.toString());
        } else {
            HttpsServerClient client = new HttpsServerClient();
            client.setUrl(tradeHost);
            response = client.doHttpsPostJSONStr(jsonObject.toString());
        }
        return CommonTools.getJsonObjectFromStr(response);
    }

    /**
     * 响应是否成功
     *
     * @param response 响应报文
     * @return true|false
     */
    private boolean isSuccessFull(JSONObject response) {
        return ResultStatusEnum.success.getCode() == response.getJSONObject("head").getInt("status");
    }

}

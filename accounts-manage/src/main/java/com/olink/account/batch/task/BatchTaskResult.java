package com.olink.account.batch.task;

/**
 * Created by zhangbin on 2016/12/29.
 * 任务执行结果
 */
public class BatchTaskResult {

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private boolean isPass;

    private String message;

    /**
     * 生成成功结果
     *
     * @return 结果对象
     */
    public static BatchTaskResult getSuccessResult() {
        BatchTaskResult batchTaskResult = new BatchTaskResult();
        batchTaskResult.setPass(true);
        batchTaskResult.setMessage("任务执行完成");
        return batchTaskResult;
    }

    /**
     * 生成失败结果
     *
     * @param message 错误信息
     * @return 结果对象
     */
    public static BatchTaskResult getFaildResult(String message) {
        BatchTaskResult batchTaskResult = new BatchTaskResult();
        batchTaskResult.setPass(false);
        batchTaskResult.setMessage(message);
        return batchTaskResult;
    }

}

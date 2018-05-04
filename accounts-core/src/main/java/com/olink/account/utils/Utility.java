package com.olink.account.utils;

import com.olink.account.base.impl.BaseServerResult;
import com.olink.account.enumration.ResultStatusEnum;
import pers.acp.tools.common.CommonTools;

import java.lang.reflect.Field;
import java.util.regex.Pattern;

/**
 * Created by Shepherd on 2016-08-05.
 * 工具类
 */
public class Utility {

    /**
     * 获取结果对象
     *
     * @param cls 类
     * @return 结果对象
     */
    private static BaseServerResult getResultObj(Class<? extends BaseServerResult> cls) {
        BaseServerResult result;
        try {
            if (cls == null) {
                result = new BaseServerResult();
            } else {
                result = cls.newInstance();
            }
        } catch (Exception e) {
            result = new BaseServerResult();
        }
        return result;
    }

    /**
     * 默认返回成功
     *
     * @return BaseServerResult
     */
    public static BaseServerResult getDefaultSuccussResult(Class<? extends BaseServerResult> cls) {
        BaseServerResult result = getResultObj(cls);
        result.setMessage(ResultStatusEnum.success.getName());
        result.setStatus(ResultStatusEnum.success.getCode());
        return result;
    }

    /**
     * 默认返回失败
     *
     * @return BaseServerResult
     */
    public static BaseServerResult getDefaultFailedResult(Class<? extends BaseServerResult> cls) {
        BaseServerResult result = getResultObj(cls);
        result.setMessage(ResultStatusEnum.failed.getName());
        result.setStatus(ResultStatusEnum.failed.getCode());
        return result;
    }

    /**
     * 默认返回失败
     *
     * @return BaseServerResult
     */
    public static BaseServerResult getFailedResult(Class<? extends BaseServerResult> cls, String message) {
        BaseServerResult result = getResultObj(cls);
        result.setMessage(message);
        result.setStatus(ResultStatusEnum.failed.getCode());
        return result;
    }

    /**
     * 合并数组
     *
     * @param first  第一个数组
     * @param second 第二个数组
     * @return 合并后的目标数组
     */
    private static Field[] contact(Field[] first, Field[] second) {
        Field[] fields = new Field[first.length + second.length];
        System.arraycopy(first, 0, fields, 0, first.length);
        System.arraycopy(second, 0, fields, first.length, second.length);
        return fields;
    }

    /**
     * 判断金额是否合法
     * 金额规则：1、全数字；2、非负数；3、小数点后最多两位
     *
     * @param amont 金额字符串
     * @return true|false
     */
    public static boolean isAvailableAmont(String amont) {
        if (CommonTools.isNullStr(amont)) {
            return false;
        }
        Pattern pattern = Pattern.compile("^(([1-9][0-9]+)|[0-9])(\\.[0-9]{1,2})?$");
        return pattern.matcher(amont).matches();
    }

}

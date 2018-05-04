package com.olink.account.service;

import com.olink.account.base.IBaseTradeService;
import com.olink.account.enumration.DictionaryTableEnum;
import pers.acp.tools.dbconnection.entity.DBTable;

import java.util.List;
import java.util.Map;

/**
 * Created by zhangbin on 2016/9/8.
 * 字典服务
 */
public interface IDictionaryService extends IBaseTradeService {

    /**
     * 查询数据字典列表
     *
     * @param dictionaryTableEnum 字典枚举
     * @param whereValues         查询条件，key=java类名.字段名
     * @param attachStr           排序条件（例如：order by ${java类名.字段名}）
     * @return 结果 List
     */
    List<DBTable> findDictionaryList(DictionaryTableEnum dictionaryTableEnum, Map<String, Object> whereValues, String attachStr);

    /**
     * 查询数据字典里列表
     *
     * @param dictionaryTableEnum 字典枚举
     * @param whereValues         查询条件，key=java类名.字段名
     * @param attachStr           排序条件（例如：order by ${java类名.字段名}）
     * @param currentPage         当前页
     * @param pageSize            每页数量
     * @return 结果集 Object[]:[0]-int总页数,[1]-int总记录数,[2]-ArrayList结果集
     */
    Object[] findDictionaryList(DictionaryTableEnum dictionaryTableEnum, Map<String, Object> whereValues, String attachStr, int currentPage, int pageSize);

    /**
     * 查询数据字典列表
     *
     * @param dictionaryTableEnum 字典枚举
     * @param whereValues         查询条件，key=java类名.字段名
     * @param attachStr           排序条件（例如：order by ${java类名.字段名}）
     * @return 结果 List
     */
    List<DBTable> findDictionaryAvailableList(DictionaryTableEnum dictionaryTableEnum, Map<String, Object> whereValues, String attachStr);

    /**
     * 查询数据字典里列表
     *
     * @param dictionaryTableEnum 字典枚举
     * @param whereValues         查询条件，key=java类名.字段名
     * @param attachStr           排序条件（例如：order by ${java类名.字段名}）
     * @param currentPage         当前页
     * @param pageSize            每页数量
     * @return 结果集 Object[]:[0]-int总页数,[1]-int总记录数,[2]-ArrayList结果集
     */
    Object[] findDictionaryAvailableList(DictionaryTableEnum dictionaryTableEnum, Map<String, Object> whereValues, String attachStr, int currentPage, int pageSize);

    /**
     * 外部交易是否可用
     *
     * @return true|false
     */
    boolean isOuterTradeEnabled();

    /**
     * 校验会计日期是否合法
     *
     * @return true|false
     */
    boolean validateAccountDate();

}

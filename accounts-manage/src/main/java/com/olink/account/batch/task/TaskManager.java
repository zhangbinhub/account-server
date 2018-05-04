package com.olink.account.batch.task;

import com.olink.account.batch.config.MainServerConfig;
import com.olink.account.batch.exception.LoadJARException;
import com.olink.account.batch.service.IDateService;
import com.olink.account.batch.service.ISituationService;
import com.olink.account.batch.service.impl.DateService;
import com.olink.account.batch.service.impl.SituationService;
import com.olink.account.enumration.BatchTaskType;
import com.olink.account.enumration.DictionaryTableEnum;
import com.olink.account.model.trade.dictionary.D_Batch;
import com.olink.account.model.trade.dictionary.Dictionary;
import com.olink.account.service.IDictionaryService;
import com.olink.account.service.impl.DictionaryService;
import pers.acp.communications.server.ctrl.DaemonServiceManager;
import pers.acp.tools.common.CommonTools;
import pers.acp.tools.dbconnection.entity.DBTable;
import pers.acp.tools.file.FileTools;
import pers.acp.tools.task.timer.ruletype.CircleType;
import pers.acp.tools.task.timer.ruletype.ExcuteType;
import org.apache.log4j.Logger;
import sun.misc.ClassLoaderUtil;

import java.io.File;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by zhangbin on 2016/12/14.
 * 批量任务管理类
 */
public class TaskManager {

    /**
     * 日志对象
     */
    private static final Logger log = Logger.getLogger(TaskManager.class);

    /**
     * 任务配置
     */
    private static SortedMap<Integer, D_Batch> batchMap = new TreeMap<>();

    /**
     * 任务配置
     */
    private static Map<String, D_Batch> batchClassNameMap = new HashMap<>();

    /**
     * 类装载器
     */
    private static final ConcurrentHashMap<String, URLClassLoader> classLoaderMap = new ConcurrentHashMap<>();

    /**
     * 初始化批量任务配置
     */
    public static boolean initTaskList() {
        try {
            batchMap.clear();
            batchClassNameMap.clear();
            synchronized (classLoaderMap) {
                if (!classLoaderMap.isEmpty()) {
                    for (Map.Entry<String, URLClassLoader> entry : classLoaderMap.entrySet()) {
                        ClassLoaderUtil.releaseLoader(entry.getValue());
                    }
                    classLoaderMap.clear();
                }
            }
            IDictionaryService dictionaryService = new DictionaryService();
            List<DBTable> batchList = dictionaryService.findDictionaryAvailableList(DictionaryTableEnum.batch, null, null);
            for (DBTable dbTable : batchList) {
                Dictionary batch = (Dictionary) dbTable;
                int sort = Integer.valueOf(batch.getType().trim());
                batchMap.put(sort, (D_Batch) batch);
                batchClassNameMap.put(batch.getCode().trim(), (D_Batch) batch);
            }
            return true;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return false;
        }
    }

    /**
     * 获取任务配置信息
     *
     * @param batchTaskClassName 任务类名
     * @return 任务配置信息
     */
    public static D_Batch getBatchConfig(String batchTaskClassName) {
        if (!batchClassNameMap.isEmpty()) {
            if (batchClassNameMap.containsKey(batchTaskClassName)) {
                return batchClassNameMap.get(batchTaskClassName);
            }
        }
        return null;
    }

    /**
     * 执行批量任务
     *
     * @return 批量任务执行结果
     */
    public static BatchTaskResult doExcuteTask() {
        BatchTaskResult batchTaskResult = null;
        try {
            ISituationService situationService = new SituationService();
            IDateService dateService = new DateService();
            String accountDate = dateService.getAccountDate();
            List<DBTable> batchSituationList = situationService.findBatchSituationForNoPass();
            if (batchSituationList.isEmpty()) {
                Integer prevTaskNo = null;
                for (Map.Entry<Integer, D_Batch> entry : batchMap.entrySet()) {
                    D_Batch batchConfig = entry.getValue();
                    //动态加载任务模块，并执行 start
                    BaseBatchTask baseBatchTask = loadTask(batchConfig, accountDate, prevTaskNo, false);
                    if (baseBatchTask.beginTask()) {
                        BatchTaskType batchTaskType = BatchTaskType.getEnum(Integer.valueOf(batchConfig.getField5()));
                        if (batchTaskType.equals(BatchTaskType.sequentially.getValue())) {
                            baseBatchTask.run();
                            BatchTaskResult batchTaskResultTmp = baseBatchTask.getBatchTaskResult();
                            if (!batchTaskResultTmp.isPass()) {
                                batchTaskResult = batchTaskResultTmp;
                                break;
                            }
                        } else {
                            CircleType circleType = CircleType.getEnum(batchConfig.getField2());
                            ExcuteType excuteType = ExcuteType.getEnum(batchConfig.getField3());
                            String rules = batchConfig.getField4();
                            BatchTimerDriver server = new BatchTimerDriver(baseBatchTask.getTaskName() + "定时器", circleType, rules, excuteType);
                            server.setTimerTask(baseBatchTask);
                            DaemonServiceManager.addService(server);
                            break;
                        }
                    } else {
                        batchTaskResult = baseBatchTask.getBatchTaskResult();
                        break;
                    }
                    //动态加载任务模块，并执行 end
                    prevTaskNo = Integer.valueOf(batchConfig.getType().trim());
                }
                if (batchTaskResult == null) {
                    batchTaskResult = BatchTaskResult.getSuccessResult();
                    batchTaskResult.setMessage("批量任务执行完毕！");
                }
            } else {
                batchTaskResult = BatchTaskResult.getFaildResult("存在未完成或失败的批量任务！");
            }
        } catch (Exception e) {
            batchTaskResult = BatchTaskResult.getFaildResult("任务执行异常【" + e.getMessage() + "】");
        }
        return batchTaskResult;
    }

    /**
     * 动态装载任务
     *
     * @param batchConfig          任务配置
     * @param prevTaskNo           前置任务序号
     * @param needExecuteImmediate 是否需要立即执行
     * @return 任务对象
     */
    public static BaseBatchTask loadTask(D_Batch batchConfig, String accountDate, Integer prevTaskNo, boolean needExecuteImmediate) throws LoadJARException {
        try {
            MainServerConfig mainServerConfig = MainServerConfig.getInstance();
            String jarPath = FileTools.getAbsPath(mainServerConfig.getTaskJarPath()) + File.separator + batchConfig.getField1();
            File jarFile = new File(jarPath);
            String packagelastmodifydate = CommonTools.getDateTimeString(new Date(jarFile.lastModified()), null);
            URLClassLoader loader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()});
            //释放已有的类加载器
            releaseLoad(batchConfig.getCode().trim());
            //重新放入map
            classLoaderMap.put(batchConfig.getCode().trim(), loader);
            Class<?> baseBatchTaskClass = loader.loadClass(batchConfig.getCode().trim());
            Class<?>[] parameterTypes = {String.class, String.class, String.class, D_Batch.class, Integer.TYPE, Boolean.TYPE};
            Constructor<?> constructor = baseBatchTaskClass.getConstructor(parameterTypes);
            Object[] parameters = {accountDate, packagelastmodifydate, batchConfig.getName(), batchConfig, prevTaskNo, needExecuteImmediate};
            return (BaseBatchTask) constructor.newInstance(parameters);
        } catch (Exception e) {
            throw new LoadJARException(e.getMessage());
        }
    }

    /**
     * 获取任务配置信息
     *
     * @param taskno 任务序号
     * @return 任务配置信息
     */
    static D_Batch getBatchConfig(int taskno) {
        if (!batchMap.isEmpty()) {
            if (batchMap.containsKey(taskno)) {
                return batchMap.get(taskno);
            }
        }
        return null;
    }

    /**
     * 释放类加载器
     *
     * @param taskClassName 任务类名
     */
    static void releaseLoad(String taskClassName) {
        synchronized (classLoaderMap) {
            if (classLoaderMap.containsKey(taskClassName)) {
                ClassLoaderUtil.releaseLoader(classLoaderMap.get(taskClassName));
            }
        }
    }

}

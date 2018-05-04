import com.olink.account.batch.task.BatchTaskResult;
import com.olink.account.batch.task.innerstatistics.StatisticsInner;

/**
 * Created by zhangbin on 2017/5/3.
 * 测试类
 */
public class TestBatchTask {

    public static void main(String[] arg) {
        StatisticsInner statisticsInner = new StatisticsInner("2017-05-03", null, null, null, null, true);
        try {
            BatchTaskResult result = statisticsInner.doExcuteTask();
            System.out.println("ispass:" + result.isPass() + ",message:" + result.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

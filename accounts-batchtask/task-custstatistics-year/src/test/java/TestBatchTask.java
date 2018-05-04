import com.olink.account.batch.task.BatchTaskResult;
import com.olink.account.batch.task.custstatistics.StatisticsYear;

/**
 * Created by zhangbin on 2017/5/3.
 * 测试类
 */
public class TestBatchTask {

    public static void main(String[] arg) {
        StatisticsYear statisticsYear = new StatisticsYear("2017-05-03", null, null, null, null, true);
        try {
            BatchTaskResult result = statisticsYear.doExcuteTask();
            System.out.println("ispass:" + result.isPass() + ",message:" + result.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

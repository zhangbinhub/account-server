import com.olink.account.batch.task.BatchTaskResult;
import com.olink.account.batch.task.custstatistics.StatisticsMonth;

/**
 * Created by zhangbin on 2017/5/3.
 * 测试类
 */
public class TestBatchTask {

    public static void main(String[] arg) {
        StatisticsMonth statisticsMonth = new StatisticsMonth("2017-05-04", null, null, null, null, true);
        try {
            BatchTaskResult result = statisticsMonth.doExcuteTask();
            if (result.isPass()) {
                statisticsMonth.afterExcute(result);
            }
            System.out.println("ispass:" + result.isPass() + ",message:" + result.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

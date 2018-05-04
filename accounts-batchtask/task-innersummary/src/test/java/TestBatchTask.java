import com.olink.account.batch.task.BatchTaskResult;
import com.olink.account.batch.task.innersummary.InnerSummaryStatistics;

/**
 * Created by zhangbin on 2017/5/3.
 * 测试类
 */
public class TestBatchTask {

    public static void main(String[] arg) {
        InnerSummaryStatistics innerSummaryStatistics = new InnerSummaryStatistics("2017-05-03", null, null, null, null, true);
        try {
            if (innerSummaryStatistics.beforeExcute()) {
                BatchTaskResult result = innerSummaryStatistics.doExcuteTask();
                System.out.println("ispass:" + result.isPass() + ",message:" + result.getMessage());
            } else {
                System.out.println("执行失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

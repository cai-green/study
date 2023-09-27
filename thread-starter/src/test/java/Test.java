import com.cai.ThreadApplication;
import com.cai.utils.MultiplyThreadTransactionManager;
import lombok.SneakyThrows;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;

@SpringBootTest(classes = ThreadApplication.class)
public class Test {
    @Resource
    private MultiplyThreadTransactionManager multiplyThreadTransactionManager;

    @SneakyThrows
    @org.junit.jupiter.api.Test
    public void test() {
        List<Runnable> tasks = new ArrayList<>();

        tasks.add(() -> {
            System.out.println("删除1成功");
            throw new RuntimeException("我就要抛出异常!");
        });

        tasks.add(() -> {
            System.out.println("删除2成功");
        });

        multiplyThreadTransactionManager.runAsyncButWaitUntilAllDown(tasks, Executors.newCachedThreadPool());
    }

}
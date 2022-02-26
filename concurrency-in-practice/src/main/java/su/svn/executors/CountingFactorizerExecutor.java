package su.svn.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountingFactorizerExecutor {

    public static final int MAX_BOUND = 10;
    public static final int MAX_SIZE = 10 * MAX_BOUND;

    private final ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE);

    private CountingFactorizerExecutor() {}

    private void go() throws Exception {
        for (int i = 0; i < MAX_SIZE - 1; i++) {
            exec.execute(new CountingFactorizerRunnable());
        }
        Thread.sleep(MAX_SIZE);
        exec.execute(new CountingFactorizerRunnable());
        Thread.sleep(MAX_SIZE);
        exec.shutdown();
    }

    public static void race() throws Exception {
        new CountingFactorizerExecutor().go();
    }
}

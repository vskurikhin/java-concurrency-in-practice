package su.svn.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SynchronizedFactorizerExecutor {

    public static final int MAX_BOUND = 7;
    public static final int MAX_SIZE = 9 * MAX_BOUND;

    private final ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE);

    private SynchronizedFactorizerExecutor() {}

    private void go() throws Exception {
        for (int i = 0; i < MAX_SIZE; i++) {
            exec.execute(new SynchronizedFactorizerRunnable());
        }
        Thread.sleep(MAX_SIZE);
        exec.shutdown();
    }

    public static void race() throws Exception {
        new SynchronizedFactorizerExecutor().go();
    }
}

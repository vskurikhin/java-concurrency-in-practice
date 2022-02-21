package su.svn.executors;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnsafeCachingFactorizerExecutor {

    public static final int MAX_BOUND = 2;
    public static final int MAX_SIZE = 3 * MAX_BOUND;

    private final ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE);

    private UnsafeCachingFactorizerExecutor() {}

    private void go() throws Exception {
        for (int i = 0; i < MAX_SIZE; i++) {
            exec.execute(new UnsafeCachingFactorizerRunnable());
        }
        Thread.sleep(MAX_SIZE);
        exec.shutdown();
    }

    public static void race() throws Exception {
        new UnsafeCachingFactorizerExecutor().go();
    }
}

package su.svn.executors;

import net.jcip.annotations.GuardedBy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CachedFactorizerExecutor {

    public static final int MAX_BOUND = 2;
    public static final int MAX_SIZE = 3 * MAX_BOUND;

    private static CachedFactorizerExecutor cachedFactorizerExecutor;

    private final ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE);

    @GuardedBy("this")
    private volatile Boolean finished = false;

    private CachedFactorizerExecutor() {}

    public boolean getFinished() {
        return finished;
    }

    private void go() throws Exception {
        for (int i = 0; i < MAX_SIZE; i++) {
            exec.execute(new CachedFactorizerRunnable());
        }
        Thread.sleep(MAX_SIZE);
        exec.shutdown();
        synchronized (this) {
            finished = true;
        }
    }

    public static void race() throws Exception {
        get().go();
    }

    public static CachedFactorizerExecutor get() {
        if (cachedFactorizerExecutor == null) {
            cachedFactorizerExecutor = new CachedFactorizerExecutor();
        }
        return cachedFactorizerExecutor;
    }
}

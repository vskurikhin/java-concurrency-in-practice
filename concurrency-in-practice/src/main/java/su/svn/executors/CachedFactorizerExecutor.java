package su.svn.executors;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ThreadSafe
public class CachedFactorizerExecutor {

    public static final int MAX_BOUND = 11;
    public static final int MAX_SIZE = 7 * MAX_BOUND;

    private static CachedFactorizerExecutor cachedFactorizerExecutor;

    private final ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE);

    @GuardedBy("this")
    private volatile int finished = 1;

    private CachedFactorizerExecutor() {}

    public int getFinished() {
        synchronized (this) {
            return finished;
        }
    }

    public boolean isFinished() {
        synchronized (this) {
            return finished == 0;
        }
    }

    public void start() {
        synchronized (this) {
            finished++;
        }
    }

    public void finish() {
        synchronized (this) {
            finished--;
        }
    }

    private void go() throws Exception {
        for (int i = 0; i < MAX_SIZE; i++) {
            start();
            exec.execute(new CachedFactorizerRunnable());
        }
        Thread.sleep(MAX_SIZE);
        exec.shutdown();
        synchronized (this) {
            finished--;
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

package su.svn.executors;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ThreadSafe
public class FactorizerExecutor {

    public static final int MAX_BOUND = 31;
    public static final int MAX_SIZE = 3 * MAX_BOUND;

    public static final int START_POINT = 100;
    public static final int SLEEP_BEFORE_GET = 5000;

    private static FactorizerExecutor cachedFactorizerExecutor;

    private final ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE);

    @GuardedBy("this")
    private volatile int finished = 1;

    private FactorizerExecutor() {
    }

    public synchronized int getFinished() {
        return finished;
    }

    public synchronized boolean isFinished() {
        return finished == 0;
    }

    public synchronized void start() {
        finished++;
    }

    public synchronized void finish() {
        finished--;
    }

    private void go() throws Exception {
        for (int i = 0; i < MAX_SIZE; i++) {
            start();
            exec.execute(new FactorizerRunnable());
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

    public static FactorizerExecutor get() {
        if (cachedFactorizerExecutor == null) {
            cachedFactorizerExecutor = new FactorizerExecutor();
        }
        return cachedFactorizerExecutor;
    }
}

package su.svn.executors;

import net.jcip.annotations.GuardedBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import su.svn.enums.Environment;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public enum FactorizerExecutor {
    Singleton;

    private static final Logger LOGGER = LoggerFactory.getLogger(FactorizerExecutor.class);

    public static final int MAX_BOUND = 11;
    public static final int MAX_SIZE = 3 * MAX_BOUND;

    public static final int START_POINT = 100;

    @GuardedBy("this")
    private volatile int finished = 1;

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

    public void race() {
        ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE);
        for (int i = 0; i < MAX_SIZE; i++) {
            start();
            exec.execute(new FactorizerRunnable());
        }
        exec.shutdown();
        try {
            if (exec.awaitTermination(9L * Environment.PAUSE_BEFORE_WARMUP_IN_MS, TimeUnit.MILLISECONDS)) {
                LOGGER.debug("awaitTermination returned true");
            } else {
                LOGGER.error("awaitTermination returned false");
            }
        } catch (InterruptedException e) {
            LOGGER.error("awaitTermination with exception ", e);
        }
        exec.shutdownNow();
        finish();
        // Нужно System.gc() для ускорения очистки пула потоков.
        //noinspection UnusedAssignment
        exec = null;
        System.gc();
    }

    @Override
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}

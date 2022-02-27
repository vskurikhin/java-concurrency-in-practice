package su.svn.executors;

import su.svn.console.ConsoleStub;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class UnsafeCountingFactorizerExecutor {

    public static final int MAX_BOUND = 10;
    public static final int MAX_SIZE = 10 * MAX_BOUND;
    private static volatile ConsoleStub consoleNull;

    private final ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE);

    private UnsafeCountingFactorizerExecutor() {}

    private void go() throws Exception {
        for (int i = 0; i < MAX_SIZE; i++) {
            exec.execute(new UnsafeCountingFactorizerRunnable());
        }
        Thread.sleep(MAX_SIZE);
        exec.shutdown();
    }

    public static void race() throws Exception {
        new UnsafeCountingFactorizerExecutor().go();
    }
}

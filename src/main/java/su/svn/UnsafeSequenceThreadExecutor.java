package su.svn;

import net.jcip.examples.UnsafeSequence;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

final class UnsafeSequenceThreadExecutor {

    private static final int MAX_BOUND = 10;
    static final int MAX_SIZE = 10 * MAX_BOUND;

    private static final UnsafeSequence unsafeSequence = new UnsafeSequence();

    private static final Runnable runnable = () -> {
        try {
            Thread.sleep(new Random().nextInt(MAX_BOUND));
            final int next = unsafeSequence.getNext();
            synchronized (System.out) {
                System.out.print(" " + next);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    };

    private final ExecutorService exec = Executors.newFixedThreadPool(MAX_SIZE);

    private UnsafeSequenceThreadExecutor() {}

    private void go() throws Exception {
        for (int i = 0; i < MAX_SIZE; i++) {
            exec.execute(runnable);
        }
        Thread.sleep(MAX_SIZE);
        exec.shutdownNow();
    }

    static void race() throws Exception {
        new UnsafeSequenceThreadExecutor().go();
    }
}

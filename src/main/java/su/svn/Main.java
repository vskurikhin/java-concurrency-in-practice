package su.svn;

import net.jcip.examples.ExpensiveObject;
import net.jcip.examples.LazyInitRace;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {

    public static final int BOUND = 10;

    public static final int PAUSE = 1_073_741_824;

    static volatile long syncTime = System.nanoTime() + PAUSE;

    static final Queue<String> queue = new ConcurrentLinkedQueue<>();

    static final LazyInitRace lazyInitRace = new LazyInitRace();

    public static void main(String[] args) throws Exception {

        final ExecutorService exec = Executors.newFixedThreadPool(BOUND);

        for (int i = 0; i < BOUND; i++) {
            exec.execute(() -> {
                while (System.nanoTime() < syncTime) {
                    try {
                        Thread.sleep(0, 1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ExpensiveObject o = lazyInitRace.getInstance();
                queue.add(o.toString());
            });
        }
        if ( ! exec.awaitTermination((Math.abs(System.nanoTime() - syncTime) + PAUSE), TimeUnit.NANOSECONDS)) {
            exec.shutdown();
        }
        Set<String> setOfExpensiveObjects = new HashSet<>(queue);
        System.out.println("setOfExpensiveObjects.size() = " + setOfExpensiveObjects.size());
        setOfExpensiveObjects.forEach(s -> System.out.println("bad ExpensiveObject = " + s));
    }
}

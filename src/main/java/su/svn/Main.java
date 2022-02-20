package su.svn;

import su.svn.console.ConsoleStub;
import su.svn.executors.UnsafeCountingFactorizerExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {
        ConsoleStub.get();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                UnsafeCountingFactorizerExecutor.race();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Application.create().start();
        List<Integer> result = Arrays.stream(ConsoleStub.get().toString().trim().split(" "))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        for (int i = 0; i < UnsafeCountingFactorizerExecutor.MAX_SIZE; i++) {
            if ( ! result.contains(i)) {
                System.err.println("Not contains in result " + i);
            }
        }
    }
}

package su.svn;

import su.svn.console.ConsoleStub;
import su.svn.executors.UnsafeCachingFactorizerExecutor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) throws Exception {

        ConsoleStub.get();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                UnsafeCachingFactorizerExecutor.race();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Application.create().start();
    }
}

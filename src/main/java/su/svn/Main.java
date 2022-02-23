package su.svn;

import su.svn.console.ConsoleStub;
import su.svn.executors.CachedFactorizerExecutor;

public class Main {

    public static void main(String[] args) throws Exception {

        ConsoleStub.get();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                CachedFactorizerExecutor.race();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Application.create().start();
    }
}

package su.svn;

import su.svn.console.ConsoleStub;
import su.svn.executors.SynchronizedFactorizerExecutor;

public class Main {

    public static void main(String[] args) throws Exception {

        ConsoleStub.get();
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                SynchronizedFactorizerExecutor.race();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Application.create().start();
    }
}

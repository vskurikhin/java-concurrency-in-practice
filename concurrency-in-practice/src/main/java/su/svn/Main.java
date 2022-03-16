/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package su.svn;

import su.svn.console.ConsoleStub;
import su.svn.executors.FactorizerExecutor;

public class Main {
    public static void main(String[] args) throws Exception {
        ConsoleStub.get();
        new Thread(() -> {
            try {
                Thread.sleep(10000);
                FactorizerExecutor.race();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
        Application.get().start();
    }
}

/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package su.svn;

import org.apache.catalina.LifecycleException;
import org.junit.Before;
import org.junit.Test;
import su.svn.executors.CachedFactorizerExecutor;
import su.svn.tomcat.Embedded;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.function.BinaryOperator;

public class MainTest {
    private static final boolean TEST_VULNERABILITY_WINDOW = false;

    public static void clearEmbedded() throws Exception {
        Class<Embedded> clazz = Embedded.class;

        Constructor<Embedded> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        Embedded mock = constructor.newInstance();

        Field consoleOutputField = clazz.getDeclaredField("embedded");
        consoleOutputField.setAccessible(true);
        consoleOutputField.set(mock, null);
    }

    @Before
    public void setUp() throws Exception {
        clearEmbedded();
    }

    @Test
    public void main() throws Exception {
        new Thread(() -> {
            try {
                while ( ! CachedFactorizerExecutor.get().isFinished()) {
                    Thread.sleep(500);
                }
                Embedded.get().stop();
            } catch (InterruptedException | LifecycleException e) {
                e.printStackTrace();
            }
        }).start();
        Main.main(new String[]{});
    }
}

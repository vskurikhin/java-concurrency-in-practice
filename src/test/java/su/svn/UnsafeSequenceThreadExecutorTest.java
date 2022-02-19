package su.svn;

import junit.framework.TestCase;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;

public class UnsafeSequenceThreadExecutorTest extends TestCase {

    ConsoleOutput consoleOutput;

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testMethod_race() throws Exception {
        UnsafeSequenceThreadExecutor.race();
    }

    public void test_runnable_InterruptedException() throws Exception {

        Class<UnsafeSequenceThreadExecutor> c = UnsafeSequenceThreadExecutor.class;
        Constructor<UnsafeSequenceThreadExecutor> constructor = c.getDeclaredConstructor();
        constructor.setAccessible(true);

        UnsafeSequenceThreadExecutor mock = constructor.newInstance();

        Field runnableField = c.getDeclaredField("runnable");
        runnableField.setAccessible(true);
        Object runnableObject = runnableField.get(mock);

        Field execField = c.getDeclaredField("exec");
        execField.setAccessible(true);
        ExecutorService exec = (ExecutorService) execField.get(mock);

        exec.execute((Runnable) runnableObject);
        exec.shutdownNow();
        assertTrue(exec.isShutdown());
    }
}
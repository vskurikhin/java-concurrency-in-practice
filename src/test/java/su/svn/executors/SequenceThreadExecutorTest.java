package su.svn.executors;

import junit.framework.TestCase;
import org.junit.Ignore;
import su.svn.console.ConsoleOutput;
import su.svn.executors.SequenceThreadExecutor;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutorService;

@Ignore
public class SequenceThreadExecutorTest extends TestCase {

    ConsoleOutput consoleOutput;

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testMethod_race() throws Exception {
        consoleOutput = ConsoleOutput.redirect();
        SequenceThreadExecutor.race();
        consoleOutput.revertBack();
        String[] result = consoleOutput.toString().trim().split(" ");
        assertEquals(SequenceThreadExecutor.MAX_SIZE, result.length);
    }

    public void test_runnable_InterruptedException() throws Exception {

        Class<SequenceThreadExecutor> c = SequenceThreadExecutor.class;
        Constructor<SequenceThreadExecutor> constructor = c.getDeclaredConstructor();
        constructor.setAccessible(true);

        SequenceThreadExecutor mock = constructor.newInstance();

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

    public void tearDown() throws Exception {
        super.tearDown();
        if (ConsoleOutput.get() != null) {
            ConsoleOutput.get().revertBack();
        }
    }
}
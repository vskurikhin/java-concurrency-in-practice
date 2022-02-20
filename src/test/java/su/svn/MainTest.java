package su.svn;

import org.apache.catalina.LifecycleException;
import org.junit.Before;
import org.junit.Test;
import su.svn.console.ConsoleStub;
import su.svn.executors.CountingFactorizerExecutor;
import su.svn.tomcat.Embedded;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class MainTest {


    public static int PAUSE = 5000;

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
                Thread.sleep(PAUSE);
                Embedded.get().stop();
            } catch (InterruptedException | LifecycleException e) {
                e.printStackTrace();
            }
        }).start();
        Main.main(new String[]{});
        List<Integer> result = Arrays.stream(ConsoleStub.get().toString().trim().split(" "))
                .filter(Objects::nonNull)
                .filter(s -> ! s.equals(""))
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        assertEquals(CountingFactorizerExecutor.MAX_SIZE, result.size());
        assertTrue(result.contains(CountingFactorizerExecutor.MAX_SIZE));
    }
}
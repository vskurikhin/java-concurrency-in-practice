package su.svn;

import org.apache.catalina.LifecycleException;
import org.junit.Before;
import org.junit.Test;
import su.svn.console.ConsoleStub;
import su.svn.executors.CountingFactorizerExecutor;
import su.svn.tomcat.Embedded;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
        System.out.println("ConsoleStub.get().toString() = " + ConsoleStub.get().toString());
        List<String> result = Arrays.stream(ConsoleStub.get().toString().trim().split("\n"))
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> ! s.equals(""))
                .map(MainTest::first)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        System.out.println("result = " + result);
        System.out.println("result.size() = " + result.size());
    }

    public static Optional<String> first(String s) {
        String a[] = s.split(" ");
        if (a.length > 0) {
            return Optional.of(a[0]);
        }
        return Optional.empty();
    }
}
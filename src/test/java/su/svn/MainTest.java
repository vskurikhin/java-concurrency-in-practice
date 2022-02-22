package su.svn;

import org.apache.catalina.LifecycleException;
import org.junit.Before;
import org.junit.Test;
import su.svn.console.ConsoleStub;
import su.svn.tomcat.Embedded;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BinaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class MainTest {

    private static final boolean TEST_VULNERABILITY_WINDOW = false;

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
        BinaryOperator<String[]> binaryOperator = new AssertArrayEquals();
        Map<String, String[]> result = Arrays.stream(ConsoleStub.get().toString().trim().split("\n"))
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(s -> !s.equals(""))
                .map(MainTest::entry)
                .map(MainTest::parseValue)
                .peek(MainTest::printEntry)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, binaryOperator));
        System.out.println("result.size() = " + result.size());
    }

    private static Map.Entry<String, String[]> parseValue(Map.Entry<String, Optional<String[]>> stringEntry) {
        String[] values = stringEntry.getValue()
                .map(strings -> Arrays.stream(strings)
                        .filter(Objects::nonNull)
                        .map(MainTest::prepareValue)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .flatMap(MainTest::parseValueAsArray)
                        .filter(Objects::nonNull)
                        .filter(s -> !s.equals(""))
                        .toArray(String[]::new))
                .orElse(new String[]{});
        return new AbstractMap.SimpleEntry<>(stringEntry.getKey(), values);
    }

    private static Optional<String> prepareValue(String s) {
        if (s.charAt(0) == '[' && s.charAt(s.length() - 1) == ']' && s.length() > 3) {
            return Optional.of(s.substring(1, s.length() - 2));
        }
        return Optional.empty();
    }

    private static Stream<String> parseValueAsArray(String s) {
        return null == s ? Stream.of() : Arrays.stream(s.split(","));
    }

    private static void printEntry(Map.Entry<String, String[]> stringEntry) {
        System.out.printf("%s -> %s\n", stringEntry.getKey(), Arrays.toString(stringEntry.getValue()));
    }

    private static Map.Entry<String, Optional<String[]>> entry(String s) {
        String[] a = s.split(" ", 2);
        if (a.length > 1) {
            String[] values = Arrays.copyOfRange(a, 1, a.length);
            return new AbstractMap.SimpleEntry<>(a[0], Optional.of(values));
        } else if (a.length > 0) {
            return new AbstractMap.SimpleEntry<>(a[0], Optional.empty());
        }
        return new AbstractMap.SimpleEntry<>(null, Optional.empty());
    }

    public static Optional<String> first(String s) {
        String[] a = s.split(" ");
        if (a.length > 0) {
            return Optional.of(a[0]);
        }
        return Optional.empty();
    }

    static class AssertArrayEquals implements BinaryOperator<String[]> {

        @Override
        public String[] apply(String[] strings1, String[] strings2) {
            if (TEST_VULNERABILITY_WINDOW) {
                System.out.println("Arrays.toString(strings1) = " + Arrays.toString(strings1));
                System.out.println("Arrays.toString(strings2) = " + Arrays.toString(strings2));
                assertArrayEquals(strings1, strings2);
            }
            return strings1;
        }
    }
}
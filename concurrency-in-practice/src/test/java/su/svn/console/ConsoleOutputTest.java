package su.svn.console;

import junit.framework.TestCase;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ConsoleOutputTest extends TestCase {

    static Class<ConsoleOutput> clazz;

    ConsoleOutput consoleOutput;

    ConsoleOutput mock;


    public void setUp() throws Exception {
        super.setUp();
        clazz = ConsoleOutput.class;

        Constructor<ConsoleOutput> constructor = clazz.getDeclaredConstructor();
        constructor.setAccessible(true);
        mock = constructor.newInstance();

        Field consoleOutputField = clazz.getDeclaredField("consoleOutput");
        consoleOutputField.setAccessible(true);
        consoleOutputField.set(mock, null);
    }

    public void testFields() throws ReflectiveOperationException {
        Field baosField = clazz.getDeclaredField("baos");
        baosField.setAccessible(true);
        assertNotNull(baosField.get(mock));

        Field oldField = clazz.getDeclaredField("old");
        oldField.setAccessible(true);
        assertNotNull(oldField.get(mock));

        Field consoleErrorField = clazz.getDeclaredField("consoleOutput");
        consoleErrorField.setAccessible(true);
        assertNull(consoleErrorField.get(mock));
    }

    public void testMethod_redirect() {
        consoleOutput = ConsoleOutput.redirect();
        //noinspection SimplifiableAssertion
        assertTrue(consoleOutput == ConsoleOutput.redirect());
    }

    public void testMethod_get() {
        consoleOutput = ConsoleOutput.redirect();
        assertEquals(consoleOutput, ConsoleOutput.get());
    }

    public void testMethod_toString() {
        consoleOutput = ConsoleOutput.redirect();
        System.out.print("id");
        assertEquals("id", consoleOutput.toString());
    }

    public void tearDown() throws Exception {
        super.tearDown();
        if (ConsoleOutput.get() != null) {
            ConsoleOutput.get().revertBack();
        }
        consoleOutput = null;
    }
}
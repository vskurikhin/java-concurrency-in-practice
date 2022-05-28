package su.svn.console;

import junit.framework.TestCase;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ConsoleErrorTest extends TestCase {

    static Class<ConsoleError> clazz;

    ConsoleError consoleError;

    ConsoleError mock;

    public void setUp() throws Exception {
        super.setUp();

        clazz = ConsoleError.class;

        Class<ConsoleError> c = ConsoleError.class;
        Constructor<ConsoleError> constructor = c.getDeclaredConstructor();
        constructor.setAccessible(true);

        mock = constructor.newInstance();

        Field consoleErrorField = c.getDeclaredField("consoleError");
        consoleErrorField.setAccessible(true);
        consoleErrorField.set(mock, null);
    }

    public void testFields() throws ReflectiveOperationException {
        Field baosField = clazz.getDeclaredField("baos");
        baosField.setAccessible(true);
        assertNotNull(baosField.get(mock));

        Field oldField = clazz.getDeclaredField("old");
        oldField.setAccessible(true);
        assertNotNull(oldField.get(mock));

        Field consoleErrorField = clazz.getDeclaredField("consoleError");
        consoleErrorField.setAccessible(true);
        assertNull(consoleErrorField.get(mock));
    }

    public void testMethod_redirect() {
        consoleError = ConsoleError.redirect();
        //noinspection SimplifiableAssertion
        assertTrue(consoleError == ConsoleError.redirect());
    }

    public void testMethod_get() {
        consoleError = ConsoleError.redirect();
        assertEquals(consoleError, ConsoleError.get());
    }

    public void testMethod_toString() {
        consoleError = ConsoleError.redirect();
        System.err.print("id");
        assertEquals("id", consoleError.toString());
    }

    public void tearDown() throws Exception {
        super.tearDown();
        if (ConsoleError.get() != null) {
            ConsoleError.get().revertBack();
        }
        consoleError = null;
    }
}
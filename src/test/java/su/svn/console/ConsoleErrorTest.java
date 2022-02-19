package su.svn.console;

import junit.framework.TestCase;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ConsoleErrorTest extends TestCase {

    ConsoleError consoleError;

    static ConsoleError mock;

    public void setUp() throws Exception {
        super.setUp();
        if (mock == null) {
            Class<ConsoleError> c = ConsoleError.class;
            Constructor<ConsoleError> constructor = c.getDeclaredConstructor();
            constructor.setAccessible(true);

            mock = constructor.newInstance();

            Field baosField = c.getDeclaredField("baos");
            baosField.setAccessible(true);
            assertNotNull(baosField.get(mock));

            Field oldField = c.getDeclaredField("old");
            oldField.setAccessible(true);
            assertNotNull(oldField.get(mock));

            Field consoleErrorField = c.getDeclaredField("consoleError");
            consoleErrorField.setAccessible(true);
            assertNull(consoleErrorField.get(mock));
        }
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
        System.err.print("test");
        assertEquals("test", consoleError.toString());
    }

    public void tearDown() throws Exception {
        super.tearDown();
        if (consoleError != null) {
            consoleError.revertBack();
        }
        consoleError = null;
    }
}
package su.svn.console;

import junit.framework.TestCase;
import org.junit.Ignore;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

@Ignore
public class ConsoleStubTest extends TestCase {

    static Class<ConsoleStub> clazz;

    ConsoleStub consoleError;

    ConsoleStub mock;

    public void setUp() throws Exception {
        super.setUp();

        clazz = ConsoleStub.class;

        Class<ConsoleStub> c = ConsoleStub.class;
        Constructor<ConsoleStub> constructor = c.getDeclaredConstructor();
        constructor.setAccessible(true);

        mock = constructor.newInstance();

        Field consoleErrorField = c.getDeclaredField("consoleStub");
        consoleErrorField.setAccessible(true);
        consoleErrorField.set(mock, null);
    }

    public void testFields() throws ReflectiveOperationException {
        Field baosField = clazz.getDeclaredField("baos");
        baosField.setAccessible(true);
        assertNotNull(baosField.get(mock));

        Field consoleErrorField = clazz.getDeclaredField("consoleStub");
        consoleErrorField.setAccessible(true);
        assertNull(consoleErrorField.get(mock));
    }

    public void testMethod_get() {
        consoleError = ConsoleStub.get();
        assertEquals(consoleError, ConsoleStub.get());
    }

    public void testMethod_toString() {
        consoleError = ConsoleStub.get();
        ConsoleStub.print("test");
        assertEquals("test", consoleError.toString());
    }

    public void tearDown() throws Exception {
    }
}
package su.svn;

import junit.framework.TestCase;
import su.svn.console.ConsoleOutput;

public class MainTest extends TestCase {

    public void tearDown() throws Exception {
        super.tearDown();
        if (ConsoleOutput.get() != null) {
            ConsoleOutput.get().revertBack();
        }
    }

    public void testMethod_main() throws Exception {
        Main.main(new String[]{});
    }
}
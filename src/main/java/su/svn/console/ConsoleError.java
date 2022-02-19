package su.svn.console;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public final class ConsoleError {

    private static ConsoleError consoleError;

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private final PrintStream old;

    public synchronized static ConsoleError redirect() {
        if (consoleError == null) {
            consoleError = new ConsoleError();
        }
        consoleError.consoleOutputRedirect();
        return consoleError;
    }

    private ConsoleError() {
        old = System.err;
    }

    private void consoleOutputRedirect() {
        PrintStream ps = new PrintStream(baos);
        System.setErr(ps);
    }

    public void revertBack() {
        System.err.flush();
        if (old != null) {
            synchronized (old) {
                System.setErr(old);
            }
        }
    }

    public static ConsoleError get() {
        return consoleError;
    }

    @Override
    public String toString() {
        return baos.toString();
    }
}
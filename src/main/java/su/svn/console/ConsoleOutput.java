package su.svn.console;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public final class ConsoleOutput {

    private static ConsoleOutput consoleOutput;

    private final ByteArrayOutputStream baos = new ByteArrayOutputStream();

    private final PrintStream old;

    public synchronized static ConsoleOutput redirect() {
        if (consoleOutput == null) {
            consoleOutput = new ConsoleOutput();
        }
        consoleOutput.consoleOutputRedirect();
        return consoleOutput;
    }

    private ConsoleOutput() {
        old = System.out;
    }

    private void consoleOutputRedirect() {
        PrintStream ps = new PrintStream(baos);
        System.setOut(ps);
    }

    public void revertBack() {
        System.out.flush();
        synchronized (old) {
            System.setOut(old);
        }
    }

    public static ConsoleOutput get() {
        return consoleOutput;
    }

    @Override
    public String toString() {
        return baos.toString();
    }
}
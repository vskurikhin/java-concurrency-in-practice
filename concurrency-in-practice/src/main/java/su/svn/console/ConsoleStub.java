package su.svn.console;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Locale;

public final class ConsoleStub {

    private static volatile ConsoleStub consoleStub;

    private final ByteArrayOutputStream baos;

    private final PrintStream ps;

    public synchronized static ConsoleStub get() {
        if (consoleStub == null) {
            consoleStub = new ConsoleStub();
        }
        return consoleStub;
    }

    private ConsoleStub() {
        baos = new ByteArrayOutputStream();
        ps = new PrintStream(baos);
    }


    /* Methods that do not terminate lines */

    public static void print(boolean b) {
        ConsoleStub.get().ps.print(b);
    }

    public static void print(char c) {
        ConsoleStub.get().ps.print(c);
    }

    public static void print(int i) {
        ConsoleStub.get().ps.print(i);
    }

    public static void print(long l) {
        ConsoleStub.get().ps.print(l);
    }

    public void print(float f) {
        ConsoleStub.get().ps.print(f);
    }

    public static void print(double d) {
        ConsoleStub.get().ps.print(d);
    }

    public static void print(char s[]) {
        ConsoleStub.get().ps.print(s);
    }

    public static void print(String s) {
        ConsoleStub.get().ps.print(s);
    }

    public static void print(Object obj) {
        ConsoleStub.get().ps.print(obj);
    }


    /* Methods that do terminate lines */

    public static void println() {
        ConsoleStub.get().ps.println();
    }

    public static void println(boolean x) {
        ConsoleStub.get().ps.println(x);
    }

    public static void println(char x) {
        ConsoleStub.get().ps.println(x);
    }

    public static void println(int x) {
        ConsoleStub.get().ps.println(x);
    }

    public static void println(long x) {
        ConsoleStub.get().ps.println(x);
    }

    public static void println(float x) {
        ConsoleStub.get().ps.println(x);
    }

    public static void println(double x) {
        ConsoleStub.get().ps.println(x);
    }

    public static void println(char x[]) {
        ConsoleStub.get().ps.println(x);
    }

    public static void println(String x) {
        ConsoleStub.get().ps.println(x);
    }

    public static void println(Object x) {
        ConsoleStub.get().ps.println(x);
    }

    public static void printf(String format, Object ... args) {
        ConsoleStub.get().ps.printf(format, args);
    }

    public static void printf(Locale l, String format, Object ... args) {
        ConsoleStub.get().ps.printf(l, format, args);
    }

    public static void format(String format, Object ... args) {
        ConsoleStub.get().ps.format(format, args);
    }

    public static void format(Locale l, String format, Object ... args) {
        ConsoleStub.get().ps.format(l, format, args);
    }

    public static void append(CharSequence csq) {
        ConsoleStub.get().ps.append(csq);
    }

    public static void append(CharSequence csq, int start, int end) {
        ConsoleStub.get().ps.append(csq, start, end);
    }

    public static void append(char c) {
        ConsoleStub.get().ps.print(c);
    }


    @Override
    public String toString() {
        return baos.toString();
    }
}
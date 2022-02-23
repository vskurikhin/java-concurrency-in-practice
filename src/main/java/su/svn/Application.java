package su.svn;

import net.jcip.examples.OptionalCachedFactorizer;
import org.apache.catalina.LifecycleException;
import su.svn.tomcat.Embedded;
import su.svn.utils.SLF4JConfigurer;

import java.io.PrintStream;

public final class Application {

    private static Application application;

    private final Embedded tomcat;

    private final PrintStream oldErr;

    public static Application create() {
        if (application == null) {
            application = new Application();
        }
        return application;
    }

    private Application() {
        oldErr = System.err;
        SLF4JConfigurer.install();
        this.tomcat = Embedded.get();

        // нужно для правильной остановки сервлетов
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                tomcat.stop();
            } catch (LifecycleException e) {
                e.printStackTrace();
            }
        }));
    }

    public void start() throws LifecycleException {
        tomcat.start(new OptionalCachedFactorizer());
    }

    public void stop() throws LifecycleException {
        tomcat.stop();
    }
}
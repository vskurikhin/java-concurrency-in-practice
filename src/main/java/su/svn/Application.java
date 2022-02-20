package su.svn;

import net.jcip.examples.StatelessFactorizer;
import org.apache.catalina.LifecycleException;
import su.svn.tomcat.Embedded;
import su.svn.utils.SLF4JConfigurer;

public final class Application {

    private final Embedded tomcat;

    public static Application create() {
        return new Application();
    }

    private Application() {

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
        tomcat.start(new StatelessFactorizer());
    }

    public void stop() throws LifecycleException {
        tomcat.stop();
    }
}

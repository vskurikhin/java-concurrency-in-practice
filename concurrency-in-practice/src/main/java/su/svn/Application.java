package su.svn;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import net.jcip.examples.OptionalCachedFactorizer;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.servlets.DefaultServlet;
import org.springframework.context.ApplicationContext;
import su.svn.tomcat.Embedded;
import su.svn.utils.SLF4JConfigurer;

@ThreadSafe
public class Application {

    private static volatile Application application;

    @GuardedBy("this")
    private ApplicationContext rootContext;

    private final Embedded tomcat;

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

    public static Application get() {
        synchronized (Application.class) {
            if (application == null) {
                application = new Application();
            }
        }
        return application;
    }

    public void start() throws LifecycleException {
        synchronized (this) {
            tomcat.start(new OptionalCachedFactorizer());
        }
    }

    public void stop() throws LifecycleException {
        tomcat.stop();
    }

    public void setRootContext(Object provider, ApplicationContext rootContext) {
        if (provider instanceof su.svn.configs.ApplicationConfig && rootContext != null) {
            synchronized (this) {
                this.rootContext = rootContext;
            }
        }
    }

    public ApplicationContext getRootContext() {
        synchronized (this) {
            return this.rootContext;
        }
    }
}

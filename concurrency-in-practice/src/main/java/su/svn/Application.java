package su.svn;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import net.jcip.examples.Factorizer;
import org.apache.catalina.LifecycleException;
import org.springframework.context.ApplicationContext;
import su.svn.tomcat.Embedded;
import su.svn.utils.SLF4JConfigurer;

import java.util.concurrent.atomic.AtomicReference;

@ThreadSafe
public class Application {

    private static volatile Application application;

    private static final AtomicReference<ApplicationContext> ctxRef = new AtomicReference<>(null);

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
            tomcat.start(new Factorizer());
        }
    }

    public void stop() throws LifecycleException {
        tomcat.stop();
    }

    public void setRootContext(Object provider, ApplicationContext rootContext) {
        if (provider instanceof su.svn.configs.ApplicationConfig && rootContext != null) {
            this.ctxRef.compareAndSet(null, rootContext);
            System.err.println("rootContext = " + this.ctxRef.getOpaque());
            System.out.println("application = " + application);
        }
    }

    public ApplicationContext getRootContext() {
        System.err.println("ctxRef.get() = " + this.ctxRef.getOpaque());
        System.out.println("application = " + application);
        return this.ctxRef.getOpaque();
    }
}
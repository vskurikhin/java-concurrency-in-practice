package su.svn;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import net.jcip.examples.Factorizer;
import org.apache.catalina.LifecycleException;
import org.springframework.context.ApplicationContext;
import su.svn.enums.Environment;
import su.svn.tomcat.Embedded;
import su.svn.utils.SLF4JConfigurer;

import java.util.Optional;

@ThreadSafe
public enum Application {
    Instance;

    @GuardedBy("this")
    private ApplicationContext rootContext;

    private final Embedded tomcat;

    Application() {
        SLF4JConfigurer.install();
        this.tomcat = Embedded.createInstance(Environment.HOSTNAME, Environment.PORT);

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
        synchronized (this) {
            tomcat.start(new Factorizer());
        }
    }

    public void stop() throws LifecycleException {
        tomcat.stop();
    }

    public synchronized void setRootContext(Object provider, ApplicationContext rootContext) {
        if (provider instanceof su.svn.configs.ApplicationConfig && rootContext != null) {
            this.rootContext = rootContext;
        }
    }

    public synchronized Optional<ApplicationContext> getRootContext() {
        return Optional.ofNullable(this.rootContext);
    }
}
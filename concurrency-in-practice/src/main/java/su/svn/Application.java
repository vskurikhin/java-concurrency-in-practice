package su.svn;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.apache.catalina.LifecycleException;
import org.springframework.context.ApplicationContext;
import su.svn.enums.Environment;
import su.svn.tomcat.Embedded;
import su.svn.utils.SLF4JConfigurer;

@ThreadSafe
public enum Application {
    Instance;

    @GuardedBy("Application.class")
    private volatile ApplicationContext context;

    @GuardedBy("Application.class")
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
        synchronized (Application.class) {
            tomcat.start();
        }
    }

    public void stop() throws LifecycleException {
        tomcat.stop();
    }

    public void setRootContext(Object provider, ApplicationContext rootContext) {
        if (provider instanceof su.svn.configs.ApplicationConfig && rootContext != null) {
            synchronized (Application.class) {
                this.context = rootContext;
            }
        }
    }

    public ApplicationContext getRootContext() {
        synchronized (Application.class) {
            return this.context;
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + "@" + Integer.toHexString(hashCode());
    }
}
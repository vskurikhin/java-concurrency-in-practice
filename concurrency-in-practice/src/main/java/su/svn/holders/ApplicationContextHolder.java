package su.svn.holders;

import net.jcip.annotations.GuardedBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

public final class ApplicationContextHolder {

    // Авторы JSR-133 рекомендуют использовать voloatile для Double Cheсked Lock.
    private static volatile ApplicationContextHolder instance;

    private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationContextHolder.class);

    @GuardedBy("ApplicationContextHolder.class")
    private volatile ApplicationContext context;

    // Ленивая инициализация.
    public static ApplicationContextHolder createInstance() {
        ApplicationContextHolder localInstance = instance;
        if (localInstance == null) {
            synchronized (ApplicationContextHolder.class) {
                localInstance = instance;
                if (localInstance == null) {
                    instance = new ApplicationContextHolder();
                }
            }
        }
        return instance;
    }

    private ApplicationContextHolder() {
    }

    public void setRootContext(Object provider, ApplicationContext rootContext) {
        if (provider instanceof su.svn.configs.ApplicationConfig && rootContext != null) {
            synchronized (ApplicationContextHolder.class) {
                this.context = rootContext;
            }
        }
    }

    public ApplicationContext getRootContext() {
        synchronized (ApplicationContextHolder.class) {
            return this.context;
        }
    }
}
package su.svn.configs;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import su.svn.Application;

import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

@Configuration
@ComponentScan(basePackages = {"su.svn"})
@Import({WebConfig.class})
public class ApplicationConfig implements WebApplicationInitializer, ApplicationContextAware {

    @Override
    public void onStartup(ServletContext container) {
        /// Создаём «root» контекст приложения Spring.
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(ApplicationConfig.class);
        rootContext.register(DispatcherConfig.class);
        // rootContext.getEnvironment().setConversionService(new ApplicationConversionService());

        // Управление жизненным циклом «root» контекста приложения.
        container.addListener(new ContextLoaderListener(rootContext));

        // Регистрируем и сопоставляем сервлет диспетчера.
        ServletRegistration.Dynamic dispatcher = container
                .addServlet("dispatcher", new DispatcherServlet(rootContext));
        dispatcher.setLoadOnStartup(1);
        dispatcher.addMapping("/");
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Application.Instance.setRootContext(this, applicationContext);
    }
}

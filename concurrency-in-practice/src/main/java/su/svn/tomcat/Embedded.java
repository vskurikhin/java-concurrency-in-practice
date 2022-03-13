package su.svn.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.JarResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import su.svn.enums.Environment;
import su.svn.utils.Resource;

import javax.servlet.GenericServlet;
import java.io.File;
import java.io.IOException;

public final class Embedded {

    // Авторы JSR-133 рекомендуют использовать voloatile для Double Cheсked Lock.
    private static volatile Embedded embedded;

    private static final Logger LOGGER = LoggerFactory.getLogger(Embedded.class);

    private final Tomcat tomcat;

    private final int port;

    private final String contextPath;

    private Context context;

    public static Embedded get() {
        return createInstance(Environment.HOSTNAME, Environment.PORT);
    }

    // Ленивая инициализация.
    public static Embedded createInstance(String hostname, int port) {
        Embedded localInstance = embedded;
        if (localInstance == null) {
            synchronized (Embedded.class) {
                localInstance = embedded;
                if (localInstance == null) {
                    embedded = new Embedded(hostname, port);
                }
            }
        }
        return embedded;
    }

    private Embedded(String hostname, int port) {
        this.tomcat = new Tomcat();
        this.tomcat.setBaseDir(Environment.TMP_DIR);
        this.tomcat.setHostname(hostname);
        this.tomcat.setPort(port);
        this.port = port;

        this.tomcat.setConnector(this.tomcat.getConnector());
        // предотвратить регистрацию сервлета jsp
        this.tomcat.setAddDefaultWebXmlToWebapp(false);

        this.contextPath = ""; // root context
        //noinspection ResultOfMethodCallIgnored
        new File(Environment.STATIC_DIR).mkdirs();
        try {
            String docBase = new File(Environment.STATIC_DIR).getCanonicalPath();
            this.context = this.tomcat.addWebapp(contextPath, docBase);
            standardContextConfigurer(this.context);
        } catch (IOException e) {
            LOGGER.error("StandardContext configuring", e);
        }
    }

    public int getPort() {
        return port;
    }

    public Context getContext() {
        return context;
    }

    public void addServlet(String servletName, GenericServlet servlet, String pattern) {
        tomcat.addServlet(contextPath, servletName, servlet);
        context.addServletMappingDecoded(pattern, servletName);
    }

    public void start(GenericServlet servlet) throws LifecycleException {

        String servletName = "go";
        String urlPattern = "/go";
        addServlet(servletName, servlet, urlPattern);

        WebResourceRoot webResourceRoot = new StandardRoot(context);
        defaultServletConfiguring();
        resourceRootConfiguring(webResourceRoot);
        start();
    }

    private void standardContextConfigurer(Context context) {
        context.setAddWebinfClassesResources(true); // process /META-INF/resources for static

        // fix Illegal reflective access by org.apache.catalina.loader.WebappClassLoaderBase
        // https://github.com/spring-projects/spring-boot/issues/15101#issuecomment-437384942
        StandardContext standardContext = (StandardContext) context;
        standardContext.setClearReferencesObjectStreamClassCaches(false);
        standardContext.setClearReferencesRmiTargets(false);
        standardContext.setClearReferencesThreadLocals(false);
    }

    private void defaultServletConfiguring() {
        // Для статической работы.
        final String defaultServletName = "default";
        Wrapper defaultServlet = context.createWrapper();
        defaultServlet.setName(defaultServletName);
        defaultServlet.setServletClass("org.apache.catalina.servlets.DefaultServlet");
        defaultServlet.addInitParameter("debug", "0");
        defaultServlet.addInitParameter("listings", "false");
        defaultServlet.setLoadOnStartup(1);
        // Otherwise the default location of a Spring DispatcherServlet cannot be set
        defaultServlet.setOverridable(true);
        context.addChild(defaultServlet);
        context.addServletMappingDecoded("/", defaultServletName);
        // index.html on http://127.0.0.1:8080
        context.addWelcomeFile("index.html");
    }

    private void resourceRootConfiguring(WebResourceRoot webResourceRoot) {

        // Добавим сам jar со статическими ресурсами (html) и аннотированными сервлетами.

        String webAppMount = "/WEB-INF/classes";
        WebResourceSet webResourceSet;
        if ( ! Resource.isJar()) {
            // potential dangerous - if last argument will "/" that means tomcat will serves self jar with .class files
            String base = Resource.getResourceFromFs();
            webResourceSet = new DirResourceSet(webResourceRoot, webAppMount, base, "/");
        } else {
            String base = Resource.getResourceFromJarFile();
            webResourceSet = new JarResourceSet(webResourceRoot, webAppMount, base, "/");
        }
        webResourceRoot.addJarResources(webResourceSet);
        context.setResources(webResourceRoot);
    }

    private void start() throws LifecycleException {
        tomcat.start();
        Server server = tomcat.getServer();
        server.await();
    }

    public void stop() throws LifecycleException {
        tomcat.getServer().stop();
    }

    @Override
    public String toString() {
        return "Embedded{" +
                "tomcat=" + tomcat +
                '}';
    }
}
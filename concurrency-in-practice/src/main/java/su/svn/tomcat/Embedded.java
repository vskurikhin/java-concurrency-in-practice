package su.svn.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.Server;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.Wrapper;
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

    private static Embedded embedded;

    private static final Logger LOGGER = LoggerFactory.getLogger(Embedded.class);

    private final Tomcat tomcat;

    private final String contextPath;

    private Context context;

    public synchronized static Embedded get() {
        if (embedded == null) {
            embedded = new Embedded();
        }
        return embedded;
    }

    private Embedded() {
        this.tomcat = new Tomcat();
        this.tomcat.setBaseDir(Environment.TMP_DIR);
        this.tomcat.setPort(Environment.PORT);

        this.tomcat.setConnector(this.tomcat.getConnector());
        // prevent register jsp servlet
        this.tomcat.setAddDefaultWebXmlToWebapp(false);

        contextPath = ""; // root context
        //noinspection ResultOfMethodCallIgnored
        new File(Environment.STATIC_DIR).mkdirs();
        try {
            String docBase = new File(Environment.STATIC_DIR).getCanonicalPath();
            this.context = this.tomcat.addWebapp(contextPath, docBase);
            StandardContextConfigurer.setUp(this.context);
        } catch (IOException e) {
            LOGGER.error("StandardContext configuring", e);
        }
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

    private void defaultServletConfiguring() {
        // Additions to make serving static work
        final String defaultServletName = "default";
        Wrapper defaultServlet = context.createWrapper();
        defaultServlet.setName(defaultServletName);
        defaultServlet.setServletClass("org.apache.catalina.servlets.DefaultServlet");
        defaultServlet.addInitParameter("debug", "0");
        defaultServlet.addInitParameter("listings", "false");
        defaultServlet.setLoadOnStartup(1);
        context.addChild(defaultServlet);
        context.addServletMappingDecoded("/", defaultServletName);
        // display index.html on http://127.0.0.1:8080
        context.addWelcomeFile("index.html");
    }

    private void resourceRootConfiguring(WebResourceRoot webResourceRoot) {

        // add itself jar with static resources (html) and annotated servlets

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
package su.svn.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.Wrapper;

final class DefaultServletConfigurer {

    private DefaultServletConfigurer(Embedded tomcat) {
        Context context = tomcat.getContext();
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

    public static DefaultServletConfigurer setUp(Embedded tomcat) {
        return new DefaultServletConfigurer(tomcat);
    }
}

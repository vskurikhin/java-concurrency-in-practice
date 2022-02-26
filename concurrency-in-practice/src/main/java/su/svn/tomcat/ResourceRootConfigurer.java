package su.svn.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.JarResourceSet;
import su.svn.utils.Resource;

public class ResourceRootConfigurer {

    private ResourceRootConfigurer(Context context, WebResourceRoot webResourceRoot) {

        // add itself jar with static resources (html) and annotated servlets

        String webAppMount = "/WEB-INF/classes";
        WebResourceSet webResourceSet;
        if ( ! Resource.isJar()) {
            // potential dangerous - if last argument will "/" that means tomcat will serves self jar with .class files
            webResourceSet = new DirResourceSet(webResourceRoot, webAppMount, Resource.getResourceFromFs(), "/");
        } else {
            webResourceSet = new JarResourceSet(webResourceRoot, webAppMount, Resource.getResourceFromJarFile(), "/");
        }
        webResourceRoot.addJarResources(webResourceSet);
        context.setResources(webResourceRoot);
    }

    public static ResourceRootConfigurer setUp(Context context, WebResourceRoot webResourceRoot) {
        //noinspection InstantiationOfUtilityClass
        return new ResourceRootConfigurer(context, webResourceRoot);
    }
}

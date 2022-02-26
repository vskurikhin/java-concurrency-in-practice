package su.svn.tomcat;

import org.apache.catalina.Context;
import org.apache.catalina.core.StandardContext;

import java.io.IOException;

public final class StandardContextConfigurer {

    private StandardContextConfigurer(Context context) throws IOException {

        context.setAddWebinfClassesResources(true); // process /META-INF/resources for static

        // fix Illegal reflective access by org.apache.catalina.loader.WebappClassLoaderBase
        // https://github.com/spring-projects/spring-boot/issues/15101#issuecomment-437384942
        StandardContext standardContext = (StandardContext) context;
        standardContext.setClearReferencesObjectStreamClassCaches(false);
        standardContext.setClearReferencesRmiTargets(false);
        standardContext.setClearReferencesThreadLocals(false);
    }

    public static StandardContextConfigurer setUp(Context context) throws IOException {
        //noinspection InstantiationOfUtilityClass
        return new StandardContextConfigurer(context);
    }
}

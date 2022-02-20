package su.svn.utils;

import org.slf4j.bridge.SLF4JBridgeHandler;

public class SLF4JConfigurer {
    /**
     * initialize logback
     */
    public static void install() {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }
}

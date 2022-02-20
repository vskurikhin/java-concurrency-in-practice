package su.svn.enums;

import su.svn.utils.TomcatConfigurer;

public enum Environment {
    Environment;

    public static final int PORT = TomcatConfigurer.port("8080");

    public static final String STATIC_DIR = TomcatConfigurer.staticDir("/tmp/tomcat-static");

    public static final String TMP_DIR = TomcatConfigurer.tmpDir("/tmp/tomcat-tmp");
}

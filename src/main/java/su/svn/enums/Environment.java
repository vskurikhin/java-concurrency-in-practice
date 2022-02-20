package su.svn.enums;

import java.util.Optional;

public enum Environment {
    Environment;

    public static final int PORT = port("8080");

    public static final String STATIC_DIR = staticDir("/tmp/tomcat-static");

    public static final String TMP_DIR = tmpDir("/tmp/tomcat-tmp");

    public static int port(String port) {
        return Optional.ofNullable(System.getenv("PORT"))
                .map(Integer::parseInt)
                .orElse(Integer.parseInt(port));
    }

    public static String staticDir(String dir) {
        return Optional.ofNullable(System.getenv("STATIC_DIR")).orElse("/tmp/tomcat-static");
    }

    public static String tmpDir(String dir) {
        return Optional.ofNullable(System.getenv("TMP_DIR")).orElse(dir);
    }
}

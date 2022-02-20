package su.svn.utils;

import java.util.Optional;

public class TomcatConfigurer {
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
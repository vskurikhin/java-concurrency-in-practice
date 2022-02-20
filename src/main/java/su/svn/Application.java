package su.svn;

import org.apache.catalina.LifecycleException;
import su.svn.tomcat.Embedded;
import su.svn.utils.SLF4JConfigurer;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public final class Application {

    private final Embedded tomcat;

    private final HttpServlet servlet = new HttpServlet() {
        @Override
        protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
            PrintWriter writer = resp.getWriter();

            writer.println("<html><title>Welcome</title><body>");
            writer.println("<h1>Have a Great Day!</h1>");
            writer.println("</body></html>");
        }
    };

    public static Application create() {
        return new Application();
    }

    private Application() {

        SLF4JConfigurer.install();
        this.tomcat = Embedded.get();

        // need for proper destroying servlets
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                tomcat.stop();
            } catch (LifecycleException e) {
                e.printStackTrace();
            }
        }));
    }

    public void start() throws LifecycleException {
        tomcat.start(servlet);
    }

    public void stop() throws LifecycleException {
        tomcat.stop();
    }
}

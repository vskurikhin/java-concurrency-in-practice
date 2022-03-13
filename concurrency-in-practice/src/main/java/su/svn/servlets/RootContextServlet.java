package su.svn.servlets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import su.svn.Application;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serial;

@WebServlet(urlPatterns = "/context", loadOnStartup = 1)
public class RootContextServlet extends HttpServlet {

    @Serial
    private static final long serialVersionUID = -670850199949906410L;

    private static final Logger LOGGER = LoggerFactory.getLogger(RootContextServlet.class);

    @Override
    public void init() {
        LOGGER.info("Initializing {}", RootContextServlet.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        final PrintWriter writer = resp.getWriter();

        writer.println("<html><title>Spring context</title><body>");
        writer.println("<h1>Spring context bean list:</h1>");
        Application.Instance
                .getRootContext()
                .ifPresentOrElse(
                        c -> outRootContext(writer, c),
                        () -> outEmpty(writer)
                );
        writer.println("</body></html>");
    }

    private void outRootContext(PrintWriter writer, ApplicationContext context) {
        LOGGER.info("Root context {}", context);
        for (String name : context.getBeanDefinitionNames()) {
            writer.println(name + "<br>");
        }
    }

    private void outEmpty(PrintWriter writer) {
        LOGGER.error("Root context is null!");
    }

    @Override
    public void destroy() {
        LOGGER.info("Destroying {}", RootContextServlet.class);
    }
}

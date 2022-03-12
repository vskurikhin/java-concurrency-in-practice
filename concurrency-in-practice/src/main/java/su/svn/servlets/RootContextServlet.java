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

@WebServlet(urlPatterns = "/context", loadOnStartup = 1)
public class RootContextServlet extends HttpServlet {

    private static final long serialVersionUID = -670850199949906410L;

    private static final Logger LOGGER = LoggerFactory.getLogger(RootContextServlet.class);

    @Override
    public void init() {
        LOGGER.info("Initializing {}", RootContextServlet.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();

        writer.println("<html><title>Spring context</title><body>");
        writer.println("<h1>Spring context bean list:</h1>");
        LOGGER.info("{}", Application.get().getRootContext());
        ApplicationContext context = Application.get().getRootContext();
        for (String name : context.getBeanDefinitionNames()) {
            writer.println(name + "<br>");
        }
        writer.println("</body></html>");
    }

    @Override
    public void destroy() {
        LOGGER.info("Destroying {}", RootContextServlet.class);
    }
}

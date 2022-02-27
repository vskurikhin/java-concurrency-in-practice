package net.jcip.examples;

import java.io.IOException;
import java.math.BigInteger;
import javax.servlet.*;

import net.jcip.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UnsafeCountingFactorizer
 *
 * Servlet that counts requests without the necessary synchronization
 *
 * @author Brian Goetz and Tim Peierls
 */
@NotThreadSafe
public class UnsafeCountingFactorizer extends GenericServlet implements Servlet {

    public static final Logger LOGGER = LoggerFactory.getLogger(UnsafeCountingFactorizer.class);

    private long count = 0;

    public long getCount() {
        return count;
    }

    public void service(ServletRequest req, ServletResponse resp) throws IOException {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        ++count;
        encodeIntoResponse(resp, factors);
    }

    void encodeIntoResponse(ServletResponse res, BigInteger[] factors) throws IOException {
        long count = this.count;
        LOGGER.trace(" " + count);
        ServletOutputStream out = res.getOutputStream();
        out.println(count);
        out.close();
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[] { i };
    }
}

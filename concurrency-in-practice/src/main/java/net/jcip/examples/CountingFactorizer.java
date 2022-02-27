package net.jcip.examples;

import java.io.IOException;
import java.math.BigInteger;
import java.util.concurrent.atomic.*;
import javax.servlet.*;

import net.jcip.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CountingFactorizer
 *
 * Servlet that counts requests using AtomicLong
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class CountingFactorizer extends GenericServlet implements Servlet {

    private static final long serialVersionUID = 9050419463334234096L;

    public static final Logger LOGGER = LoggerFactory.getLogger(CountingFactorizer.class);

    private final AtomicLong count = new AtomicLong(0);

    public long getCount() { return count.get(); }

    public void service(ServletRequest req, ServletResponse resp) throws IOException {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        count.incrementAndGet();
        encodeIntoResponse(resp, factors);
    }

    void encodeIntoResponse(ServletResponse res, BigInteger[] factors) throws IOException {
            long count = this.count.get();
            LOGGER.info(" " + count);
            ServletOutputStream out = res.getOutputStream();
            out.println(count);
            out.close();
    }

    BigInteger extractFromRequest(ServletRequest req) {return null; }

    BigInteger[] factor(BigInteger i) { return null; }
}

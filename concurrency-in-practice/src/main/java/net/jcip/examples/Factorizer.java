package net.jcip.examples;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import javax.servlet.GenericServlet;
import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import net.jcip.annotations.GuardedBy;
import net.jcip.annotations.ThreadSafe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import su.svn.utils.PollardRho;

/**
 * Factorizer
 * <p/>
 * Factorizing servlet that caches results using Memoizer
 *
 * @author Victor N. Skurikhin
 */
@ThreadSafe
public class Factorizer extends GenericServlet implements Servlet {

    private static final long serialVersionUID = -6342065447646341351L;

    public static final Logger LOGGER = LoggerFactory.getLogger(Factorizer.class);

    private final Computable<BigInteger, BigInteger[]> factorComputable = this::factor;

    private final Memoizer<BigInteger, BigInteger[]> cache = new Memoizer<>(factorComputable);

    @GuardedBy("this")
    private int hits = 0;

    @GuardedBy("this")
    private int computed = 0;

    public void service(ServletRequest req, ServletResponse resp) {
        synchronized (this) {
            hits++;
        }
        try {
            BigInteger i = extractFromRequest(req);
            encodeIntoResponse(resp, i, cache.compute(i));
        } catch (InterruptedException | IOException e) {
            try {
                encodeError(resp, "factorization interrupted");
            } catch (IOException ex) {
                LOGGER.error("e: {}\nex: {}", e, ex);
            }
        }
    }

    private void encodeIntoResponse(ServletResponse resp, BigInteger number, BigInteger[] factors) throws IOException {
        String numberFactors = Arrays.toString(factors);
        LOGGER.debug(" " + number
                + " " + numberFactors
                + " hits: " + getHits()
                + " computed: " + getComputed()
                + " cacheHits: " + cache.getCacheHits()
        );
        ServletOutputStream out = resp.getOutputStream();
        out.close();
    }

    public int getHits() {
        synchronized (this) {
            return hits;
        }
    }

    private Integer getComputed() {
        synchronized (this) {
            return computed;
        }
    }

    private void encodeError(ServletResponse resp, String errorString) throws IOException {
        LOGGER.error(errorString);
        ServletOutputStream out = resp.getOutputStream();
        out.close();
    }

    private BigInteger extractFromRequest(ServletRequest req) {
        String n = req.getParameter("n");
        String number = n != null ? n : "13";
        LOGGER.trace(" " + number);
        return new BigInteger(number);
    }

    private BigInteger[] factor(BigInteger number) {
        BigInteger[] result = new PollardRho().factor(number);
        synchronized (this) {
            computed++;
        }
        return result;
    }
}

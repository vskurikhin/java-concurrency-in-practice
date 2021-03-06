package net.jcip.examples;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Optional;
import javax.servlet.*;

import net.jcip.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import su.svn.utils.PollardRho;

/**
 * CachedFactorizer
 * <p/>
 * Servlet that caches its last request and result
 *
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class OptionalCachedFactorizer extends GenericServlet implements Servlet {

    private static final long serialVersionUID = -3958427413779292492L;

    public static final Logger LOGGER = LoggerFactory.getLogger(OptionalCachedFactorizer.class);

    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;
    @GuardedBy("this") private long hits;
    @GuardedBy("this") private long cacheHits;

    public synchronized long getHits() {
        return hits;
    }

    public synchronized double getCacheHitRatio() {
        return (double) cacheHits / (double) hits;
    }

    public void service(ServletRequest req, ServletResponse resp) throws IOException {
        BigInteger i = extractFromRequest(req);
        Optional<BigInteger[]> factors = Optional.empty();
        synchronized (this) {
            ++hits;
            if (i.equals(lastNumber)) {
                ++cacheHits;
                factors = Optional.of(lastFactors.clone());
            }
        }
        if (factors.isEmpty()) {
            factors = Optional.of(factor(i));
            synchronized (this) {
                lastNumber = i;
                lastFactors = factors.get().clone();
            }
        }
        encodeIntoResponse(resp, factors.get());
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) throws IOException {
        String lastFactors = Arrays.toString(factors);
        LOGGER.debug(" " + this.lastNumber + " " + lastFactors + " hits: " + hits + " cacheHits: " + cacheHits);
        ServletOutputStream out = resp.getOutputStream();
        out.println(this.lastNumber + " " + lastFactors);
        out.close();
    }

    BigInteger extractFromRequest(ServletRequest req) {
        String n = req.getParameter("n");
        String number = n != null ? n : "7";
        LOGGER.trace(" " + number);
        return new BigInteger(number);
    }

    BigInteger[] factor(BigInteger number) {
        return new PollardRho().factor(number);
    }
}

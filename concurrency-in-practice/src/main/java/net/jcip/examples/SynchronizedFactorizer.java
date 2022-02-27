package net.jcip.examples;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.servlet.*;

import net.jcip.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SynchronizedFactorizer
 *
 * Servlet that caches last result, but with unnacceptably poor concurrency
 *
 * @author Brian Goetz and Tim Peierls
 */

@ThreadSafe
public class SynchronizedFactorizer extends GenericServlet implements Servlet {

    private static final long serialVersionUID = -1586706938500169581L;

    public static final Logger LOGGER = LoggerFactory.getLogger(UnsafeCachingFactorizer.class);

    @GuardedBy("this") private BigInteger lastNumber;
    @GuardedBy("this") private BigInteger[] lastFactors;

    public synchronized void service(ServletRequest req,
                                     ServletResponse resp) throws IOException {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber))
            encodeIntoResponse(resp, lastFactors);
        else {
            BigInteger[] factors = factor(i);
            lastNumber = i;
            lastFactors = factors;
            encodeIntoResponse(resp, factors);
        }
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) throws IOException {
        String lastFactors = Arrays.toString(factors);
        LOGGER.debug(" " + this.lastNumber + " " + lastFactors);
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

        List<BigInteger> numbers = new ArrayList<>();
        BigInteger i = BigInteger.ONE;
        while (i.compareTo(number.sqrt().add(BigInteger.ONE)) < 0) {
            i = i.add(BigInteger.ONE);
            if (number.mod(i).equals(BigInteger.ZERO)) {
                numbers.add(i);
                number = number.divide(i);
            }
        }
        return numbers.toArray(new BigInteger[]{});
    }
}


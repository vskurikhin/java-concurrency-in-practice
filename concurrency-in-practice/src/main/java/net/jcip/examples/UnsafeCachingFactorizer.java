package net.jcip.examples;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.*;
import javax.servlet.*;

import net.jcip.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * UnsafeCachingFactorizer
 *
 * Servlet that attempts to cache its last result without adequate atomicity
 *
 * @author Brian Goetz and Tim Peierls
 */

@NotThreadSafe
public class UnsafeCachingFactorizer extends GenericServlet implements Servlet {

    private static final long serialVersionUID = 5249874834575715280L;

    public static final Logger LOGGER = LoggerFactory.getLogger(UnsafeCachingFactorizer.class);

    private final AtomicReference<BigInteger> lastNumber
            = new AtomicReference<BigInteger>();
    private final AtomicReference<BigInteger[]> lastFactors
            = new AtomicReference<BigInteger[]>();

    public void service(ServletRequest req, ServletResponse resp) throws IOException {
        BigInteger i = extractFromRequest(req);
        if (i.equals(lastNumber.get()))
            encodeIntoResponse(resp, lastFactors.get());
        else {
            BigInteger[] factors = factor(i);
            lastNumber.set(i);
            // осталось окно уязвимости
            try {
                Thread.sleep(1, new Random().nextInt(999999));
            } catch (InterruptedException e) {
                LOGGER.error("окно уязвимости ", e);
            }
            lastFactors.set(factors);
            encodeIntoResponse(resp, factors);
        }
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) throws IOException {
        long lastNumber0 = this.lastNumber.get().longValue();
        String lastFactors = Arrays.toString(factors);
        LOGGER.debug(" " + lastNumber0 + " " + lastFactors);
        ServletOutputStream out = resp.getOutputStream();
        out.println(lastNumber0 + " " + lastFactors);
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
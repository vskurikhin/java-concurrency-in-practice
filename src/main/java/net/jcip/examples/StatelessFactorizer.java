package net.jcip.examples;

import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import javax.servlet.*;

import net.jcip.annotations.*;

/**
 * StatelessFactorizer
 *
 * A stateless servlet
 * 
 * @author Brian Goetz and Tim Peierls
 */
@ThreadSafe
public class StatelessFactorizer extends GenericServlet implements Servlet {

    private static final long serialVersionUID = -7956970569697060919L;

    public void service(ServletRequest req, ServletResponse resp) throws IOException {
        BigInteger i = extractFromRequest(req);
        BigInteger[] factors = factor(i);
        encodeIntoResponse(resp, factors);
    }

    void encodeIntoResponse(ServletResponse resp, BigInteger[] factors) throws IOException {
        resp.getOutputStream().print(Arrays.toString(factors));
    }

    BigInteger extractFromRequest(ServletRequest req) {
        return new BigInteger("7");
    }

    BigInteger[] factor(BigInteger i) {
        // Doesn't really factor
        return new BigInteger[] { i };
    }
}

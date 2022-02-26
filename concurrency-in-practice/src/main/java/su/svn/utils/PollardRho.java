package su.svn.utils;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Set;
import java.util.TreeSet;

public class PollardRho {

    private final static SecureRandom random = new SecureRandom();

    Set<BigInteger> result = new TreeSet<>();

    private static BigInteger rho(BigInteger number) {
        BigInteger divisor;
        BigInteger c = new BigInteger(number.bitLength(), random);
        BigInteger x = new BigInteger(number.bitLength(), random);
        BigInteger xx = x;

        // check divisibility by 2
        if (number.mod(BigInteger.TWO).compareTo(BigInteger.ZERO) == 0) {
            return BigInteger.TWO;
        }

        do {
            x = x.multiply(x).mod(number).add(c).mod(number);
            xx = xx.multiply(xx).mod(number).add(c).mod(number);
            xx = xx.multiply(xx).mod(number).add(c).mod(number);
            divisor = x.subtract(xx).gcd(number);
        } while ((divisor.compareTo(BigInteger.ONE)) == 0);

        return divisor;
    }

    public void fact(BigInteger number) {
        if (number.compareTo(BigInteger.ONE) == 0) return;
        if (number.isProbablePrime(20)) {
            result.add(number);
            return;
        }
        BigInteger divisor = rho(number);
        fact(divisor);
        fact(number.divide(divisor));
    }

    public BigInteger[] factor(BigInteger number) {
        fact(number);
        return result.toArray(new BigInteger[0]);
    }
}

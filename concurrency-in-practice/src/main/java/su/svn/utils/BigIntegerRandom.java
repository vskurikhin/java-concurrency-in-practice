package su.svn.utils;

import java.math.BigInteger;
import java.util.Random;

public final class BigIntegerRandom {

    private BigIntegerRandom() {
    }

    public static final BigInteger UPPER = BigInteger.valueOf(Long.MAX_VALUE / 101 * 3);

    private static final BigInteger SUB = BigInteger.valueOf(new Random().nextInt(100));

    private static final int DELTA = 92 + new Random().nextInt(16);

    public static BigInteger get() {
        int nlen = UPPER.bitLength();
        BigInteger nm1 = UPPER.subtract(SUB);
        BigInteger randomNumber, temp;
        do {
            Random rand = new Random();
            temp = new BigInteger(nlen + DELTA, rand);
            randomNumber = temp.mod(UPPER);
        } while (temp.subtract(randomNumber).add(nm1).bitLength() >= nlen + DELTA);

        return randomNumber;
    }

    public static BigInteger get(BigInteger bound) {
        byte[] bytes = bound.toByteArray();
        BigInteger result;

        do {
            new Random().nextBytes(bytes);
            bytes[bytes.length - 1] &= (byte) 0x7F; //force sign bit to positive
            result = new BigInteger(bytes);
        } while (bound.compareTo(result) <= 0);

        return result;
    }
}

package su.svn.utils;

import junit.framework.TestCase;

import java.math.BigInteger;

public class BigIntegerRandomTest extends TestCase {

    public void setUp() throws Exception {
        super.setUp();
    }

    public void testGet() {
        for (int i = 0; i < 100; i++) {
            BigInteger b = BigIntegerRandom.get();
            assertTrue(BigIntegerRandom.UPPER.compareTo(b) > 0);
        }
    }
}
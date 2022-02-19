package net.jcip.examples;

import junit.framework.TestCase;

public class SequenceTest extends TestCase {

    Sequence sequence;

    public void setUp() throws Exception {
        super.setUp();
        sequence = new Sequence();
    }

    public void testMethod_getNext() {
        assertEquals(0, sequence.getNext());
    }
}
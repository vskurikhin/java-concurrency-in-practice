package net.jcip.examples;

import junit.framework.TestCase;

public class UnsafeSequenceTest extends TestCase {

    UnsafeSequence unsafeSequence;

    public void setUp() throws Exception {
        super.setUp();
        unsafeSequence = new UnsafeSequence();
    }

    public void testMethod_getNext() {
        assertEquals(0, unsafeSequence.getNext());
    }
}
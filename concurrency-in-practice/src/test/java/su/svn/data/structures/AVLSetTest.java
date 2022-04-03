package su.svn.data.structures;

import org.junit.Before;
import org.junit.Test;

import java.util.Random;

import static org.junit.Assert.*;

public class AVLSetTest {

    AVLSet<String> set;
    @Before
    public void setUp() throws Exception {
        set = new AVLSet<>();
    }


    @Test
    public void insert() {
        for (int i = 0; i < Short.MAX_VALUE; i++) {
            set.insert(Integer.toString(new Random().nextInt(Short.MAX_VALUE*1024)));
        }
        set.print("ZZZZZZZZZZZZZZZZZZZZZZZZ");
    }

}
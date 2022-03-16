package su.svn.utils;

import org.junit.Assert;
import org.junit.Test;

import java.util.Comparator;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Stream;

public class OwnCollectorsTest {

    Comparator<String> comparatorStringLength = Comparator.comparingInt(String::length);

    Collector<String, ?, Optional<String>> oc = OwnCollectors.minMax(
            comparatorStringLength, (min, max) -> min + "|" + max
    );

    @Test
    public void test_minMax() {
        Optional<String> test = Stream.of(CountingChart.NUMBERS_1_TO_100)
                .parallel() // for Accumulator::combine
                .collect(oc);
        Assert.assertTrue(test.isPresent());
        Assert.assertEquals("one|seventy-three", test.get());
    }

    @Test
    public void test_minMax_empty() {
        Optional<String> test = Stream.<String>of()
                .collect(oc);
        Assert.assertFalse(test.isPresent());
    }
}
package su.svn.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import su.svn.identification.Ident;
import su.svn.identification.Identification;
import su.svn.identification.IdentificationLong;

import java.util.function.Function;

public class ClassSwitchMapFunctionTest {

    ClassMap<Function<Object, String>> switchMap = new ClassSwitchMapFunction<>() {{
        put(long.class, o -> "long");
        put(Long.class, o -> "Long");
        put(String.class, o -> "String");
    }};

    String switchCase(Ident record) {
        if (record instanceof IdentificationLong) {
            IdentificationLong r = (IdentificationLong) record;
            return switchMap.get(r.idClass()).map(c -> c.apply(r.id())).orElseThrow();
        }
        if (record instanceof Identification) {
            Identification<?> r = (Identification<?>) record;
            return switchMap.get(r.idClass()).map(c -> c.apply(r.id())).orElseThrow();
        }
        return null;
    }

    Ident test1;

    Ident test2;

    Ident test3;

    @Before
    public void setUp() {
        test1 = TestIdentLong.builder().id(1).build();
        test2 = TestIdentBoxingLong.builder().id(2L).build();
        test3 = TestIdentString.builder().id("3").build();
    }

    @Test
    public void test() {
        Assert.assertEquals("long", switchCase(test1));
        Assert.assertEquals("Long", switchCase(test2));
        Assert.assertEquals("String", switchCase(test3));
    }
}
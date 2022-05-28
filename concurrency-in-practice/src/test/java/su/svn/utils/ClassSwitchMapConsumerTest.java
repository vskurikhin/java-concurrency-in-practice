package su.svn.utils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import su.svn.identification.Ident;
import su.svn.identification.Identification;
import su.svn.identification.IdentificationLong;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class ClassSwitchMapConsumerTest {

    AtomicReference<String> test = new AtomicReference<>();

    ClassMap<Consumer<Object>> switchMap = new ClassSwitchMapConsumer() {{
        put(long.class, o -> {
            long id = (long) o;
            test.set("long " + id);
        });
        put(Long.class, o -> test.set("Long " + o));
        put(String.class, o -> test.set("String " + o));
    }};

    void switchCase(Ident record) {
        if (record instanceof IdentificationLong) {
            IdentificationLong r = (IdentificationLong) record;
            switchMap.get(r.idClass()).ifPresent(c -> c.accept(r.id()));
        }
        if (record instanceof Identification) {
            Identification<?> r = (Identification<?>) record;
            switchMap.get(r.idClass()).ifPresent(c -> c.accept(r.id()));
        }
    }

    Ident test1;

    Ident test2;

    Ident test3;

    @Before
    public void setUp() throws Exception {
        test1 = TestIdentLong.builder().id(1).build();
        test2 = TestIdentBoxingLong.builder().id(2L).build();
        test3 = TestIdentString.builder().id("3").build();
    }

    @Test
    public void test() {
        switchCase(test1);
        Assert.assertEquals("long 1", test.get());
        switchCase(test2);
        Assert.assertEquals("Long 2", test.get());
        switchCase(test3);
        Assert.assertEquals("String 3", test.get());
    }
}
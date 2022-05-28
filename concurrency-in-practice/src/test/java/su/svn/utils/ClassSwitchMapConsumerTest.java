package su.svn.utils;

import lombok.Builder;
import lombok.Value;
import org.junit.Before;
import org.junit.Test;
import su.svn.identification.Ident;
import su.svn.identification.Identification;
import su.svn.identification.IdentificationLong;

import java.util.function.Consumer;

public class ClassSwitchMapConsumerTest {

    ClassMap<Consumer<Object>> switchMap = new ClassSwitchMapConsumer() {{
        put(long.class, o -> {
            long id = (long) o;
            System.out.println("long id = " + id);
        });
        put(Long.class, o -> {
            System.out.println("Long id = " + o);
        });
        put(String.class, o -> {
            System.out.println("String id = " + o);
        });
    }};

    void switchCase(Ident record) {
        if (record instanceof IdentificationLong r) {
            switchMap.get(r.idClass()).ifPresent(c -> c.accept(r.id()));
        }
        if (record instanceof Identification r) {
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
        switchCase(test2);
        switchCase(test3);
    }

    @Value
    @Builder(toBuilder = true)
    static class TestIdentLong implements IdentificationLong {

        long id;

        public long id() {
            return id;
        }
    }

    @Value
    @Builder(toBuilder = true)
    static class TestIdentBoxingLong implements Identification<Long> {

        Long id;

        public Long id() {
            return id;
        }

        @Override
        public Class<?> idClass() {
            return Long.class;
        }
    }

    @Value
    @Builder(toBuilder = true)
    static class TestIdentString implements Identification<String> {

        String id;

        public String id() {
            return id;
        }

        @Override
        public Class<?> idClass() {
            return String.class;
        }
    }
}
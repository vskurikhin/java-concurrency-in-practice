package su.svn.components;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import su.svn.aspects.Loggable;

import javax.annotation.Resource;

@Primary
@Component("testComp2")
public class TestComp2Impl {

    @Resource(name = "testComp2")
    private TestComp2Impl self;

    @Loggable(type = "ok test1")
    public String test1() {
        return "test1 ";
    }

    @Loggable
    public String test2() {
        return "test2 ";
    }

    @Loggable
    public String test3() {
        return "test3 " + this.test1();
    }

    @Loggable
    public String test4() {
        return "test4 " + self.test2();
    }
}

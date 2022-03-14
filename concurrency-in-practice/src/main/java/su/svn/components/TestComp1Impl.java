package su.svn.components;

import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component("testComp1")
public class TestComp1Impl implements TestComp1 {

    @Resource(name = "testComp1")
    private TestComp1 self;

    public String test1() {
        return "test1 ";
    }

    public String test2() {
        return "test2 ";
    }

    public String test3() {
        return "test3 " + this.test1();
    }

    public String test4() {
        return "test4 " + self.test2();
    }
}

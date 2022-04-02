package su.svn.components;

import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Component;
import su.svn.aspects.Loggable;

@Component
public class TestComp2Impl1 {

    @Loggable(type = "ok test1")
    public String test1() {
        try {
            throw new ExecutionControl.NotImplementedException("The method in the bean not implemented!");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Loggable
    public String test2() {
        try {
            throw new ExecutionControl.NotImplementedException("The method in the bean not implemented!");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Loggable
    public String test3() {
        try {
            throw new ExecutionControl.NotImplementedException("The method in the bean not implemented!");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    @Loggable
    public String test4() {
        try {
            throw new ExecutionControl.NotImplementedException("The method in the bean not implemented!");
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}

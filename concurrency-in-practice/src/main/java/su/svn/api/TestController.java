package su.svn.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import su.svn.components.TestComp1;
import su.svn.components.TestComp2;
import su.svn.components.TestComp2Impl;

import java.util.Map;

@RestController("testApi")
@RequestMapping("api")
public class TestController {

    private final TestComp1 testComp1;

    private final TestComp2Impl testComp2;

    public TestController(TestComp1 testComp1, TestComp2Impl testComp2) {
        this.testComp1 = testComp1;
        this.testComp2 = testComp2;
    }

    @GetMapping(value = "test", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> test() {
        return Map.of(
                "testComp1 test1", testComp1.test1(),
                "testComp1 test2", testComp1.test2(),
                "testComp1 test3", testComp1.test3(),
                "testComp1 test4", testComp1.test4(),
                "testComp2 test1", testComp2.test1(),
                "testComp2 test2", testComp2.test2(),
                "testComp2 test3", testComp2.test3(),
                "testComp2 test4", testComp2.test4()
        );
    }
}

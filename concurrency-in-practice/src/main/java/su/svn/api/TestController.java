package su.svn.api;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("testApi")
@RequestMapping("api")
public class TestController {

    @GetMapping(value = "test", produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> test() {
        return Map.of("test","test");
    }
}

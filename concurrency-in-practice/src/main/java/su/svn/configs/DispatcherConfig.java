package su.svn.configs;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@ComponentScan(
        basePackages = "su.svn.api",
        useDefaultFilters = false,
        includeFilters = { @ComponentScan.Filter(RestController.class) })
public class DispatcherConfig {
}

package su.svn.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
public class SystemPropertiesConfig {

    @Value("#{systemProperties}")
    private Map<String, String> systemPropertiesMap;

    @Bean
    public Map<String, String> sysProps() {
        return systemPropertiesMap;
    }
}

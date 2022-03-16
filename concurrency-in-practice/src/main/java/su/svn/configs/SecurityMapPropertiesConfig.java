package su.svn.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import java.util.List;
import java.util.Map;

@Configuration
@PropertySource("classpath:security.properties")
public class SecurityMapPropertiesConfig {

    @Value("#{${roles}}")
    private Map<String, List<String>> rolesProperty;

    @Bean
    public Map<String, List<String>> roles() {
        return rolesProperty;
    }

    @Value("#{${users}}")
    private Map<String, String> usersProperty;

    @Bean
    public Map<String, String> users() {
        return usersProperty;
    }
}

package pt.psoft.g1.psoftg1.shared.services;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Setter
@Getter
@Component
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private AgeThreshold age;

    // Nested static class for age thresholds
    @Setter
    @Getter
    public static class AgeThreshold {
        // Getters and setters
        private int min; // Minimum age
        private int max; // Maximum age

    }

}
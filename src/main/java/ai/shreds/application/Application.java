package ai.shreds.application;

import ai.shreds.application.config.ApplicationConfig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * Main Spring Boot application class for the Real-time Data Aggregation service.
 * This class serves as the entry point for the application.
 */
@SpringBootApplication(
    scanBasePackages = {
        "ai.shreds.application",
        "ai.shreds.adapter",
        "ai.shreds.infrastructure",
        "ai.shreds.domain"
    }
)
@Import(ApplicationConfig.class)
public class Application {

    /**
     * Main method to start the Spring Boot application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
package ai.shreds.infrastructure.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.prometheus.PrometheusMeterRegistry;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InfrastructureConfigMonitoring {

    @Bean
    public MeterRegistry meterRegistry() {
        return new PrometheusMeterRegistry(io.micrometer.prometheus.PrometheusConfig.DEFAULT);
    }
}

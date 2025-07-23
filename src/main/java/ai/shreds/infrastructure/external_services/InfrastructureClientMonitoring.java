package ai.shreds.infrastructure.external_services;

import ai.shreds.application.ports.ApplicationOutputPortMonitoring;
import ai.shreds.infrastructure.exceptions.InfrastructureExceptionExternalServiceException;
import ai.shreds.shared.dtos.SharedMonitoringEventDTO;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicDouble;

@Service
public class InfrastructureClientMonitoring implements ApplicationOutputPortMonitoring {

    private static final Logger logger = LoggerFactory.getLogger(InfrastructureClientMonitoring.class);
    private static final Logger errorLogger = LoggerFactory.getLogger("ERROR_LOGGER");
    private static final Logger metricsLogger = LoggerFactory.getLogger("METRICS_LOGGER");

    private final MeterRegistry meterRegistry;
    private final Map<String, Counter> counters = new ConcurrentHashMap<>();
    private final Map<String, Timer> timers = new ConcurrentHashMap<>();
    private final Map<String, AtomicDouble> gaugeValues = new ConcurrentHashMap<>();

    @Autowired
    public InfrastructureClientMonitoring(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }

    @Override
    public void recordMetric(SharedMonitoringEventDTO event) {
        try {
            logger.debug("Recording metric event: {}", event.getEventType());
            
            String metricName = sanitizeMetricName(event.getMetricName());
            String eventType = event.getEventType();
            Double metricValue = event.getMetricValue();
            Map<String, String> tags = event.getTags() != null ? event.getTags() : new HashMap<>();
            
            // Add common tags
            tags.put("event_type", eventType);
            tags.put("timestamp", event.getTimestamp());
            
            // Log the metric
            metricsLogger.info("Metric recorded - Name: {}, Value: {}, Type: {}, Tags: {}", 
                metricName, metricValue, eventType, tags);
            
            // Record different types of metrics based on event type
            switch (eventType.toLowerCase()) {
                case "counter":
                case "count":
                    recordCounter(metricName, metricValue, tags);
                    break;
                    
                case "gauge":
                    recordGauge(metricName, metricValue, tags);
                    break;
                    
                case "timer":
                case "duration":
                    recordTimer(metricName, metricValue, tags);
                    break;
                    
                case "histogram":
                case "summary":
                    recordHistogram(metricName, metricValue, tags);
                    break;
                    
                default:
                    // Default to gauge for unknown types
                    recordGauge(metricName, metricValue, tags);
                    logger.warn("Unknown metric event type: {}, defaulting to gauge", eventType);
            }
            
        } catch (Exception e) {
            logger.error("Failed to record metric: {} - {}", event.getMetricName(), e.getMessage());
            throw new InfrastructureExceptionExternalServiceException(
                "Monitoring service error while recording metric: " + e.getMessage(),
                "MONITORING_SERVICE",
                500
            );
        }
    }

    @Override
    public void recordError(String error, Map<String, Object> context) {
        try {
            // Set MDC context for structured logging
            if (context != null) {
                context.forEach((key, value) -> 
                    MDC.put(key, value != null ? value.toString() : "null"));
            }
            
            // Add timestamp to context
            MDC.put("error_timestamp", OffsetDateTime.now().toString());
            MDC.put("service", "real-time-data-aggregation");
            
            // Log the error
            errorLogger.error("Application error recorded: {}", error);
            
            // Record error metrics
            recordCounter("application.errors.total", 1.0, 
                Map.of("error_type", extractErrorType(error)));
            
            // Clear MDC after logging
            MDC.clear();
            
        } catch (Exception e) {
            logger.error("Failed to record error: {} - {}", error, e.getMessage());
            // Don't throw exception here to avoid cascading errors
        }
    }

    private void recordCounter(String metricName, Double value, Map<String, String> tags) {
        String counterKey = buildMetricKey(metricName, tags);
        Counter counter = counters.computeIfAbsent(counterKey, key -> 
            Counter.builder(metricName)
                .tags(tags)
                .description("Counter metric: " + metricName)
                .register(meterRegistry));
        
        counter.increment(value != null ? value : 1.0);
    }

    private void recordGauge(String metricName, Double value, Map<String, String> tags) {
        String gaugeKey = buildMetricKey(metricName, tags);
        AtomicDouble gaugeValue = gaugeValues.computeIfAbsent(gaugeKey, key -> {
            AtomicDouble atomicValue = new AtomicDouble(value != null ? value : 0.0);
            Gauge.builder(metricName)
                .tags(tags)
                .description("Gauge metric: " + metricName)
                .register(meterRegistry, atomicValue, AtomicDouble::get);
            return atomicValue;
        });
        
        if (value != null) {
            gaugeValue.set(value);
        }
    }

    private void recordTimer(String metricName, Double value, Map<String, String> tags) {
        String timerKey = buildMetricKey(metricName, tags);
        Timer timer = timers.computeIfAbsent(timerKey, key -> 
            Timer.builder(metricName)
                .tags(tags)
                .description("Timer metric: " + metricName)
                .register(meterRegistry));
        
        if (value != null) {
            timer.record(java.time.Duration.ofMillis(value.longValue()));
        }
    }

    private void recordHistogram(String metricName, Double value, Map<String, String> tags) {
        // For histogram, we use Timer.Sample which provides histogram functionality
        Timer timer = Timer.builder(metricName + ".histogram")
            .tags(tags)
            .description("Histogram metric: " + metricName)
            .register(meterRegistry);
        
        if (value != null) {
            timer.record(java.time.Duration.ofMillis(value.longValue()));
        }
    }

    private String sanitizeMetricName(String metricName) {
        if (metricName == null || metricName.trim().isEmpty()) {
            return "unknown.metric";
        }
        // Replace special characters with dots and lowercase
        return metricName.toLowerCase()
            .replaceAll("[^a-zA-Z0-9.]", ".");
    }

    private String buildMetricKey(String metricName, Map<String, String> tags) {
        StringBuilder keyBuilder = new StringBuilder(metricName);
        if (tags != null && !tags.isEmpty()) {
            tags.entrySet().stream()
                .sorted(Map.Entry.comparingByKey())
                .forEach(entry -> keyBuilder.append("|").append(entry.getKey())
                    .append("=").append(entry.getValue()));
        }
        return keyBuilder.toString();
    }

    private String extractErrorType(String error) {
        if (error == null) {
            return "unknown";
        }
        
        // Try to extract error type from error message
        if (error.contains("Exception")) {
            int index = error.indexOf("Exception");
            int start = Math.max(0, error.lastIndexOf(" ", index) + 1);
            return error.substring(start, index + 9).toLowerCase();
        }
        
        if (error.contains("Error")) {
            int index = error.indexOf("Error");
            int start = Math.max(0, error.lastIndexOf(" ", index) + 1);
            return error.substring(start, index + 5).toLowerCase();
        }
        
        return "general_error";
    }

    /**
     * Health check method to verify the metrics registry is working
     */
    public boolean isHealthy() {
        try {
            // Test if we can register a simple counter
            Counter testCounter = Counter.builder("health.check.test")
                .description("Health check test counter")
                .register(meterRegistry);
            testCounter.increment();
            return true;
        } catch (Exception e) {
            logger.error("Monitoring health check failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Get current metrics count
     */
    public int getMetricsCount() {
        return meterRegistry.getMeters().size();
    }
}
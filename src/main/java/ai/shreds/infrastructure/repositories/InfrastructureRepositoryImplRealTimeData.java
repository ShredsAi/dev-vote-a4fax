package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.ports.DomainOutputPortRealTimeDataRepository;
import ai.shreds.infrastructure.exceptions.InfrastructureExceptionExternalServiceException;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

@Repository
public class InfrastructureRepositoryImplRealTimeData implements DomainOutputPortRealTimeDataRepository {

    private final Map<String, Sinks.Many<Map<String, Object>>> metricSinks;
    private final Map<String, Flux<Map<String, Object>>> metricFluxes;

    public InfrastructureRepositoryImplRealTimeData() {
        this.metricSinks = new ConcurrentHashMap<>();
        this.metricFluxes = new ConcurrentHashMap<>();
    }

    @Override
    public Flux<Map<String, Object>> streamData(String metricName) {
        try {
            return metricFluxes.computeIfAbsent(metricName, metric -> {
                Sinks.Many<Map<String, Object>> sink = createSinkForMetric(metric);
                return sink.asFlux()
                    .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false, true)
                    .share(); // Share the flux among multiple subscribers
            });
        } catch (Exception e) {
            throw new InfrastructureExceptionExternalServiceException(
                "Failed to stream data for metric: " + metricName + ". Error: " + e.getMessage(),
                "REAL_TIME_STREAM",
                500
            );
        }
    }

    @Override
    public void publishData(String metricName, Map<String, Object> data) {
        try {
            Sinks.Many<Map<String, Object>> sink = metricSinks.computeIfAbsent(metricName, this::createSinkForMetric);
            
            // Try to emit the data to the sink
            Sinks.EmitResult result = sink.tryEmitNext(data);
            
            if (result.isFailure()) {
                // Handle different failure scenarios
                switch (result) {
                    case FAIL_OVERFLOW:
                        throw new InfrastructureExceptionExternalServiceException(
                            "Buffer overflow when publishing data for metric: " + metricName,
                            "REAL_TIME_BUFFER_OVERFLOW",
                            503
                        );
                    case FAIL_CANCELLED:
                        throw new InfrastructureExceptionExternalServiceException(
                            "Stream cancelled when publishing data for metric: " + metricName,
                            "REAL_TIME_STREAM_CANCELLED",
                            410
                        );
                    case FAIL_TERMINATED:
                        // Recreate the sink if it's terminated
                        recreateSinkForMetric(metricName);
                        break;
                    default:
                        throw new InfrastructureExceptionExternalServiceException(
                            "Failed to emit data for metric: " + metricName + ". Reason: " + result,
                            "REAL_TIME_EMIT_FAILURE",
                            500
                        );
                }
            }
        } catch (InfrastructureExceptionExternalServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new InfrastructureExceptionExternalServiceException(
                "Unexpected error while publishing data for metric: " + metricName + ". Error: " + e.getMessage(),
                "REAL_TIME_PUBLISH_ERROR",
                500
            );
        }
    }

    @Override
    public void subscribeToMetric(String metricName, Consumer<Map<String, Object>> callback) {
        try {
            streamData(metricName)
                .subscribe(
                    callback::accept,
                    error -> {
                        throw new InfrastructureExceptionExternalServiceException(
                            "Error in subscription for metric: " + metricName + ". Error: " + error.getMessage(),
                            "REAL_TIME_SUBSCRIPTION_ERROR",
                            500
                        );
                    }
                );
        } catch (Exception e) {
            throw new InfrastructureExceptionExternalServiceException(
                "Failed to subscribe to metric: " + metricName + ". Error: " + e.getMessage(),
                "REAL_TIME_SUBSCRIBE_ERROR",
                500
            );
        }
    }

    private Sinks.Many<Map<String, Object>> createSinkForMetric(String metricName) {
        // Create a multicast sink that can handle multiple subscribers
        // with buffer to handle backpressure
        Sinks.Many<Map<String, Object>> sink = Sinks.many()
            .multicast()
            .onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
            
        metricSinks.put(metricName, sink);
        return sink;
    }

    private void recreateSinkForMetric(String metricName) {
        // Remove the old sink and flux
        metricSinks.remove(metricName);
        metricFluxes.remove(metricName);
        
        // Create new sink for the metric
        createSinkForMetric(metricName);
    }

    /**
     * Utility method to check if a metric stream exists
     */
    public boolean hasActiveStream(String metricName) {
        return metricSinks.containsKey(metricName) && !metricSinks.get(metricName).currentSubscriberCount() == 0;
    }

    /**
     * Utility method to get the number of subscribers for a metric
     */
    public int getSubscriberCount(String metricName) {
        Sinks.Many<Map<String, Object>> sink = metricSinks.get(metricName);
        return sink != null ? sink.currentSubscriberCount() : 0;
    }

    /**
     * Cleanup method to terminate all sinks
     */
    public void terminateAllStreams() {
        metricSinks.values().forEach(Sinks.Many::tryEmitComplete);
        metricSinks.clear();
        metricFluxes.clear();
    }
}
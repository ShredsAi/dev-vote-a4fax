package ai.shreds.domain.services;

import ai.shreds.domain.exceptions.DomainExceptionDataValidationException;
import ai.shreds.domain.ports.DomainOutputPortRealTimeDataRepository;
import ai.shreds.domain.value_objects.DomainValueVotingMetric;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public class DomainServiceRealTimeProcessing {
    private final DomainOutputPortRealTimeDataRepository realTimeDataRepository;
    private final DomainServiceDataAggregation dataAggregationService;
    private final DomainServiceMetricCalculation metricCalculationService;

    public DomainServiceRealTimeProcessing(DomainOutputPortRealTimeDataRepository realTimeDataRepository,
                                          DomainServiceDataAggregation dataAggregationService,
                                          DomainServiceMetricCalculation metricCalculationService) {
        this.realTimeDataRepository = realTimeDataRepository;
        this.dataAggregationService = dataAggregationService;
        this.metricCalculationService = metricCalculationService;
    }

    public void processRealTimeData(Map<String, Object> data) throws DomainExceptionDataValidationException {
        validateRealTimeData(data);
        
        // Process the data through aggregation service
        dataAggregationService.validateIncomingData(data);
        
        // Extract relevant information for metric calculation
        String metricType = determineMetricType(data);
        Double metricValue = extractMetricValue(data);
        
        if (metricValue != null) {
            try {
                metricCalculationService.updateMetric(metricType, metricValue);
                
                // Publish processed data to real-time stream
                realTimeDataRepository.publishData(metricType, data);
            } catch (DomainExceptionDataValidationException e) {
                // Log error but continue processing
                System.err.println("Error updating metric: " + e.getMessage());
            }
        }
    }

    public Flux<DomainValueVotingMetric> streamMetricData(String metricName) {
        return realTimeDataRepository.streamData(metricName)
                .map(this::convertToVotingMetric)
                .onErrorContinue((error, obj) -> {
                    System.err.println("Error streaming metric data: " + error.getMessage());
                });
    }

    public void validateRealTimeData(Map<String, Object> data) throws DomainExceptionDataValidationException {
        if (data == null || data.isEmpty()) {
            throw new DomainExceptionDataValidationException(
                "Real-time data cannot be null or empty", List.of("data")
            );
        }
        
        // Validate timestamp
        if (!data.containsKey("timestamp") || data.get("timestamp") == null) {
            throw new DomainExceptionDataValidationException(
                "Timestamp is required for real-time data", List.of("timestamp")
            );
        }
        
        // Validate that at least one metric field is present
        boolean hasMetricData = data.containsKey("votes") || 
                               data.containsKey("candidateId") || 
                               data.containsKey("turnoutRate") ||
                               data.containsKey("metricValue");
        
        if (!hasMetricData) {
            throw new DomainExceptionDataValidationException(
                "Real-time data must contain at least one metric field", List.of("metricData")
            );
        }
    }
    
    private String determineMetricType(Map<String, Object> data) {
        if (data.containsKey("candidateId")) {
            return "votes_per_candidate";
        } else if (data.containsKey("pollingStationId")) {
            return "station_activity";
        } else if (data.containsKey("turnoutRate")) {
            return "turnout_rate";
        } else {
            return "general_metric";
        }
    }
    
    private Double extractMetricValue(Map<String, Object> data) {
        Object value = data.get("votes");
        if (value == null) {
            value = data.get("metricValue");
        }
        if (value == null) {
            value = data.get("turnoutRate");
        }
        
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        
        return null;
    }
    
    private DomainValueVotingMetric convertToVotingMetric(Map<String, Object> data) {
        try {
            String name = (String) data.getOrDefault("metricName", "unknown");
            Double value = extractMetricValue(data);
            String unit = (String) data.getOrDefault("unit", "count");
            
            return new DomainValueVotingMetric(name, value != null ? value : 0.0, unit);
        } catch (Exception e) {
            // Return default metric on conversion error
            try {
                return new DomainValueVotingMetric("error", 0.0, "count");
            } catch (DomainExceptionDataValidationException ex) {
                throw new RuntimeException("Failed to create error metric", ex);
            }
        }
    }
}
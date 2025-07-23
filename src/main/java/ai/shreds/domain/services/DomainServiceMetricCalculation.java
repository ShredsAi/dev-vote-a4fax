package ai.shreds.domain.services;

import ai.shreds.domain.entities.DomainEntityAggregatedMetrics;
import ai.shreds.domain.exceptions.DomainExceptionDataValidationException;
import ai.shreds.domain.ports.DomainOutputPortAggregatedMetricsRepository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class DomainServiceMetricCalculation {
    private final DomainOutputPortAggregatedMetricsRepository aggregatedMetricsRepository;

    public DomainServiceMetricCalculation(DomainOutputPortAggregatedMetricsRepository aggregatedMetricsRepository) {
        this.aggregatedMetricsRepository = aggregatedMetricsRepository;
    }

    public DomainEntityAggregatedMetrics calculateMetric(String metricName, List<Double> dataPoints) 
            throws DomainExceptionDataValidationException {
        
        if (dataPoints == null || dataPoints.isEmpty()) {
            throw new DomainExceptionDataValidationException(
                "Data points cannot be null or empty", List.of("dataPoints")
            );
        }
        
        double calculatedValue = performCalculation(metricName, dataPoints);
        
        DomainEntityAggregatedMetrics metric = new DomainEntityAggregatedMetrics(
            UUID.randomUUID(),
            metricName,
            calculatedValue,
            OffsetDateTime.now()
        );
        
        return aggregatedMetricsRepository.save(metric);
    }

    public DomainEntityAggregatedMetrics updateMetric(String metricName, Double newValue) 
            throws DomainExceptionDataValidationException {
        
        Optional<DomainEntityAggregatedMetrics> existingMetric = aggregatedMetricsRepository.findByMetricName(metricName);
        
        if (existingMetric.isPresent()) {
            DomainEntityAggregatedMetrics metric = existingMetric.get();
            metric.updateMetricValue(newValue);
            return aggregatedMetricsRepository.save(metric);
        } else {
            // Create new metric if it doesn't exist
            DomainEntityAggregatedMetrics newMetric = new DomainEntityAggregatedMetrics(
                UUID.randomUUID(),
                metricName,
                newValue,
                OffsetDateTime.now()
            );
            return aggregatedMetricsRepository.save(newMetric);
        }
    }

    public DomainEntityAggregatedMetrics getMetricByName(String metricName) {
        return aggregatedMetricsRepository.findByMetricName(metricName).orElse(null);
    }

    public Double calculateAverageVotesPerStation(Map<UUID, Integer> stationData) {
        if (stationData == null || stationData.isEmpty()) {
            return 0.0;
        }
        
        double total = stationData.values().stream()
                .mapToInt(Integer::intValue)
                .sum();
        
        return total / stationData.size();
    }
    
    private double performCalculation(String metricName, List<Double> dataPoints) {
        switch (metricName.toLowerCase()) {
            case "average":
            case "mean":
                return dataPoints.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
            case "sum":
            case "total":
                return dataPoints.stream().mapToDouble(Double::doubleValue).sum();
            case "max":
            case "maximum":
                return dataPoints.stream().mapToDouble(Double::doubleValue).max().orElse(0.0);
            case "min":
            case "minimum":
                return dataPoints.stream().mapToDouble(Double::doubleValue).min().orElse(0.0);
            case "count":
                return dataPoints.size();
            default:
                // Default to average for unknown metrics
                return dataPoints.stream().mapToDouble(Double::doubleValue).average().orElse(0.0);
        }
    }
}
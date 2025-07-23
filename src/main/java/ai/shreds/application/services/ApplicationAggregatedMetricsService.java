package ai.shreds.application.services;

import ai.shreds.application.ports.ApplicationInputPortGetAggregatedMetrics;
import ai.shreds.application.ports.ApplicationOutputPortMonitoring;
import ai.shreds.domain.services.DomainServiceMetricCalculation;
import ai.shreds.domain.ports.DomainOutputPortAggregatedMetricsRepository;
import ai.shreds.domain.entities.DomainEntityAggregatedMetrics;
import ai.shreds.shared.dtos.SharedAggregatedMetricItemDTO;
import ai.shreds.shared.dtos.SharedMonitoringEventDTO;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ApplicationAggregatedMetricsService implements ApplicationInputPortGetAggregatedMetrics {

    private final DomainServiceMetricCalculation domainServiceMetricCalculation;
    private final DomainOutputPortAggregatedMetricsRepository domainOutputPortAggregatedMetricsRepository;
    private final ApplicationOutputPortMonitoring applicationOutputPortMonitoring;

    @Override
    public List<SharedAggregatedMetricItemDTO> getAggregatedMetrics(
            String metricName,
            String fromTimestamp,
            String toTimestamp
    ) {
        try {
            List<DomainEntityAggregatedMetrics> metrics;
            
            // Apply filtering based on provided parameters
            if (fromTimestamp != null && toTimestamp != null) {
                OffsetDateTime start = OffsetDateTime.parse(fromTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                OffsetDateTime end = OffsetDateTime.parse(toTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
                metrics = domainOutputPortAggregatedMetricsRepository.findByTimestampBetween(start, end);
                
                // Further filter by metric name if provided
                if (metricName != null && !metricName.isEmpty()) {
                    metrics = metrics.stream()
                            .filter(metric -> metric.getMetricName().equals(metricName))
                            .collect(Collectors.toList());
                }
            } else if (metricName != null && !metricName.isEmpty()) {
                Optional<DomainEntityAggregatedMetrics> entityOpt = domainOutputPortAggregatedMetricsRepository.findByMetricName(metricName);
                metrics = entityOpt.map(List::of).orElse(List.of());
                
                // Apply single timestamp filtering if needed
                if (fromTimestamp != null || toTimestamp != null) {
                    metrics = filterByTimeRange(metrics, fromTimestamp, toTimestamp);
                }
            } else {
                metrics = domainOutputPortAggregatedMetricsRepository.findAll();
                
                // Apply single timestamp filtering if needed
                if (fromTimestamp != null || toTimestamp != null) {
                    metrics = filterByTimeRange(metrics, fromTimestamp, toTimestamp);
                }
            }

            List<SharedAggregatedMetricItemDTO> dtos = metrics.stream()
                    .map(DomainEntityAggregatedMetrics::toDTO)
                    .toList();

            applicationOutputPortMonitoring.recordMetric(new SharedMonitoringEventDTO(
                    "aggregated_metrics_retrieved",
                    metricName != null ? metricName : "all",
                    (double) dtos.size(),
                    new HashMap<>() {{
                        put("metricName", metricName);
                        put("fromTimestamp", fromTimestamp);
                        put("toTimestamp", toTimestamp);
                    }},
                    OffsetDateTime.now().toString()
            ));
            
            return dtos;
        } catch (Exception e) {
            applicationOutputPortMonitoring.recordError(
                    "Error retrieving aggregated metrics",
                    Map.of(
                        "metricName", metricName != null ? metricName : "all", 
                        "fromTimestamp", fromTimestamp != null ? fromTimestamp : "none",
                        "toTimestamp", toTimestamp != null ? toTimestamp : "none", 
                        "error", e.getMessage()
                    )
            );
            throw e;
        }
    }
    
    private List<DomainEntityAggregatedMetrics> filterByTimeRange(
            List<DomainEntityAggregatedMetrics> metrics, 
            String fromTimestamp, 
            String toTimestamp) {
        
        if (fromTimestamp == null && toTimestamp == null) {
            return metrics;
        }
        
        OffsetDateTime start = fromTimestamp != null ? 
                OffsetDateTime.parse(fromTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
        OffsetDateTime end = toTimestamp != null ? 
                OffsetDateTime.parse(toTimestamp, DateTimeFormatter.ISO_OFFSET_DATE_TIME) : null;
        
        return metrics.stream()
                .filter(metric -> {
                    OffsetDateTime timestamp = metric.getTimestamp();
                    boolean afterStart = start == null || timestamp.isAfter(start) || timestamp.isEqual(start);
                    boolean beforeEnd = end == null || timestamp.isBefore(end) || timestamp.isEqual(end);
                    return afterStart && beforeEnd;
                })
                .collect(Collectors.toList());
    }
}

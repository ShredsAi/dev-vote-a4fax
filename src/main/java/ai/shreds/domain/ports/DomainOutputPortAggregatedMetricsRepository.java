package ai.shreds.domain.ports;

import ai.shreds.domain.entities.DomainEntityAggregatedMetrics;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface DomainOutputPortAggregatedMetricsRepository {
    DomainEntityAggregatedMetrics save(DomainEntityAggregatedMetrics entity);
    
    Optional<DomainEntityAggregatedMetrics> findByMetricName(String metricName);
    
    List<DomainEntityAggregatedMetrics> findAll();
    
    List<DomainEntityAggregatedMetrics> findByTimestampBetween(OffsetDateTime start, OffsetDateTime end);
}
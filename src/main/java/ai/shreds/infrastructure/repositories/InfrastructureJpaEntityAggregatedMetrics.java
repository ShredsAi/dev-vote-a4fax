package ai.shreds.infrastructure.repositories;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "aggregated_metrics")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfrastructureJpaEntityAggregatedMetrics {

    @Id
    @Column(name = "metric_id", nullable = false)
    private UUID metricId;

    @Column(name = "metric_name", nullable = false)
    private String metricName;

    @Column(name = "metric_value", nullable = false)
    private Double metricValue;

    @Column(name = "timestamp", nullable = false)
    private OffsetDateTime timestamp;
}

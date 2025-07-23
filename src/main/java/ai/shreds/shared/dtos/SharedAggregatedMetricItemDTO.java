package ai.shreds.shared.dtos;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ai.shreds.domain.entities.DomainEntityAggregatedMetrics;

/**
 * DTO representing an aggregated metric item.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedAggregatedMetricItemDTO {
    private UUID metricId;
    private String metricName;
    private Double metricValue;
    private String timestamp;

    /**
     * Convert this DTO to its domain entity representation.
     */
    public DomainEntityAggregatedMetrics toEntity() {
        return DomainEntityAggregatedMetrics.fromDTO(this);
    }

    /**
     * Create a DTO from the given domain entity.
     */
    public static SharedAggregatedMetricItemDTO fromEntity(DomainEntityAggregatedMetrics entity) {
        return entity.toDTO();
    }
}
package ai.shreds.shared.value_objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Request parameters for fetching aggregated metrics.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedAggregatedMetricsRequestParams {
    private String metricName;
    private String fromTimestamp;
    private String toTimestamp;
}
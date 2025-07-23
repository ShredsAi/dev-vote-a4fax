package ai.shreds.application.ports;

import java.util.List;
import ai.shreds.shared.dtos.SharedAggregatedMetricItemDTO;

/**
 * Input port for retrieving aggregated metrics.
 */
public interface ApplicationInputPortGetAggregatedMetrics {

    /**
     * Retrieves aggregated metrics, optionally filtered by metric name and time range.
     *
     * @param metricName    Name of the metric (optional).
     * @param fromTimestamp Start of the time range (ISO8601 string, optional).
     * @param toTimestamp   End of the time range (ISO8601 string, optional).
     * @return List of SharedAggregatedMetricItemDTO matching the criteria.
     */
    List<SharedAggregatedMetricItemDTO> getAggregatedMetrics(
            String metricName,
            String fromTimestamp,
            String toTimestamp
    );
}

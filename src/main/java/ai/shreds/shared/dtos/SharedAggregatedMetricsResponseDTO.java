package ai.shreds.shared.dtos;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Response DTO containing list of aggregated metric items.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedAggregatedMetricsResponseDTO extends SharedApiResponseDTO<List<SharedAggregatedMetricItemDTO>> {
}
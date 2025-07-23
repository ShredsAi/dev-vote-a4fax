package ai.shreds.shared.dtos;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Response DTO containing list of statistical report items.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedStatisticalReportsResponseDTO extends SharedApiResponseDTO<List<SharedStatisticalReportItemDTO>> {
}
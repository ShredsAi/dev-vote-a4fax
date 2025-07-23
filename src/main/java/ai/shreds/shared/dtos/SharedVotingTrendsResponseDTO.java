package ai.shreds.shared.dtos;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Response DTO containing list of voting trend items.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedVotingTrendsResponseDTO extends SharedApiResponseDTO<List<SharedVotingTrendItemDTO>> {
}
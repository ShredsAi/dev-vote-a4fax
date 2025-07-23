package ai.shreds.shared.dtos;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Response DTO containing list of total votes per candidate.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedTotalVotesResponseDTO extends SharedApiResponseDTO<List<SharedTotalVotesItemDTO>> {
}
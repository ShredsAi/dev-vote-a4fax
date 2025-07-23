package ai.shreds.shared.dtos;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Response DTO containing list of turnout rate items.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedTurnoutRatesResponseDTO extends SharedApiResponseDTO<List<SharedTurnoutRateItemDTO>> {
}
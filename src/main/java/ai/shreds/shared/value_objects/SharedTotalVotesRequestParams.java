package ai.shreds.shared.value_objects;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Request parameters for fetching total votes per candidate.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedTotalVotesRequestParams {
    private UUID candidateId;
    private UUID pollingStationId;
    private String fromTimestamp;
    private String toTimestamp;
}
package ai.shreds.shared.value_objects;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Request parameters for fetching turnout rates.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedTurnoutRatesRequestParams {
    private UUID stationId;
    private String fromTimestamp;
    private String toTimestamp;
}
package ai.shreds.shared.value_objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Request parameters for fetching voting trends.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedVotingTrendsRequestParams {
    private String metric;
    private String timeInterval;
    private String fromTimestamp;
    private String toTimestamp;
}
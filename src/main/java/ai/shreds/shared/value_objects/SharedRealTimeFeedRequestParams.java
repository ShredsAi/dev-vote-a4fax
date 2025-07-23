package ai.shreds.shared.value_objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Request parameters for streaming real-time data feed.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedRealTimeFeedRequestParams {
    private String metric;
}
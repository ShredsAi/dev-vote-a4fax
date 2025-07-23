package ai.shreds.shared.value_objects;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Request parameters for fetching statistical reports.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedStatisticalReportsRequestParams {
    private String reportType;
    private String fromTimestamp;
    private String toTimestamp;
}
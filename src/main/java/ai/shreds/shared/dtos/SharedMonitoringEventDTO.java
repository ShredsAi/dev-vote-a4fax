package ai.shreds.shared.dtos;

import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for system monitoring events.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedMonitoringEventDTO {
    private String eventType;
    private String metricName;
    private Double metricValue;
    private Map<String, String> tags;
    private String timestamp;
}
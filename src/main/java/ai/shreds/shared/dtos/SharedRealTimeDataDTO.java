package ai.shreds.shared.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO representing real-time streaming data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedRealTimeDataDTO {
    private String metric;
    private Double value;
    private String timestamp;
}
package ai.shreds.shared.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.UUID;

/**
 * DTO for preprocessed voting data.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedPreprocessedDataDTO {
    private UUID candidateId;
    private String candidateName;
    private UUID pollingStationId;
    private Integer votes;
    private String timestamp;
}
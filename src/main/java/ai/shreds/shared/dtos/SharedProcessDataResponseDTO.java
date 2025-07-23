package ai.shreds.shared.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for process data responses.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedProcessDataResponseDTO {
    private String status;
    private String message;
    private String timestamp;
}
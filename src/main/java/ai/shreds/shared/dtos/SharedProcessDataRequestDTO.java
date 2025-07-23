package ai.shreds.shared.dtos;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for submitting batch data processing requests.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedProcessDataRequestDTO {
    private List<SharedPreprocessedDataDTO> data;
}
package ai.shreds.shared.dtos;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Generic API response wrapper for all endpoints.
 * @param <T> Type of data being returned
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedApiResponseDTO<T> {
    private String status;
    private T data;
    private String timestamp;
}
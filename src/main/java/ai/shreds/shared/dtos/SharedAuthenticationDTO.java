package ai.shreds.shared.dtos;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * DTO for authentication and authorization information.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedAuthenticationDTO {
    private String userId;
    private String token;
    private List<String> permissions;
    private String timestamp;
}
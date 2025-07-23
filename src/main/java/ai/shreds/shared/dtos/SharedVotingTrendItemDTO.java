package ai.shreds.shared.dtos;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ai.shreds.domain.entities.DomainEntityVotingTrendsOverTime;

/**
 * DTO representing voting trend data over a specific time period.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedVotingTrendItemDTO {
    private UUID trendId;
    private String metric;
    private String timeInterval;
    private Double value;
    private String timestamp;
    
    /**
     * Convert this DTO to its domain entity representation.
     */
    public DomainEntityVotingTrendsOverTime toEntity() {
        return DomainEntityVotingTrendsOverTime.fromDTO(this);
    }
    
    /**
     * Create a DTO from the given domain entity.
     */
    public static SharedVotingTrendItemDTO fromEntity(DomainEntityVotingTrendsOverTime entity) {
        return entity.toDTO();
    }
}
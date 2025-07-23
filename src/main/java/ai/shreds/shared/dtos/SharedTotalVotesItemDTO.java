package ai.shreds.shared.dtos;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ai.shreds.domain.entities.DomainEntityTotalVotesPerCandidate;

/**
 * DTO representing total votes for a candidate at a polling station.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedTotalVotesItemDTO {
    private UUID candidateId;
    private String candidateName;
    private Integer totalVotes;
    private UUID pollingStationId;
    private String timestamp;
    
    /**
     * Convert this DTO to its domain entity representation.
     */
    public DomainEntityTotalVotesPerCandidate toEntity() {
        return DomainEntityTotalVotesPerCandidate.fromDTO(this);
    }
    
    /**
     * Create a DTO from the given domain entity.
     */
    public static SharedTotalVotesItemDTO fromEntity(DomainEntityTotalVotesPerCandidate entity) {
        return entity.toDTO();
    }
}
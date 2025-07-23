package ai.shreds.shared.dtos;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ai.shreds.domain.entities.DomainEntityTurnoutRates;

/**
 * DTO representing turnout rate data for a polling station.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedTurnoutRateItemDTO {
    private UUID stationId;
    private Integer registeredVoters;
    private Integer votesCast;
    private Double turnoutPercentage;
    private String timestamp;
    
    /**
     * Convert this DTO to its domain entity representation.
     */
    public DomainEntityTurnoutRates toEntity() {
        return DomainEntityTurnoutRates.fromDTO(this);
    }
    
    /**
     * Create a DTO from the given domain entity.
     */
    public static SharedTurnoutRateItemDTO fromEntity(DomainEntityTurnoutRates entity) {
        return entity.toDTO();
    }
}
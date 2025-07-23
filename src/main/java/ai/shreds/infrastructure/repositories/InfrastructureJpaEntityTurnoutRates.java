package ai.shreds.infrastructure.repositories;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "turnout_rates")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfrastructureJpaEntityTurnoutRates {

    @Id
    @Column(name = "station_id", nullable = false)
    private UUID stationId;

    @Column(name = "registered_voters", nullable = false)
    private Integer registeredVoters;

    @Column(name = "votes_cast", nullable = false)
    private Integer votesCast;

    @Column(name = "turnout_percentage", nullable = false)
    private Double turnoutPercentage;

    @Column(name = "timestamp", nullable = false)
    private OffsetDateTime timestamp;
}
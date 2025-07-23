package ai.shreds.infrastructure.repositories;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "total_votes_per_candidate")
@IdClass(InfrastructureJpaEntityTotalVotesPerCandidateId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfrastructureJpaEntityTotalVotesPerCandidate {

    @Id
    @Column(name = "candidate_id", nullable = false)
    private UUID candidateId;

    @Column(name = "candidate_name", nullable = false)
    private String candidateName;

    @Column(name = "total_votes", nullable = false)
    private Integer totalVotes;

    @Id
    @Column(name = "polling_station_id", nullable = false)
    private UUID pollingStationId;

    @Id
    @Column(name = "timestamp", nullable = false)
    private OffsetDateTime timestamp;
}

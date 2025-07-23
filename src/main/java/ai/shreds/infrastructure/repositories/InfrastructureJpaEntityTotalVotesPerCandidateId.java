package ai.shreds.infrastructure.repositories;

import lombok.*;
import java.io.Serializable;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfrastructureJpaEntityTotalVotesPerCandidateId implements Serializable {

    private UUID candidateId;
    private UUID pollingStationId;
    private OffsetDateTime timestamp;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InfrastructureJpaEntityTotalVotesPerCandidateId that = (InfrastructureJpaEntityTotalVotesPerCandidateId) o;
        return Objects.equals(candidateId, that.candidateId) &&
               Objects.equals(pollingStationId, that.pollingStationId) &&
               Objects.equals(timestamp, that.timestamp);
    }

    @Override
    public int hashCode() {
        return Objects.hash(candidateId, pollingStationId, timestamp);
    }
}
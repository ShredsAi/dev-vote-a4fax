package ai.shreds.domain.ports;

import ai.shreds.domain.entities.DomainEntityTotalVotesPerCandidate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DomainOutputPortTotalVotesPerCandidateRepository {
    DomainEntityTotalVotesPerCandidate save(DomainEntityTotalVotesPerCandidate entity);
    
    List<DomainEntityTotalVotesPerCandidate> findByCandidateId(UUID candidateId);
    
    List<DomainEntityTotalVotesPerCandidate> findByPollingStationId(UUID pollingStationId);
    
    List<DomainEntityTotalVotesPerCandidate> findByTimestampBetween(OffsetDateTime start, OffsetDateTime end);
    
    Optional<DomainEntityTotalVotesPerCandidate> findLatestByCandidateId(UUID candidateId);
}
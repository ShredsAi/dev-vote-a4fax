package ai.shreds.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InfrastructureJpaRepositoryTotalVotesPerCandidate 
        extends JpaRepository<InfrastructureJpaEntityTotalVotesPerCandidate, InfrastructureJpaEntityTotalVotesPerCandidateId> {

    List<InfrastructureJpaEntityTotalVotesPerCandidate> findByCandidateId(UUID candidateId);
    
    List<InfrastructureJpaEntityTotalVotesPerCandidate> findByPollingStationId(UUID pollingStationId);
    
    List<InfrastructureJpaEntityTotalVotesPerCandidate> findByTimestampBetween(OffsetDateTime start, OffsetDateTime end);
    
    @Query("SELECT t FROM InfrastructureJpaEntityTotalVotesPerCandidate t WHERE t.candidateId = :candidateId ORDER BY t.timestamp DESC LIMIT 1")
    Optional<InfrastructureJpaEntityTotalVotesPerCandidate> findTopByCandidateIdOrderByTimestampDesc(@Param("candidateId") UUID candidateId);
}
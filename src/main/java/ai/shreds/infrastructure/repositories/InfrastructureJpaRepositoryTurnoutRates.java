package ai.shreds.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface InfrastructureJpaRepositoryTurnoutRates extends JpaRepository<InfrastructureJpaEntityTurnoutRates, UUID> {

    Optional<InfrastructureJpaEntityTurnoutRates> findByStationId(UUID stationId);
    
    List<InfrastructureJpaEntityTurnoutRates> findByTimestampBetween(OffsetDateTime start, OffsetDateTime end);
}
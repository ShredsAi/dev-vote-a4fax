package ai.shreds.domain.ports;

import ai.shreds.domain.entities.DomainEntityTurnoutRates;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DomainOutputPortTurnoutRatesRepository {
    DomainEntityTurnoutRates save(DomainEntityTurnoutRates entity);
    
    Optional<DomainEntityTurnoutRates> findByStationId(UUID stationId);
    
    List<DomainEntityTurnoutRates> findAll();
    
    List<DomainEntityTurnoutRates> findByTimestampBetween(OffsetDateTime start, OffsetDateTime end);
}
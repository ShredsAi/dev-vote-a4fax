package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.DomainEntityTurnoutRates;
import ai.shreds.domain.ports.DomainOutputPortTurnoutRatesRepository;
import ai.shreds.infrastructure.exceptions.InfrastructureExceptionDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InfrastructureRepositoryImplTurnoutRates implements DomainOutputPortTurnoutRatesRepository {

    private final InfrastructureJpaRepositoryTurnoutRates jpaRepository;
    private final InfrastructureMapperEntityMapper entityMapper;

    @Autowired
    public InfrastructureRepositoryImplTurnoutRates(
            InfrastructureJpaRepositoryTurnoutRates jpaRepository,
            InfrastructureMapperEntityMapper entityMapper) {
        this.jpaRepository = jpaRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public DomainEntityTurnoutRates save(DomainEntityTurnoutRates entity) {
        try {
            InfrastructureJpaEntityTurnoutRates jpaEntity = entityMapper.toJpaEntity(entity);
            InfrastructureJpaEntityTurnoutRates savedJpaEntity = jpaRepository.save(jpaEntity);
            return entityMapper.toDomainEntity(savedJpaEntity);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to save turnout rates entity: " + e.getMessage(),
                "DB_SAVE_ERROR",
                null
            );
        }
    }

    @Override
    public Optional<DomainEntityTurnoutRates> findByStationId(UUID stationId) {
        try {
            Optional<InfrastructureJpaEntityTurnoutRates> jpaEntity = jpaRepository.findByStationId(stationId);
            return jpaEntity.map(entityMapper::toDomainEntity);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find turnout rates by station ID: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }

    @Override
    public List<DomainEntityTurnoutRates> findAll() {
        try {
            List<InfrastructureJpaEntityTurnoutRates> jpaEntities = jpaRepository.findAll();
            return entityMapper.toDomainEntityList(jpaEntities);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find all turnout rates: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }

    @Override
    public List<DomainEntityTurnoutRates> findByTimestampBetween(OffsetDateTime start, OffsetDateTime end) {
        try {
            List<InfrastructureJpaEntityTurnoutRates> jpaEntities = jpaRepository.findByTimestampBetween(start, end);
            return entityMapper.toDomainEntityList(jpaEntities);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find turnout rates by timestamp range: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }
}
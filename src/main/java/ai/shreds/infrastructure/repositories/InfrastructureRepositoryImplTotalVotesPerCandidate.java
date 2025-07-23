package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.DomainEntityTotalVotesPerCandidate;
import ai.shreds.domain.ports.DomainOutputPortTotalVotesPerCandidateRepository;
import ai.shreds.infrastructure.exceptions.InfrastructureExceptionDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InfrastructureRepositoryImplTotalVotesPerCandidate implements DomainOutputPortTotalVotesPerCandidateRepository {

    private final InfrastructureJpaRepositoryTotalVotesPerCandidate jpaRepository;
    private final InfrastructureMapperEntityMapper entityMapper;

    @Autowired
    public InfrastructureRepositoryImplTotalVotesPerCandidate(
            InfrastructureJpaRepositoryTotalVotesPerCandidate jpaRepository,
            InfrastructureMapperEntityMapper entityMapper) {
        this.jpaRepository = jpaRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public DomainEntityTotalVotesPerCandidate save(DomainEntityTotalVotesPerCandidate entity) {
        try {
            InfrastructureJpaEntityTotalVotesPerCandidate jpaEntity = entityMapper.toJpaEntity(entity);
            InfrastructureJpaEntityTotalVotesPerCandidate savedJpaEntity = jpaRepository.save(jpaEntity);
            return entityMapper.toDomainEntity(savedJpaEntity);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to save total votes per candidate entity: " + e.getMessage(),
                "DB_SAVE_ERROR",
                null
            );
        }
    }

    @Override
    public List<DomainEntityTotalVotesPerCandidate> findByCandidateId(UUID candidateId) {
        try {
            List<InfrastructureJpaEntityTotalVotesPerCandidate> jpaEntities = jpaRepository.findByCandidateId(candidateId);
            return entityMapper.toDomainEntityList(jpaEntities);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find total votes by candidate ID: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }

    @Override
    public List<DomainEntityTotalVotesPerCandidate> findByPollingStationId(UUID pollingStationId) {
        try {
            List<InfrastructureJpaEntityTotalVotesPerCandidate> jpaEntities = jpaRepository.findByPollingStationId(pollingStationId);
            return entityMapper.toDomainEntityList(jpaEntities);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find total votes by polling station ID: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }

    @Override
    public List<DomainEntityTotalVotesPerCandidate> findByTimestampBetween(OffsetDateTime start, OffsetDateTime end) {
        try {
            List<InfrastructureJpaEntityTotalVotesPerCandidate> jpaEntities = jpaRepository.findByTimestampBetween(start, end);
            return entityMapper.toDomainEntityList(jpaEntities);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find total votes by timestamp range: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }

    @Override
    public Optional<DomainEntityTotalVotesPerCandidate> findLatestByCandidateId(UUID candidateId) {
        try {
            Optional<InfrastructureJpaEntityTotalVotesPerCandidate> jpaEntity = jpaRepository.findTopByCandidateIdOrderByTimestampDesc(candidateId);
            return jpaEntity.map(entityMapper::toDomainEntity);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find latest total votes by candidate ID: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }
}
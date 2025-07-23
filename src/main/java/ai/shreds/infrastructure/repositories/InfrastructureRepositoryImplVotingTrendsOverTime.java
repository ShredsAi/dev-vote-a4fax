package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.DomainEntityVotingTrendsOverTime;
import ai.shreds.domain.ports.DomainOutputPortVotingTrendsOverTimeRepository;
import ai.shreds.infrastructure.exceptions.InfrastructureExceptionDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class InfrastructureRepositoryImplVotingTrendsOverTime implements DomainOutputPortVotingTrendsOverTimeRepository {

    private final InfrastructureJpaRepositoryVotingTrendsOverTime jpaRepository;
    private final InfrastructureMapperEntityMapper entityMapper;

    @Autowired
    public InfrastructureRepositoryImplVotingTrendsOverTime(
            InfrastructureJpaRepositoryVotingTrendsOverTime jpaRepository,
            InfrastructureMapperEntityMapper entityMapper) {
        this.jpaRepository = jpaRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public DomainEntityVotingTrendsOverTime save(DomainEntityVotingTrendsOverTime entity) {
        try {
            InfrastructureJpaEntityVotingTrendsOverTime jpaEntity = entityMapper.toJpaEntity(entity);
            InfrastructureJpaEntityVotingTrendsOverTime savedJpaEntity = jpaRepository.save(jpaEntity);
            return entityMapper.toDomainEntity(savedJpaEntity);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to save voting trends over time entity: " + e.getMessage(),
                "DB_SAVE_ERROR",
                null
            );
        }
    }

    @Override
    public List<DomainEntityVotingTrendsOverTime> findByMetric(String metric) {
        try {
            List<InfrastructureJpaEntityVotingTrendsOverTime> jpaEntities = jpaRepository.findByMetric(metric);
            return entityMapper.toDomainEntityList(jpaEntities);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find voting trends by metric: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }

    @Override
    public List<DomainEntityVotingTrendsOverTime> findByMetricAndTimeInterval(String metric, String timeInterval) {
        try {
            List<InfrastructureJpaEntityVotingTrendsOverTime> jpaEntities = jpaRepository.findByMetricAndTimeInterval(metric, timeInterval);
            return entityMapper.toDomainEntityList(jpaEntities);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find voting trends by metric and time interval: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }

    @Override
    public List<DomainEntityVotingTrendsOverTime> findByTimestampBetween(OffsetDateTime start, OffsetDateTime end) {
        try {
            List<InfrastructureJpaEntityVotingTrendsOverTime> jpaEntities = jpaRepository.findByTimestampBetween(start, end);
            return entityMapper.toDomainEntityList(jpaEntities);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find voting trends by timestamp range: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }
}
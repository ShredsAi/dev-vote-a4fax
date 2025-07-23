package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.DomainEntityAggregatedMetrics;
import ai.shreds.domain.ports.DomainOutputPortAggregatedMetricsRepository;
import ai.shreds.infrastructure.exceptions.InfrastructureExceptionDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public class InfrastructureRepositoryImplAggregatedMetrics implements DomainOutputPortAggregatedMetricsRepository {

    private final InfrastructureJpaRepositoryAggregatedMetrics jpaRepository;
    private final InfrastructureMapperEntityMapper entityMapper;

    @Autowired
    public InfrastructureRepositoryImplAggregatedMetrics(
            InfrastructureJpaRepositoryAggregatedMetrics jpaRepository,
            InfrastructureMapperEntityMapper entityMapper) {
        this.jpaRepository = jpaRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public DomainEntityAggregatedMetrics save(DomainEntityAggregatedMetrics entity) {
        try {
            InfrastructureJpaEntityAggregatedMetrics jpaEntity = entityMapper.toJpaEntity(entity);
            InfrastructureJpaEntityAggregatedMetrics savedJpaEntity = jpaRepository.save(jpaEntity);
            return entityMapper.toDomainEntity(savedJpaEntity);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to save aggregated metrics entity: " + e.getMessage(),
                "DB_SAVE_ERROR",
                null
            );
        }
    }

    @Override
    public Optional<DomainEntityAggregatedMetrics> findByMetricName(String metricName) {
        try {
            Optional<InfrastructureJpaEntityAggregatedMetrics> jpaEntity = jpaRepository.findByMetricName(metricName);
            return jpaEntity.map(entityMapper::toDomainEntity);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find aggregated metrics by metric name: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }

    @Override
    public List<DomainEntityAggregatedMetrics> findAll() {
        try {
            List<InfrastructureJpaEntityAggregatedMetrics> jpaEntities = jpaRepository.findAll();
            return entityMapper.toDomainEntityList(jpaEntities);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find all aggregated metrics: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }

    @Override
    public List<DomainEntityAggregatedMetrics> findByTimestampBetween(OffsetDateTime start, OffsetDateTime end) {
        try {
            List<InfrastructureJpaEntityAggregatedMetrics> jpaEntities = jpaRepository.findByTimestampBetween(start, end);
            return entityMapper.toDomainEntityList(jpaEntities);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find aggregated metrics by timestamp range: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }
}
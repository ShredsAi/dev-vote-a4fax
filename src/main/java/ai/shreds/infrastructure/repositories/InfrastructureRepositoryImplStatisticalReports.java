package ai.shreds.infrastructure.repositories;

import ai.shreds.domain.entities.DomainEntityStatisticalReports;
import ai.shreds.domain.ports.DomainOutputPortStatisticalReportsRepository;
import ai.shreds.infrastructure.exceptions.InfrastructureExceptionDatabaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class InfrastructureRepositoryImplStatisticalReports implements DomainOutputPortStatisticalReportsRepository {

    private final InfrastructureJpaRepositoryStatisticalReports jpaRepository;
    private final InfrastructureMapperEntityMapper entityMapper;

    @Autowired
    public InfrastructureRepositoryImplStatisticalReports(
            InfrastructureJpaRepositoryStatisticalReports jpaRepository,
            InfrastructureMapperEntityMapper entityMapper) {
        this.jpaRepository = jpaRepository;
        this.entityMapper = entityMapper;
    }

    @Override
    public DomainEntityStatisticalReports save(DomainEntityStatisticalReports entity) {
        try {
            InfrastructureJpaEntityStatisticalReports jpaEntity = entityMapper.toJpaEntity(entity);
            InfrastructureJpaEntityStatisticalReports savedJpaEntity = jpaRepository.save(jpaEntity);
            return entityMapper.toDomainEntity(savedJpaEntity);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to save statistical reports entity: " + e.getMessage(),
                "DB_SAVE_ERROR",
                null
            );
        }
    }

    @Override
    public List<DomainEntityStatisticalReports> findByReportType(String reportType) {
        try {
            List<InfrastructureJpaEntityStatisticalReports> jpaEntities = jpaRepository.findByReportType(reportType);
            return entityMapper.toDomainEntityList(jpaEntities);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find statistical reports by report type: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }

    @Override
    public List<DomainEntityStatisticalReports> findByGeneratedAtBetween(OffsetDateTime start, OffsetDateTime end) {
        try {
            List<InfrastructureJpaEntityStatisticalReports> jpaEntities = jpaRepository.findByGeneratedAtBetween(start, end);
            return entityMapper.toDomainEntityList(jpaEntities);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find statistical reports by generated at range: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }

    @Override
    public Optional<DomainEntityStatisticalReports> findById(UUID reportId) {
        try {
            Optional<InfrastructureJpaEntityStatisticalReports> jpaEntity = jpaRepository.findById(reportId);
            return jpaEntity.map(entityMapper::toDomainEntity);
        } catch (Exception e) {
            throw new InfrastructureExceptionDatabaseException(
                "Failed to find statistical report by ID: " + e.getMessage(),
                "DB_QUERY_ERROR",
                null
            );
        }
    }
}
package ai.shreds.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface InfrastructureJpaRepositoryStatisticalReports extends JpaRepository<InfrastructureJpaEntityStatisticalReports, UUID> {

    List<InfrastructureJpaEntityStatisticalReports> findByReportType(String reportType);
    
    List<InfrastructureJpaEntityStatisticalReports> findByGeneratedAtBetween(OffsetDateTime start, OffsetDateTime end);
}
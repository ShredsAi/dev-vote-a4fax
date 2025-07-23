package ai.shreds.domain.ports;

import ai.shreds.domain.entities.DomainEntityStatisticalReports;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface DomainOutputPortStatisticalReportsRepository {
    DomainEntityStatisticalReports save(DomainEntityStatisticalReports entity);
    
    List<DomainEntityStatisticalReports> findByReportType(String reportType);
    
    List<DomainEntityStatisticalReports> findByGeneratedAtBetween(OffsetDateTime start, OffsetDateTime end);
    
    Optional<DomainEntityStatisticalReports> findById(UUID reportId);
    
    List<DomainEntityStatisticalReports> findAll();
}
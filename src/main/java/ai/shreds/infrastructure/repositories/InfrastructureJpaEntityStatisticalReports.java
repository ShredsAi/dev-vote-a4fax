package ai.shreds.infrastructure.repositories;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "statistical_reports")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InfrastructureJpaEntityStatisticalReports {

    @Id
    @Column(name = "report_id", nullable = false)
    private UUID reportId;

    @Column(name = "report_type", nullable = false)
    private String reportType;

    @Column(name = "generated_at", nullable = false)
    private OffsetDateTime generatedAt;

    @Column(name = "data_summary", nullable = false, columnDefinition = "TEXT")
    private String dataSummary;

    @Column(name = "timestamp", nullable = false)
    private OffsetDateTime timestamp;
}

package ai.shreds.shared.dtos;

import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import ai.shreds.domain.entities.DomainEntityStatisticalReports;

/**
 * DTO representing a statistical report item.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SharedStatisticalReportItemDTO {
    private UUID reportId;
    private String reportType;
    private String generatedAt;
    private String dataSummary;
    private String timestamp;

    /**
     * Convert this DTO to its domain entity representation.
     */
    public DomainEntityStatisticalReports toEntity() {
        return DomainEntityStatisticalReports.fromDTO(this);
    }

    /**
     * Create a DTO from the given domain entity.
     */
    public static SharedStatisticalReportItemDTO fromEntity(DomainEntityStatisticalReports entity) {
        return entity.toDTO();
    }
}
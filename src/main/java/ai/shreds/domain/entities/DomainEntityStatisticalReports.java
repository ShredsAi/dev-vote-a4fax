package ai.shreds.domain.entities;

import ai.shreds.domain.exceptions.DomainExceptionDataValidationException;
import ai.shreds.shared.dtos.SharedStatisticalReportItemDTO;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class DomainEntityStatisticalReports {

    private final UUID reportId;
    private final String reportType;
    private final OffsetDateTime generatedAt;
    private final String dataSummary;
    private final OffsetDateTime timestamp;

    public DomainEntityStatisticalReports(UUID reportId,
                                          String reportType,
                                          OffsetDateTime generatedAt,
                                          String dataSummary,
                                          OffsetDateTime timestamp) throws DomainExceptionDataValidationException {
        this.reportId = reportId;
        this.reportType = reportType;
        this.generatedAt = generatedAt;
        this.dataSummary = dataSummary;
        this.timestamp = timestamp;
        validate();
    }

    public void validate() throws DomainExceptionDataValidationException {
        if (reportId == null) {
            throw new DomainExceptionDataValidationException(
                "Report ID cannot be null", List.of("reportId")
            );
        }
        if (reportType == null || reportType.isBlank()) {
            throw new DomainExceptionDataValidationException(
                "Report type cannot be blank", List.of("reportType")
            );
        }
        if (generatedAt == null) {
            throw new DomainExceptionDataValidationException(
                "Generated at timestamp cannot be null", List.of("generatedAt")
            );
        }
        if (dataSummary == null || dataSummary.isBlank()) {
            throw new DomainExceptionDataValidationException(
                "Data summary cannot be blank", List.of("dataSummary")
            );
        }
        if (timestamp == null) {
            throw new DomainExceptionDataValidationException(
                "Timestamp cannot be null", List.of("timestamp")
            );
        }
    }

    public SharedStatisticalReportItemDTO toDTO() {
        return new SharedStatisticalReportItemDTO(
                this.reportId,
                this.reportType,
                this.generatedAt.toString(),
                this.dataSummary,
                this.timestamp.toString()
        );
    }

    public static DomainEntityStatisticalReports fromDTO(SharedStatisticalReportItemDTO dto) throws DomainExceptionDataValidationException {
        return new DomainEntityStatisticalReports(
            dto.getReportId(),
            dto.getReportType(),
            OffsetDateTime.parse(dto.getGeneratedAt()),
            dto.getDataSummary(),
            OffsetDateTime.parse(dto.getTimestamp())
        );
    }

    public UUID getReportId() {
        return reportId;
    }

    public String getReportType() {
        return reportType;
    }

    public OffsetDateTime getGeneratedAt() {
        return generatedAt;
    }

    public String getDataSummary() {
        return dataSummary;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}
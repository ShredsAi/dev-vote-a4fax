package ai.shreds.domain.entities;

import ai.shreds.domain.exceptions.DomainExceptionDataValidationException;
import ai.shreds.shared.dtos.SharedAggregatedMetricItemDTO;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class DomainEntityAggregatedMetrics {

    private final UUID metricId;
    private final String metricName;
    private double metricValue;
    private final OffsetDateTime timestamp;

    public DomainEntityAggregatedMetrics(UUID metricId,
                                        String metricName,
                                        double metricValue,
                                        OffsetDateTime timestamp) throws DomainExceptionDataValidationException {
        this.metricId = metricId;
        this.metricName = metricName;
        this.metricValue = metricValue;
        this.timestamp = timestamp;
        validate();
    }

    public void validate() throws DomainExceptionDataValidationException {
        if (metricId == null) {
            throw new DomainExceptionDataValidationException(
                "Metric ID cannot be null", List.of("metricId")
            );
        }
        if (metricName == null || metricName.isBlank()) {
            throw new DomainExceptionDataValidationException(
                "Metric name cannot be blank", List.of("metricName")
            );
        }
        if (timestamp == null) {
            throw new DomainExceptionDataValidationException(
                "Timestamp cannot be null", List.of("timestamp")
            );
        }
    }

    public void updateMetricValue(double newValue) {
        this.metricValue = newValue;
    }

    public SharedAggregatedMetricItemDTO toDTO() {
        return new SharedAggregatedMetricItemDTO(
                this.metricId,
                this.metricName,
                this.metricValue,
                this.timestamp.toString()
        );
    }

    public static DomainEntityAggregatedMetrics fromDTO(SharedAggregatedMetricItemDTO dto) throws DomainExceptionDataValidationException {
        return new DomainEntityAggregatedMetrics(
            dto.getMetricId(),
            dto.getMetricName(),
            dto.getMetricValue(),
            OffsetDateTime.parse(dto.getTimestamp())
        );
    }

    public UUID getMetricId() {
        return metricId;
    }

    public String getMetricName() {
        return metricName;
    }

    public double getMetricValue() {
        return metricValue;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}
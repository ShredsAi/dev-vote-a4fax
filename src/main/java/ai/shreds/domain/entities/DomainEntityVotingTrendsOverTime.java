package ai.shreds.domain.entities;

import ai.shreds.domain.exceptions.DomainExceptionDataValidationException;
import ai.shreds.domain.value_objects.DomainValueTimeInterval;
import ai.shreds.shared.dtos.SharedVotingTrendItemDTO;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.UUID;

public class DomainEntityVotingTrendsOverTime {

    private final UUID trendId;
    private final String metric;
    private final DomainValueTimeInterval timeInterval;
    private double value;
    private final OffsetDateTime timestamp;

    public DomainEntityVotingTrendsOverTime(UUID trendId,
                                            String metric,
                                            DomainValueTimeInterval timeInterval,
                                            double value,
                                            OffsetDateTime timestamp) throws DomainExceptionDataValidationException {
        this.trendId = trendId;
        this.metric = metric;
        this.timeInterval = timeInterval;
        this.value = value;
        this.timestamp = timestamp;
        validate();
    }

    public void validate() throws DomainExceptionDataValidationException {
        if (trendId == null) {
            throw new DomainExceptionDataValidationException(
                "Trend ID cannot be null", List.of("trendId")
            );
        }
        if (metric == null || metric.isBlank()) {
            throw new DomainExceptionDataValidationException(
                "Metric cannot be blank", List.of("metric")
            );
        }
        if (timeInterval == null) {
            throw new DomainExceptionDataValidationException(
                "Time interval cannot be null", List.of("timeInterval")
            );
        }
        if (value < 0) {
            throw new DomainExceptionDataValidationException(
                "Value cannot be negative", List.of("value")
            );
        }
        if (timestamp == null) {
            throw new DomainExceptionDataValidationException(
                "Timestamp cannot be null", List.of("timestamp")
            );
        }
    }

    public void updateTrendValue(double newValue) throws DomainExceptionDataValidationException {
        if (newValue < 0) {
            throw new DomainExceptionDataValidationException(
                "Value cannot be negative", List.of("value")
            );
        }
        this.value = newValue;
    }

    public SharedVotingTrendItemDTO toDTO() {
        return new SharedVotingTrendItemDTO(
                this.trendId,
                this.metric,
                this.timeInterval.toString(),
                this.value,
                this.timestamp.toString()
        );
    }

    public static DomainEntityVotingTrendsOverTime fromDTO(SharedVotingTrendItemDTO dto) throws DomainExceptionDataValidationException {
        DomainValueTimeInterval interval = new DomainValueTimeInterval(dto.getTimeInterval());
        return new DomainEntityVotingTrendsOverTime(
            dto.getTrendId(),
            dto.getMetric(),
            interval,
            dto.getValue(),
            OffsetDateTime.parse(dto.getTimestamp())
        );
    }

    public UUID getTrendId() {
        return trendId;
    }

    public String getMetric() {
        return metric;
    }

    public DomainValueTimeInterval getTimeInterval() {
        return timeInterval;
    }

    public double getValue() {
        return value;
    }

    public OffsetDateTime getTimestamp() {
        return timestamp;
    }
}
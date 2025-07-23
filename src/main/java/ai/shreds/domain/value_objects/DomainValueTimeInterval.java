package ai.shreds.domain.value_objects;

import ai.shreds.domain.exceptions.DomainExceptionInvalidTimeIntervalException;

import java.time.Duration;

public class DomainValueTimeInterval {
    private final Duration duration;
    private final String unit;

    public DomainValueTimeInterval(String interval) throws DomainExceptionInvalidTimeIntervalException {
        try {
            this.duration = Duration.parse(interval);
        } catch (Exception e) {
            throw new DomainExceptionInvalidTimeIntervalException("Invalid time interval format: " + interval, interval);
        }
        this.unit = duration.toString();
        validate();
    }

    private void validate() throws DomainExceptionInvalidTimeIntervalException {
        if (duration.isZero() || duration.isNegative()) {
            throw new DomainExceptionInvalidTimeIntervalException(
                "Time interval must be positive: " + duration, duration.toString()
            );
        }
    }

    public Duration toDuration() {
        return duration;
    }

    @Override
    public String toString() {
        return unit;
    }
    
    public String getUnit() {
        return unit;
    }
}
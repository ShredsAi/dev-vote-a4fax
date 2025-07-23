package ai.shreds.domain.value_objects;

import ai.shreds.domain.exceptions.DomainExceptionDataValidationException;

import java.util.ArrayList;
import java.util.List;

public class DomainValueVotingMetric {
    private final String name;
    private final double value;
    private final String unit;

    public DomainValueVotingMetric(String name, double value, String unit) throws DomainExceptionDataValidationException {
        this.name = name;
        this.value = value;
        this.unit = unit;
        validate();
    }

    private void validate() throws DomainExceptionDataValidationException {
        List<String> errors = new ArrayList<>();
        if (name == null || name.isBlank()) {
            errors.add("Metric name must be provided");
        }
        if (value < 0) {
            errors.add("Metric value cannot be negative");
        }
        if (unit == null || unit.isBlank()) {
            errors.add("Metric unit must be provided");
        }
        if (!errors.isEmpty()) {
            throw new DomainExceptionDataValidationException("Invalid voting metric", errors);
        }
    }

    public String getName() {
        return name;
    }

    public double getValue() {
        return value;
    }

    public String getUnit() {
        return unit;
    }

    @Override
    public String toString() {
        return name + ": " + value + " " + unit;
    }
}
package ai.shreds.domain.value_objects;

import ai.shreds.domain.exceptions.DomainExceptionInvalidTurnoutPercentageException;

public class DomainValueTurnoutPercentage {

    private final double percentage;

    public DomainValueTurnoutPercentage(double percentage) throws DomainExceptionInvalidTurnoutPercentageException {
        if (percentage < 0.0 || percentage > 100.0) {
            throw new DomainExceptionInvalidTurnoutPercentageException(
                "Turnout percentage must be between 0 and 100", percentage
            );
        }
        this.percentage = percentage;
    }

    public double getValue() {
        return percentage;
    }

    @Override
    public String toString() {
        return percentage + "%";
    }
}
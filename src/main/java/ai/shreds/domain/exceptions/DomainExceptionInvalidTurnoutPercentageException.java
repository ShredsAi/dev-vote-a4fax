package ai.shreds.domain.exceptions;

public class DomainExceptionInvalidTurnoutPercentageException extends Exception {
    private static final long serialVersionUID = 1L;
    private final Double percentage;

    public DomainExceptionInvalidTurnoutPercentageException(String message, Double percentage) {
        super(message);
        this.percentage = percentage;
    }

    public Double getPercentage() {
        return percentage;
    }
}
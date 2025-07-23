package ai.shreds.domain.exceptions;

public class DomainExceptionInvalidTimeIntervalException extends Exception {
    private static final long serialVersionUID = 1L;
    private final String interval;

    public DomainExceptionInvalidTimeIntervalException(String message, String interval) {
        super(message);
        this.interval = interval;
    }

    public String getInterval() {
        return interval;
    }
}
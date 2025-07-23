package ai.shreds.domain.exceptions;

import java.util.List;

public class DomainExceptionDataValidationException extends Exception {
    private static final long serialVersionUID = 1L;
    private final List<String> validationErrors;

    public DomainExceptionDataValidationException(String message, List<String> validationErrors) {
        super(message);
        this.validationErrors = validationErrors;
    }

    public List<String> getValidationErrors() {
        return validationErrors;
    }
}
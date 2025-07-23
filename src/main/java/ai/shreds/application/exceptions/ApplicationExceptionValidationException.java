package ai.shreds.application.exceptions;

import java.util.List;
import lombok.Getter;

/**
 * Exception thrown when validation errors occur in the application layer.
 */
@Getter
public class ApplicationExceptionValidationException extends RuntimeException {
    private final String errorCode;
    private final List<String> validationErrors;

    public ApplicationExceptionValidationException(String message, String errorCode, List<String> validationErrors) {
        super(message);
        this.errorCode = errorCode;
        this.validationErrors = validationErrors;
    }

    public ApplicationExceptionValidationException(String message, String errorCode, List<String> validationErrors, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.validationErrors = validationErrors;
    }
}

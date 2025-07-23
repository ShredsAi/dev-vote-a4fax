package ai.shreds.application.exceptions;

import java.util.Map;
import lombok.Getter;

/**
 * Exception thrown when data processing fails in the application layer.
 */
@Getter
public class ApplicationExceptionDataProcessingException extends RuntimeException {
    private final String errorCode;
    private final Map<String, Object> details;

    public ApplicationExceptionDataProcessingException(String message, String errorCode, Map<String, Object> details) {
        super(message);
        this.errorCode = errorCode;
        this.details = details;
    }

    public ApplicationExceptionDataProcessingException(String message, String errorCode, Map<String, Object> details, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.details = details;
    }
}

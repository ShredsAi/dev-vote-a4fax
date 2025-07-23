package ai.shreds.application.exceptions;

import lombok.Getter;

/**
 * Exception thrown when authentication fails in the application layer.
 */
@Getter
public class ApplicationExceptionAuthenticationException extends RuntimeException {
    private final String errorCode;
    private final String userId;

    public ApplicationExceptionAuthenticationException(String message, String errorCode, String userId) {
        super(message);
        this.errorCode = errorCode;
        this.userId = userId;
    }

    public ApplicationExceptionAuthenticationException(String message, String errorCode, String userId, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.userId = userId;
    }
}

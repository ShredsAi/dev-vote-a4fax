package ai.shreds.adapter.exceptions;

import lombok.Getter;

/**
 * Exception thrown when an invalid request is received by an adapter controller.
 */
@Getter
public class AdapterExceptionInvalidRequestException extends RuntimeException {
    
    private final String errorCode;
    
    /**
     * Constructs a new invalid request exception with the specified detail message and error code.
     *
     * @param message   the detail message
     * @param errorCode the error code for categorizing the exception
     */
    public AdapterExceptionInvalidRequestException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * Constructs a new invalid request exception with the specified detail message, 
     * error code, and cause.
     *
     * @param message   the detail message
     * @param errorCode the error code for categorizing the exception
     * @param cause     the cause of the exception
     */
    public AdapterExceptionInvalidRequestException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    @Override
    public String toString() {
        return String.format("AdapterExceptionInvalidRequestException{message='%s', errorCode='%s'}", 
                getMessage(), errorCode);
    }
}
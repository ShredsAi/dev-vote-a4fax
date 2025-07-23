package ai.shreds.adapter.exceptions;

import lombok.Getter;

/**
 * Exception thrown when there are issues with real-time data streaming in adapter controllers.
 */
@Getter
public class AdapterExceptionStreamingException extends RuntimeException {
    
    private final String errorCode;
    
    /**
     * Constructs a new streaming exception with the specified detail message and error code.
     *
     * @param message   the detail message
     * @param errorCode the error code for categorizing the exception
     */
    public AdapterExceptionStreamingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * Constructs a new streaming exception with the specified detail message, 
     * error code, and cause.
     *
     * @param message   the detail message
     * @param errorCode the error code for categorizing the exception
     * @param cause     the cause of the exception
     */
    public AdapterExceptionStreamingException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
    
    @Override
    public String toString() {
        return String.format("AdapterExceptionStreamingException{message='%s', errorCode='%s'}", 
                getMessage(), errorCode);
    }
}
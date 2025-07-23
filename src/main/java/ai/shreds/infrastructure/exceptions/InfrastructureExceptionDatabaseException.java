package ai.shreds.infrastructure.exceptions;

/**
 * Exception thrown when database operations fail in the infrastructure layer.
 */
public class InfrastructureExceptionDatabaseException extends RuntimeException {

    private final String errorCode;
    private final String sqlState;

    public InfrastructureExceptionDatabaseException(String message, String errorCode, String sqlState) {
        super(message);
        this.errorCode = errorCode;
        this.sqlState = sqlState;
    }

    public InfrastructureExceptionDatabaseException(String message, String errorCode, String sqlState, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.sqlState = sqlState;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getSqlState() {
        return sqlState;
    }

    @Override
    public String toString() {
        return "InfrastructureExceptionDatabaseException{" +
                "message='" + getMessage() + '\'' +
                ", errorCode='" + errorCode + '\'' +
                ", sqlState='" + sqlState + '\'' +
                '}';
    }
}
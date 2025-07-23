package ai.shreds.infrastructure.exceptions;

/**
 * Exception thrown when external service calls fail in the infrastructure layer.
 */
public class InfrastructureExceptionExternalServiceException extends RuntimeException {

    private final String serviceName;
    private final Integer statusCode;

    public InfrastructureExceptionExternalServiceException(String message, String serviceName, Integer statusCode) {
        super(message);
        this.serviceName = serviceName;
        this.statusCode = statusCode;
    }

    public InfrastructureExceptionExternalServiceException(String message, String serviceName, Integer statusCode, Throwable cause) {
        super(message, cause);
        this.serviceName = serviceName;
        this.statusCode = statusCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        return "InfrastructureExceptionExternalServiceException{" +
                "message='" + getMessage() + '\'' +
                ", serviceName='" + serviceName + '\'' +
                ", statusCode=" + statusCode +
                '}';
    }
}